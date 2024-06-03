package com.ufa.cs.project;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
