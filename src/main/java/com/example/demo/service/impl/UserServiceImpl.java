package com.example.demo.service.impl;

import com.example.demo.config.Constant;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthencationUser;
import com.example.demo.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = modelMapper.map(registrationDto, User.class);

        // Set password with encrypt
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        // get role from DB
        Role roleCustomer = roleRepository.findById(Constant.ROLE_CUSTOMER).get();
        user.setRole(roleCustomer);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findOrCreateUser(String name, String email) {
        return userRepository.findUserByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
//                    user.setId(facebookId);
                    user.setLastName(name);
                    user.setEmail(email);
                    return userRepository.save(user);
                });
    }
}
