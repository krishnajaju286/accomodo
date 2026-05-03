-- Run this script in your MySQL Workbench or XAMPP phpMyAdmin
-- to create the tables required for Authentication and Recommendation Tracking

CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    gender VARCHAR(20),
    preferences VARCHAR(255),
    budget VARCHAR(50),
    food_pref VARCHAR(50),
    sharing_pref VARCHAR(50),
    user_type VARCHAR(20) DEFAULT 'Student'
);

CREATE TABLE IF NOT EXISTS User_Searches (
    search_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    search_query VARCHAR(255),
    search_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS User_Views (
    view_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    property_id INT,
    view_count INT DEFAULT 1,
    last_viewed TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_property (user_id, property_id)
);
