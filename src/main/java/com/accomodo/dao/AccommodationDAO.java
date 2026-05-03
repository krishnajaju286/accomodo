package com.accomodo.dao;

import com.accomodo.exceptions.AccommodationNotFoundException;
import com.accomodo.exceptions.DatabaseOperationException;
import com.accomodo.interfaces.Searchable;
import com.accomodo.models.Accommodation;
import com.accomodo.models.Hostel;
import com.accomodo.models.PG;
import com.accomodo.models.Flat;
import com.accomodo.utils.DatabaseConnection;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Access Object (DAO) Class: AccommodationDAO
 * 
 * Core Concepts Demonstrated:
 * 1. JDBC (Java Database Connectivity) with Try-With-Resources.
 * 2. Collections Framework (HashMap, TreeMap, HashSet, TreeSet).
 * 3. Lambda Expressions & Stream API.
 * 4. Persistent vs. Non-Persistent Data Management.
 * 5. Autoboxing & Unboxing.
 * 6. Exception Handling.
 * 
 * This class serves as the bridge between the application's business logic and the underlying MySQL database.
 * It manages the retrieval of 'Persistent' data from the DB, and caches it into 'Non-Persistent' in-memory 
 * Collections for highly optimized, fast subsequent read operations.
 */
public class AccommodationDAO implements Searchable {
    
    // The active connection to the MySQL Database (Persistent Layer)
    private final Connection connection;

    // --- Collections Implementation (Non-Persistent Layer) ---
    
    /* 
     * HashMap is used as an in-memory Cache. 
     * Key: Integer (The unique Property ID). Note the use of the Wrapper Class 'Integer'.
     * Value: Accommodation (The actual object containing all details).
     * 
     * Why HashMap? It provides O(1) constant-time complexity for retrieving items by their ID.
     */
    private final Map<Integer, Accommodation> accommodationCache = new HashMap<>();

    /**
     * Default constructor. Initializes the database connection and immediately
     * pre-loads the in-memory cache to ensure the application is ready to serve
     * requests rapidly from the moment it starts.
     */
    public AccommodationDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        
        // Ensure the connection is valid before proceeding
        if (this.connection == null) {
            throw new DatabaseOperationException("Failed to establish a database connection during DAO initialization.");
        }
        
        // Pre-load the data cache
        loadCache();
    }

    /**
     * Fetches all records from the persistent database and populates the non-persistent HashMap cache.
     * This method acts as the synchronization point between the slow DB and the fast RAM.
     */
    private void loadCache() {
        System.out.println("Initializing Accommodation Cache...");
        List<Accommodation> allFromDb = fetchAllFromDatabase();
        
        for (Accommodation acc : allFromDb) {
            /* 
             * AUTOBOXING DEMONSTRATION:
             * 'acc.getPropertyId()' returns a primitive 'int'.
             * However, our HashMap requires an 'Integer' object as the key.
             * Java automatically converts (boxes) the 'int' into an 'Integer' behind the scenes.
             */
            accommodationCache.put(acc.getPropertyId(), acc);
        }
        System.out.println("Cache initialized with " + accommodationCache.size() + " properties.");
    }

    /**
     * Helper method to map a raw JDBC ResultSet row into a fully formed Accommodation Java Object.
     * 
     * @param rs The ResultSet currently pointing to a specific row in the database.
     * @return A subclass of Accommodation (Hostel, PG, or Flat).
     * @throws SQLException If any column name is invalid or a DB error occurs.
     */
    private Accommodation mapResultSetToAccommodation(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Accommodation acc;

        // Utilizing polymorphism to instantiate the correct specific subclass based on the 'type' column
        if ("Hostel".equalsIgnoreCase(type)) acc = new Hostel();
        else if ("PG".equalsIgnoreCase(type)) acc = new PG();
        else acc = new Flat();

        acc.setPropertyId(rs.getInt("property_id"));
        acc.setName(rs.getString("name"));
        acc.setType(rs.getString("type"));
        acc.setPrice(rs.getDouble("price"));
        acc.setDistance(rs.getDouble("distance"));
        acc.setCapacity(rs.getInt("capacity"));
        acc.setGenderPref(rs.getString("gender_pref"));
        acc.setFoodAvailable(rs.getBoolean("food_available"));
        acc.setCurfewTime(rs.getString("curfew_time"));
        acc.setAvailable(rs.getBoolean("is_available"));
        acc.setImageUrl(rs.getString("image_url"));
        acc.setAmenities(rs.getString("amenities"));
        acc.setDescription(rs.getString("description"));

        return acc;
    }

    /**
     * JDBC implementation for fetching all records.
     * Demonstrates robust Database interaction and Exception Handling.
     * 
     * @return A raw List of all accommodations found in the Database.
     */
    private List<Accommodation> fetchAllFromDatabase() {
        List<Accommodation> list = new ArrayList<>();
        String query = "SELECT * FROM Accommodations";
        
        /* 
         * TRY-WITH-RESOURCES:
         * We declare the Statement and ResultSet inside the try() parentheses.
         * This guarantees that even if an exception occurs, or when the block finishes naturally,
         * both 'stmt' and 'rs' will automatically have their .close() methods called.
         * This prevents memory leaks and exhausted database connection pools.
         */
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                list.add(mapResultSetToAccommodation(rs));
            }
            
        } catch (SQLException e) {
            // EXCEPTION HANDLING:
            // We catch the low-level SQLException, and wrap it in our custom unchecked exception.
            // We pass 'e' as the cause to preserve the original stack trace for debugging.
            throw new DatabaseOperationException("Critical Error: Failed to fetch accommodations from the MySQL Database.", e);
        }
        return list;
    }

    /**
     * Retrieves all accommodations currently held in the cache.
     * If the cache was somehow cleared or is empty, it will trigger a reload.
     * 
     * @return A List of all active accommodations.
     */
    public List<Accommodation> getAllAccommodations() {
        if (accommodationCache.isEmpty()) {
            System.out.println("Cache is empty. Triggering emergency reload.");
            loadCache();
        }
        // Converts the Collection of values in the HashMap back into a standard ArrayList
        return new ArrayList<>(accommodationCache.values());
    }

    /**
     * Retrieves a specific accommodation by its ID, demonstrating Unboxing and Checked Exceptions.
     * 
     * @param id The unique integer ID of the property.
     * @return The specific Accommodation object.
     * @throws AccommodationNotFoundException If no property in the cache matches the ID.
     */
    public Accommodation getAccommodationById(int id) throws AccommodationNotFoundException {
        /*
         * AUTOBOXING: The primitive 'id' is boxed to an 'Integer' object to search the Map.
         */
        Accommodation acc = accommodationCache.get(id);
        
        if (acc == null) {
            // EXCEPTION HANDLING: Throwing our Custom Checked Exception if the property doesn't exist.
            throw new AccommodationNotFoundException("Property with ID " + id + " does not exist in the system.", id);
        }
        return acc;
    }

    /**
     * Demonstrates the TreeMap Collection.
     * Returns accommodations sorted automatically by their price.
     * 
     * @return A Map sorted by the Double key representing the price.
     */
    public Map<Double, Accommodation> getAccommodationsSortedByPrice() {
        /*
         * TreeMap is a sorted map implementation based on a Red-Black tree.
         * It automatically orders its entries based on the natural ordering of its keys.
         */
        Map<Double, Accommodation> sortedMap = new TreeMap<>();
        
        for (Accommodation acc : accommodationCache.values()) {
            /* 
             * AUTOBOXING: 'acc.getPrice()' (double) is boxed to 'Double'.
             * To prevent properties with the EXACT SAME PRICE from overwriting each other in the map,
             * we add a microscopic unique modifier based on their ID.
             */
            double uniquePriceKey = acc.getPrice() + (acc.getPropertyId() * 0.000001);
            sortedMap.put(uniquePriceKey, acc);
        }
        return sortedMap;
    }

    /**
     * Demonstrates the HashSet Collection.
     * Retrieves a set of unique amenities across all available properties.
     * 
     * @return A Set of distinct string amenities (e.g., ["WiFi", "AC", "Laundry"]).
     */
    public Set<String> getUniqueAmenities() {
        /*
         * HashSet implements the Set interface, backed by a hash table.
         * It makes no guarantees as to the iteration order of the set; in particular,
         * it does not guarantee that the order will remain constant over time.
         * However, it guarantees absolute uniqueness of its elements.
         */
        Set<String> uniqueAmenities = new HashSet<>();
        
        for (Accommodation acc : accommodationCache.values()) {
            if (acc.getAmenities() != null && !acc.getAmenities().isEmpty()) {
                String[] amenitiesList = acc.getAmenities().split(",");
                for (String amenity : amenitiesList) {
                    // HashSet.add() automatically ignores the item if it already exists in the set.
                    uniqueAmenities.add(amenity.trim().toLowerCase()); 
                }
            }
        }
        return uniqueAmenities;
    }

    /**
     * Demonstrates the TreeSet Collection.
     * Retrieves a sorted set of unique accommodation types.
     * 
     * @return A Set of distinct string types sorted alphabetically (e.g., ["Flat", "Hostel", "PG"]).
     */
    public Set<String> getUniqueSortedTypes() {
        /*
         * TreeSet implements the NavigableSet interface backed by a TreeMap.
         * It guarantees that the elements will be in ascending element order, sorted according 
         * to the natural ordering of its elements (alphabetical for Strings).
         */
        Set<String> sortedTypes = new TreeSet<>();
        
        for (Accommodation acc : accommodationCache.values()) {
            sortedTypes.add(acc.getType());
        }
        return sortedTypes;
    }

    /**
     * Implementation of the Searchable Interface.
     * Demonstrates the power of Lambda Expressions and the Java Stream API for complex filtering.
     * 
     * @param searchQuery The text to search for within Names and Types.
     * @return A filtered List of accommodations matching the query.
     */
    @Override
    public List<Accommodation> searchByQuery(String searchQuery) {
        // Validation check
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return getAllAccommodations();
        }

        final String query = searchQuery.trim().toLowerCase();
        
        /*
         * STREAM API & LAMBDA EXPRESSIONS:
         * 1. .stream() converts our Collection of values into a sequential Stream.
         * 2. .filter() takes a 'Predicate' functional interface. The lambda expression 
         *    'acc -> ...' defines the condition. If true, the item remains in the stream.
         * 3. .collect() gathers the surviving elements back into a standard List.
         */
        return accommodationCache.values().stream()
                .filter(acc -> 
                    (acc.getName() != null && acc.getName().toLowerCase().contains(query)) || 
                    (acc.getType() != null && acc.getType().toLowerCase().contains(query))
                )
                .collect(Collectors.toList());
    }

    /**
     * Demonstrates advanced chaining of Lambda Expressions and the Stream API for multi-criteria filtering.
     * 
     * @param maxPrice    The maximum acceptable price.
     * @param maxDistance The maximum acceptable distance from the university.
     * @param genPref     The required gender preference ("Boys", "Girls", "Any").
     * @return A List of accommodations strictly meeting all criteria.
     */
    @Override
    public List<Accommodation> filterProperties(double maxPrice, double maxDistance, String genPref) {
        
        /*
         * By chaining multiple .filter() methods using lambda expressions, 
         * we create highly readable, declarative code that avoids messy nested if-statements.
         * Each filter sequentially narrows down the stream of data.
         */
        return accommodationCache.values().stream()
                .filter(acc -> acc.getPrice() <= maxPrice)
                .filter(acc -> acc.getDistance() <= maxDistance)
                .filter(acc -> {
                    // Using a multi-line block lambda for slightly more complex logic
                    if (genPref == null || genPref.trim().isEmpty() || "Any".equalsIgnoreCase(genPref)) {
                        return true; // No specific preference required, so allow all
                    }
                    return acc.getGenderPref() != null && acc.getGenderPref().equalsIgnoreCase(genPref);
                })
                .collect(Collectors.toList());
    }
}
