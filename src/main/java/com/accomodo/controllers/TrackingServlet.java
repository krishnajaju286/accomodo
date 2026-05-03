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

@WebServlet("/api/track/*")
public class TrackingServlet extends HttpServlet {
    private UserDAO userDAO;
    private Gson gson;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        User user = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        try {
            JsonObject jsonObject = gson.fromJson(buffer.toString(), JsonObject.class);

            if ("/search".equals(pathInfo)) {
                String query = jsonObject.get("query").getAsString();
                new Thread(() -> {
                    userDAO.logSearch(user.getUserId(), query);
                }).start();
                
            } else if ("/view".equals(pathInfo)) {
                int propertyId = jsonObject.get("propertyId").getAsInt();
                new Thread(() -> {
                    userDAO.logPropertyView(user.getUserId(), propertyId);
                }).start();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Tracking Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
