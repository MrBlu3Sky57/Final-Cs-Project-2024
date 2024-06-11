/*
 * This a class with methods that implement sorting and calculations for the recommendations
 * on the Restaurant Tracker app. 
 * 
 *  Completely Programmed by Aaron Avram
 *  Date Programmed: June 14, 2024
 */

package com.ufa.cs.project;

import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

public class Recommend {

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
     * @param restrRatings An array of restaurant tag value and rating
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
        // If edge cases stop
        if (arr == null || arr.length < 2) {
            return;
        } else {
            mergeSort(arr, 0, arr.length - 1, key);
        }
    }

    /**
     * The recursive sorting function
     * @param arr The array to sort
     * @param left The left bound
     * @param right The right bound
     * @param key The key by which to sort the array
     */
    public static void mergeSort(String[] arr, int left, int right, Map<String, Double> key) {
        // If indices are in proper order
        if (left < right) {
            // Find middle
            int mid = (left + right) / 2;

            // Sort left and right halves then merge them
            mergeSort(arr, left, mid, key);
            mergeSort(arr, mid + 1, right, key);
            merge(arr, left, mid, right, key);
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

        // Populate half arrays with data
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

        // Merge two half arrays into a new sorted one
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

        // Add any remaining data in both halves

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
     * @return The ids of the top 50 restaurants
     */
    public static String[] avgRecommend(Map<String, Restaurant> restaurants) {
        Map<String, Double> netAvgRatings = new HashMap<String, Double>();

        // Get the ids of all of the restaurants
        Set<String> keySet = restaurants.keySet();
        String[] ids = new String[keySet.size()];
        ids = keySet.toArray(ids);

        // For each id, get the avg ratings of the restaurant
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

        // Sort the ids based on average rating
        sort(ids, netAvgRatings);

        int numReturn = 0;
        if (ids.length < 50) {
            numReturn = ids.length;
        } else {
            numReturn = 50;
        }

        // Return the recommendations
        String[] recs = new String[numReturn];
        for (int i = 0; i < numReturn; i++) {
            recs[i] = ids[i];
        }

        return recs;
    }

    /**
     * Gets the top n highest rated restaurants on average
     * @param restaurants The restaurants map
     * @param numReturns The number, n, of restaurants to return
     * @return The ids of the top n restaurants
     */
    public static String[] avgRecommend(Map<String, Restaurant> restaurants, int numReturns) {
        Map<String, Double> netAvgRatings = new HashMap<String, Double>();

        // Get the restaurant ids
        Set<String> keySet = restaurants.keySet();
        String[] ids = new String[keySet.size()];
        ids = keySet.toArray(ids);

        // For each restaurant id get the average rating
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

        // Sort ids based on average rating
        sort(ids, netAvgRatings);

        int numReturn = 0;
        if (ids.length < numReturns) {
            numReturn = ids.length;
        } else {
            numReturn = numReturns;
        }

        // Return the right number of ids
        String[] recs = new String[numReturn];
        for (int i = 0; i < numReturn; i++) {
            recs[i] = ids[i];
        }

        return recs;
    }

    /**
     * Get the recommendations for a user based on their preferences
     * @param restaurants The restaurants map
     * @param user_id The user id
     * @return The top new restaurants for the user by id.
     */
    public static String[] smartRecommend(Map<String, Restaurant> restaurants, String user_id) {
        String[][] userRatings = Helpers.getUserRatings(user_id);
        Map<String, Double> weights = getWeights(userRatings);
        Map<String, Double> netRatings = new HashMap<String, Double>();

        // Get all of the restaurant ids
        Set<String> keySet = restaurants.keySet();
        String[] ids = new String[keySet.size()];
        ids = keySet.toArray(ids);

        // Get the average rating and personal user rating for each id
        for (String id : ids) {
            String[][] restrRatings = Helpers.getRestrRatings(id);
            Map<String, Double> avgRating = getAvgRatings(restrRatings);

            Double net = 0.0;

            // Find a weighted average, unique to the user, for each id
            for (String key : avgRating.keySet()) {
                if (weights.containsKey(key)) {
                    net += avgRating.get(key) * weights.get(key);
                } else {
                    net += avgRating.get(key);
                }
            }
            
            netRatings.put(id, net);
        }

        // Sort the restaurant ids based on these ratings
        sort(ids, netRatings);

        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            if (!Helpers.rated(user_id, ids[i])) {
                temp.add(ids[i]);
                if (temp.size() == 5) {
                    break;
                }
            }
        }

        // Return the correct number of recommendations
        String[] recs = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            recs[i] = temp.get(i);
        }

        return recs;
    }

}
