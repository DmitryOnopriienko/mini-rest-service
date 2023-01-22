DROP DATABASE IF EXISTS testdb;

CREATE DATABASE IF NOT EXISTS testdb;

USE testdb;

DROP TABLE IF EXISTS customer;

CREATE TABLE IF NOT EXISTS customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255)
);

DROP TABLE IF EXISTS waybill;

CREATE TABLE IF NOT EXISTS waybill (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `type` VARCHAR(255) NOT NULL,
    `date` DATE NOT NULL,
    customer_id INT NOT NULL,
    CONSTRAINT fk_customer_id
        FOREIGN KEY (customer_id) REFERENCES customer(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
