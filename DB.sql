create
database train_booking_system;

USE
train_booking_system;

-- Database: train_booking_system

-- Table: users
CREATE TABLE `users`
(
    `user_id`      int          NOT NULL AUTO_INCREMENT,
    `full_name`    varchar(100) NOT NULL,
    `email`        varchar(100) NOT NULL,
    `password`     varchar(50)  NOT NULL,
    `phone_number` varchar(20)  NOT NULL,
    `address`      varchar(200) NOT NULL,
    `role`         enum('admin','user') NOT NULL DEFAULT 'user',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `email_UNIQUE` (`email`)
);

-- Table: stations
CREATE TABLE `stations`
(
    `station_id`   int          NOT NULL AUTO_INCREMENT,
    `station_name` varchar(100) NOT NULL,
    PRIMARY KEY (`station_id`),
    UNIQUE KEY `station_name_UNIQUE` (`station_name`)
);

-- Table: trains
CREATE TABLE `trains`
(
    `train_id`   int         NOT NULL AUTO_INCREMENT,
    `train_name` varchar(50) NOT NULL,
    PRIMARY KEY (`train_id`),
    UNIQUE KEY `train_name` (`train_name`)
);

-- Table: trips
CREATE TABLE `trips`
(
    `trip_id`         int          NOT NULL AUTO_INCREMENT,
    `station_from`    varchar(100) NOT NULL,
    `station_to`      varchar(100) NOT NULL,
    `train_name`      varchar(50)           DEFAULT NULL,
    `available_seats` int          NOT NULL DEFAULT '50',
    `journey_date`    varchar(15)  NOT NULL,
    `journey_time`    varchar(15)  NOT NULL,
    PRIMARY KEY (`trip_id`),
    KEY               `trips_ibfk_1_idx` (`station_from`),
    KEY               `trips_ibfk_2` (`station_to`),
    CONSTRAINT `trips_ibfk_1` FOREIGN KEY (`station_from`) REFERENCES `stations` (`station_name`),
    CONSTRAINT `trips_ibfk_2` FOREIGN KEY (`station_to`) REFERENCES `stations` (`station_name`)
);

-- Table: booking
CREATE TABLE `booking`
(
    `booking_id`   int NOT NULL AUTO_INCREMENT,
    `user_id`      int          DEFAULT NULL,
    `trip_id`      int          DEFAULT NULL,
    `seat_number`  int          DEFAULT NULL,
    `booking_time` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`booking_id`),
    KEY            `user_id` (`user_id`),
    KEY            `trip_id` (`trip_id`),
    CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
    CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`)
);

-- Table: seat_booked
CREATE TABLE `seat_booked`
(
    `seat_id`     int NOT NULL AUTO_INCREMENT,
    `trip_id`     int DEFAULT NULL,
    `booking_id`  int DEFAULT NULL,
    `seat_number` int DEFAULT NULL,
    PRIMARY KEY (`seat_id`),
    KEY           `trip_id` (`trip_id`),
    KEY           `booking_id` (`booking_id`),
    CONSTRAINT `seat_booked_ibfk_1` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`trip_id`),
    CONSTRAINT `seat_booked_ibfk_2` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`)
);