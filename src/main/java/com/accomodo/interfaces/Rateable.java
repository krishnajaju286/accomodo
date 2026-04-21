package com.accomodo.interfaces;

public interface Rateable {
    void addRating(int userId, int score, String reviewText);
    double getAverageRating();
}
