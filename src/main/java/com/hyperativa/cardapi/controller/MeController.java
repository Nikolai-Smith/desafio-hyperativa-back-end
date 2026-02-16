/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 *
 * @author nikolaismith
 */

@RestController
@RequestMapping("/api/v1")
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of("username", auth.getName());
    }
}
