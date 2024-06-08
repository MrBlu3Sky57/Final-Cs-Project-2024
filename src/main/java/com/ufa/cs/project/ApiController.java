package com.ufa.cs.project;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ApiController {
    Map<String, Restaurant> restaurants = null;
    Map<String, User> users = null;

    public ApiController() {
        if (restaurants == null) {
            restaurants = Helpers.getRestr();
        }

        if (users == null) {
            users = Helpers.getUsers();
        }
    }
    
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

    @PostMapping("/signUp")
    public ResponseEntity<Object> signUp(@RequestParam(name="username") String username, @RequestParam(name="password") String password) {
        boolean status = Helpers.addUser(username, password, users);
        System.out.println(username + password);
        System.out.println(status);
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
