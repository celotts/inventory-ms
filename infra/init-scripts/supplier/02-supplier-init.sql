-- =====================================================================
-- supplier-init.sql  (BD de Compras / Proveedores)
-- =====================================================================

-- 0) Rol
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'supplier') THEN
      CREATE ROLE supplier LOGIN PASSWORD 'supplier123';
   END IF;
END$$;

-- 1) Extensiones
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2) Limpieza segura
DROP VIEW  IF EXISTS v_purchase_active;
DROP TABLE IF EXISTS purchase_item CASCADE;
DROP TABLE IF EXISTS purchase_order CASCADE;
DROP TABLE IF EXISTS supplier_contact CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;
DROP TABLE IF EXISTS outbox_event CASCADE;

-- 3) Catálogo de proveedores
CREATE TABLE supplier (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(40) NOT NULL CHECK (code ~ '^[A-Z0-9\-_]{3,40}$'),
    name VARCHAR(150) NOT NULL CHECK (length(trim(name)) >= 2),
    tax_id VARCHAR(30),                -- RFC u otro identificador
    email VARCHAR(120),
    phone VARCHAR(40),
    address TEXT,
    enabled BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
CREATE UNIQUE INDEX uq_supplier_code_active
  ON supplier(code) WHERE deleted_at IS NULL;

CREATE TABLE supplier_contact (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_id UUID NOT NULL REFERENCES supplier(id),
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120),
    phone VARCHAR(40),
    role VARCHAR(80),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
CREATE INDEX idx_supplier_contact_supplier_id ON supplier_contact(supplier_id);

-- 4) Compras (encabezado)
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'purchase_status') THEN
     CREATE TYPE purchase_status AS ENUM ('DRAFT','APPROVED','RECEIVED','CANCELLED');
  END IF;
END$$;

CREATE TABLE purchase_order (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_id UUID NOT NULL REFERENCES supplier(id),
    order_number VARCHAR(50) NOT NULL,                -- full code, human-readable
    status purchase_status NOT NULL DEFAULT 'DRAFT',
    currency VARCHAR(10) DEFAULT 'MXN',
    subtotal DECIMAL(14,2) DEFAULT 0 CHECK (subtotal >= 0),
    tax_total DECIMAL(14,2) DEFAULT 0 CHECK (tax_total >= 0),
    discount_total DECIMAL(14,2) DEFAULT 0 CHECK (discount_total >= 0),
    grand_total DECIMAL(14,2) DEFAULT 0 CHECK (grand_total >= 0),

    expected_at DATE,          -- fecha prometida por proveedor
    received_at TIMESTAMP,     -- cuándo se recepcionó totalmente
    notes VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
-- Unicidad por número sólo para no borrados
CREATE UNIQUE INDEX uq_purchase_order_number_active
  ON purchase_order(order_number) WHERE deleted_at IS NULL;
CREATE INDEX idx_purchase_supplier   ON purchase_order(supplier_id);
CREATE INDEX idx_purchase_status     ON purchase_order(status);

-- 5) Renglones de compra
CREATE TABLE purchase_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_id UUID NOT NULL REFERENCES purchase_order(id) ON DELETE CASCADE,

    -- Referencia al producto del catalogo (product-service)
    product_id UUID,                 -- opcional si te riges por code
    product_code VARCHAR(50) NOT NULL,      -- denormalizado, evita FK directa cross-DB
    product_name VARCHAR(150),              -- foto del nombre al momento de comprar
    unit_id UUID,                            -- unidad (puedes guardar el id o symbol)
    unit_symbol VARCHAR(10),

    quantity DECIMAL(14,2) NOT NULL CHECK (quantity > 0),
    unit_cost DECIMAL(14,4) NOT NULL CHECK (unit_cost >= 0),
    tax_rate DECIMAL(5,2) DEFAULT 0 CHECK (tax_rate >= 0),
    discount_rate DECIMAL(5,2) DEFAULT 0 CHECK (discount_rate >= 0),

    -- Si manejas lotes desde compras (útil para perecederos):
    lot_code VARCHAR(50),
    mfg_date DATE,
    expiration_date DATE,

    line_total DECIMAL(14,2) GENERATED ALWAYS AS
      (ROUND((quantity * unit_cost) * (1 - (COALESCE(discount_rate,0)/100.0)) * (1 + (COALESCE(tax_rate,0)/100.0)), 2)) STORED,

    notes VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
CREATE INDEX idx_purchase_item_purchase_id ON purchase_item(purchase_id);
CREATE INDEX idx_purchase_item_product_code ON purchase_item(product_code);

-- 6) Vista de compras activas
CREATE OR REPLACE VIEW v_purchase_active AS
SELECT
  p.id, p.order_number, p.status, p.supplier_id, s.name AS supplier_name,
  p.currency, p.subtotal, p.tax_total, p.discount_total, p.grand_total,
  p.expected_at, p.received_at, p.notes,
  p.created_at, p.created_by, p.updated_at, p.updated_by
FROM purchase_order p
JOIN supplier s ON s.id = p.supplier_id
WHERE p.deleted_at IS NULL AND s.deleted_at IS NULL;

-- 7) Outbox para eventos de dominio (integración eventual con inventario)
CREATE TABLE outbox_event (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_type VARCHAR(60) NOT NULL,    -- "purchase"
    aggregate_id UUID NOT NULL,
    event_type VARCHAR(60) NOT NULL,        -- "PurchaseApproved", "PurchaseReceived"
    payload JSONB NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',   -- PENDING | SENT | ERROR
    last_error TEXT
);
CREATE INDEX idx_outbox_pending ON outbox_event(status, occurred_at);
CREATE INDEX idx_outbox_agg ON outbox_event(aggregate_type, aggregate_id);

-- 8) Triggers útiles (mantener totales/estado)
CREATE OR REPLACE FUNCTION fn_recalc_purchase_totals()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE
  v_sub DECIMAL(14,2);
  v_tax DECIMAL(14,2);
  v_disc DECIMAL(14,2);
  v_total DECIMAL(14,2);
BEGIN
  SELECT
    ROUND(SUM(quantity * unit_cost),2),
    ROUND(SUM((quantity * unit_cost) * (COALESCE(tax_rate,0)/100.0)),2),
    ROUND(SUM((quantity * unit_cost) * (COALESCE(discount_rate,0)/100.0)),2),
    ROUND(SUM(line_total),2)
  INTO v_sub, v_tax, v_disc, v_total
  FROM purchase_item WHERE purchase_id = COALESCE(NEW.purchase_id, OLD.purchase_id) AND deleted_at IS NULL;

  UPDATE purchase_order
     SET subtotal = COALESCE(v_sub,0),
         tax_total = COALESCE(v_tax,0),
         discount_total = COALESCE(v_disc,0),
         grand_total = COALESCE(v_total,0),
         updated_at = CURRENT_TIMESTAMP
   WHERE id = COALESCE(NEW.purchase_id, OLD.purchase_id);

  RETURN COALESCE(NEW, OLD);
END$$;

CREATE TRIGGER trg_purchase_recalc_totals
AFTER INSERT OR UPDATE OR DELETE ON purchase_item
FOR EACH ROW EXECUTE FUNCTION fn_recalc_purchase_totals();

-- 9) Permisos
GRANT USAGE ON SCHEMA public TO supplier;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO supplier;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO supplier;

-- 10) Datos base opcionales
INSERT INTO supplier(id, code, name, enabled) VALUES
  (gen_random_uuid(), 'SUP-DEFAULT', 'Proveedor Genérico', TRUE)
ON CONFLICT DO NOTHING;



Construí ms producto service con docker,  ahora voy a comenzar con la construcción del ms  de proveedores, tengo unas dudas: Se debe levantar en docker que levanta product-service o debe ser independiente (me gustaría que sea así). Aquí el experto eres tú. Me gustaría que sea independiente. El ambiente es desarrollo, estoy trabajando con mi MacBook m3. E desarrollo es en sprint boot 3. Bd Postgres, lo mismo que product-service. Anexo el el script para la bd -- =====================================================================
-- supplier-init.sql  (BD de Compras / Proveedores)
-- =====================================================================

-- 0) Rol
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'supplier') THEN
      CREATE ROLE supplier LOGIN PASSWORD 'supplier123';
   END IF;
END$$;

-- 1) Extensiones
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2) Limpieza segura
DROP VIEW  IF EXISTS v_purchase_active;
DROP TABLE IF EXISTS purchase_item CASCADE;
DROP TABLE IF EXISTS purchase_order CASCADE;
DROP TABLE IF EXISTS supplier_contact CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;
DROP TABLE IF EXISTS outbox_event CASCADE;

-- 3) Catálogo de proveedores
CREATE TABLE supplier (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(40) NOT NULL CHECK (code ~ '^[A-Z0-9\-_]{3,40}$'),
    name VARCHAR(150) NOT NULL CHECK (length(trim(name)) >= 2),
    tax_id VARCHAR(30),                -- RFC u otro identificador
    email VARCHAR(120),
    phone VARCHAR(40),
    address TEXT,
    enabled BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
CREATE UNIQUE INDEX uq_supplier_code_active
  ON supplier(code) WHERE deleted_at IS NULL;

CREATE TABLE supplier_contact (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_id UUID NOT NULL REFERENCES supplier(id),
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120),
    phone VARCHAR(40),
    role VARCHAR(80),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
CREATE INDEX idx_supplier_contact_supplier_id ON supplier_contact(supplier_id);

-- 4) Compras (encabezado)
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'purchase_status') THEN
     CREATE TYPE purchase_status AS ENUM ('DRAFT','APPROVED','RECEIVED','CANCELLED');
  END IF;
END$$;

CREATE TABLE purchase_order (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_id UUID NOT NULL REFERENCES supplier(id),
    order_number VARCHAR(50) NOT NULL,                -- full code, human-readable
    status purchase_status NOT NULL DEFAULT 'DRAFT',
    currency VARCHAR(10) DEFAULT 'MXN',
    subtotal DECIMAL(14,2) DEFAULT 0 CHECK (subtotal >= 0),
    tax_total DECIMAL(14,2) DEFAULT 0 CHECK (tax_total >= 0),
    discount_total DECIMAL(14,2) DEFAULT 0 CHECK (discount_total >= 0),
    grand_total DECIMAL(14,2) DEFAULT 0 CHECK (grand_total >= 0),

    expected_at DATE,          -- fecha prometida por proveedor
    received_at TIMESTAMP,     -- cuándo se recepcionó totalmente
    notes VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
-- Unicidad por número sólo para no borrados
CREATE UNIQUE INDEX uq_purchase_order_number_active
  ON purchase_order(order_number) WHERE deleted_at IS NULL;
CREATE INDEX idx_purchase_supplier   ON purchase_order(supplier_id);
CREATE INDEX idx_purchase_status     ON purchase_order(status);

-- 5) Renglones de compra
CREATE TABLE purchase_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_id UUID NOT NULL REFERENCES purchase_order(id) ON DELETE CASCADE,

    -- Referencia al producto del catalogo (product-service)
    product_id UUID,                 -- opcional si te riges por code
    product_code VARCHAR(50) NOT NULL,      -- denormalizado, evita FK directa cross-DB
    product_name VARCHAR(150),              -- foto del nombre al momento de comprar
    unit_id UUID,                            -- unidad (puedes guardar el id o symbol)
    unit_symbol VARCHAR(10),

    quantity DECIMAL(14,2) NOT NULL CHECK (quantity > 0),
    unit_cost DECIMAL(14,4) NOT NULL CHECK (unit_cost >= 0),
    tax_rate DECIMAL(5,2) DEFAULT 0 CHECK (tax_rate >= 0),
    discount_rate DECIMAL(5,2) DEFAULT 0 CHECK (discount_rate >= 0),

    -- Si manejas lotes desde compras (útil para perecederos):
    lot_code VARCHAR(50),
    mfg_date DATE,
    expiration_date DATE,

    line_total DECIMAL(14,2) GENERATED ALWAYS AS
      (ROUND((quantity * unit_cost) * (1 - (COALESCE(discount_rate,0)/100.0)) * (1 + (COALESCE(tax_rate,0)/100.0)), 2)) STORED,

    notes VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255)
);
CREATE INDEX idx_purchase_item_purchase_id ON purchase_item(purchase_id);
CREATE INDEX idx_purchase_item_product_code ON purchase_item(product_code);

-- 6) Vista de compras activas
CREATE OR REPLACE VIEW v_purchase_active AS
SELECT
  p.id, p.order_number, p.status, p.supplier_id, s.name AS supplier_name,
  p.currency, p.subtotal, p.tax_total, p.discount_total, p.grand_total,
  p.expected_at, p.received_at, p.notes,
  p.created_at, p.created_by, p.updated_at, p.updated_by
FROM purchase_order p
JOIN supplier s ON s.id = p.supplier_id
WHERE p.deleted_at IS NULL AND s.deleted_at IS NULL;

-- 7) Outbox para eventos de dominio (integración eventual con inventario)
CREATE TABLE outbox_event (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_type VARCHAR(60) NOT NULL,    -- "purchase"
    aggregate_id UUID NOT NULL,
    event_type VARCHAR(60) NOT NULL,        -- "PurchaseApproved", "PurchaseReceived"
    payload JSONB NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',   -- PENDING | SENT | ERROR
    last_error TEXT
);
CREATE INDEX idx_outbox_pending ON outbox_event(status, occurred_at);
CREATE INDEX idx_outbox_agg ON outbox_event(aggregate_type, aggregate_id);

-- 8) Triggers útiles (mantener totales/estado)
CREATE OR REPLACE FUNCTION fn_recalc_purchase_totals()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE
  v_sub DECIMAL(14,2);
  v_tax DECIMAL(14,2);
  v_disc DECIMAL(14,2);
  v_total DECIMAL(14,2);
BEGIN
  SELECT
    ROUND(SUM(quantity * unit_cost),2),
    ROUND(SUM((quantity * unit_cost) * (COALESCE(tax_rate,0)/100.0)),2),
    ROUND(SUM((quantity * unit_cost) * (COALESCE(discount_rate,0)/100.0)),2),
    ROUND(SUM(line_total),2)
  INTO v_sub, v_tax, v_disc, v_total
  FROM purchase_item WHERE purchase_id = COALESCE(NEW.purchase_id, OLD.purchase_id) AND deleted_at IS NULL;

  UPDATE purchase_order
     SET subtotal = COALESCE(v_sub,0),
         tax_total = COALESCE(v_tax,0),
         discount_total = COALESCE(v_disc,0),
         grand_total = COALESCE(v_total,0),
         updated_at = CURRENT_TIMESTAMP
   WHERE id = COALESCE(NEW.purchase_id, OLD.purchase_id);

  RETURN COALESCE(NEW, OLD);
END$$;

CREATE TRIGGER trg_purchase_recalc_totals
AFTER INSERT OR UPDATE OR DELETE ON purchase_item
FOR EACH ROW EXECUTE FUNCTION fn_recalc_purchase_totals();

-- 9) Permisos
GRANT USAGE ON SCHEMA public TO supplier;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO supplier;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO supplier;

-- 10) Datos base opcionales
INSERT INTO supplier(id, code, name, enabled) VALUES
  (gen_random_uuid(), 'SUP-DEFAULT', 'Proveedor Genérico', TRUE)
ON CONFLICT DO NOTHING; Debra ser una bd independiente de la product-service?

