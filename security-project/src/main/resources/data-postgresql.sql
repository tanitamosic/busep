INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_OWNER') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_RENTER') ON CONFLICT DO NOTHING;

-- password: admin
INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type, failed_attempt, lock_time)
    VALUES (1, 'AdminIme', 'AdminPrezime', '{bcrypt}$2a$10$a7zldOJKb..18dftiaRZmOqMlnt9lUxjG/6BYhLFVc5FEqhwgiz0u', 'admin@gmail.com', false, true, null, 'SuperAdmin', 0, null)
        ON CONFLICT DO NOTHING;
-- password: vlasnik
INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type, failed_attempt, lock_time)
    VALUES (2, 'VlasnikIme', 'VlasnikPrezime', '{bcrypt}$2a$10$DAiZpF0xgqYi1RIKr/J1FOcFNMgbO8BQMhx02/UmBEqafS7e/czYa', 'vlasnik@gmail.com', false, true, null, 'StandardUser', 0, null)
        ON CONFLICT DO NOTHING;
-- password: stanar
INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type, failed_attempt, lock_time)
    VALUES (3, 'StanarIme', 'StanarPrezime', '{bcrypt}$2a$10$2aQcJkVqNDeQHXunk25tLOgqRlZF4zlg1H9zqtayUzspusVugED5y', 'stanar@gmail.com', false, true, null, 'StandardUser', 0, null)
        ON CONFLICT DO NOTHING;

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2) ON CONFLICT DO NOTHING;
INSERT INTO users_roles (user_id, role_id) VALUES (3, 3) ON CONFLICT DO NOTHING;
