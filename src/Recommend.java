import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public interface Recommend {


    /**
     * Gets the weight of each tag for a user to determine their preferences
     * @param userRatings All of the user's ratings, storing tag value in parallel to rating
     * @return The tag weights for the user
     */
    public static Map<String, Double> getWeights(String[][] userRatings) {
        Map<String, Double> weighted = new HashMap<String, Double>(); // Map to store tag weights
        Map<String, Integer> amounts = new HashMap<String, Integer>(); // Map to store tag frequencies

        // Iterate over each entry in the user rating array
        for (int i = 0; i < userRatings[0].length; i++) {

            // If current tag is already present in weighted map
            if (weighted.containsKey(userRatings[0][i])) {
                weighted.replace(userRatings[0][i], weighted.get(userRatings[0][i]) + Double.parseDouble(userRatings[1][i])); // Add new value to current
                amounts.replace(userRatings[0][i], amounts.get(userRatings[0][i]) + 1); // Increment amount
            } 
            
            // If current tag is a new key in weighted map
            else {
                weighted.put(userRatings[0][i], Double.parseDouble(userRatings[1][i])); // Set rating value as current
                amounts.put(userRatings[0][i], 1); // Set amount to 1
            }
        }

        // Iterate through weights map and divide each value by the corresponding frequency
        for (String key : weighted.keySet()) {
            weighted.replace(key, weighted.get(key) / (float) amounts.get(key));
        }

        return weighted;
    }

    /**
     * Get the average rating for a restaurant for each tag value
     * @param restrRatings Map of restaurant tag value and rating
     * @return The average rating for a restaurant for each of its tag values
     */
    public static Map<String, Double> getAvgRatings(String[][] restrRatings) {
        Map<String, Double> average = new HashMap<String, Double>(); // Map to store average rating per tag
        Map<String, Integer> amounts = new HashMap<String, Integer>(); // Map to store rating frequencies

        // Iterate over each entry in the restr rating array
        for (int i = 0; i < restrRatings[0].length; i++) {

            // If current tag is already present in average map
            if (average.containsKey(restrRatings[0][i])) {
                average.replace(restrRatings[0][i], average.get(restrRatings[0][i]) + Double.parseDouble(restrRatings[1][i])); // Set rating value as current
                amounts.replace(restrRatings[0][i], amounts.get(restrRatings[0][i]) + 1); // Set amount to 1
            }

            // If current tag is a new key in average map
            else {
                average.put(restrRatings[0][i], Double.parseDouble(restrRatings[1][i])); // Set rating value as current
                amounts.put(restrRatings[0][i], 1); // Set amount to 1
            }
        }

        // Iterate through average map and divide each value by the corresponding frequency
        for (String key : average.keySet()) {
            average.replace(key, average.get(key) / (float) amounts.get(key));
        }

        return average;
    }

    /**
     * Sort an array using the merge sort algorithm
     * @param arr The array
     * @param key The key by which to sort the array
     */
    public static void sort(String[] arr, Map<String, Double> key) {
        int size, left;

        for (size = 1; size <= arr.length; size = 2 * size) {
            for (left = 0; left < arr.length - 1; left += 2*size) {
                int mid = Math.min(left + size - 1, arr.length - 1);
                int right = Math.min(left + 2*size - 1, arr.length-1);
                merge(arr, left, mid, right, key);
            }
        }
    }

    /**
     * Merge function for each step of the merge sort algorithm
     * @param arr The array
     * @param l The starting index
     * @param m The middle index
     * @param r The end index
     * @param key The key by which to sort the array
     */
    public static void merge(String[] arr, int l, int m, int r, Map<String, Double> key) {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 = r - m;

        String[] half1 = new String[n1];
        String[] half2 = new String[n2];

        for (i = 0; i < n1; i++) {
            half1[i] = arr[l + i];
        }

        for (j = 0; j < n2; j++) {
            half2[j] = arr[m + 1 + j];
        }

        i = 0;
        j = 0;
        k = l;
        while (i < n1 && j < n2) {
            if (key.get(half1[i]) >= key.get(half2[j])) {
                arr[k] = half1[i];
                i++;
            } else {
                arr[k] = half2[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = half1[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = half2[j];
            j++;
            k++;
        }
    }

    /**
     * Gets the top 50 highest rated restaurants on average
     * @param restaurants The restaurants map
     * @return The map of the top 50 restaurants
     */
    public static Map<String, Restaurant> avgRecommend(Map<String, Restaurant> restaurants) {
        Map<String, Double> netAvgRatings = new HashMap<String, Double>();

        Set<String> keySet = restaurants.keySet();
        String[] ids = new String[keySet.size()];
        ids = keySet.toArray(ids);
        for (String id : ids) {
            String[][] restrRatings = Helpers.getRestrRatings(id);
            Map<String, Double> avgRatings = getAvgRatings(restrRatings);

            Double avg = 0.0;
            for (Double rating : avgRatings.values()) {
                avg += rating;
            }
            avg = avg / (double) avgRatings.size();

            netAvgRatings.put(id, avg);
        }

        sort(ids, netAvgRatings);

        int numReturn = 0;
        if (ids.length < 50) {
            numReturn = ids.length;
        } else {
            numReturn = 50;
        }

        Map<String, Restaurant> recs = new HashMap<String, Restaurant>();
        for (int i = 0; i < numReturn; i++) {
            recs.put(ids[i], restaurants.get(ids[i]));
        }

        return recs;
    }

    /**
     * Get the recommendations for a user based on their preferences
     * @param restaurants The restaurants map
     * @param user_id The user id
     * @return The top new restaurants for the user.
     */
    public static Map<String, Restaurant> smartRecommend(Map<String, Restaurant> restaurants, String user_id) {
        String[][] userRatings = Helpers.getUserRatings(user_id);
        Map<String, Double> weights = getWeights(userRatings);
        Map<String, Double> netRatings = new HashMap<String, Double>();

        Set<String> keySet = restaurants.keySet();
        String[] ids = new String[keySet.size()];
        ids = keySet.toArray(ids);

        for (String id : ids) {
            String[][] restrRatings = Helpers.getRestrRatings(id);
            Map<String, Double> avgRating = getAvgRatings(restrRatings);

            Double net = 0.0;
            for (String key : avgRating.keySet()) {
                net += avgRating.get(key) * weights.get(key);
            }
            
            netRatings.put(id, net);
        }

        sort(ids, netRatings);

        int numReturn = 0;
        if (ids.length < 50) {
            numReturn = ids.length;
        } else {
            numReturn = 50;
        }

        Map<String, Restaurant> recs = new HashMap<String, Restaurant>();
        for (int i = 0; i < numReturn; i++) {
            if (!Helpers.rated(user_id, ids[1])) {
                recs.put(ids[i], restaurants.get(ids[i]));
            }
        }

        return recs;
    }

}
