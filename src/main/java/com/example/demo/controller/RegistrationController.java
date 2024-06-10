package com.example.demo.controller;

import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private IUserService iUserService;


    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("dtoUser", new RegisterDto());
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("dtoUser") RegisterDto registerDto) {
        try {
            iUserService.save(registerDto.getUserRegistrationDto());
        }catch(Exception e)
        {
            System.out.println(e);
            return "redirect:/registration?email_invalid";
        }
        return "redirect:/registration?success";
    }
}
