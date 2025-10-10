-- Crear tabla Category
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);
-- Crear tabla Product con relación a Category
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);