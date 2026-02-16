/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.repository;

import com.hyperativa.cardapi.domain.entity.ImportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nikolaismith
 */

public interface ImportRepository extends JpaRepository<ImportEntity, String> {
}
