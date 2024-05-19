import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.*;

public interface Helpers {
    final static String[] TAG_TYPES = {"Cuisine", "Ambiance", "Price", "Style"};
    final static String[] RATING_TYPES = {"Taste", "Ambiance", "WorthIt", "Enjoy", "Hygiene", "Service"};
    final static String DB = "jdbc:sqlite:data/taftr.db";

    /**
     * Add a user to the database and user map
     * @param username The user's name
     * @param password The user's password
     * @param users The user map
     * @return Returns true if the user can be added and false if the username already exists
     */
    public static boolean addUser(String username, String password, Map<String, User> users) {
        if (checkUser(username)) {
            return false;
        }

        Connection con;
        User user = new User(username, password);

        try {
            con = DriverManager.getConnection(DB);

            String input = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)){
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }

            String id = null;
            input = "SELECT id FROM users WHERE username = ? and password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)){
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                id = pstmt.executeQuery().getString(1);
            }

            users.put(id, user);
        } catch(Exception e) {
            System.out.println(e);
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

            String input = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                numUsers = pstmt.executeQuery().getInt(1);
            }

            if (numUsers == 0) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
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

            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            User user = null;
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
            String input = "INSERT INTO restaurants (name, location) VALUES (?, ?)";
            
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.setString(2, location);

                pstmt.executeUpdate();
            }

            input = "SELECT id FROM restaurants WHERE name = ?";

            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);

                id = pstmt.executeQuery().getString(1);
            }

            input = "INSERT INTO menus (restaurant_id, item_name, price) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (Map.Entry<String, Double> pair : menu.entrySet()) {
                    pstmt.setString(1, id);
                    pstmt.setString(2, pair.getKey());
                    pstmt.setDouble(3, pair.getValue());

                    pstmt.executeUpdate();
                }

            }

            input = "INSERT INTO tags (restaurant_id, tag_type, tag_value) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (Map.Entry<String, String> pair : tags.entrySet()) {
                    pstmt.setString(1, id);
                    pstmt.setString(2, pair.getKey());
                    pstmt.setString(3, pair.getValue());

                    pstmt.executeUpdate();
                }

            }
        } catch(Exception e) {
            System.out.println(e);
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

            String input = "SELECT COUNT(*) FROM restaurants WHERE name = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, restrName);
                numRestr = pstmt.executeQuery().getInt(1);
            }

            if (numRestr == 0) {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;

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
            if (rated(userId, restrId, ratingType)) {
                input = "UPDATE ratings SET rating = ? WHERE user_id = ? and restaurant_id = ? and rating_type = ?";

                try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pstmt.setDouble(1, rating);
                    pstmt.setString(2, userId);
                    pstmt.setString(3, restrId);
                    pstmt.setString(4, ratingType);

                    pstmt.executeUpdate();
                }
            } else {
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
            String input = "SELECT COUNT(*) FROM ratings WHERE user_id = ? and restaurant_id = ?";

            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, restrId);

                numRates = pstmt.executeQuery().getInt(1);

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
            String input = "SELECT COUNT(*) FROM ratings WHERE user_id = ? and restaurant_id = ? and rating_type = ?";

            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, restrId);
                pstmt.setString(3, ratingType);

                numRates = pstmt.executeQuery().getInt(1);

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
        Connection con;
        String id = null;
        
        try {
            con = DriverManager.getConnection(DB);

            String input = "SELECT id FROM restaurants WHERE name = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, restrName);
                id = pstmt.executeQuery().getString(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return id;
    }

    /**
     * Gets the user's ratings from the database
     * @param userId The specific user's id
     * @return The user's ratings
     */
    public static String[][] getUserRatings(String userId) {
        Connection con;
        String[][] ratings = null;

        try {
            con = DriverManager.getConnection(DB);

            ResultSet rs = null;
            String input = "SELECT rating_type, rating FROM ratings WHERE user_id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, userId);
                rs = pstmt.executeQuery();
            }

            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> rates = new ArrayList<>();
            while(rs.next()) {
                types.add(rs.getString(1));
                rates.add(rs.getString(2));
            }

            ratings = new String[2][types.size()];
            ratings[0] = types.toArray(ratings[0]);
            ratings[1] = rates.toArray(ratings[1]);

        } catch(Exception e) {
            System.out.println(e);
        }
        return ratings;
    }


    /**
     * Gets the restaurant's ratings from the database
     * @param restrId The specific restaurant's id
     * @return The restaurant's ratings
     */
    public static String[][] getRestrRatings(String restrId) {
        Connection con;
        String[][] ratings = null;

        try {
            con = DriverManager.getConnection(DB);

            ResultSet rs = null;
            String input = "SELECT rating_type, rating FROM ratings WHERE restaurant_id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(input, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, restrId);
                rs = pstmt.executeQuery();
            }

            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> rates = new ArrayList<>();
            while(rs.next()) {
                types.add(rs.getString(1));
                rates.add(rs.getString(2));
            }

            ratings = new String[2][types.size()];
            ratings[0] = types.toArray(ratings[0]);
            ratings[1] = rates.toArray(ratings[1]);
        } catch(Exception e) {
            System.out.println(e);
        }
        return ratings;
    }

}