# ACCOMODO – Living Made Easier
## Academic Project Documentation

### 1. Project Introduction
With the growing number of students and faculty members relocating to areas near UPES Dehradun in Uttarakhand (specifically Bidholi and Kandoli), finding suitable accommodation has become a significantly challenging and time-consuming task. Many individuals struggle with information scattered across local brokers, outdated listings, and unverified properties. 

**Accomodo** is a digital solution that bridges this gap. It is a Java Object-Oriented Programming (OOP) based full-stack platform designed to simplify the accommodation search process. By providing a centralized, smart listing platform, Accomodo enables users to discover, compare, and connect with hostels, PGs, flats, and rental accommodations efficiently.

### 2. Problem Statement
Newcomers to educational hubs like UPES face considerable difficulty in finding verified and suitable accommodation options. The primary challenges include:
- Lack of centralized and reliable information regarding available properties.
- Difficulty in filtering properties based on budget, distance from the college, safety, and specific timings.
- Unavailability of transparent details about amenities, food availability, and resident reviews.
- Inefficient comparison methods leading to suboptimal decision-making and potential fraud.

### 3. Objectives of the Project
The primary objectives of building the Accomodo platform are to:
- **Simplify property discovery** by providing a unified digital platform for all local accommodations.
- **Enable smart comparison** of properties based on various quantitative and qualitative parameters (price, distance, ratings).
- **Improve decision making** for students and faculty by providing verified details, reviews, and dynamic availability indicators.
- **Create a centralized listing platform** that brings property owners and seekers together efficiently.

### 4. Tagline Suggestions
- *Accomodo – Living Made Easier* (Primary)
- *Accomodo – Your Home Away from Home*
- *Accomodo – Discover Your Perfect Stay*
- *Accomodo – Smart Living Near Campus*
- *Accomodo – Simplified Stays for Students & Faculty*

### 5. Scope of the System
The system is designed to cater to three main categories of users:
- **Student Users:** Can search, filter, view details, bookmark favorites, and review accommodations.
- **Faculty Users:** Have similar access to students, potentially with specific filters for faculty-preferred or family-friendly accommodations.
- **Property Owners/Administrators:** (Future Scope/Admin view) Can list their properties, update availability, and manage details and images.

### 6. System Features
- **Homepage Recommendations:** Displays the top 10 best-rated or best-selling property listings to guide new users.
- **Direct Search:** A direct property name search bar for users who already have a specific accommodation in mind.
- **Advanced Search Filters:** Comprehensive filtering based on:
  - Price Range
  - Distance from College (UPES Bidholi/Kandoli)
  - Number of Residents (Occupancy type)
  - Availability status
  - In and Out Timings (Curfew)
  - Gender Preference (Boys/Girls/Co-ed)
  - Food Availability (Included/Not Included)
  - Rating and Amenities (WiFi, Laundry, AC, etc.)
- **Property Detail Page:** A dedicated page for each property showing comprehensive details, images, and contact info.
- **Sorting Options:** Users can sort results by cheapest first, closest first, highest rated, and a balanced smart recommendation algorithm.
- **Favorites/Bookmark Feature:** Allows users to save properties for later review and comparison.
- **Rating and Review System:** Functionality for users to leave feedback and rate their stay.
- **Availability Indicator:** Real-time or regularly updated indicators showing if rooms are currently available.

### 7. Complete Tech Stack Recommendation
- **Frontend:** HTML, CSS, JavaScript (Alternatively, a Java Swing-based UI if built as a pure desktop application).
- **Backend:** Core Java utilizing strict Object-Oriented Programming principles.
- **Database:** MySQL relational database for robust data management.
- **Connectivity:** Java Database Connectivity (JDBC) to bridge the backend Java application with the MySQL database.

### 8. System Architecture
The application follows a standard **3-Tier Architecture**:
1. **Presentation Layer (Frontend):** Handles the User Interface (HTML/CSS/JS or Swing). It takes user inputs (like search parameters) and displays the results (property listings).
2. **Business Logic Layer (Backend - Java):** Contains the core application logic. It processes the searches, handles the sorting/filtering algorithms, logic for user sessions, and applies Object-Oriented principles to model accommodations.
3. **Database Layer (MySQL):** Stores all persistent data including user profiles, property details, amenities, ratings, and favorites. Accessed via JDBC.

### 9. Database Design
The MySQL database will encompass several key tables:
- **`Users`**: `user_id` (PK), `name`, `email`, `password`, `user_type` (Student/Faculty).
- **`Accommodations`**: `property_id` (PK), `name`, `type` (Hostel/PG/Flat), `price`, `distance`, `capacity`, `gender_pref`, `food_available`, `curfew_time`, `is_available`.
- **`Ratings`**: `rating_id` (PK), `property_id` (FK), `user_id` (FK), `score`, `review_text`.
- **`Favorites`**: `favorite_id` (PK), `user_id` (FK), `property_id` (FK).

### 10. OOP Concepts Used in the Project
The backend is heavily reliant on Java's OOP paradigms:
- **Abstraction:** An abstract base class `Accommodation` defines common properties (e.g., ID, name, location) and abstract methods (`calculateRent()`, `displayDetails()`) that hide complex implementation details from the user.
- **Inheritance:** Concrete subclasses like `Hostel`, `PG`, and `Flat` inherit from the `Accommodation` class, reusing common variables while allowing for specific features (e.g., `Flat` might have a `bhkCount`, while `Hostel` has `messTimings`).
- **Polymorphism:** Method overriding is prominently used. Calling `displayDetails()` on an `Accommodation` reference will execute the specific implementation of the `Hostel`, `PG`, or `Flat` object it points to.
- **Encapsulation:** All class attributes (like price, owner details) are kept `private`. They are accessed and modified strictly through `public` getters and setters to ensure data validation (e.g., preventing a negative price).
- **Interfaces:** Interfaces such as `Searchable` or `Rateable` define standard behaviors that different classes can implement.
- **Multithreading:** Used to improve responsiveness, such as loading heavy property listings in the background or handling multiple concurrent search requests asynchronously.
- **Exception Handling:** Try-catch blocks are extensively used to gracefully handle SQL exceptions during database operations via JDBC, ensuring the application doesn't crash upon a lost connection.
- **Singleton Pattern:** Employed for the Database Connection class to ensure only one instance of the JDBC connection is created and reused throughout the application lifecycle, optimizing resources.

### 11. Frontend Website Structure
The user interface is broken down into 5 primary pages:
1. **Homepage:** Features the "Living Made Easier" tagline, a prominent central search bar, and a grid showing the top 10 property listings.
2. **Search Results Page:** Displays a list/grid of properties matching the user's query, complemented by a sidebar containing the advanced filters and sorting dropdowns.
3. **Property Details Page:** Shows in-depth information about a specific property, including image galleries, the list of amenities, policies (timings, food), and user reviews.
4. **User Favorites / Profile Page:** A personalized dashboard where users can view their saved/bookmarked properties and manage their profile details.
5. **Contact / About Page:** Information about the platform, terms of service, and a contact form for support or owner listing requests.

### 12. User Flow
1. **Entry:** User opens the website and lands on the Homepage.
2. **Discovery:** User either clicks on a top 10 recommendation or enters a query in the search bar.
3. **Refinement:** The user is taken to the Search Results page, where they apply filters (e.g., Boys PG, < ₹10,000, distance < 2km) to narrow down choices.
4. **Detailing:** The user clicks on a listing that catches their eye, opening the Property Details page to read reviews and check the curfew/food policies.
5. **Action:** The user bookmarks the property to their Favorites for later discussion with parents or friends.

### 13. Future Enhancements
- **Map Integration:** Embedding Google Maps API to visualize property locations relative to the UPES campus directly on the search results page.
- **AI-Based Recommendations:** Implementing machine learning to suggest properties based on user browsing behavior and preferences.
- **Mobile App Version:** Developing cross-platform mobile apps for Android and iOS for accessibility on the go.
- **Owner Listing Portal:** A dedicated dashboard for property owners to directly onboard, update occupancies, and manage payments.

### 14. Conclusion
The "Accomodo" platform serves as a vital bridge between students, faculty, and local property owners in the fast-growing educational hub of UPES. By leveraging robust Java Object-Oriented principles and an efficient database design, the system provides a structured, scalable, and highly user-friendly approach to accommodation discovery. Accomodo not only simplifies the search process but significantly improves transparent decision-making, truly making living made easier for the academic community.
