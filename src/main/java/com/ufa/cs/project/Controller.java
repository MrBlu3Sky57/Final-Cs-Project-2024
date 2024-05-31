package com.ufa.cs.project;

// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Controller {
    @GetMapping(path="/search")
    public ResponseEntity<Object> getMethodName(@RequestParam(name="name") String name) {
        Map<String, Object> responseBody = new HashMap<String, Object>();
        if (!Helpers.checkUser(name)) {
            responseBody.put("error", true);
            responseBody.put("message", "There is no user with this name.");

            return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
        } else {
            responseBody.put("error", false);
            responseBody.put("userId", Helpers.getUserId(name));
            return new ResponseEntity<Object>(Helpers.getUserId(name), HttpStatus.OK);
        }
    }
    
}
