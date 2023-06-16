INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO roles (name) VALUES ('ROLE_OWNER') ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name;
INSERT INTO roles (name) VALUES ('ROLE_RENTER') ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO users (user_id, name, surname, password, email, locked, enabled, last_password_reset_date, type, failed_attempt, lock_time, act_string, pin, is_active)
VALUES (1001, 'AdminIme', 'AdminPrezime', '{bcrypt}$2a$10$a7zldOJKb..18dftiaRZmOqMlnt9lUxjG/6BYhLFVc5FEqhwgiz0u', 'admin@gmail.com', false, true, null, 'SuperAdmin', 0, null, 'actadmin', 100001, true),
    (1002, 'VlasnikIme', 'VlasnikPrezime', '{bcrypt}$2a$10$DAiZpF0xgqYi1RIKr/J1FOcFNMgbO8BQMhx02/UmBEqafS7e/czYa', 'vlasnik@gmail.com', false, true, null, 'StandardUser', 0, null, 'actvlasnik', 100002, true),
   (1003, 'StanarIme', 'StanarPrezime', '{bcrypt}$2a$10$2aQcJkVqNDeQHXunk25tLOgqRlZF4zlg1H9zqtayUzspusVugED5y', 'stanar@gmail.com', false, true, null, 'StandardUser', 0, null, 'actstanar', 100003, true)
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
     pin = EXCLUDED.pin,
     is_active = EXCLUDED.is_active;

-- passwords for users are: admin, vlasnik, stanar ^^^

INSERT INTO users_roles (user_id, role_id)
    VALUES (1001, 1),
           (1002, 2),
           (1003, 3)
    ON CONFLICT (user_id, role_id) DO UPDATE SET user_id = EXCLUDED.user_id, role_id = EXCLUDED.role_id;

INSERT INTO houses (home_id, address, is_active)
VALUES (1001, 'Bulevar Despota Stefana 7, Novi Sad', true),
       (1002, 'Strazilovska 15, Novi Sad', true)
    ON CONFLICT (home_id) DO UPDATE SET
    address = EXCLUDED.address,
   is_active = EXCLUDED.is_active;

INSERT INTO house_user_roles (house_user_role_id, house_id, user_id, role, is_active)
VALUES (1001, 1001, 1002, 'OWNER', true),
       (1002, 1001, 1003, 'RENTER', true),
       (1003, 1002, 1002, 'OWNER', true)
    ON CONFLICT (house_user_role_id) DO UPDATE SET
    house_id = excluded.house_id,
    user_id = excluded.user_id,
    role = excluded.role,
    is_active = excluded.is_active;

INSERT INTO devices (device_id, house_id, type, read_time, filter_regex, name, is_active)
VALUES (1001, 1001, 'SMART_CAM', 3, '', 'CAM_1', true),
       (1002, 1001, 'SMART_SMOKE', 5, '', 'SMOKE_1', true)
    ON CONFLICT (device_id) DO UPDATE SET
    house_id = EXCLUDED.house_id,
   type = EXCLUDED.type,
   read_time = EXCLUDED.read_time,
   filter_regex = EXCLUDED.filter_regex,
   name = EXCLUDED.name,
   is_active = EXCLUDED.is_active;
