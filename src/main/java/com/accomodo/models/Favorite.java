package com.accomodo.models;

public class Favorite {
    private int favoriteId;
    private int userId;
    private int propertyId;
    private Accommodation accommodationDetails; // Object reference for UI rendering

    public Favorite() {}

    public Favorite(int favoriteId, int userId, int propertyId) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.propertyId = propertyId;
    }

    public int getFavoriteId() { return favoriteId; }
    public void setFavoriteId(int favoriteId) { this.favoriteId = favoriteId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

    public Accommodation getAccommodationDetails() { return accommodationDetails; }
    public void setAccommodationDetails(Accommodation accommodationDetails) { this.accommodationDetails = accommodationDetails; }
}
