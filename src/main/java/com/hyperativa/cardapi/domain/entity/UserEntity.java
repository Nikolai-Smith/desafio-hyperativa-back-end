/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 *
 * @author nikolaismith
 */

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { 
        return id; 
    }

    public String getUsername() { 
        return username; 
    }

    public String getPasswordHash() { 
        return passwordHash; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }
}