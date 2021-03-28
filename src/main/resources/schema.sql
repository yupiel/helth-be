DROP TABLE IF EXISTS users, activities;

CREATE TABLE users (
    id              VARCHAR(255) NOT NULL PRIMARY KEY,
    username        VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    creation_date   DATE         NOT NULL
);

CREATE TABLE activities (
    id              VARCHAR(255)    NOT NULL PRIMARY KEY,
    type            VARCHAR(255)    NOT NULL,
    creation_date   DATE            NOT NULL,
    user_id         VARCHAR(255)    NOT NULL REFERENCES users(id)
);

INSERT INTO users (id, username, password, creation_date) VALUES
('c776e082-6407-49a5-a246-9d7265fc2583', 'Yupiel', '$2y$12$VAcIZbFYSzidr5f9DUr3t.Cl0X/5dchQZiq9haWEDkePGK.4oQvdS', '2021-03-20');

INSERT INTO activities(id, type, creation_date, user_id) VALUES
('10af72c0-b4df-4a8b-b80f-49b5a46ba4a0', 'DRINK_WATER', '2021-03-24', 'c776e082-6407-49a5-a246-9d7265fc2583'),
('0e1d87fd-1ba5-41d5-9f8a-321a4f52d507', 'WALKING', '2021-03-24', 'c776e082-6407-49a5-a246-9d7265fc2583'),
('ae23daae-7cef-4752-b951-a73d899348d9', 'RUNNING', '2021-03-24', 'c776e082-6407-49a5-a246-9d7265fc2583');