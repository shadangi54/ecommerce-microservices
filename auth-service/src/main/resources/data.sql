-- Insert initial roles
INSERT INTO roles (name) VALUES ('USER') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO roles (name) VALUES ('MODERATOR') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO roles (name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Insert sample users (passwords are encoded with BCrypt - these are just examples)
-- Password for admin@example.com is 'admin123' 
INSERT INTO users (username, email, password) 
VALUES ('admin', 'admin@example.com', '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi') 
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Password for user@example.com is 'user123'
INSERT INTO users (username, email, password) 
VALUES ('testuser', 'user@example.com', '$2a$10$4FVMxV8ynZ41/dTRlCyZ2.5Jt1eSPMagm9Z5PQJzVB/B904tLEfxa') 
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Password for customer@example.com is 'customer123'
INSERT INTO users (username, email, password) 
VALUES ('customer', 'customer@example.com', '$2a$10$7euJCPcUQBIHC8Pgm9yXkOzQZtSIw5.89yux/8uGG52tCDFtLmZ/C')
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Assign roles to users
-- Admin user gets admin role
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ADMIN'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Test user gets user role
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'testuser' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Customer gets user role
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'customer' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
