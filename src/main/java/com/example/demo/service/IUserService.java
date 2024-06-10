package com.example.demo.service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService{
    User save(UserRegistrationDto registrationDto);
    List<User> getAll();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User findOrCreateUser(String name, String email);
}
