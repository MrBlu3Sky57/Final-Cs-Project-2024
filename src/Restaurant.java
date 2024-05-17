// File header comment

import java.util.HashMap;
import java.util.Map;

public class Restaurant {
    private String name;
    private String location;
    private Map<String, Double> menu; // change
    private Map<String, String> tags;

    /**
     * Instantiate a restaurant object
     * @param name Restaurant name
     * @param location Restaurant location
     * @param menu Restaurant menu
     * @param tags Restaurant tags
     */
    public Restaurant(String name, String location, Map<String, Double> menu, Map<String, String> tags) {
        setName(name);
        setLocation(location);
        setMenu(menu);
        setTags(tags);
    }

    /**
     * Copy constructor for the restaurant class
     * @param restaurant The restaurant object to be copied
     */
    public Restaurant(Restaurant restaurant) {
        setName(restaurant.getName());
        setLocation(restaurant.getLocation());
        setMenu(restaurant.getMenu());
        setTags(restaurant.getTags());
    }

    /**
     * Gets the name of the restaurant
     * @return The name of the restaurant
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the location of the restaurant
     * @return The location of the restaurant
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Gets the menu of the restaurant
     * @return A copy of the restaurant map
     */
    public Map<String, Double> getMenu() {
        return new HashMap<String, Double>(menu);
    }

    /**
     * The tags of the specifc restaurant
     * @return The restaurant's tags
     */
    public Map<String, String> getTags() {
        return new HashMap<String, String>(tags);
    }

    /**
     * Set the name of the restaurant
     * @param name The name of the restaurant
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the location of the restaurant
     * @param location The location of the restaurant 
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Set the menu of the restaurant
     * @param menu The menu of the restaurant
     */
    public void setMenu(Map<String, Double> menu) {
        this.menu = new HashMap<String, Double>(menu);
    }

    /**
     * Set the tags of a restaurant
     * @param tags The tags for a restaurant
     */
    public void setTags(Map<String, String> tags) {
        this.tags = new HashMap<String, String>(tags);
    }
}