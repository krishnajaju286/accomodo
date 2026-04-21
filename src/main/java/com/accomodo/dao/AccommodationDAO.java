package com.accomodo.dao;

import com.accomodo.interfaces.Searchable;
import com.accomodo.models.Accommodation;
import com.accomodo.models.Hostel;
import com.accomodo.models.PG;
import com.accomodo.models.Flat;
import com.accomodo.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccommodationDAO implements Searchable {
    private Connection connection;

    public AccommodationDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    private Accommodation mapResultSetToAccommodation(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Accommodation acc;

        if ("Hostel".equalsIgnoreCase(type)) acc = new Hostel();
        else if ("PG".equalsIgnoreCase(type)) acc = new PG();
        else acc = new Flat();

        acc.setPropertyId(rs.getInt("property_id"));
        acc.setName(rs.getString("name"));
        // Type is already set by constructors but we ensure consistency
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

    public List<Accommodation> getAllAccommodations() {
        List<Accommodation> list = new ArrayList<>();
        String query = "SELECT * FROM Accommodations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapResultSetToAccommodation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Accommodation> searchByQuery(String searchQuery) {
        List<Accommodation> list = new ArrayList<>();
        String query = "SELECT * FROM Accommodations WHERE name LIKE ? OR type LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAccommodation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Accommodation> filterProperties(double maxPrice, double maxDistance, String genPref) {
        List<Accommodation> list = new ArrayList<>();
        String query = "SELECT * FROM Accommodations WHERE price <= ? AND distance <= ? AND gender_pref = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, maxPrice);
            pstmt.setDouble(2, maxDistance);
            pstmt.setString(3, genPref);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAccommodation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
