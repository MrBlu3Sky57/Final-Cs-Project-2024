package com.ufa.cs.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping(path="/")
    public String index() {
        return "index";
    }
}
