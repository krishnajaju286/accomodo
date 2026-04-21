# Accomodo – Living Made Easier
## Academic BTech Project Submission

**Accomodo** is a Java Object-Oriented Programming (OOP) based full-stack platform designed to simplify the accommodation search process for students near educational campuses (like UPES Bidholi/Kandoli).

### Tech Stack Details
1. **Frontend**: HTML5, Vanilla CSS3 (Custom Glassmorphism Design System), JavaScript.
2. **Backend**: Core Java (Java 11+), Java Servlets API.
3. **Database**: MySQL Server.
4. **Connectivity**: JDBC (Java Database Connectivity) via Singleton Pattern.
5. **Build Tool**: Maven (`pom.xml` handles the servlet and mysql-connector dependencies).

### Object-Oriented Principles Applied
- **Abstraction**: `Accommodation` abstract base class.
- **Inheritance**: `Hostel`, `PG`, and `Flat` classes extending the base class.
- **Polymorphism**: Overridden `getSpecificDetails()` representing dynamic behavior.
- **Encapsulation**: Private fields accessed via carefully constructed getters/setters.
- **Interfaces**: Behaviors mapped via `Searchable` and `Rateable` interfaces.

### How to Run Locally

#### 1. Database Setup
1. Open MySQL Workbench or any MySQL client.
2. Run the SQL script found in `src/main/resources/schema.sql` to generate the `accomodo_db` database, tables, and initial dummy data.
3. Update your local database login credentials in `src/main/java/com/accomodo/utils/DatabaseConnection.java` (lines 12-13).

#### 2. Backend & Frontend Deployment (Tomcat)
1. Import the `accomodo` folder into your IDE (like IntelliJ IDEA Ultimate or Eclipse Enterprise) as a Standard Maven project.
2. Run `mvn clean install` to automatically download the Servlet API, MySQL Connector, and Gson libraries.
3. Configure a Local Apache Tomcat Server (v9+) in your IDE.
4. Deploy the `accomodo.war` artifact to the Tomcat server.
5. Start the server! Open your browser and navigate to `http://localhost:8080/accomodo/index.html` to experience the dynamic frontend UI!

> Note: All HTML files and the `style.css` are located in the `src/main/webapp/` directory and can be viewed as static UI files independently if the server is off.
