package com.example.ribbon.controller;

import com.example.ribbon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/test")
    public String test(){

        return "test";
    }
}
