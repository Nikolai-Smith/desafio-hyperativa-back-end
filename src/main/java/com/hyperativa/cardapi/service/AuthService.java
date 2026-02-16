/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.service;

import com.hyperativa.cardapi.config.security.JwtService;
import com.hyperativa.cardapi.controller.dto.LoginRequest;
import com.hyperativa.cardapi.controller.dto.LoginResponse;
import com.hyperativa.cardapi.domain.entity.UserEntity;
import com.hyperativa.cardapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author nikolaismith
 */

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest req) {
        String username = req.getUsername();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Invalid login attempt (user not found): {}", username);
                    return new BadCredentialsException("Invalid credentials");
                });

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            log.warn("Invalid login attempt (wrong password): {}", username);
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        log.info("User authenticated successfully: {}", username);

        return new LoginResponse(token);
    }
}
