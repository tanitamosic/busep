INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_OWNER') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_RENTER') ON CONFLICT DO NOTHING;

INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type)
    VALUES (1, 'AdminIme', 'AdminPrezime', 'rawpassword', 'admin@gmail.com', false, true, null, 'SuperAdmin')
        ON CONFLICT DO NOTHING;
INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type)
    VALUES (2, 'VlasnikIme', 'VlasnikPrezime', 'rawpassword', 'vlasnik@gmail.com', false, true, null, 'StandardUser')
        ON CONFLICT DO NOTHING;
INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type)
    VALUES (3, 'StanarIme', 'StanarPrezime', 'rawpassword', 'stanar@gmail.com', false, true, null, 'StandardUser')
        ON CONFLICT DO NOTHING;

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2) ON CONFLICT DO NOTHING;
INSERT INTO users_roles (user_id, role_id) VALUES (3, 3) ON CONFLICT DO NOTHING;
