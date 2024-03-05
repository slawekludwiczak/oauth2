drop table if exists authorities;
drop table if exists users;
drop table if exists product;

create table application_user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL
);

create table user_scope
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        varchar(30)  NOT NULL,
    description varchar(200) NOT NULL
);

create table user_scopes
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    scope_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES application_user(id),
    FOREIGN KEY (scope_id) REFERENCES user_scope(id)
);

create table product
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    varchar(250) not null,
    price   double not null
);