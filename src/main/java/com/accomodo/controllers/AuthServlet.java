package com.accomodo.controllers;

import com.accomodo.dao.UserDAO;
import com.accomodo.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO;
    private Gson gson;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        
        try {
            JsonObject jsonObject = gson.fromJson(buffer.toString(), JsonObject.class);

            if ("/register".equals(pathInfo)) {
                User newUser = new User(
                    0,
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("email").getAsString(),
                    jsonObject.get("password").getAsString(),
                    "Student",
                    jsonObject.has("gender") ? jsonObject.get("gender").getAsString() : null,
                    jsonObject.has("preferences") ? jsonObject.get("preferences").getAsString() : null
                );
                
                if (userDAO.registerUser(newUser)) {
                    out.print("{\"status\":\"success\", \"message\":\"Registration successful!\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"status\":\"error\", \"message\":\"Registration failed or email exists.\"}");
                }
            } else if ("/login".equals(pathInfo)) {
                String email = jsonObject.get("email").getAsString();
                String pass = jsonObject.get("password").getAsString();
                
                User user = userDAO.authenticateUser(email, pass);
                if (user != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("user", user);
                    out.print("{\"status\":\"success\", \"message\":\"Login successful!\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print("{\"status\":\"error\", \"message\":\"Invalid email or password.\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"status\":\"error\", \"message\":\"Endpoint not found.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"An internal server error occurred.\"}");
        }
        out.flush();
    }
}
