package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Role findById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundException("User not exists"));
    }
}
