DROP TABLE IF EXISTS users, activities;

CREATE TABLE users (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    username            VARCHAR(255)    NOT NULL,
    password            VARCHAR(255)    NOT NULL,
    creation_date       DATE            NOT NULL
);

CREATE TABLE activities (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    type                VARCHAR(255)    NOT NULL,
    creation_date       DATE            NOT NULL,
    user_id             VARCHAR(255)    NOT NULL REFERENCES users(id)
);

CREATE TABLE challenges (
    id                  VARCHAR(255)    NOT NULL PRIMARY KEY,
    activity_type       VARCHAR(255)    NOT NULL,
    amount_of_times_day INTEGER         NOT NULL,
    start_date          DATE            NOT NULL,
    expiration_date     DATE            NOT NULL,
    challenge_status    VARCHAR(255)    NOT NULL,
    user_id             VARCHAR(255)    NOT NULL REFERENCES users(id)
);
