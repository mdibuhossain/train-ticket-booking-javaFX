create database train_booking_system;

USE train_booking_system;

ALTER TABLE `train_booking_system`.`users` 
ADD UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE;
;


CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL
);

CREATE TABLE stations (
    station_id INT PRIMARY KEY AUTO_INCREMENT,
    station_name VARCHAR(100) NOT NULL
);

CREATE TABLE trips (
    trip_id INT PRIMARY KEY AUTO_INCREMENT,
    source_station_id INT,
    destination_station_id INT,
    trip_time DATETIME,
    total_seats INT,
    FOREIGN KEY (source_station_id) REFERENCES stations(station_id),
    FOREIGN KEY (destination_station_id) REFERENCES stations(station_id)
);

CREATE TABLE booking (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    trip_id INT,
    seat_number INT,
    booking_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (trip_id) REFERENCES trips(trip_id)
);

CREATE TABLE seat_booked (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    trip_id INT,
    booking_id INT,
    seat_number INT,
    FOREIGN KEY (trip_id) REFERENCES trip(trip_id),
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);