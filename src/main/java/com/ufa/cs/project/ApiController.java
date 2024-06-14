/*
 * This is the API controller for my application. It routes HTTP requests 
 * and sends the necessary responses
 * 
 *  Completely Programmed by Aaron Avram
 *  Date Programmed: June 14, 2024
 */

package com.ufa.cs.project;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;




// Rest controller that implenents REStful APIs
@RestController
public class ApiController {
    Map<String, Restaurant> restaurants = null;
    Map<String, User> users = null;

    /**
     * When ApiController is instantiated, load the users and restaurants from the database
     */
    public ApiController() {
        if (restaurants == null) {
            restaurants = Helpers.getRestr();
        }

        if (users == null) {
            users = Helpers.getUsers();
        }
    }
    
    /**
     * Takes a login request and responds with a verification
     * @param username The entered username
     * @param password The entered password
     * @return Verify user and return id if user is in database
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam(name="username") String username, @RequestParam(name="password") String password) {
        boolean status = Helpers.authenticateUser(username, password);
        Map<String, Object> responseBody = new HashMap<String, Object>();
    
        if (status) {
            responseBody.put("verification", true);
            responseBody.put("id", Helpers.getUserId(username));
            return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("verification", false);
            return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Takes a signUp request and responds with a verification
     * @param username The tentative username
     * @param password The tentative password
     * @return Verify user and return id if username is not in database
     */
    @PostMapping("/signUp")
    public ResponseEntity<Object> signUp(@RequestParam(name="username") String username, @RequestParam(name="password") String password) {
        Map<String, Object> responseBody = new HashMap<String, Object>();
        
        if (username == "" || password == "") {
            responseBody.put("verification", false);
            return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
        }
        boolean status = Helpers.addUser(username, password, users);
    
        if (status) {
            responseBody.put("verification", true);
            responseBody.put("id", Helpers.getUserId(username));
            return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("verification", false);
            return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Processes an added restaurant
     * @param name The restaurant name
     * @param location The restaurant location
     * @param menu The restaurant's menu
     * @param tags The restaurant's tags
     * @return A verification if the restaurant was valid
     */ 
    @PostMapping("/addRestr")
public ResponseEntity<Object> addRestr(@RequestParam(name="name") String name, @RequestParam(name="location") String location, @RequestParam(name="menu") String menu, @RequestParam(name="tags") String tags) {
    Map<String, Object> responseBody = new HashMap<>();

    try {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> menuMap = objectMapper.readValue(menu, new TypeReference<Map<String, Double>>() {});
        Map<String, String> tagsMap = objectMapper.readValue(tags, new TypeReference<Map<String, String>>() {});

        boolean status = Helpers.addRestaurant(name, location, menuMap, tagsMap, restaurants);

        if (status) {
            responseBody.put("verification", true);
            responseBody.put("id", Helpers.getRestrId(name));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("verification", false);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        System.out.println("Error in addRestr: " + e.getMessage());
        e.printStackTrace();
        responseBody.put("verification", false);
        responseBody.put("error", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

   
    /**
     * Processes a new rating
     * @param ratingType The rating type
     * @param rating The rating value
     * @param userId The user who rated
     * @param restrName The restaurant that was rated
     * @return A verification if the rating was valid
     */
    @PostMapping("/rate")
    public ResponseEntity<Object> rate(@RequestParam(name="rating_type") String ratingType, @RequestParam(name="rating") Double rating, @RequestParam(name="user_id") String userId, @RequestParam(name="restr_name") String restrName) {
        Map<String, Object> responseBody = new HashMap<>();
        
        if (Helpers.checkRestaurant(restrName)) {
            Helpers.addRating(userId, Helpers.getRestrId(restrName), ratingType, rating);
            responseBody.put("success", true);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("success", false);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Search restaurant by name
     * @param name The name of the restaurant
     * @return The restaurant if it exists, else some popular restaurants
     */
    @GetMapping(path="/search")
    public ResponseEntity<Object> search(@RequestParam(name="name") String name) {
        Map<String, Object> responseBody = new HashMap<String, Object>();
        if (!Helpers.checkRestaurant(name)) {
            responseBody.put("error", true);
            responseBody.put("error_message", "There is no restaurant with this name in our records.");

            String[] ids = Recommend.avgRecommend(restaurants, 5);

            Map<String, Restaurant> recs = new HashMap<>();
            Map<String, Map<String, Double>> ratings = new HashMap<>();
            for (String id : ids) {
                recs.put(id, restaurants.get(id));
                ratings.put(id, Recommend.getAvgRatings(Helpers.getRestrRatings(id)));
            } 
            responseBody.put("restaurant", recs);
            responseBody.put("ratings", ratings);
            return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
        } else {
            responseBody.put("error", false);
            responseBody.put("restaurant", restaurants.get(Helpers.getRestrId(name)));
            responseBody.put("ratings", Recommend.getAvgRatings(Helpers.getRestrRatings(Helpers.getRestrId(name))));
            return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
        }
    }

    /**
     * Give the standard restaurant recommendations
     * @return Most popular restaurants
     */
    @GetMapping(path="/avgRecommend")
    public ResponseEntity<Object> avgRecommend() {
        Map<String, Object> responseBody = new HashMap<String, Object>();
        String[] ids = Recommend.avgRecommend(restaurants, 5);

        Map<String, Restaurant> recs = new HashMap<>();
        Map<String, Map<String, Double>> ratings = new HashMap<>();
        for (String id : ids) {
            recs.put(id, restaurants.get(id));
            ratings.put(id, Recommend.getAvgRatings(Helpers.getRestrRatings(id)));
        } 
        responseBody.put("restaurant", recs);
        responseBody.put("ratings", ratings);
        return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
    }


    /**
     * Gives a user personalized recommendations
     * @param userId The user's id
     * @return The personalized recommended restaurants
     */
    @GetMapping(path="/smartRecommend")
    public ResponseEntity<Object> smartRecommend(@RequestParam(name="user_id") String userId) {
        Map<String, Object> responseBody = new HashMap<String, Object>();
        String[] ids = Recommend.smartRecommend(restaurants, userId);

        Map<String, Restaurant> recs = new HashMap<>();
        Map<String, Map<String, Double>> ratings = new HashMap<>();
        for (String id : ids) {
            recs.put(id, restaurants.get(id));
            ratings.put(id, Recommend.getAvgRatings(Helpers.getRestrRatings(id)));
        } 
        responseBody.put("restaurant", recs);
        responseBody.put("ratings", ratings);
        return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
    }
}
