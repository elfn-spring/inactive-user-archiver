CREATE TABLE users
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(255),
    email           VARCHAR(255),
    last_login_date DATE
);

CREATE TABLE archived_users
(
    id            BIGINT PRIMARY KEY,
    username      VARCHAR(255),
    email         VARCHAR(255),
    archived_date TIMESTAMP
);
