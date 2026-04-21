package com.accomodo.interfaces;

import com.accomodo.models.Accommodation;
import java.util.List;

public interface Searchable {
    List<Accommodation> searchByQuery(String query);
    List<Accommodation> filterProperties(double maxPrice, double maxDistance, String genPref);
}
