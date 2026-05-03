# Accomodo Backend Improvements Detailed Explanation

This document serves as an academic and technical deep-dive into the architectural enhancements made to the Accomodo Backend. It provides a comprehensive, line-by-line explanation of how core Advanced Java Concepts—Multithreading, the Collections Framework, JDBC refinements, Lambda Expressions, Persistent vs. Non-Persistent data management, Autoboxing/Unboxing, and Custom Exception Handling—were seamlessly integrated to create a robust, production-ready backend system.

---

## 1. Exception Handling (Custom Exceptions)

**Core Files Modified:** 
* `com.accomodo.exceptions.DatabaseOperationException.java`
* `com.accomodo.exceptions.AccommodationNotFoundException.java`

Exception handling is critical for building resilient applications. Instead of relying on generic Java exceptions (like `Exception` or `SQLException`), we created **Custom Exceptions**. This practice provides precise semantic meaning to errors and allows us to attach domain-specific data (like error codes or the specific IDs that caused the failure).

### The Unchecked Exception (`DatabaseOperationException`)
```java
public class DatabaseOperationException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime timestamp;

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause); 
        this.errorCode = "DB_ERR_" + System.currentTimeMillis();
        this.timestamp = LocalDateTime.now();
    }
}
```
* **Explanation:** 
  * By extending `RuntimeException`, we create an **unchecked exception**. This is an architectural decision: database connectivity failures are typically systemic and unrecoverable at runtime, so we don't want to force every method in the call stack to write boilerplate `try-catch` blocks.
  * The constructor takes the original `Throwable cause` (usually the raw `SQLException`). This "wraps" the technical SQL exception inside a business-logic exception, preventing database implementation details from leaking into our Servlet layer (encapsulation).
  * We also automatically generate a unique `errorCode` and `timestamp`, making debugging significantly easier when looking at application logs.

### The Checked Exception (`AccommodationNotFoundException`)
```java
public class AccommodationNotFoundException extends Exception {
    private final int requestedId;
    
    public AccommodationNotFoundException(String message, int requestedId) {
        super(message);
        this.requestedId = requestedId;
    }
}
```
* **Explanation:** 
  * By extending `Exception`, we create a **checked exception**. This forces developers calling methods that throw this exception (like `getAccommodationById()`) to explicitly handle the scenario where data is missing, guaranteeing robust application behavior.

---

## 2. Multithreading & Concurrency

**Core Files Modified:** 
* `com.accomodo.services.AsyncLogger.java`
* `com.accomodo.controllers.AccommodationServlet.java`

Multithreading is utilized to offload non-critical, potentially blocking operations from the main execution thread, thereby vastly improving the response time of the web server.

**The Runnable Service (`AsyncLogger.java`)**
```java
public class AsyncLogger implements Runnable {
    // ... constructor and fields ...

    @Override
    public void run() {
        try {
            Thread.sleep(500); // Simulates network latency or slow disk I/O
            writeToFile(fullLogEntry); // Appends to a physical log file
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }
}
```
* **Explanation:** 
  * Implementing the `Runnable` interface makes this class a blueprint for a concurrent task. The `run()` method defines exactly what work the background thread will execute. We simulate a 500-millisecond delay and write to a file, proving that complex tasks can occur without slowing the user experience.

**Executing the Thread (`AccommodationServlet.java`)**
```java
Thread loggerThread = new Thread(new AsyncLogger(logMessage, "INFO"));
loggerThread.start(); 
```
* **Explanation:** 
  * In the `doGet` method, just as the user requests data, we instantiate a new `Thread` object, inject our `AsyncLogger`, and call `.start()`.
  * **Crucial Concept:** Calling `.start()` tells the Java Virtual Machine (JVM) to allocate a new, separate thread of execution. The main Servlet thread *immediately* moves to the next line of code, parsing JSON and responding to the user, completely unaware of and unblocked by the background logging process.

---

## 3. Persistent vs. Non-Persistent Data & JDBC Enhancements

**Core Files Modified:** `AccommodationDAO.java`

**Persistent Data:** Data that survives application restarts (e.g., Accommodations stored in MySQL). We interact with this via JDBC.
**Non-Persistent Data:** Data stored temporarily in RAM (e.g., the `HashMap` cache). This is volatile but provides lightning-fast access.

**JDBC Enhancements**
```java
try (Statement stmt = connection.createStatement();
     ResultSet rs = stmt.executeQuery(query)) {
    
    while (rs.next()) {
        list.add(mapResultSetToAccommodation(rs));
    }
    
} catch (SQLException e) {
    throw new DatabaseOperationException("Failed DB connection", e);
}
```
* **Explanation:** 
  * We upgraded standard `try-catch` blocks to **Try-With-Resources**. The `Statement` and `ResultSet` are declared inside the parenthesis of the `try` block. The JVM guarantees that the `.close()` method will be called on these resources automatically the moment the block exits—whether normally or due to an exception. This completely eliminates resource and memory leaks associated with unclosed database connections.

---

## 4. The Collections Framework & Autoboxing

**Core Files Modified:** `AccommodationDAO.java`

We aggressively utilized the Java Collections framework to build a robust in-memory **Non-Persistent Data Cache**.

### 4.1 HashMap (O(1) Time Complexity Caching)
```java
private final Map<Integer, Accommodation> accommodationCache = new HashMap<>();
// ... later ...
accommodationCache.put(acc.getPropertyId(), acc);
```
* **Explanation:** We use a `HashMap` to cache the entire database in memory. `HashMap` uses hashing to provide O(1) (constant time) retrieval. 
* **Autoboxing Demonstration:** `acc.getPropertyId()` returns a primitive `int`. Our Map requires an Object (`Integer`). Java automatically "autoboxes" the primitive `int` into an `Integer` object behind the scenes.
* **Unboxing:** When we do `accommodationCache.get(id)`, Java searches using the Integer key. If we assign the result to a primitive, Java automatically "unboxes" the Object back to a primitive.

### 4.2 TreeMap (Automatic Sorting)
```java
Map<Double, Accommodation> sortedMap = new TreeMap<>();
for (Accommodation acc : accommodationCache.values()) {
    sortedMap.put(acc.getPrice() + (acc.getPropertyId() * 0.000001), acc);
}
```
* **Explanation:** `TreeMap` is backed by a Red-Black tree structure. It automatically sorts its entries based on the natural order of its keys. By using the `price` (which is autoboxed to `Double`) as the key, the map naturally and automatically sorts all accommodations from lowest to highest price. The microscopic ID addition prevents properties with identical prices from overwriting one another.

### 4.3 HashSet (Guaranteed Uniqueness)
```java
Set<String> uniqueAmenities = new HashSet<>();
// ... loop through amenities
uniqueAmenities.add(amenity.trim().toLowerCase()); 
```
* **Explanation:** `HashSet` does not permit duplicate entries. If we iterate over 100 properties, and 80 of them have "WiFi", the `HashSet` ensures "wifi" will only be added exactly once to our final collection.

### 4.4 TreeSet (Sorted Uniqueness)
```java
Set<String> sortedTypes = new TreeSet<>();
for (Accommodation acc : accommodationCache.values()) {
    sortedTypes.add(acc.getType());
}
```
* **Explanation:** `TreeSet` is the best of both worlds. It guarantees uniqueness (like `HashSet`) but also automatically sorts the items alphabetically (like `TreeMap`). This efficiently generates the exact data structure needed to populate a sorted dropdown menu on the frontend interface.

---

## 5. Functional Programming: Lambda Expressions & Stream API

**Core Files Modified:** `AccommodationDAO.java`

We replaced traditional, verbose `for` loops with modern, declarative Java Stream API methods and Lambda expressions.

```java
@Override
public List<Accommodation> filterProperties(double maxPrice, double maxDistance, String genPref) {
    return accommodationCache.values().stream()
            .filter(acc -> acc.getPrice() <= maxPrice)
            .filter(acc -> acc.getDistance() <= maxDistance)
            .filter(acc -> {
                if (genPref == null || "Any".equalsIgnoreCase(genPref)) return true;
                return acc.getGenderPref().equalsIgnoreCase(genPref);
            })
            .collect(Collectors.toList());
}
```
* **Explanation:**
  * `.stream()`: Transforms our static Collection into a dynamic data pipeline.
  * `.filter(acc -> ...)`: This is a **Lambda Expression**. It is an anonymous, inline implementation of the `Predicate` interface. It effectively means "take 'acc', and only let it pass through this filter if the condition following the arrow (->) is true".
  * We "chain" multiple filters together. This creates incredibly readable, declarative code that avoids deeply nested `if/else` logic blocks.
  * `.collect(Collectors.toList())`: The terminal operation that gathers the surviving objects that passed all filters back into a standard `List`.

---
*End of Document. The backend is now fully optimized utilizing industry-standard Object-Oriented and Functional Java concepts.*
