package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthencationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByFirstName(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        User user = userOptional.get();

        return new AuthencationUser(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(Arrays.asList(user.getRole())),
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    private List<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
