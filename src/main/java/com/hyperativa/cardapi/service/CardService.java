/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.service;

import com.hyperativa.cardapi.domain.entity.CardEntity;
import com.hyperativa.cardapi.repository.CardRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author nikolaismith
 */

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final PanHashService panHashService;

    public CardService(CardRepository cardRepository, PanHashService panHashService) {
        this.cardRepository = cardRepository;
        this.panHashService = panHashService;
    }

    @Transactional
    public CreateResult create(String panRaw) {
        String pan = normalize(panRaw);
        byte[] hash = panHashService.sha256Bytes(pan);

        var existing = cardRepository.findByPanHash(hash);
        if (existing.isPresent()) {
            return new CreateResult(existing.get().getId(), true);
        }

        CardEntity entity = new CardEntity();
        entity.setPanHash(hash);
        try {
            CardEntity saved = cardRepository.save(entity);
            return new CreateResult(saved.getId(), false);
        } catch (DataIntegrityViolationException ex) {
            return cardRepository.findByPanHash(hash)
                    .map(e -> new CreateResult(e.getId(), true))
                    .orElseThrow(() -> ex);
        }
    }

    @Transactional(readOnly = true)
    public LookupResult lookup(String panRaw) {
        String pan = normalize(panRaw);
        byte[] hash = panHashService.sha256Bytes(pan);

        return cardRepository.findByPanHash(hash)
                .map(e -> new LookupResult(true, e.getId()))
                .orElseGet(() -> new LookupResult(false, null));
    }

    private String normalize(String pan) {
        if (pan == null) {
            throw new IllegalArgumentException("pan is required");
        }
        return pan.trim();
    }

    public record CreateResult(String id, boolean duplicate) {}
    public record LookupResult(boolean exists, String id) {}
}
