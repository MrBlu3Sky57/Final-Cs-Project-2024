package com.ufa.cs.project;

// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Controller {
    @GetMapping(path="/search")
    public ResponseEntity<Object> getMethodName(@RequestParam(name="name") String name) {
        if (!Helpers.checkUser(name)) {
            return new ResponseEntity<Object>("There is no user with this name.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Object>(Helpers.getUserId(name), HttpStatus.OK);
        }
    }
    
}
