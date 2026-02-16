/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.controller;

import com.hyperativa.cardapi.service.AuthService;
import com.hyperativa.cardapi.controller.dto.LoginRequest;
import com.hyperativa.cardapi.controller.dto.LoginResponse;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author nikolaismith
 */

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
