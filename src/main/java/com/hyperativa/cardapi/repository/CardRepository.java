/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.repository;

import com.hyperativa.cardapi.domain.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 *
 * @author nikolaismith
 */

public interface CardRepository extends JpaRepository<CardEntity, String> {
    Optional<CardEntity> findByPanHash(byte[] panHash);
}
