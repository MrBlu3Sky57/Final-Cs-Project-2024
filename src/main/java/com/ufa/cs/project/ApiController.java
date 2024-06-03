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
        if (!Helpers.checkUser(name)) {
            responseBody.put("error", true);
            responseBody.put("error_message", "There is no restaurant with this name in our records.");
            responseBody.put("message", "Here are some other popular restaurants you might like:");
            responseBody.put("restaurant", Recommend.avgRecommend(restaurants, 5));
            return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
        } else {
            responseBody.put("error", false);
            responseBody.put("restaurant", Helpers.getUserId(name));
            return new ResponseEntity<Object>(Helpers.getUserId(name), HttpStatus.OK);
        }
    }
}
