// Testing stuff
package com.ufa.cs.project;

import java.util.Map;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, Restaurant> restr = new HashMap<>();
        Map<String, Double> menu = new HashMap<>();
        Map<String, String> tags = new HashMap<>();

        String name = "mcdonalds";
        String location = "bayview";
        menu.put("burger", 2.0);
        tags.put("Cuisine", "American");
        tags.put("Ambiance", "Tweaky");
        tags.put("Price", "Cheap");
        tags.put("Style", "Casual");

        Helpers.addRestaurant(name, location, menu, tags, restr);
        System.out.println(restr.get(Helpers.getRestrId(name)));
    }     
}
