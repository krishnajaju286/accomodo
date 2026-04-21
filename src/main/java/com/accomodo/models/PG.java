package com.accomodo.models;

public class PG extends Accommodation {
    
    public PG() {
        super();
        this.setType("PG");
    }

    @Override
    public String getSpecificDetails() {
        return "PG specific details: Suitable for " + getGenderPref() + ". Shared living spaces.";
    }
}
