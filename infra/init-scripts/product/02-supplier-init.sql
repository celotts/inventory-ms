-- =====================================================================
-- supplier-init.sql  (BD de Compras / Proveedores)
-- =====================================================================

-- 0) Rol (si ejecutas como superusuario; si no, omite y crea la DB/usuario fuera)
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'supplier') THEN
      CREATE ROLE supplier LOGIN PASSWORD 'supplier123';
   END IF;
END$$;

-- 1) Extensiones
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2) Limpieza segura (orden para evitar dependencias)
DROP VIEW  IF EXISTS v_purchase_active;

-- Triggers y funciones que vamos a recrear
DO $$ BEGIN
  IF EXISTS (SELECT 1 FROM pg_proc WHERE proname = 'fn_recalc_purchase_totals') THEN
    DROP FUNCTION fn_recalc_purchase_totals() CASCADE;
  END IF;
  IF EXISTS (SELECT 1 FROM pg_proc WHERE proname = 'fn_set_tax_rate_from_catalog') THEN
    DROP FUNCTION fn_set_tax_rate_from_catalog() CASCADE;
  END IF;
  IF EXISTS (SELECT 1 FROM pg_proc WHERE proname = 'set_timestamp') THEN
    DROP FUNCTION set_timestamp() CASCADE;
  END IF;
END $$;

DROP TABLE IF EXISTS purchase_item CASCADE;
DROP TABLE IF EXISTS purchase_order CASCADE;
DROP TABLE IF EXISTS supplier_contact CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;
DROP TABLE IF EXISTS tax CASCADE;
DROP TABLE IF EXISTS outbox_event CASCADE;

-- Tipo ENUM solo si no existe
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'purchase_status') THEN
     CREATE TYPE purchase_status AS ENUM ('DRAFT','APPROVED','RECEIVED','CANCELLED');
  END IF;
END$$;

-- 3) Catálogo de impuestos
CREATE TABLE tax (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code          VARCHAR(30)  NOT NULL,
  name          VARCHAR(120) NOT NULL,
  rate          NUMERIC(7,4) NOT NULL CHECK (rate >= 0 AND rate <= 1),
  jurisdiction  CHAR(2),              -- ISO-3166-1 alpha-2; puede ser NULL si es global
  tax_type      VARCHAR(30),          -- p.ej. 'VAT','IEPS' (idealmente FK a catálogo)
  valid_from    DATE NOT NULL DEFAULT CURRENT_DATE,
  valid_to      DATE,                 -- NULL = vigente
  enabled       BOOLEAN NOT NULL DEFAULT TRUE,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT ck_tax_valid_window CHECK (valid_to IS NULL OR valid_to >= valid_from)
);

-- Único: solo 1 impuesto ACTIVO por code + jurisdicción + tipo
CREATE UNIQUE INDEX uq_tax_active_code_jur_type
  ON tax(code, jurisdiction, tax_type)
  WHERE enabled IS TRUE AND valid_to IS NULL;

-- Índices útiles de lectura
CREATE INDEX ix_tax_active
  ON tax (code)
  WHERE enabled IS TRUE AND valid_to IS NULL;

CREATE INDEX ix_tax_jur_type
  ON tax (jurisdiction, tax_type);

-- Trigger para mantener updated_at
CREATE OR REPLACE FUNCTION trg_set_updated_at()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  NEW.updated_at := NOW();
  RETURN NEW;
END;
$$;

CREATE TRIGGER set_updated_at
BEFORE UPDATE ON tax
FOR EACH ROW EXECUTE FUNCTION trg_set_updated_at();

-- 4) Catálogo de proveedores
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
    deleted_reason VARCHAR(255),

    -- ✅ Validación RFC (ajústala a tus reglas)
    CONSTRAINT chk_supplier_tax_id_format
    CHECK (
      tax_id IS NULL
      OR tax_id ~ '^(?:[A-Z]|Ñ|&){3,4}[0-9]{6}[A-Z0-9]{3}$'
    )
);
CREATE UNIQUE INDEX uq_supplier_code_active
  ON supplier(code) WHERE deleted_at IS NULL;
CREATE INDEX idx_supplier_code ON supplier(code);
CREATE INDEX idx_supplier_created_at ON supplier(created_at DESC);

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

-- 5) Compras (encabezado)
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
    deleted_reason VARCHAR(255),

    CONSTRAINT chk_totals_nonnegative
      CHECK (subtotal >= 0 AND tax_total >= 0 AND discount_total >= 0 AND grand_total >= 0)
);
-- Unicidad por número sólo para no borrados
CREATE UNIQUE INDEX uq_purchase_order_number_active
  ON purchase_order(order_number) WHERE deleted_at IS NULL;
CREATE INDEX idx_purchase_supplier   ON purchase_order(supplier_id);
CREATE INDEX idx_purchase_status     ON purchase_order(status);
CREATE INDEX idx_purchase_order_created_at ON purchase_order(created_at DESC);

-- 6) Renglones de compra
CREATE TABLE purchase_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_id UUID NOT NULL REFERENCES purchase_order(id) ON DELETE CASCADE,

    -- Referencia al producto del catalogo (product-service)
    product_id UUID,                       -- opcional si te riges por code
    product_code VARCHAR(50) NOT NULL,     -- denormalizado, evita FK directa cross-DB
    product_name VARCHAR(150),             -- foto del nombre al momento de comprar
    unit_id UUID,                          -- unidad (puedes guardar el id o symbol)
    unit_symbol VARCHAR(10),

    -- Impuestos y descuentos (fotografías del momento)
    tax_id UUID,                           -- referencia a catálogo
    tax_rate DECIMAL(5,2) DEFAULT 0 CHECK (tax_rate >= 0),
    discount_rate DECIMAL(5,2) DEFAULT 0 CHECK (discount_rate >= 0),

    quantity DECIMAL(14,2) NOT NULL CHECK (quantity > 0),
    unit_cost DECIMAL(14,4) NOT NULL CHECK (unit_cost >= 0),

    -- Si manejas lotes desde compras (útil para perecederos):
    lot_code VARCHAR(50),
    mfg_date DATE,
    expiration_date DATE,

    line_total DECIMAL(14,2) GENERATED ALWAYS AS
      (ROUND((quantity * unit_cost)
             * (1 - (COALESCE(discount_rate,0)/100.0))
             * (1 + (COALESCE(tax_rate,0)/100.0)), 2)) STORED,

    notes VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(120),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(120),
    deleted_reason VARCHAR(255),

    CONSTRAINT fk_purchase_item_tax
      FOREIGN KEY (tax_id) REFERENCES tax(id)
);
CREATE INDEX idx_purchase_item_purchase_id  ON purchase_item(purchase_id);
CREATE INDEX idx_purchase_item_product_code ON purchase_item(product_code);

-- 7) Vista de compras activas
CREATE OR REPLACE VIEW v_purchase_active AS
SELECT
  p.id, p.order_number, p.status, p.supplier_id, s.name AS supplier_name,
  p.currency, p.subtotal, p.tax_total, p.discount_total, p.grand_total,
  p.expected_at, p.received_at, p.notes,
  p.created_at, p.created_by, p.updated_at, p.updated_by
FROM purchase_order p
JOIN supplier s ON s.id = p.supplier_id
WHERE p.deleted_at IS NULL AND s.deleted_at IS NULL;

-- 8) Outbox para eventos de dominio (integración eventual con inventario)
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

-- 9) Funciones y triggers útiles

-- 9.1) updated_at automático
CREATE OR REPLACE FUNCTION set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp_supplier
BEFORE UPDATE ON supplier
FOR EACH ROW EXECUTE FUNCTION set_timestamp();

CREATE TRIGGER set_timestamp_supplier_contact
BEFORE UPDATE ON supplier_contact
FOR EACH ROW EXECUTE FUNCTION set_timestamp();

CREATE TRIGGER set_timestamp_purchase_order
BEFORE UPDATE ON purchase_order
FOR EACH ROW EXECUTE FUNCTION set_timestamp();

CREATE TRIGGER set_timestamp_purchase_item
BEFORE UPDATE ON purchase_item
FOR EACH ROW EXECUTE FUNCTION set_timestamp();

-- 9.2) Si viene tax_id, copiar tasa a tax_rate (fotografía)
CREATE OR REPLACE FUNCTION fn_set_tax_rate_from_catalog()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE v_rate DECIMAL(5,2);
BEGIN
  IF NEW.tax_id IS NOT NULL THEN
    SELECT rate
      INTO v_rate
      FROM tax
     WHERE id = NEW.tax_id
       AND enabled = TRUE
       AND (valid_to IS NULL OR valid_to >= CURRENT_DATE);

    IF v_rate IS NULL THEN
      RAISE EXCEPTION 'Tax id % no vigente o no encontrado', NEW.tax_id;
    END IF;

    NEW.tax_rate := v_rate; -- fotografía
  END IF;

  RETURN NEW;
END$$;

CREATE TRIGGER trg_purchase_item_set_tax
BEFORE INSERT OR UPDATE OF tax_id ON purchase_item
FOR EACH ROW EXECUTE FUNCTION fn_set_tax_rate_from_catalog();

-- 9.3) Recalcular totales de la orden al tocar renglones
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
  FROM purchase_item
  WHERE purchase_id = COALESCE(NEW.purchase_id, OLD.purchase_id)
    AND deleted_at IS NULL;

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

-- 10) Permisos
GRANT USAGE ON SCHEMA public TO supplier;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO supplier;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO supplier;

-- 11) Semillas (ELIMINADAS a petición) -------------------------------
-- (Ya no se insertan datos por defecto; el CRUD se hará cargo)
-- --------------------------------------------------------------------

-- 12) Búsquedas rápidas por "contiene" (trigramas) y opcionalmente sin acentos
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS unaccent;

-- Proveedor
CREATE INDEX IF NOT EXISTS idx_supplier_name_trgm
  ON supplier USING gin (unaccent(lower(name)) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_supplier_code_trgm
  ON supplier USING gin (unaccent(lower(code)) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_supplier_email_trgm
  ON supplier USING gin (unaccent(lower(email)) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_supplier_address_trgm
  ON supplier USING gin (unaccent(lower(address)) gin_trgm_ops);

-- (Opcional) Si filtras mucho por enabled y ordenas por name
CREATE INDEX IF NOT EXISTS idx_supplier_enabled_name
  ON supplier (enabled, name);

-- Compras: si buscas mucho por order_number “contiene”
CREATE INDEX IF NOT EXISTS idx_purchase_order_number_trgm
  ON purchase_order USING gin (unaccent(lower(order_number)) gin_trgm_ops);