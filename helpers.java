import java.util.Map;
import java.util.HashMap;

public interface helpers {
    final static String[] tagTypes = {"Cuisine", "Ambiance", "Price", "Style"};
    final static String[] ratingTypes = {"Taste", "Ambiance", "WorthIt", "Enjoy", "Hygiene", "Service"};


    /**
     * Gets the weight of each tag for a user to determine their preferences
     * @param userRatings All of the user's ratings, storing tag value : rating
     * @return The tag weights for the user
     */
    public static Map<String, Double> getWeights(Map<String, Double> userRatings) {
        Map<String, Double> weighted = new HashMap<String, Double>(); // Map to store tag weights
        Map<String, Integer> amounts = new HashMap<String, Integer>(); // Map to store tag frequencies

        // Iterate over each entry in the user rating map
        for (Map.Entry<String, Double> rating : userRatings.entrySet()) {

            // If current tag is already present in weighted map
            if (weighted.containsKey(rating.getKey())) {
                weighted.replace(rating.getKey(), weighted.get(rating.getKey()) + rating.getValue()); // Add new value to current
                amounts.replace(rating.getKey(), amounts.get(rating.getKey()) + 1); // Increment amount
            } 
            
            // If current tag is a new key in weighted map
            else {
                weighted.put(rating.getKey(), rating.getValue()); // Set rating value as current
                amounts.put(rating.getKey(), 1); // Set amount to 1
            }
        }

        // Iterate through weights map and divide each value by the corresponding frequency
        for (String key : weighted.keySet()) {
            weighted.replace(key, weighted.get(key) / (float) amounts.get(key));
        }

        return weighted;
    }

    public static Map<String, Double> getAvgRatings(Map<String, Double> restrRatings) {
        Map<String, Double> average = new HashMap<String, Double>(); // Map to store average rating per tag
        Map<String, Integer> amounts = new HashMap<String, Integer>(); // Map to store rating frequencies

        // Iterate over each entry in the restr rating map
        for (Map.Entry<String, Double> rating : restrRatings.entrySet()) {

            // If current tag is already present in average map
            if (average.containsKey(rating.getKey())) {
                average.replace(rating.getKey(), average.get(rating.getKey()) + rating.getValue()); // Set rating value as current
                amounts.replace(rating.getKey(), amounts.get(rating.getKey()) + 1); // Set amount to 1
            }

            // If current tag is a new key in average map
            else {
                average.put(rating.getKey(), rating.getValue()); // Set rating value as current
                amounts.put(rating.getKey(), 1); // Set amount to 1
            }
        }

        // Iterate through average map and divide each value by the corresponding frequency
        for (String key : average.keySet()) {
            average.replace(key, average.get(key) / (float) amounts.get(key));
        }

        return average;
    }

}
