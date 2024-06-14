/*
 * This a class with methods that implement database retrieval, insertion and verification
 * for the Restaurant Tracker app. 
 * 
 *  Completely Programmed by Aaron Avram
 *  Date Programmed: June 14, 2024
 */

package com.ufa.cs.project;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.*;

public class Helpers {
    final static String[] TAG_TYPES = {"Cuisine", "Ambiance", "Price", "Style"};
    final static String[] RATING_TYPES = {"Taste", "Ambiance", "WorthIt", "Enjoy", "Hygiene", "Service"};
    final static Map<String, String> CONVERT = Map.of(
            RATING_TYPES[0], TAG_TYPES[0],
            RATING_TYPES[1], TAG_TYPES[1],
            RATING_TYPES[2], TAG_TYPES[2],
            RATING_TYPES[3], TAG_TYPES[3]

    );
    final static String DB = "jdbc:sqlite:./target/classes/taftr.db";

    /**
     * Add a user to the database and user map
     * @param username The user's name
     * @param password The user's password
     * @param users The user map
     * @return Returns true if the user can be added and false if the username already exists
     */
    public static boolean addUser(String username, String password, Map<String, User> users) {

        // If user already exists, cannot add user
        if (checkUser(username)) {
            return false;
        }

        Connection con;

        // Create new user
        User user = new User(username, password);

        try {
            con = DriverManager.getConnection(DB);

            // Insert user into database
            String input = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)){
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }

            // Get user id
            String id = null;
            input = "SELECT id FROM users WHERE username = ? and password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)){
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                id = pstmt.executeQuery().getString(1);
            }

            // Put user and their id into the map
            users.put(id, user);
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Check if username is already in database
     * @param username Tentative username
     * @return True or False based on whether the username is in the table
     */
    public static boolean checkUser(String username) {
        Connection con;
        int numUsers;
        
        try {
            con = DriverManager.getConnection(DB);

            // Find how many users have the inputted username
            String input = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                numUsers = pstmt.executeQuery().getInt(1);
            }

            // If there are none, the user does not exist
            if (numUsers == 0) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Verify that username and password match database values
     * @param username Inputted username
     * @param password Inputted password
     * @return True if valid input and false otherwise
     */
    public static boolean authenticateUser(String username, String password) {
        Connection con;
        int numUsers;
        
        try {
            con = DriverManager.getConnection(DB);

            // Find how many users in the database have this username and password
            String input = "SELECT COUNT(*) FROM users WHERE username = ? and password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                numUsers = pstmt.executeQuery().getInt(1);
            }

            // If there are none, user is not verified
            if (numUsers == 0) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Get all of the users in the Database
     * @return A Map of user id's to their user objects
     */
    public static Map<String, User> getUsers() {
        Connection con;
        Statement stmt;
        Map<String, User> users = new HashMap<String, User>();

        try {
            con = DriverManager.getConnection(DB);
            stmt = con.createStatement();

            // Get every user from the database
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            User user = null;
            
            // Fill user map with database data
            while (rs.next()) {
                String id = rs.getString(1);
                user = new User(rs.getString(2), rs.getString(3));

                users.put(id, user);
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return users;
    }

    /**
     * Add a restaurant's data to the database
     * @param name The restaurant's name
     * @param location The location of the restaurant
     * @param menu The restaurant's menu
     * @param tags The restaurant's tags
     * @param restaurants The restaurants map
     * @return True or false, if restaurant is not already in database
     */        
    public static boolean addRestaurant(String name, String location, Map<String, Double> menu, Map<String, String> tags, Map<String, Restaurant> restaurants) {
            if (checkRestaurant(name)) {
                return false;
            }
            Connection con;
            String id = null;
        
            try {
                con = DriverManager.getConnection(DB);
        
                // Insert new restaurant into database
                String input = "INSERT INTO restaurants (name, location) VALUES (?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, location);
                    pstmt.executeUpdate();
        
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        id = rs.getString(1);
                    }
                }
        
                // Insert restaurant menu into database
                input = "INSERT INTO menus (restaurant_id, item_name, price) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(input)) {
                    for (Map.Entry<String, Double> pair : menu.entrySet()) {
                        pstmt.setString(1, id);
                        pstmt.setString(2, pair.getKey());
                        pstmt.setDouble(3, pair.getValue());
                        pstmt.executeUpdate();
                    }
                }
        
                // Insert restaurant tags into database
                input = "INSERT INTO tags (restaurant_id, tag_type, tag_value) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(input)) {
                    for (Map.Entry<String, String> pair : tags.entrySet()) {
                        pstmt.setString(1, id);
                        pstmt.setString(2, pair.getKey());
                        pstmt.setString(3, pair.getValue());
                        pstmt.executeUpdate();
                    }
                }
        
            } catch (Exception e) {
                System.out.println("Error in addRestaurant: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        
            restaurants.put(id, new Restaurant(name, location, menu, tags));
            return true;
        }
        

    /**
     * Check if a restaurant is already in the database
     * @param restrName The name of the restaurant
     * @return True or False based on whether the restaurant is in the database
     */
    public static boolean checkRestaurant(String restrName) {
        Connection con;
        int numRestr;
        
        try {
            con = DriverManager.getConnection(DB);

            // Get how many restaurants have this name
            String input = "SELECT COUNT(*) FROM restaurants WHERE name like ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, restrName);
                numRestr = pstmt.executeQuery().getInt(1);
            }

            // If there are none, return false
            if (numRestr == 0) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;

    }

    /**
     * Get all of the restaurants in the database
     * @return A map of id to restaurant object
     */
    public static Map<String, Restaurant> getRestr() {
        Connection con;
        Statement stmt;
        Map<String, Restaurant> restr = new HashMap<String, Restaurant>();

        try {
            con = DriverManager.getConnection(DB);
            stmt = con.createStatement();

            // Get all of the restaurant data
            ResultSet rs = stmt.executeQuery("SELECT * FROM restaurants");

            Map<String, Double> menu = new HashMap<>();
            Map<String, String> tags = new HashMap<>();

            // For each restaurant get the tag and menu data
            while (rs.next()) {
                String id = rs.getString(1);

                // Get the menu data for a restaurant
                String input = "SELECT item_name, price FROM menus WHERE restaurant_id = ?";
                ResultSet tempRs;
                try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, id);

                    tempRs = pstmt.executeQuery();

                    while(tempRs.next()) {
                        menu.put(tempRs.getString(1), tempRs.getDouble(2));
                    }
                }

                // Get the tag data for a restaurant
                input = "SELECT tag_type, tag_value FROM tags WHERE restaurant_id = ?";
                try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, id);

                    tempRs = pstmt.executeQuery();

                    while(tempRs.next()) {
                        tags.put(tempRs.getString(1), tempRs.getString(2));
                    }
                }

                // Add new restaurant to restr map
                restr.put(id, new Restaurant(rs.getString(2), rs.getString(3), menu, tags));

                // Clear temporary maps
                menu.clear();
                tags.clear();
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return restr;
    }

    /**
     * Add a rating to the database
     * @param userId The user's id
     * @param restrId The restaurant's id
     * @param ratingType The rating type
     * @param rating The rating value
     */
    public static void addRating(String userId, String restrId, String ratingType, double rating) {
        Connection con;

        try {
            con = DriverManager.getConnection(DB);

            String input = "";

            // If user has already rated this restaurant
            if (rated(userId, restrId, ratingType)) {

                // Update user's rating
                input = "UPDATE ratings SET rating = ? WHERE user_id = ? and restaurant_id = ? and rating_type = ?";
                try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setDouble(1, rating);
                    pstmt.setString(2, userId);
                    pstmt.setString(3, restrId);
                    pstmt.setString(4, ratingType);

                    pstmt.executeUpdate();
                }
            } 
            
            // If this is a new rating
            else {

                // Insert new rating into database
                input = "INSERT INTO ratings (user_id, restaurant_id, rating_type, rating) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, userId);
                    pstmt.setString(2, restrId);
                    pstmt.setString(3, ratingType);
                    pstmt.setDouble(4, rating);

                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Check if a user has already rated a restaurant
     * @param userId The user's id
     * @param restrId The restaurant's id
     * @return True or false based on whether the user has rated the restaurant
     */
    public static boolean rated(String userId, String restrId) {
        Connection con;
        int numRates;

        try {
            con = DriverManager.getConnection(DB);

            // Check how many times a user has rated a restaurant
            String input = "SELECT COUNT(*) FROM ratings WHERE user_id = ? and restaurant_id = ?";

            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, restrId);

                numRates = pstmt.executeQuery().getInt(1);

                // If there are none, return false
                if (numRates == 0) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    /**
     * Check if a user has already rated a restaurant for a specific rating type
     * @param userId User id
     * @param restrId Restaurant id
     * @param ratingType The rating type
     * @return True or False based on whether or not user has rated the restaurant
     */
    public static boolean rated(String userId, String restrId, String ratingType) {
        Connection con;
        int numRates;

        try {
            con = DriverManager.getConnection(DB);

            // Check how many times a user has a rated a restaurant for a particular category
            String input = "SELECT COUNT(*) FROM ratings WHERE user_id = ? and restaurant_id = ? and rating_type = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, restrId);
                pstmt.setString(3, ratingType);

                numRates = pstmt.executeQuery().getInt(1);

                // If there are none, return false
                if (numRates == 0) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    /**
     * Gets the id of a user
     * @param username The username
     * @return The id for the username
     */
    public static String getUserId(String username) {
        Connection con;
        String id = null;
        
        try {
            con = DriverManager.getConnection(DB);

            // Get id of user with inputted username
            String input = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                id = pstmt.executeQuery().getString(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return id;
    }

    /**
     * Gets the id of a user
     * @param username The username
     * @return The id for the username
     */
    public static String getRestrId(String restrName) {
        String id = null;
        String query = "SELECT id FROM restaurants WHERE name = ?";
        try (Connection con = DriverManager.getConnection(DB);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, restrName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getString("id");
            }
        } catch (Exception e) {
            System.out.println("Error in getRestrId: " + e.getMessage());
            e.printStackTrace();
        }
        return id;
    }
    

    /**
     * Gets the user's ratings from the database
     * @param userId The specific user's id
     * @return The user's ratings
     */
    public static String[][] getUserRatings(String userId) {
        Connection con = null;
        String[][] ratings = null;

        try {
            con = DriverManager.getConnection(DB);

            // Get all of a users ratings for each tag value
            String input = "SELECT restaurant_id, rating_type, rating FROM ratings WHERE user_id = ?";
            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> rates = new ArrayList<>();

            try (PreparedStatement pstmt = con.prepareStatement(input)) {
                pstmt.setString(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {

                    // Iterate through each rating and convert rating type to tag type
                    while(rs.next()) {
                        String restrId = rs.getString("restaurant_id");
                        String tag = CONVERT.get(rs.getString("rating_type"));
                        String tagValue = null;
        
                        // Get the corresponding tag value for the tag type of each rating
                        input = "SELECT tag_value FROM tags WHERE restaurant_id = ? and tag_type = ?";
                        try (PreparedStatement tagPstmt = con.prepareStatement(input)) {
                            tagPstmt.setString(1, restrId);
                            tagPstmt.setString(2, tag);
                            try (ResultSet tagsRs = tagPstmt.executeQuery()) {
                                if (tagsRs.next()) {
                                    tagValue = tagsRs.getString("tag_value");
                                }
                            }
                        }

                        if (tagValue != null) {
                            types.add(tagValue);
                            rates.add(rs.getString("rating"));
                        }
                    }

                }
            }
            
            // Format ratings as a two dimensional array
            ratings = new String[2][types.size()];
            for (int i = 0; i < types.size(); i++) {
                ratings[0][i] = types.get(i);
                ratings[1][i] = rates.get(i);
            }

        } catch(Exception e) {
            System.out.println(e);
        } finally {
            if (con != null ) {
                try {
                    con.close();
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }
        return ratings;
    }

    /**
     * Gets the restaurant's ratings from the database
     * @param restrId The specific restaurant's id
     * @return The restaurant's ratings
     */
    public static String[][] getRestrRatings(String restrId) {
        Connection con = null;
        String[][] ratings = null;

        try {
            con = DriverManager.getConnection(DB);

            // Get all of the ratings for a specific restaurants
            String input = "SELECT rating_type, rating FROM ratings WHERE restaurant_id = ?";
            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> rates = new ArrayList<>();

            try (PreparedStatement pstmt = con.prepareStatement(input)) {
                pstmt.setString(1, restrId);
                try (ResultSet rs = pstmt.executeQuery()) {

                    // For each rating convert rating type to tag type
                    while(rs.next()) {
                        String tag = CONVERT.get(rs.getString("rating_type"));
                        String tagValue = null;
        
                        // Get the corresponding tag value for each tag type
                        input = "SELECT tag_value FROM tags WHERE restaurant_id = ? and tag_type = ?";
                        try (PreparedStatement tagPstmt = con.prepareStatement(input)) {
                            tagPstmt.setString(1, restrId);
                            tagPstmt.setString(2, tag);
                            try (ResultSet tagsRs = tagPstmt.executeQuery()) {
                                if (tagsRs.next()) {
                                    tagValue = tagsRs.getString("tag_value");
                                }
                            }
                        }
                        
                        if (tagValue != null) {
                            types.add(tagValue);
                            rates.add(rs.getString("rating"));
                        }
                    }
                }
            }

            // Format ratings as a two-dimensional array
            ratings = new String[2][types.size()];
            for (int i = 0; i < types.size(); i++) {
                ratings[0][i] = types.get(i);
                ratings[1][i] = rates.get(i);
            }
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return ratings;
    }

}