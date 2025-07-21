-- Drop tables if they exist
DROP TABLE IF EXISTS order_products;
DROP TABLE IF EXISTS orders;

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(255) NOT NULL UNIQUE,
    customer_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_date VARCHAR(255),
    updated_date VARCHAR(255),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- Create order_products table
CREATE TABLE IF NOT EXISTS order_products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    sku_code VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
