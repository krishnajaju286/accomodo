package com.accomodo.exceptions;

import java.time.LocalDateTime;

/**
 * Custom Exception Class: AccommodationNotFoundException
 * 
 * Concept Demonstrated: Exception Handling (Custom Checked Exception)
 * 
 * This class extends the standard Exception class, making it a "Checked Exception".
 * This means any method throwing this exception must either handle it internally
 * using a try-catch block, or declare it in its method signature using the "throws" keyword.
 * It is used when an Accommodation is searched for (e.g., by ID) but does not exist in the 
 * database or the active cache.
 */
public class AccommodationNotFoundException extends Exception {
    
    // The specific property ID that could not be located
    private final int requestedId;
    
    // The time the failed request was made
    private final LocalDateTime timestamp;

    /**
     * Constructs a new AccommodationNotFoundException with a detailed message and the failed ID.
     * 
     * @param message     A descriptive error message.
     * @param requestedId The ID of the accommodation that was not found.
     */
    public AccommodationNotFoundException(String message, int requestedId) {
        super(message);
        this.requestedId = requestedId;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Retrieves the ID that caused this exception to be thrown.
     * 
     * @return the integer ID of the missing accommodation.
     */
    public int getRequestedId() {
        return requestedId;
    }

    /**
     * Retrieves the time the exception was generated.
     * 
     * @return the LocalDateTime timestamp.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Overridden toString method to provide a comprehensive string representation of the error.
     */
    @Override
    public String toString() {
        return "AccommodationNotFoundException{" +
                "message='" + super.getMessage() + '\'' +
                ", requestedId=" + requestedId +
                ", timestamp=" + timestamp +
                '}';
    }
}
