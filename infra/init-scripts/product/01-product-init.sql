-- =====================================================================
-- product-init.sql  (soft delete + índices únicos parciales + vistas explícitas)
-- =====================================================================

-- 0) Crear rol de BD si no existe
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'product') THEN
      CREATE ROLE product LOGIN PASSWORD 'product123';
   END IF;
END
$$;

-- 1) Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 2) Dropear VISTAS primero (evita dependencias al alterar tipos en tablas)
DROP VIEW IF EXISTS v_product_active;
DROP VIEW IF EXISTS v_product_unit_active;
DROP VIEW IF EXISTS v_product_tag_active;
DROP VIEW IF EXISTS v_product_brand_active;
DROP VIEW IF EXISTS v_category_active;

-- 3) Eliminar tablas existentes (orden por FKs)
DROP TABLE IF EXISTS product_tag_assignment CASCADE;
DROP TABLE IF EXISTS product_category CASCADE;
DROP TABLE IF EXISTS product_price_history CASCADE;
DROP TABLE IF EXISTS product_image CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS product_brand CASCADE;
DROP TABLE IF EXISTS product_unit CASCADE;
DROP TABLE IF EXISTS product_type CASCADE;
DROP TABLE IF EXISTS product_tag CASCADE;
DROP TABLE IF EXISTS category CASCADE;

-- 4) Tablas base
-- 4.1 product_type (PK = code)
CREATE TABLE product_type (
    id UUID DEFAULT gen_random_uuid(),
    code VARCHAR(50) PRIMARY KEY CHECK (code IN ('raw_material', 'asset', 'disposable', 'beverage')),
    name VARCHAR(100) NOT NULL CHECK (
        name ~ '^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s\-_]{2,100}$' AND LENGTH(TRIM(name)) >= 2
    ),
    description VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE product_type OWNER TO product;

-- 4.2 product_unit (code reutilizable vía índice único parcial)
CREATE TABLE product_unit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL CHECK (
        name ~ '^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s\-_]{2,100}$' AND LENGTH(TRIM(name)) >= 2
    ),
    description VARCHAR(255) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE product_unit OWNER TO product;

-- 4.3 product_brand
CREATE TABLE product_brand (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL CHECK (
        name ~ '^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s\-_]{2,100}$' AND LENGTH(TRIM(name)) >= 2
    ),
    description VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE product_brand OWNER TO product;

-- 4.4 product_tag (name reutilizable vía índice único parcial)
CREATE TABLE product_tag (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL CHECK (
        name ~ '^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s\-_]{2,50}$' AND LENGTH(TRIM(name)) >= 2
    ),
    description VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE product_tag OWNER TO product;

-- 4.5 category
CREATE TABLE category (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL CHECK (
        name ~ '^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s\-_]{2,100}$' AND LENGTH(TRIM(name)) >= 2
    ),
    description VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE category OWNER TO product;

-- 5) Tabla principal product
CREATE TABLE product (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(50) NOT NULL CHECK (code ~ '^[A-Z0-9\-_]{3,50}$'),
    name VARCHAR(100) NOT NULL CHECK (name ~ '^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s\-_]{2,100}$'),
    description VARCHAR(255),
    product_type VARCHAR(50) NOT NULL REFERENCES product_type(code),
    unit_id UUID NOT NULL REFERENCES product_unit(id),
    brand_id UUID REFERENCES product_brand(id),
    minimum_stock DECIMAL(10,2) DEFAULT 0 CHECK (minimum_stock >= 0),
    current_stock DECIMAL(10,2) DEFAULT 0 CHECK (current_stock >= 0),
    unit_price DECIMAL(10,2) CHECK (unit_price >= 0),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE product OWNER TO product;

-- 6) Tablas relacionadas
CREATE TABLE product_category (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    category_id UUID NOT NULL REFERENCES category(id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255)
);
ALTER TABLE product_category OWNER TO product;

CREATE TABLE product_price_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255)
);
ALTER TABLE product_price_history OWNER TO product;

CREATE TABLE product_image (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    url TEXT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255)
);
ALTER TABLE product_image OWNER TO product;

CREATE TABLE product_tag_assignment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    tag_id UUID NOT NULL REFERENCES product_tag(id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255)
);
ALTER TABLE product_tag_assignment OWNER TO product;

-- 7) Índices
-- 7.1 product (búsquedas comunes)
CREATE INDEX idx_product_code           ON product(code);
CREATE INDEX idx_product_name           ON product(name);
CREATE INDEX idx_product_type           ON product(product_type);
CREATE INDEX idx_product_enabled        ON product(enabled);
CREATE INDEX idx_product_brand          ON product(brand_id);
CREATE INDEX idx_product_unit_id        ON product(unit_id);

-- 7.2 category
CREATE INDEX idx_category_name          ON category(name);

-- 7.3 product_category
CREATE INDEX idx_product_category_product_id  ON product_category(product_id);
CREATE INDEX idx_product_category_category_id ON product_category(category_id);

-- 7.4 product_price_history
CREATE INDEX idx_price_hist_product_id  ON product_price_history(product_id);

-- 7.5 product_image
CREATE INDEX idx_image_product_id       ON product_image(product_id);

-- 7.6 product_tag_assignment
CREATE INDEX idx_tag_asg_product_id     ON product_tag_assignment(product_id);
CREATE INDEX idx_tag_asg_tag_id         ON product_tag_assignment(tag_id);

-- 7.7 Índices únicos parciales (solo activos)
CREATE UNIQUE INDEX uq_product_unit_code_active
    ON product_unit(code)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX uq_product_code_active
    ON product(code)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX uq_product_tag_name_active
    ON product_tag(name)
    WHERE deleted_at IS NULL;

-- (Opcional) unicidad parcial en brand.name:
-- CREATE UNIQUE INDEX uq_product_brand_name_active
--     ON product_brand(name)
--     WHERE deleted_at IS NULL;

-- 8) Vistas “activos” (sin SELECT *)
CREATE OR REPLACE VIEW v_product_active AS
SELECT
    id, code, name, description, product_type, unit_id, brand_id,
    minimum_stock, current_stock, unit_price, enabled,
    created_at, created_by, updated_by, updated_at,
    deleted_at, deleted_by, deleted_reason
FROM product
WHERE deleted_at IS NULL;

CREATE OR REPLACE VIEW v_product_unit_active AS
SELECT
    id, code, name, description, symbol, enabled,
    created_at, created_by, updated_by, updated_at,
    deleted_at, deleted_by, deleted_reason
FROM product_unit
WHERE deleted_at IS NULL;

CREATE OR REPLACE VIEW v_product_tag_active AS
SELECT
    id, name, description, enabled,
    created_at, created_by, updated_by, updated_at,
    deleted_at, deleted_by, deleted_reason
FROM product_tag
WHERE deleted_at IS NULL;

CREATE OR REPLACE VIEW v_product_brand_active AS
SELECT
    id, name, description, enabled,
    created_at, created_by, updated_by, updated_at,
    deleted_at, deleted_by, deleted_reason
FROM product_brand
WHERE deleted_at IS NULL;

CREATE OR REPLACE VIEW v_category_active AS
SELECT
    id, name, description, enabled,
    created_at, created_by, updated_by, updated_at,
    deleted_at, deleted_by, deleted_reason
FROM category
WHERE deleted_at IS NULL;

-- 9) Permisos
GRANT USAGE ON SCHEMA public TO product;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO product;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO product;

-- Visibilidad para clientes (opcional)
GRANT USAGE ON SCHEMA public TO PUBLIC;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO PUBLIC;

-- =====================================================================
-- 10) Limpieza extra para objetos de perecederos (idempotente)
-- =====================================================================

-- Vistas nuevas
DROP VIEW IF EXISTS v_lot_available;
DROP VIEW IF EXISTS v_lot_expired;
DROP VIEW IF EXISTS v_product_stock_available;
DROP VIEW IF EXISTS v_lots_expiring_soon;

-- Triggers: dropear solo si la tabla existe
DO $$
BEGIN
  IF to_regclass('public.lot') IS NOT NULL THEN
    EXECUTE 'DROP TRIGGER IF EXISTS trg_enforce_perishable_lot ON lot';
    EXECUTE 'DROP TRIGGER IF EXISTS trg_update_lot_stage_by_dates ON lot';
    EXECUTE 'DROP TRIGGER IF EXISTS trg_sync_stock_on_lot ON lot';
  END IF;

  IF to_regclass('public.inventory_movement') IS NOT NULL THEN
    EXECUTE 'DROP TRIGGER IF EXISTS trg_sync_stock_on_mov ON inventory_movement';
  END IF;
END$$;

-- Funciones
DROP FUNCTION IF EXISTS fn_enforce_perishable_lot() CASCADE;
DROP FUNCTION IF EXISTS fn_update_lot_stage_by_dates() CASCADE;
DROP FUNCTION IF EXISTS fn_sync_product_stock(UUID) CASCADE;
DROP FUNCTION IF EXISTS fn_sync_product_stock_from_lot() CASCADE;
DROP FUNCTION IF EXISTS fn_sync_product_stock_from_mov() CASCADE;

DROP FUNCTION IF EXISTS sp_consume_to_production(UUID, DECIMAL, VARCHAR, VARCHAR) CASCADE;
DROP FUNCTION IF EXISTS sp_mark_expired_lots(VARCHAR) CASCADE;
DROP FUNCTION IF EXISTS sp_dispose_lot(UUID, VARCHAR, VARCHAR, VARCHAR) CASCADE;

-- Tablas nuevas
DROP TABLE IF EXISTS inventory_movement CASCADE;
DROP TABLE IF EXISTS lot CASCADE;

-- Tipos/Enums nuevos
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movement_type') THEN DROP TYPE movement_type; END IF;
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movement_purpose') THEN DROP TYPE movement_purpose; END IF;
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'lot_stage') THEN DROP TYPE lot_stage; END IF;
END$$;

-- =====================================================================
-- 11) Campos en product para perecederos
-- =====================================================================
ALTER TABLE product
  ADD COLUMN IF NOT EXISTS is_perishable BOOLEAN NOT NULL DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS shelf_life_days INTEGER CHECK (shelf_life_days IS NULL OR shelf_life_days >= 0);

-- Actualizar la vista v_product_active para exponer los nuevos campos
DROP VIEW IF EXISTS v_product_active;
CREATE VIEW v_product_active AS
SELECT
    id, code, name, description, product_type, unit_id, brand_id,
    minimum_stock, current_stock, unit_price, enabled,
    is_perishable, shelf_life_days,
    created_at, created_by, updated_by, updated_at,
    deleted_at, deleted_by, deleted_reason
FROM product
WHERE deleted_at IS NULL;

-- =====================================================================
-- 12) Tipos y tablas para lotes y movimientos (kardex)
-- =====================================================================
DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movement_type') THEN
      CREATE TYPE movement_type AS ENUM ('IN', 'OUT', 'ADJUST', 'TRANSFER');
   END IF;
   IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movement_purpose') THEN
      CREATE TYPE movement_purpose AS ENUM ('PURCHASE', 'PRODUCTION', 'SALE', 'WASTE', 'COUNT', 'OTHER');
   END IF;
   IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'lot_stage') THEN
      CREATE TYPE lot_stage AS ENUM ('AVAILABLE', 'RESERVED', 'TO_PRODUCTION', 'IN_PRODUCTION', 'TO_DISPOSE', 'DISPOSED');
   END IF;
END$$;

CREATE TABLE IF NOT EXISTS lot (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    lot_code VARCHAR(50) NOT NULL,
    quantity DECIMAL(12,2) NOT NULL CHECK (quantity >= 0),
    unit_cost DECIMAL(12,2) CHECK (unit_cost >= 0),
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mfg_date DATE, -- Manufacturing Date → Fecha de fabricación
    expiration_date DATE,        -- NULL para no perecederos
    stage lot_stage NOT NULL DEFAULT 'AVAILABLE',
    supplier_id UUID,
    notes VARCHAR(255),

    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_reason VARCHAR(255)
);
ALTER TABLE lot OWNER TO product;

CREATE UNIQUE INDEX IF NOT EXISTS uq_lot_product_code_active
    ON lot(product_id, lot_code)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_lot_product_id      ON lot(product_id);
CREATE INDEX IF NOT EXISTS idx_lot_expiration_date ON lot(expiration_date);
CREATE INDEX IF NOT EXISTS idx_lot_stage           ON lot(stage);
CREATE INDEX IF NOT EXISTS idx_lot_enabled         ON lot(enabled);

CREATE TABLE IF NOT EXISTS inventory_movement (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    lot_id UUID REFERENCES lot(id),
    type movement_type NOT NULL,
    purpose movement_purpose NOT NULL DEFAULT 'OTHER',
    quantity DECIMAL(12,2) NOT NULL CHECK (quantity > 0),
    reference VARCHAR(255),
    reason VARCHAR(200),
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255)
);
ALTER TABLE inventory_movement OWNER TO product;

CREATE INDEX IF NOT EXISTS idx_mov_product_id   ON inventory_movement(product_id);
CREATE INDEX IF NOT EXISTS idx_mov_lot_id       ON inventory_movement(lot_id);
CREATE INDEX IF NOT EXISTS idx_mov_occurred_at  ON inventory_movement(occurred_at);
CREATE INDEX IF NOT EXISTS idx_mov_type_purpose ON inventory_movement(type, purpose);

-- =====================================================================
-- 13) Vistas operativas (disponibles, vencidos, stock)
-- =====================================================================
CREATE OR REPLACE VIEW v_lot_available AS
SELECT
  l.id, l.product_id, l.lot_code, l.quantity, l.unit_cost, l.received_at,
  l.mfg_date, l.expiration_date, l.stage, l.supplier_id, l.notes,
  l.enabled, l.created_at, (l.created_by)::varchar(255) AS created_by,
  (l.updated_by)::varchar(255) AS updated_by,
  l.updated_at, l.deleted_at, (l.deleted_by)::varchar(255) AS deleted_by,
  l.deleted_reason
FROM lot l
JOIN product p ON p.id = l.product_id
WHERE l.deleted_at IS NULL
  AND l.enabled = TRUE
  AND l.stage IN ('AVAILABLE','RESERVED','TO_PRODUCTION','IN_PRODUCTION')
  AND (l.expiration_date IS NULL OR l.expiration_date >= CURRENT_DATE);

CREATE OR REPLACE VIEW v_lot_expired AS
SELECT l.*
FROM lot l
JOIN product p ON p.id = l.product_id
WHERE l.deleted_at IS NULL
  AND l.enabled = TRUE
  AND p.is_perishable = TRUE
  AND l.expiration_date < CURRENT_DATE
  AND l.quantity > 0
  AND l.stage <> 'DISPOSED';

CREATE OR REPLACE VIEW v_product_stock_available AS
SELECT
  p.id AS product_id,
  p.code,
  p.name,
  COALESCE(SUM(l.quantity), 0) AS available_stock
FROM product p
LEFT JOIN v_lot_available l ON l.product_id = p.id
GROUP BY p.id, p.code, p.name;

CREATE OR REPLACE VIEW v_lots_expiring_soon AS
SELECT p.code, p.name, l.id AS lot_id, l.lot_code, l.expiration_date, l.quantity
FROM lot l
JOIN product p ON p.id = l.product_id
WHERE p.is_perishable = TRUE
  AND l.deleted_at IS NULL
  AND l.enabled = TRUE
  AND l.expiration_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '7 days')
ORDER BY l.expiration_date ASC;

-- =====================================================================
-- 14) Triggers: validación y sincronización de stock
-- =====================================================================
CREATE OR REPLACE FUNCTION fn_enforce_perishable_lot()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE v_is_perishable BOOLEAN;
BEGIN
  SELECT is_perishable INTO v_is_perishable FROM product WHERE id = NEW.product_id;
  IF v_is_perishable IS TRUE AND NEW.expiration_date IS NULL THEN
     RAISE EXCEPTION 'Producto perecedero requiere expiration_date en el lote (product_id=%)', NEW.product_id;
  END IF;
  RETURN NEW;
END$$;

CREATE TRIGGER trg_enforce_perishable_lot
BEFORE INSERT OR UPDATE ON lot
FOR EACH ROW EXECUTE FUNCTION fn_enforce_perishable_lot();

CREATE OR REPLACE FUNCTION fn_update_lot_stage_by_dates()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE v_is_perishable BOOLEAN;
BEGIN
  SELECT is_perishable INTO v_is_perishable FROM product WHERE id = NEW.product_id;

  IF v_is_perishable IS TRUE
     AND NEW.expiration_date IS NOT NULL
     AND NEW.expiration_date < CURRENT_DATE
     AND COALESCE(NEW.quantity,0) > 0 THEN
       NEW.stage := 'TO_DISPOSE';
  ELSIF NEW.stage = 'TO_DISPOSE' AND (NEW.expiration_date IS NULL OR NEW.expiration_date >= CURRENT_DATE) THEN
       NEW.stage := 'AVAILABLE';
  END IF;

  RETURN NEW;
END$$;

CREATE TRIGGER trg_update_lot_stage_by_dates
BEFORE INSERT OR UPDATE ON lot
FOR EACH ROW EXECUTE FUNCTION fn_update_lot_stage_by_dates();

CREATE OR REPLACE FUNCTION fn_sync_product_stock(p_product_id UUID)
RETURNS VOID LANGUAGE plpgsql AS $$
DECLARE v_stock DECIMAL(12,2);
BEGIN
  SELECT COALESCE(SUM(quantity),0)
  INTO v_stock
  FROM v_lot_available
  WHERE product_id = p_product_id;

  UPDATE product
     SET current_stock = v_stock,
         updated_at = CURRENT_TIMESTAMP
   WHERE id = p_product_id;
END$$;

CREATE OR REPLACE FUNCTION fn_sync_product_stock_from_lot()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  PERFORM fn_sync_product_stock(COALESCE(NEW.product_id, OLD.product_id));
  RETURN COALESCE(NEW, OLD);
END$$;

CREATE TRIGGER trg_sync_stock_on_lot
AFTER INSERT OR UPDATE OR DELETE ON lot
FOR EACH ROW EXECUTE FUNCTION fn_sync_product_stock_from_lot();

CREATE OR REPLACE FUNCTION fn_sync_product_stock_from_mov()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE v_prod UUID;
BEGIN
  v_prod := COALESCE(NEW.product_id, OLD.product_id);
  PERFORM fn_sync_product_stock(v_prod);
  RETURN COALESCE(NEW, OLD);
END$$;

CREATE TRIGGER trg_sync_stock_on_mov
AFTER INSERT OR UPDATE OR DELETE ON inventory_movement
FOR EACH ROW EXECUTE FUNCTION fn_sync_product_stock_from_mov();

-- =====================================================================
-- 15) Procedimientos: FEFO a producción y disposición de vencidos
-- =====================================================================
CREATE OR REPLACE FUNCTION sp_consume_to_production(
    p_product_id UUID,
    p_quantity   DECIMAL,
    p_reference  VARCHAR(255),
    p_user       VARCHAR DEFAULT 'system'
)
RETURNS TABLE (lot_id UUID, taken DECIMAL) LANGUAGE plpgsql AS $$
DECLARE
  v_remaining DECIMAL := p_quantity;
  r RECORD;
  v_take DECIMAL;
BEGIN
  IF p_quantity <= 0 THEN
     RAISE EXCEPTION 'Cantidad debe ser > 0';
  END IF;

  FOR r IN
    SELECT id, quantity
    FROM v_lot_available
    WHERE product_id = p_product_id
    ORDER BY expiration_date ASC NULLS LAST, received_at ASC
  LOOP
     EXIT WHEN v_remaining <= 0;
     IF r.quantity <= 0 THEN CONTINUE; END IF;

     v_take := LEAST(r.quantity, v_remaining);

     INSERT INTO inventory_movement(product_id, lot_id, type, purpose, quantity, reference, reason, created_by)
     VALUES (p_product_id, r.id, 'OUT', 'PRODUCTION', v_take, p_reference, 'Consumo FEFO a producción', p_user);

     UPDATE lot
        SET quantity = quantity - v_take,
            updated_at = CURRENT_TIMESTAMP,
            updated_by = p_user
      WHERE id = r.id;

     v_remaining := v_remaining - v_take;
     lot_id := r.id; taken := v_take; RETURN NEXT;
  END LOOP;

  IF v_remaining > 0 THEN
     RAISE EXCEPTION 'Stock insuficiente para producto %, faltan % unidades', p_product_id, v_remaining;
  END IF;

  PERFORM fn_sync_product_stock(p_product_id);
END$$;

CREATE OR REPLACE FUNCTION sp_mark_expired_lots(p_user VARCHAR DEFAULT 'system')
RETURNS INTEGER LANGUAGE plpgsql AS $$
DECLARE v_count INTEGER;
BEGIN
  UPDATE lot l
     SET stage = 'TO_DISPOSE',
         updated_at = CURRENT_TIMESTAMP,
         updated_by = p_user
    FROM product p
   WHERE l.product_id = p.id
     AND p.is_perishable = TRUE
     AND l.deleted_at IS NULL
     AND l.enabled = TRUE
     AND l.quantity > 0
     AND l.expiration_date < CURRENT_DATE
     AND l.stage <> 'DISPOSED';

  GET DIAGNOSTICS v_count = ROW_COUNT;
  RETURN v_count;
END$$;

CREATE OR REPLACE FUNCTION sp_dispose_lot(
    p_lot_id UUID,
    p_reference VARCHAR(255),
    p_reason    VARCHAR,
    p_user      VARCHAR DEFAULT 'system'
)
RETURNS VOID LANGUAGE plpgsql AS $$
DECLARE v_prod UUID; v_qty DECIMAL;
BEGIN
  SELECT product_id, quantity INTO v_prod, v_qty FROM lot WHERE id = p_lot_id FOR UPDATE;
  IF v_qty IS NULL THEN
     RAISE EXCEPTION 'Lote % no existe', p_lot_id;
  END IF;
  IF v_qty <= 0 THEN
     UPDATE lot SET stage='DISPOSED', updated_at=CURRENT_TIMESTAMP, updated_by=p_user WHERE id=p_lot_id;
     RETURN;
  END IF;

  INSERT INTO inventory_movement(product_id, lot_id, type, purpose, quantity, reference, reason, created_by)
  VALUES (v_prod, p_lot_id, 'OUT', 'WASTE', v_qty, p_reference, COALESCE(p_reason,'Vencido'), p_user);

  UPDATE lot
     SET quantity = 0,
         stage = 'DISPOSED',
         updated_at = CURRENT_TIMESTAMP,
         updated_by = p_user
   WHERE id = p_lot_id;

  PERFORM fn_sync_product_stock(v_prod);
END$$;

-- =====================================================================
-- 16) Permisos para objetos nuevos
-- =====================================================================
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE lot TO product;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE inventory_movement TO product;
GRANT SELECT ON TABLE v_lot_available, v_lot_expired, v_product_stock_available, v_lots_expiring_soon TO product;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO product;

-- (opcional) visibilidad pública de vistas
GRANT SELECT ON TABLE v_lot_available, v_lot_expired, v_product_stock_available, v_lots_expiring_soon TO PUBLIC;

-- =====================================================================
-- Fin de bloque de perecederos
-- =====================================================================

-- =====================================================================
-- Notas operativas:
-- * Soft delete: UPDATE ... SET deleted_at=now(), deleted_by=?, deleted_reason=? WHERE id=?
-- * Restore:     UPDATE ... SET deleted_at=NULL, deleted_by=NULL, deleted_reason=NULL WHERE id=?
-- * Filtrado app: WHERE deleted_at IS NULL
-- * FK robusta: product.unit_id -> product_unit.id (permite reusar product_unit.code)
-- * Si vas a ALTER COLUMN TYPE en tablas usadas por estas vistas:
--   - DROP VIEW ... → ALTER TABLE ... → CREATE VIEW ... (esta receta evita el error 0A000)
-- =====================================================================