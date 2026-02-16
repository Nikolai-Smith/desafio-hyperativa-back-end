/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author nikolaismith
 */

@Service
public class JwtService {

    @Value("${jwt.secretBase64}")
    private String jwtSecretBase64;

    @Value("${jwt.expMinutes}")
    private long expMinutes;

    private volatile Key cachedKey;

    private Key key() {
        Key k = cachedKey;
        if (k != null) return k;

        synchronized (this) {
            if (cachedKey != null) return cachedKey;

            if (jwtSecretBase64 == null || jwtSecretBase64.isBlank()) {
                throw new IllegalStateException("JWT secret (Base64) is missing. Set jwt.secretBase64 / JWT_SECRET_BASE64.");
            }

            byte[] secretBytes;
            try {
                secretBytes = Base64.getDecoder().decode(jwtSecretBase64);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("JWT secret is not valid Base64.", e);
            }

            if (secretBytes.length < 32) {
                throw new IllegalStateException("JWT secret is too short. Provide at least 32 bytes (Base64 decoded).");
            }

            cachedKey = Keys.hmacShaKeyFor(secretBytes);
            return cachedKey;
        }
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expMinutes * 60_000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return claims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            claims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims claims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}