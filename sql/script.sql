-- CREATING

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
    price FLOAT NOT NULL,
    `date` DATE NOT NULL,
    customer_id INT NOT NULL,
    CONSTRAINT fk_customer_id
        FOREIGN KEY (customer_id) REFERENCES customer(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- INSERTING DATA

INSERT INTO customer(name, surname, patronymic)
VALUES ('Dmitry', 'Onopriienko', 'Foobar');

INSERT INTO customer(name, surname)
VALUES ('Ivan', 'Petrov');

INSERT INTO waybill(type, price, date, customer_id)
VALUES ('ocean-shipping', 50.99, '2023-01-22', 1),
       ('air-drop', 70.99, '2023-01-20', 1),
       ('car-delivery', 20.99, '2023-01-17', 1),
       ('postman-delivery', 7.99, '2023-01-18', 2),
       ('bike-delivery', 9.99, '2023-01-22', 2);
