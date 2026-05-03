package com.accomodo.controllers;

import com.accomodo.dao.AccommodationDAO;
import com.accomodo.exceptions.DatabaseOperationException;
import com.accomodo.models.Accommodation;
import com.accomodo.services.AsyncLogger;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller Class: AccommodationServlet
 * 
 * This servlet handles incoming HTTP requests related to accommodations.
 * It demonstrates how the frontend web application communicates with our 
 * backend Java code, and how we integrate Multithreading for background tasks.
 */
@WebServlet("/api/accommodations")
public class AccommodationServlet extends HttpServlet {
    
    // The Data Access Object responsible for providing accommodation data
    private AccommodationDAO accommodationDAO;
    
    // Google's Gson library for converting Java Objects to JSON format
    private Gson gson;

    /**
     * The init() method is called exactly once by the Servlet Container (e.g., Tomcat) 
     * when the servlet is first loaded into memory. It is the perfect place to instantiate 
     * heavy objects like Database DAOs or JSON parsers.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Attempt to initialize our DAO, which in turn initializes the Database connection
            // and pre-loads the in-memory Cache.
            this.accommodationDAO = new AccommodationDAO();
            this.gson = new Gson();
            System.out.println("AccommodationServlet fully initialized and ready.");
            
        } catch (DatabaseOperationException e) {
            /*
             * EXCEPTION HANDLING:
             * If the database is offline, our DAO constructor throws a custom unchecked exception.
             * We catch it here in the initialization phase and wrap it in a ServletException.
             * This prevents the servlet from starting in a broken state.
             */
            System.err.println("CRITICAL ERROR initializing AccommodationDAO: " + e.getMessage());
            System.err.println("Original Cause: " + e.getCause().getMessage());
            throw new ServletException("DAO Initialization Failed. Ensure MySQL Database is running.", e);
        }
    }

    /**
     * The doGet() method handles all HTTP GET requests sent to the "/api/accommodations" endpoint.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Set the response headers so the client knows what kind of data to expect
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        /* 
         * --- MULTITHREADING EXECUTION DEMONSTRATION ---
         * 
         * Instead of logging the request details sequentially (which would force the user
         * to wait until the log was completely written before receiving their JSON data),
         * we fire off an asynchronous task in a separate background thread.
         * 
         * We create a new Thread object, passing in our custom 'Runnable' (AsyncLogger).
         * Calling .start() tells the JVM to execute the logger's run() method concurrently.
         */
        String clientIp = request.getRemoteAddr();
        String queryParam = request.getParameter("q");
        String logMessage = String.format("Received GET request for accommodations from IP: %s. Query param 'q': %s", clientIp, queryParam);
        
        Thread loggerThread = new Thread(new AsyncLogger(logMessage, "INFO"));
        loggerThread.start(); // The log writes to the console/file while the code below continues immediately!
        
        // 2. Extract potential search parameters from the URL
        List<Accommodation> resultList;

        try {
            // 3. Consult the DAO (Data Access Object) to retrieve the correct data
            if (queryParam != null && !queryParam.trim().isEmpty()) {
                // The user is searching for a specific name or type
                resultList = accommodationDAO.searchByQuery(queryParam);
            } else {
                // No search query, return all properties from the Cache
                resultList = accommodationDAO.getAllAccommodations();
            }

            // 4. Transform the complex Java List of Objects into a standard JSON String
            String jsonResponse = this.gson.toJson(resultList);
            
            // 5. Send the final JSON String back to the client's browser
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush(); // Ensure the data is completely sent
            
        } catch (Exception e) {
            /*
             * EXCEPTION HANDLING (Safety Net):
             * A broad catch block at the top level of our HTTP handler ensures that if 
             * absolutely anything goes wrong (NullPointers, unexpected DB errors, logic errors), 
             * the server does not simply crash or hang. Instead, we manually send back a structured 
             * HTTP 500 Error to the client, along with a JSON-formatted error message.
             */
            System.err.println("Unhandled exception during GET request: " + e.getMessage());
            e.printStackTrace(); // Log the full trace for the developer
            
            // Set the appropriate HTTP Status Code indicating a Server-Side failure
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            PrintWriter out = response.getWriter();
            // Send back a JSON object representing the error so the frontend JS can parse it
            out.print("{\"error\": \"An internal server error occurred while fetching accommodations: " + e.getMessage() + "\"}");
            out.flush();
        }
    }
}
