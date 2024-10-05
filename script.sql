DROP TABLE IF EXISTS area;
DROP TABLE IF EXISTS "user";

CREATE TABLE IF NOT EXISTS "user" (
    login varchar(45) PRIMARY KEY,
    name varchar(45) NOT NULL,
    password varchar(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS area (
    id serial PRIMARY KEY,
    name varchar(45) NOT NULL,
    longitude DECIMAL(8, 2) NOT NULL,
    latitude DECIMAL(8, 2) NOT NULL,
    radius DECIMAL(8, 2) NOT NULL,
    user_login varchar(45) NOT NULL,
    location varchar(255) NOT NULL,
    FOREIGN KEY (user_login) REFERENCES "user"(login)
);