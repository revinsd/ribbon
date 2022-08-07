package com.example.ribbon.controller;

import com.example.ribbon.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAdviceContoller {
    @ModelAttribute
    public void modelAttribute(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("user",user);
    }
}
