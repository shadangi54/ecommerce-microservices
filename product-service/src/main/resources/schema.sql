-- Schema definition for product table
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DOUBLE NOT NULL,
    category VARCHAR(50),
    sku_code VARCHAR(50) NOT NULL UNIQUE,
    image_url VARCHAR(255),
    is_active BOOLEAN NOT NULL,
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_date VARCHAR(255) NOT NULL,
    updated_date VARCHAR(255) NOT NULL
);
