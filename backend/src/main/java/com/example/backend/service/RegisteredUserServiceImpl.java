package com.example.backend.service;

import com.example.backend.repository.RegisteredUserRepository;
import com.example.backend.service.interfaces.RegisteredUserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisteredUserServiceImpl implements RegisteredUserService {

    private final RegisteredUserRepository userRepository;
}
