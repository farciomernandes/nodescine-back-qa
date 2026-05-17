-- Seed default users for each role and one user with all roles
-- Password for all seeded users: 123 (stored as bcrypt hash)

-- Use a single precomputed bcrypt hash for the password "123"
-- Note: this file is idempotent and will not duplicate users or roles if run multiple times.

DO $$
BEGIN

-- Customer
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'customer@nodescine.local') THEN
    INSERT INTO users (email, password, name, status)
    VALUES ('customer@nodescine.local', '$2b$10$d0J0ByE0G9j41OkkPSaV0eHZhqR/sVk5BOFPzqhI8ck6OoD/AVqrC', 'Customer User', 'ACTIVE');
END IF;

-- Director
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'director@nodescine.local') THEN
    INSERT INTO users (email, password, name, status)
    VALUES ('director@nodescine.local', '$2b$10$d0J0ByE0G9j41OkkPSaV0eHZhqR/sVk5BOFPzqhI8ck6OoD/AVqrC', 'Director User', 'ACTIVE');
END IF;

-- Moderator
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'moderator@nodescine.local') THEN
    INSERT INTO users (email, password, name, status)
    VALUES ('moderator@nodescine.local', '$2b$10$d0J0ByE0G9j41OkkPSaV0eHZhqR/sVk5BOFPzqhI8ck6OoD/AVqrC', 'Moderator User', 'ACTIVE');
END IF;

-- Super (all roles)
IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'super@nodescine.local') THEN
    INSERT INTO users (email, password, name, status)
    VALUES ('super@nodescine.local', '$2b$10$d0J0ByE0G9j41OkkPSaV0eHZhqR/sVk5BOFPzqhI8ck6OoD/AVqrC', 'Super User', 'ACTIVE');
END IF;

-- Insert roles for each user if not present
-- CUSTOMER
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'CUSTOMER' FROM users u
WHERE u.email = 'customer@nodescine.local'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'CUSTOMER'
  );

-- MOVIE_DIRECTOR
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'MOVIE_DIRECTOR' FROM users u
WHERE u.email = 'director@nodescine.local'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'MOVIE_DIRECTOR'
  );

-- MODERATOR
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'MODERATOR' FROM users u
WHERE u.email = 'moderator@nodescine.local'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'MODERATOR'
  );

-- SUPER USER (all roles)
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'CUSTOMER' FROM users u
WHERE u.email = 'super@nodescine.local'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'CUSTOMER'
  );

INSERT INTO user_roles (user_id, role)
SELECT u.id, 'MOVIE_DIRECTOR' FROM users u
WHERE u.email = 'super@nodescine.local'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'MOVIE_DIRECTOR'
  );

INSERT INTO user_roles (user_id, role)
SELECT u.id, 'MODERATOR' FROM users u
WHERE u.email = 'super@nodescine.local'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'MODERATOR'
  );

END $$;

