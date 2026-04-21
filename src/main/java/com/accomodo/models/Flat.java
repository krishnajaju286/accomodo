package com.accomodo.models;

public class Flat extends Accommodation {

    public Flat() {
        super();
        this.setType("Flat");
    }

    @Override
    public String getSpecificDetails() {
        return "Flat specific details: Entire apartment. Ideal for independent living and families.";
    }
}
