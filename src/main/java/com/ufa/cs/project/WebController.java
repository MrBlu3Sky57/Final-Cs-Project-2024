package com.ufa.cs.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {
    String id = "";

    @GetMapping("/")
    public String index() {
        return "index";
        // if (id == "") {
        //     return "redirect:/login";
        // } else {
            
        // }
    }
    
}
