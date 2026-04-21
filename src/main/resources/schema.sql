-- Database creation
CREATE DATABASE IF NOT EXISTS accomodo_db;
USE accomodo_db;

-- Users table
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type ENUM('Student', 'Faculty', 'Admin') DEFAULT 'Student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Accommodations Base Table
CREATE TABLE IF NOT EXISTS Accommodations (
    property_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    type ENUM('Hostel', 'PG', 'Flat') NOT NULL,
    price DOUBLE NOT NULL,
    distance DOUBLE NOT NULL,
    capacity INT NOT NULL,
    gender_pref ENUM('Boys', 'Girls', 'Co-ed') NOT NULL,
    food_available BOOLEAN DEFAULT FALSE,
    curfew_time TIME DEFAULT '22:00:00',
    is_available BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(255) DEFAULT 'assets/placeholder.jpg',
    amenities VARCHAR(500),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Note: In a fully normalized OOP mapping we could have separate tables for Hostel/PG/Flat,
-- but the Single Table Inheritance strategy is perfectly viable for this academic JDBC project.

-- Ratings and Reviews
CREATE TABLE IF NOT EXISTS Ratings (
    rating_id INT AUTO_INCREMENT PRIMARY KEY,
    property_id INT,
    user_id INT,
    score INT CHECK (score BETWEEN 1 AND 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES Accommodations(property_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- User Favorites
CREATE TABLE IF NOT EXISTS Favorites (
    favorite_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    property_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (property_id) REFERENCES Accommodations(property_id) ON DELETE CASCADE,
    UNIQUE KEY unique_favorite (user_id, property_id)
);

-- Insert Dummy Data
INSERT INTO Accommodations (name, type, price, distance, capacity, gender_pref, food_available, curfew_time, is_available, image_url, amenities, description) VALUES
('UPES Premium Hostel', 'Hostel', 15000, 1.2, 200, 'Boys', TRUE, '21:30:00', TRUE, 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?auto=format&fit=crop&q=80', 'WiFi, AC, Laundry, Gym', 'Premium boys hostel near Bidholi campus.'),
('Kandoli Girls PG', 'PG', 8000, 0.5, 30, 'Girls', TRUE, '20:00:00', TRUE, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80', 'WiFi, RO Water, Security', 'Safe girls PG very close to Kandoli campus.'),
('Sunset View Flats', 'Flat', 22000, 3.5, 3, 'Co-ed', FALSE, '23:59:00', TRUE, 'https://images.unsplash.com/photo-1502672260266-1c1e524164ed?auto=format&fit=crop&q=80', 'Furnished, Parking, Balcony', 'Spacious 3 BHK for students or faculty.'),
('Bidholi Scholars Hub', 'Hostel', 12000, 2.0, 150, 'Girls', TRUE, '21:00:00', TRUE, 'https://images.unsplash.com/photo-1595526114101-11b0e3be5384?auto=format&fit=crop&q=80', 'WiFi, Library, Bus', 'Strict library hours and dedicated bus service.'),
('Harmony PG', 'PG', 7500, 1.0, 20, 'Boys', TRUE, '22:30:00', TRUE, 'https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&q=80', 'WiFi, Power Backup', 'Budget PG for students.');

INSERT INTO Users (name, email, password, user_type) VALUES 
('Test Student', 'student@upes.ac.in', 'password123', 'Student');
