import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, User> users = new HashMap<String, User>();
        Helpers.addUser("Aaron", "password", users);
        users = Helpers.getUsers();

        for (Map.Entry<String, User> pair : users.entrySet()) {
            System.out.println("ID: " + pair.getKey() + " || USERNAME: " + pair.getValue().getUsername() + "|| PASSWORD: " + pair.getValue().getPassword() + "\n");
        }

        // Map<String, Restaurant> restaurants = new HashMap<String, Restaurant>();
        // String name = "Zakushi";
        // String location = "193 Carlton St.";
        // Map<String, Double> menu = new HashMap<String, Double>();
        // menu.put("P-Toro", 4.30);
        // menu.put("Tsukune", 3.50);
        // menu.put("Goma Ae", 5.50);
        
        // Map<String, String> tags = new HashMap<String, String>();
        // tags.put("Cuisine", "Japanese");
        // tags.put("Ambiance", "Lively");
        // tags.put("Price", "Moderately Expensive");
        // tags.put("Style", "Casual");

        // Helpers.addRestaurant(name, location, menu, tags, restaurants);
        
        Helpers.addRating(Helpers.getUserId("Aaron"), Helpers.getRestrId("Zakushi"), "Taste", 5);
        
    }     
}
