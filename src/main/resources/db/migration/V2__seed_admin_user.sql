
INSERT INTO users (username, password_hash)
VALUES ('admin', '$2a$10$GjZcY8r8QErzCiUXfNC34eqQGmD4RKj9VDMG42xIvJup92tK6LxwC')
ON DUPLICATE KEY UPDATE username = username;