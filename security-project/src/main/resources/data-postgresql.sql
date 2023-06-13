INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO roles (name) VALUES ('ROLE_OWNER') ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO roles (name) VALUES ('ROLE_RENTER') ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type, failed_attempt, lock_time, act_string, pin)
VALUES (1, 'AdminIme', 'AdminPrezime', '{bcrypt}$2a$10$a7zldOJKb..18dftiaRZmOqMlnt9lUxjG/6BYhLFVc5FEqhwgiz0u', 'admin@gmail.com', false, true, null, 'SuperAdmin', 0, null, 'actadmin', 100001)
    ON CONFLICT (user_id) DO UPDATE SET
    name = EXCLUDED.name,
                                 surname = EXCLUDED.surname,
                                 password = EXCLUDED.password,
                                 email = EXCLUDED.email,
                                 locked = EXCLUDED.locked,
                                 enabled = EXCLUDED.enabled,
                                 last_password_reset_date = EXCLUDED.last_password_reset_date,
                                 type = EXCLUDED.type,
                                 failed_attempt = EXCLUDED.failed_attempt,
                                 lock_time = EXCLUDED.lock_time,
                                 act_string = EXCLUDED.act_string,
                                 pin = EXCLUDED.pin;

-- Similar updates for other users and roles

INSERT INTO houses (home_id, address, is_active)
VALUES (1, 'Bulevar Despota Stefana 7, Novi Sad', true),
       (2, 'Strazilovska 15, Novi Sad', true)
    ON CONFLICT (home_id) DO UPDATE SET
    address = EXCLUDED.address,
   is_active = EXCLUDED.is_active;

-- Similar updates for other tables

INSERT INTO devices (device_id, house_id, type, read_time, filter_regex, name, is_active)
VALUES (1, 1, 'SMART_CAM', 3, '', 'CAM_1', true),
       (2, 1, 'SMART_SMOKE', 5, '', 'SMOKE_1', true)
    ON CONFLICT (device_id) DO UPDATE SET
    house_id = EXCLUDED.house_id,
                                   type = EXCLUDED.type,
                                   read_time = EXCLUDED.read_time,
                                   filter_regex = EXCLUDED.filter_regex,
                                   name = EXCLUDED.name,
                                   is_active = EXCLUDED.is_active;
