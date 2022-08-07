package com.example.ribbon.controller;

import com.example.ribbon.model.User;
import com.example.ribbon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    UserService userService;
    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("newUser", new User());
        model.addAttribute("title", "Регистрация");
        return "registration";
    }

    @PostMapping("/registration")
    public String newUser(Model model, @ModelAttribute("newUser") @Valid User newUser, BindingResult bindingResult){
        model.addAttribute("title","Регистрация");
        if(bindingResult.hasErrors()) {
            model.addAttribute("registrationError","Ошибка");
            return "registration";}
        if(!newUser.getPassword().equals(newUser.getPasswordConfirmation())){
            model.addAttribute("registrationError","Пароли не совпадают");
            return "registration";
        }
        if(!userService.saveUser(newUser)){
            model.addAttribute("registrationError","Пользователь с таким именем уже существует");
            return "registration";
        }
        return "redirect:/";

    }
}
