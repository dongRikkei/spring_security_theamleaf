package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HomeController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("homepage")
    public ResponseEntity<?> index() {
        return ResponseEntity.ok("Trang khong can dang nhap");
    }

    @GetMapping("user")
    public ResponseEntity<?> user() {
        return ResponseEntity.ok("User");
    }

    @GetMapping("/dang-nhap")
    public String login() {

        return "login";
    }

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/product")
    public String homeIndex() {
        try {
        // process
        } catch (Exception e) {

        }
        return "home";
    }

    @PostMapping("/user/{id}")
    public String userInfo(@PathVariable long id, Model model) {
        Optional<User> userOpt = iUserService.findById(id);
        if (!userOpt.isPresent()) {
            throw new NotFoundException("User not found");
        }

        model.addAttribute("user", userOpt.get());

        return "user-details";
    }
}
