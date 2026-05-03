package com.accomodo.dao;

import com.accomodo.exceptions.DatabaseOperationException;
import com.accomodo.models.User;
import com.accomodo.utils.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null) {
            throw new DatabaseOperationException("Failed to establish a database connection for UserDAO.");
        }
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO Users (name, email, password, gender, preferences, user_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // In a real app, hash this!
            stmt.setString(4, user.getGender());
            stmt.setString(5, user.getPreferences());
            stmt.setString(6, user.getUserType() != null ? user.getUserType() : "Student");
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("user_type"),
                        rs.getString("gender"),
                        rs.getString("preferences")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        }
        return null;
    }

    public void logSearch(int userId, String query) {
        String sql = "INSERT INTO User_Searches (user_id, search_query) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, query);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log search: " + e.getMessage());
        }
    }

    public void logPropertyView(int userId, int propertyId) {
        String sql = "INSERT INTO User_Views (user_id, property_id) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE view_count = view_count + 1, last_viewed = CURRENT_TIMESTAMP";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, propertyId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log property view: " + e.getMessage());
        }
    }
}
