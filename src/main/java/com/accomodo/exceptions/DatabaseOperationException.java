package com.accomodo.exceptions;

import java.time.LocalDateTime;

/**
 * Custom Exception Class: DatabaseOperationException
 * 
 * Concept Demonstrated: Exception Handling (Custom Unchecked Exception)
 * 
 * This class extends RuntimeException, making it an unchecked exception.
 * It is specifically designed to encapsulate standard SQLExceptions, adding
 * more context such as an error code and a timestamp of when the error occurred.
 * This prevents the underlying data access layer's technical exceptions (like 
 * java.sql.SQLException) from leaking into the business or presentation layers,
 * adhering to the principle of encapsulation and clean architecture.
 */
public class DatabaseOperationException extends RuntimeException {
    
    private final String errorCode;
    private final LocalDateTime timestamp;

    /**
     * Constructor to initialize the exception with a specific message and the root cause.
     * 
     * @param message A user-friendly message describing the database error.
     * @param cause   The original Throwable (usually SQLException) that triggered this error.
     */
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "DB_ERR_" + System.currentTimeMillis();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for cases where there is no specific root cause, just a general DB error.
     * 
     * @param message A user-friendly message describing the database error.
     */
    public DatabaseOperationException(String message) {
        super(message);
        this.errorCode = "DB_ERR_" + System.currentTimeMillis();
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Retrieves the generated error code for this specific exception instance.
     * 
     * @return The unique error code string.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Retrieves the exact date and time when this exception was instantiated.
     * 
     * @return The timestamp of the error.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "DatabaseOperationException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                ", cause=" + getCause() +
                '}';
    }
}
