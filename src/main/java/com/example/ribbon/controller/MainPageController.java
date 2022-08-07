package com.example.ribbon.controller;

import com.example.ribbon.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {
    @Autowired
    PostRepository postRepository;
    @GetMapping("/")
    public String mainPage(Model model){
        return "mainPage";
    }




}
