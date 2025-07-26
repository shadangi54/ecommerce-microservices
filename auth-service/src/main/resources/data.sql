-- Insert sample users (passwords are encoded with BCrypt)
-- Password for admin@example.com is 'admin123' 
INSERT INTO users (username, email, password, enabled) 
VALUES ('admin', 'admin@example.com', '$2a$10$Nc0EmaKm.xKBG8nk1Ec/Q.pzy7Ny23Td0U0bI8lEHmbqSvOQXgjjS', true) 
ON DUPLICATE KEY UPDATE email = VALUES(email), password = VALUES(password);

-- Password for user@example.com is 'user123'
INSERT INTO users (username, email, password, enabled) 
VALUES ('testuser', 'user@example.com', '$2a$10$c8px/mafwbWAVwwcgOlxQe7HDOPb17.wQRql.nl5JF4g.7EDjTyOW', true) 
ON DUPLICATE KEY UPDATE email = VALUES(email), password = VALUES(password);

-- Password for customer@example.com is 'customer123'
INSERT INTO users (username, email, password, enabled) 
VALUES ('customer', 'customer@example.com', '$2a$10$Rl5H7QCrwOQXgfUqqI1qzOTNo18qdwfzKgdrSzzcXLvbxbdtZlAkC', true)
ON DUPLICATE KEY UPDATE email = VALUES(email), password = VALUES(password);

-- Assign roles to users
-- Admin user gets admin role
INSERT INTO user_roles (user_id, role) 
SELECT id, 'ADMIN' FROM users WHERE username = 'admin'
ON DUPLICATE KEY UPDATE role = VALUES(role);

-- Admin also gets USER role
INSERT INTO user_roles (user_id, role) 
SELECT id, 'USER' FROM users WHERE username = 'admin'
ON DUPLICATE KEY UPDATE role = VALUES(role);

-- Test user gets user role
INSERT INTO user_roles (user_id, role) 
SELECT id, 'USER' FROM users WHERE username = 'testuser'
ON DUPLICATE KEY UPDATE role = VALUES(role);

-- Customer gets user role
INSERT INTO user_roles (user_id, role) 
SELECT id, 'USER' FROM users WHERE username = 'customer'
ON DUPLICATE KEY UPDATE role = VALUES(role);

-- Insert OAuth2 client for web application
INSERT INTO oauth2_registered_client 
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, 
client_name, client_authentication_methods, authorization_grant_types, 
redirect_uris, scopes, client_settings, token_settings) 
VALUES 
(
    '12345', 
    'web-client', 
    NOW(), 
    '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi', -- 'secret' encoded
    NULL, 
    'Web Client', 
    'client_secret_basic', 
    'authorization_code,refresh_token,client_credentials',
    'http://localhost:8080/login/oauth2/code/web-client-oidc,http://localhost:8080/authorized', 
    'openid,profile,read,write',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.access-token-time-to-live":["java.time.Duration",1800],"settings.token.refresh-token-time-to-live":["java.time.Duration",86400]}'
)
ON DUPLICATE KEY UPDATE 
client_id = VALUES(client_id);
