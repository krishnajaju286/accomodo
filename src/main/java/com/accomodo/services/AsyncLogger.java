package com.accomodo.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service Class: AsyncLogger
 * 
 * Concept Demonstrated: Multithreading (Implementing the Runnable Interface)
 * 
 * This class is responsible for logging application events asynchronously.
 * By implementing the Runnable interface, instances of this class can be
 * passed to a new Thread. This allows the main execution thread (e.g., the
 * one serving a user's HTTP request in the Servlet) to continue without
 * waiting for the potentially time-consuming logging operations (like writing
 * to a file or a database) to finish.
 */
public class AsyncLogger implements Runnable {
    
    // The specific message to be logged by this thread
    private final String logMessage;
    
    // The severity level of the log (e.g., INFO, WARNING, ERROR)
    private final String logLevel;

    // Formatting pattern for the timestamp
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor for the AsyncLogger.
     * 
     * @param logMessage The core message describing the event.
     * @param logLevel   The severity or category of the log.
     */
    public AsyncLogger(String logMessage, String logLevel) {
        this.logMessage = logMessage;
        this.logLevel = logLevel.toUpperCase();
    }

    /**
     * Overloaded constructor that defaults to "INFO" level.
     * 
     * @param logMessage The core message describing the event.
     */
    public AsyncLogger(String logMessage) {
        this(logMessage, "INFO");
    }

    /**
     * The run() method contains the code that will be executed in the new thread.
     * When Thread.start() is called on the thread containing this Runnable,
     * the JVM will automatically invoke this run() method concurrently.
     */
    @Override
    public void run() {
        // Retrieve the current time for the log entry
        String timestamp = LocalDateTime.now().format(formatter);
        
        // Retrieve the name of the thread currently executing this run() method
        String threadName = Thread.currentThread().getName();

        // Construct the full log string
        String fullLogEntry = String.format("[%s] [%s] [Thread: %s] %s", 
                                            timestamp, logLevel, threadName, logMessage);

        try {
            // Simulate a slightly time-consuming operation (e.g., network latency or slow disk I/O)
            // If this were on the main thread, the user would experience a delay of at least 500ms.
            Thread.sleep(500); 

            // Output the log to the standard console
            System.out.println(fullLogEntry);

            // Additionally, attempt to write the log to a physical file (mocking a real persistent log)
            // Using a synchronized block or thread-safe classes is crucial if multiple threads write to the same file.
            // For simplicity here, we use try-with-resources with a FileWriter in append mode.
            writeToFile(fullLogEntry);

        } catch (InterruptedException e) {
            // If the thread is interrupted while sleeping, we log the error
            System.err.println("Async logging thread was interrupted: " + e.getMessage());
            // It's a best practice to restore the interrupted status
            Thread.currentThread().interrupt(); 
        }
    }

    /**
     * Helper method to write the log entry to a text file.
     * Demonstrates File I/O operations inside a secondary thread.
     * 
     * @param logEntry The fully formatted string to write.
     */
    private void writeToFile(String logEntry) {
        // Note: In a true production environment, a logging framework like Log4j or SLF4J is used.
        // This manual implementation is for demonstrating the underlying concepts.
        try (FileWriter fw = new FileWriter("accomodo_async_application.log", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
