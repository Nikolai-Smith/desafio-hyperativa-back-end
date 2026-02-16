/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.service;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 *
 * @author nikolaismith
 */

@Service
public class PanHashService {

    public byte[] sha256Bytes(String pan) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(pan.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Could not hash PAN", e);
        }
    }
}
