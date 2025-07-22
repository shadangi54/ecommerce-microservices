-- Sample inventory data for the e-commerce inventory service
INSERT INTO inventory (sku_code, quantity, is_deleted, created_by, modified_by, created_date, updated_date)
VALUES 
    ('IPHONE14PRO-256-BLACK', 45, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('GALAXY-S23-512-PHANTOM', 28, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('NIKE-AIRMAX270-BW-10', 95, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('NIKE-AIRMAX270-BW-9', 87, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('NIKE-AIRMAX270-BW-11', 78, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('DELL-XPS15-32GB-1TB', 18, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('SONY-WH1000XM5-BLACK', 42, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('KINDLE-PW-8GB-BLACK', 55, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('ADIDAS-UB22-BLACK-9', 70, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('ADIDAS-UB22-BLACK-10', 68, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('APPLE-WATCH8-45MM-GPS', 35, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('DYSON-V12-DETECT-SLIM', 12, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('PS5-DIGITAL-WHITE', 8, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('MACBOOK-PRO-16-M3-MAX', 15, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('IPHONE15PRO-512-TITANIUM', 25, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    -- Additional inventory entries for testing scenarios
    ('IPHONE14PRO-128-BLUE', 52, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('IPHONE14PRO-512-GOLD', 38, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('GALAXY-S23-256-GREEN', 44, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('SONY-WH1000XM5-SILVER', 38, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('APPLE-WATCH8-45MM-CELL', 32, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('PS5-STANDARD-WHITE', 6, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('ADIDAS-UB22-BLACK-11', 62, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    -- Low stock items for testing low stock scenarios
    ('GAMING-LAPTOP-PRO-RTX4080', 3, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('MACBOOK-AIR-M3-512', 5, false, 'system', 'system', '2025-07-20', '2025-07-20'),
    ('IPAD-PRO-12-1TB', 7, false, 'system', 'system', '2025-07-20', '2025-07-20');
