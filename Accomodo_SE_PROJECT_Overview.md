SOFTWARE ENGINEERING
PROJECT Overview
Name: Krishna Jaju
Enrollment No / SAP ID: 590015913
Batch: 1,ccvt

SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
Accomodo - Living Made Easier

1. Introduction
1.1 Purpose
The purpose of this Software Requirements Specification (SRS) document is to provide a comprehensive and detailed description of the Accomodo System. This document defines the system’s functionality, constraints, and operational requirements. It serves as a reference for developers, testers, and evaluators to understand how the system is expected to behave and perform.
The SRS ensures clarity in system design and helps in validating whether the developed system meets the specified requirements.

1.2 Scope
The Accomodo System is a web-based application developed using Java Servlets, HTML/CSS, and integrated with a MySQL database through JDBC connectivity.
The system provides functionalities such as:
User registration and login authentication
Viewing and filtering accommodations (Flats, Hostels, PGs)
Searching for specific accommodations based on names or types
Viewing detailed property information
Rating accommodations and viewing reviews
The system is intended to simplify the search for living spaces for students and professionals by showcasing database-driven application development with a web interface.

1.3 Definitions, Acronyms, and Abbreviations
Term - Description
GUI - Graphical User Interface
JDBC - Java Database Connectivity
DBMS - Database Management System
SRS - Software Requirements Specification
DAO - Data Access Object

1.4 References
Java SE Documentation
MySQL Official Documentation
JDBC API Guide
Software Engineering Principles
Java Servlets API Documentation

2. Overall Description
2.1 Product Perspective
The system is a web application following a multi-layered architecture:
Presentation Layer: HTML/CSS Web Pages
Business Logic Layer: Java Servlets and DAOs
Data Layer: MySQL database
The system interacts with the database through JDBC to perform CRUD operations.

2.2 Product Functions
The main functionalities include:
User Authentication (Login & Registration)
List and Display Accommodations
Search Accommodations
Filter Properties by Price, Distance, Gender Preference
Add Properties to Favorites

2.3 User Classes and Characteristics
User Type - Description
General User - Can login, search, and view accommodations.
Admin (implicit) - Full access to manage system functionalities.
Users are expected to have basic knowledge of operating a web browser.

2.4 Operating Environment
Operating System: Windows / Linux / macOS
Programming Language: Java (JDK 17+)
Web Framework: Java Servlets (Tomcat Server)
Database: MySQL
IDE: Visual Studio Code / Eclipse / IntelliJ

2.5 Design and Implementation Constraints
Web UI must be implemented using HTML, CSS, JavaScript.
Database must be MySQL.
JDBC must be used for database connectivity.
System should follow Object-Oriented Principles (OOP) such as polymorphism and inheritance.
System should be modular.

2.6 Assumptions and Dependencies
MySQL server must be running.
Required database and tables must exist.
Valid user credentials must be stored.
Tomcat server must be configured and running.

3. System Architecture
The system follows this architecture:
User → Web Browser (HTML/CSS) → Java Servlets → DAO Layer → JDBC → MySQL Database
The user interacts with Web Pages.
Web pages send HTTP requests to Servlets.
Servlets interact with DAO layer for business logic.
DAO interacts with database and returns results.

4. Functional Requirements

FR-01: User Login
Input: Username, Password
Processing: System validates credentials using SQL query. Compares input with database records.
Output: If valid → “Login Successful”. If invalid → “Invalid Credentials”.

FR-02: View Accommodations
Input: None
Processing: Fetch all records from database via AccommodationDAO.
Output: Display list of properties on index.html.

FR-03: Search Accommodations
Input: Search Query (Property Name or Type)
Processing: SQL LIKE query executed in AccommodationDAO.
Output: Matching property records displayed.

FR-04: Filter Accommodations
Input: Max Price, Max Distance, Gender Preference
Processing: SQL query to filter accommodations matching the criteria.
Output: Filtered list of properties.

5. Non-Functional Requirements
5.1 Performance
System should respond within 1–2 seconds.
Database queries must execute efficiently.
5.2 Usability
Web UI should be simple, intuitive, and responsive.
Users should require minimal training.
5.3 Reliability
System should not crash during execution.
Must handle invalid inputs gracefully.
5.4 Security
Login authentication must be enforced.
Unauthorized access to user profiles should be prevented.
5.5 Maintainability
Code should be modular (Controllers, Models, DAOs).
Easy to update and debug.

6. Domain / Business Requirements
Properties must have a valid price and distance.
Search must support partial matching.
Types of accommodations are restricted to Flat, PG, and Hostel.

7. Class Diagram Description
Classes:
1. Accommodation (Abstract)
Attributes: propertyId, name, type, price, distance, capacity, isAvailable, imageUrl, etc.
Methods: getSpecificDetails(), getters, setters
2. Flat, PG, Hostel
Inherits from: Accommodation
3. AccommodationDAO
Methods: getAllAccommodations(), searchByQuery(), filterProperties()
4. AccommodationServlet
Methods: doGet(), doPost()
5. DatabaseConnection
Method: getConnection() (Singleton pattern)

Relationships:
Flat, PG, Hostel → inherit from Accommodation
AccommodationDAO → implements Searchable interface
AccommodationDAO → depends on DatabaseConnection
AccommodationServlet → uses AccommodationDAO

8. Application Screenshots
[Insert relevant screenshots of Accomodo web pages]

9. Traceability Matrix
FR ID - Requirement - Method - Test Case
FR-01 - Login - validateUser() - TC01
FR-02 - View Properties - getAllAccommodations() - TC02
FR-03 - Search - searchByQuery() - TC03
FR-04 - Filter - filterProperties() - TC04

10. Database Design
CREATE TABLE Accommodations (
    property_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    price DOUBLE,
    distance DOUBLE,
    capacity INT,
    gender_pref VARCHAR(20),
    food_available BOOLEAN,
    curfew_time VARCHAR(20),
    is_available BOOLEAN,
    image_url VARCHAR(255),
    amenities TEXT,
    description TEXT
);

Conclusion
The Accomodo System effectively demonstrates the integration of Java Servlets with MySQL using JDBC. The system provides essential functionalities such as authentication, searching, and property management. It ensures usability, efficiency, and reliability, making it a suitable academic project that applies software engineering principles in real-world web applications.
