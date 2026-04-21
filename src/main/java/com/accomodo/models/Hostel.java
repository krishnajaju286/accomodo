package com.accomodo.models;

public class Hostel extends Accommodation {
    
    public Hostel() {
        super();
        this.setType("Hostel");
    }

    @Override
    public String getSpecificDetails() {
        return "Hostel specific details: Strict curfew at " + getCurfewTime() + 
               ". Food is " + (isFoodAvailable() ? "included in the mess." : "not included.");
    }
}
