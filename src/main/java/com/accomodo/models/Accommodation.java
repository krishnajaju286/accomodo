package com.accomodo.models;

public abstract class Accommodation {
    private int propertyId;
    private String name;
    private String type;
    private double price;
    private double distance;
    private int capacity;
    private String genderPref;
    private boolean foodAvailable;
    private String curfewTime;
    private boolean isAvailable;
    private String imageUrl;
    private String amenities;
    private String description;

    // Constructors
    public Accommodation() {}

    public Accommodation(int propertyId, String name, String type, double price, double distance, int capacity,
                         String genderPref, boolean foodAvailable, String curfewTime, boolean isAvailable,
                         String imageUrl, String amenities, String description) {
        this.propertyId = propertyId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.distance = distance;
        this.capacity = capacity;
        this.genderPref = genderPref;
        this.foodAvailable = foodAvailable;
        this.curfewTime = curfewTime;
        this.isAvailable = isAvailable;
        this.imageUrl = imageUrl;
        this.amenities = amenities;
        this.description = description;
    }

    // Abstract method for Polymorphism
    public abstract String getSpecificDetails();

    // Getters and Setters for Encapsulation
    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getGenderPref() { return genderPref; }
    public void setGenderPref(String genderPref) { this.genderPref = genderPref; }

    public boolean isFoodAvailable() { return foodAvailable; }
    public void setFoodAvailable(boolean foodAvailable) { this.foodAvailable = foodAvailable; }

    public String getCurfewTime() { return curfewTime; }
    public void setCurfewTime(String curfewTime) { this.curfewTime = curfewTime; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
