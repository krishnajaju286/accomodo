package com.accomodo.models;

public class Rating {
    private int ratingId;
    private int propertyId;
    private int userId;
    private int score;
    private String reviewText;

    public Rating() {}

    public Rating(int ratingId, int propertyId, int userId, int score, String reviewText) {
        this.ratingId = ratingId;
        this.propertyId = propertyId;
        this.userId = userId;
        this.score = score;
        this.reviewText = reviewText;
    }

    // Getters and Setters
    public int getRatingId() { return ratingId; }
    public void setRatingId(int ratingId) { this.ratingId = ratingId; }

    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}
