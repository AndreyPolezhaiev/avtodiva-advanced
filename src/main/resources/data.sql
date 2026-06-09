INSERT INTO roles (name, is_deleted) VALUES ('ROLE_USER', false) ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name, is_deleted) VALUES ('ROLE_INSTRUCTOR', false) ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name, is_deleted) VALUES ('ROLE_ADMIN', false) ON CONFLICT (name) DO NOTHING;