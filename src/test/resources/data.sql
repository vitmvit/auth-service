INSERT INTO "users" ("id", "login", "password", "role")
VALUES (1, 'Admin', '$2a$10$SHt9oDxjugnvbgw2gNsp6.Lz9PjjkL0E7rVVBU.g7P7nBlMWeLQZG', 'ADMIN'),
       (2, 'username123', '$2a$10$SHt9oDxjugnvbgw2gNsp6.Lz9PjjkL0E7rVVBU.g7P7nBlMWeLQZG', 'SUBSCRIBER');

ALTER TABLE "users" DROP CONSTRAINT users_pkey;