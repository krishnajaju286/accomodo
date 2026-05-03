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
-- Insert Real Property Data
INSERT INTO Accommodations (name, type, price, distance, capacity, gender_pref, food_available, curfew_time, is_available, image_url, amenities, description) VALUES
('UNIK Boys Hostel', 'Hostel', 12500, 0.8, 150, 'Boys', TRUE, '22:00:00', TRUE, 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?auto=format&fit=crop&q=80', 'WiFi, AC, Laundry', 'Premium boys hostel with top-notch security and amenities.'),
('Green Hostel', 'Hostel', 11000, 1.2, 100, 'Boys', TRUE, '21:30:00', TRUE, 'https://images.unsplash.com/photo-1595526114101-11b0e3be5384?auto=format&fit=crop&q=80', 'WiFi, Garden, Meals', 'Eco-friendly environment with home-style food.'),
('Silver Keys PG', 'PG', 9500, 0.5, 40, 'Boys', TRUE, '22:30:00', TRUE, 'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&q=80', 'WiFi, Security, RO', 'Budget-friendly PG option for students prioritizing location.'),
('Aravli Boys Hostel', 'Hostel', 11500, 1.0, 120, 'Boys', TRUE, '21:00:00', TRUE, 'https://images.unsplash.com/photo-1554995207-c18c203602cb?auto=format&fit=crop&q=80', 'Library, WiFi, Meals', 'Quiet and study-focused atmosphere near Bidholi.'),
('Blessing Home', 'Hostel', 13000, 0.4, 80, 'Boys', TRUE, '22:00:00', TRUE, 'https://images.unsplash.com/photo-1540518614846-7eded433c457?auto=format&fit=crop&q=80', 'WiFi, Security, Meals', 'Safe and welcoming environment for freshers.'),
('Sarthak Hostel', 'Hostel', 12000, 1.8, 200, 'Boys', TRUE, '21:30:00', TRUE, 'https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&q=80', 'WiFi, Laundry, Mess', 'Large hostel with spacious rooms and good mess facilities.'),
('Marsal House', 'Hostel', 14000, 0.3, 60, 'Boys', TRUE, '22:00:00', TRUE, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80', 'AC, WiFi, Gym', 'Modern living with luxury amenities very close to campus.'),
('Rennes House', 'Hostel', 13500, 0.6, 90, 'Boys', TRUE, '21:00:00', TRUE, 'https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&q=80', 'Security, WiFi, Meals', 'Reliable security and high-speed internet for tech students.'),
('Kohsamui House', 'Hostel', 15000, 0.2, 50, 'Boys', TRUE, '22:30:00', TRUE, 'https://images.unsplash.com/photo-1502672260266-1c1e524164ed?auto=format&fit=crop&q=80', 'Pool, WiFi, AC', 'Luxury hostel featuring premium recreation facilities.'),
('Crystal Crown Girls Hostel', 'Hostel', 14500, 0.4, 100, 'Girls', TRUE, '20:30:00', TRUE, 'https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&q=80', 'WiFi, Security, AC', 'Elegant and safe living space specifically for girls.'),
('Elemento Girls Hostel', 'Hostel', 15500, 0.6, 150, 'Girls', TRUE, '21:00:00', TRUE, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&q=80', 'WiFi, Gym, Meals', 'Premium girls hostel with dedicated bus service and gym.'),
('Gera Girls Hostel', 'Hostel', 12500, 1.3, 80, 'Girls', TRUE, '20:00:00', TRUE, 'https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&q=80', 'WiFi, Laundry, Security', 'Comfortable and affordable girls hostel in the Bidholi area.'),
('Peral Residency Girls Hostel', 'Hostel', 13000, 0.9, 110, 'Girls', TRUE, '21:00:00', TRUE, 'https://images.unsplash.com/photo-1588854337236-6889d631faa8?auto=format&fit=crop&q=80', 'Security, WiFi, Meals', 'Safety-first hostel with 24/7 security and home-like food.'),
('Stanza Living Girls PG', 'PG', 16000, 0.1, 50, 'Girls', TRUE, '21:30:00', TRUE, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80', 'WiFi, Housekeeping, Lounge', 'Professional co-living space from a leading brand.'),
('Malti Girls Flat', 'Flat', 18000, 1.8, 3, 'Girls', FALSE, '23:59:00', TRUE, 'https://images.unsplash.com/photo-1502005097973-6a7082348e28?auto=format&fit=crop&q=80', 'Kitchen, Balcony, WiFi', 'Independent flat for girls seeking privacy and space.'),
('Scholars Girls PG', 'PG', 11000, 1.0, 30, 'Girls', TRUE, '20:30:00', TRUE, 'https://images.unsplash.com/photo-1542314831-c6a4d14b18c0?auto=format&fit=crop&q=80', 'Library, WiFi, Meals', 'Quiet PG for serious students near the library.'),
('UPES Off Campus Girls Hostel', 'Hostel', 9500, 3.5, 200, 'Girls', TRUE, '20:00:00', TRUE, 'https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&q=80', 'Bus, Meals, Security', 'Affordable off-campus hostel with reliable transport.'),
('Green View Girls Flat', 'Flat', 22000, 2.2, 4, 'Girls', FALSE, '23:59:00', TRUE, 'https://images.unsplash.com/photo-1502672260266-1c1e524164ed?auto=format&fit=crop&q=80', 'Furnished, Parking, Balcony', 'Spacious 3BHK flat with a great view and modern interiors.'),
('Harjas Girls Flat', 'Flat', 20000, 1.6, 2, 'Girls', FALSE, '23:59:00', TRUE, 'https://images.unsplash.com/photo-1536376072261-38c75010e6c9?auto=format&fit=crop&q=80', 'Kitchen, WiFi, AC', 'Compact and comfortable 2BHK flat for girls.'),
('Woodstock Girls Hostel', 'Hostel', 14000, 0.7, 90, 'Girls', TRUE, '21:00:00', TRUE, 'https://images.unsplash.com/photo-1592595896616-c37162298647?auto=format&fit=crop&q=80', 'WiFi, AC, Laundry', 'Premium amenities and vibrant common areas for students.'),
('Aura Girls Flats', 'Flat', 19000, 1.4, 1, 'Girls', FALSE, '23:59:00', TRUE, 'https://images.unsplash.com/photo-1600607686527-6fb886090705?auto=format&fit=crop&q=80', 'Modern, Security, AC', 'Luxury studio apartment with high-end finishes.'),
('Krishna Girls Hostel', 'Hostel', 13500, 1.2, 70, 'Girls', TRUE, '20:30:00', TRUE, 'https://images.unsplash.com/photo-1565538810643-b5bdb714032a?auto=format&fit=crop&q=80', 'WiFi, Meals, Laundry', 'Reliable and safe girls hostel in the heart of Bidholi.');

INSERT INTO Users (name, email, password, user_type) VALUES 
('Test Student', 'student@upes.ac.in', 'password123', 'Student');
