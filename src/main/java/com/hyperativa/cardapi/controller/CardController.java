/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.controller;

import com.hyperativa.cardapi.controller.dto.CardCreateRequest;
import com.hyperativa.cardapi.controller.dto.CardCreateResponse;
import com.hyperativa.cardapi.controller.dto.CardImportResponse;
import com.hyperativa.cardapi.controller.dto.CardLookupResponse;
import com.hyperativa.cardapi.domain.entity.ImportEntity;
import com.hyperativa.cardapi.service.CardImportService;
import com.hyperativa.cardapi.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;

/**
 *
 * @author nikolaismith
 */

@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;
    private final CardImportService cardImportService;

    public CardController(CardService cardService, CardImportService cardImportService) {
        this.cardService = cardService;
        this.cardImportService = cardImportService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CardCreateResponse> create(@Valid @RequestBody CardCreateRequest req) {
        CardService.CreateResult result = cardService.create(req.getPan());

        CardCreateResponse body = new CardCreateResponse(result.id(), result.duplicate());

        if (result.duplicate()) {
            return ResponseEntity.ok(body);
        }

        return ResponseEntity
                .created(URI.create("/api/v1/cards/" + result.id()))
                .body(body);
    }

    @GetMapping(value = "/lookup", produces = "application/json")
    public ResponseEntity<CardLookupResponse> lookup(
            @RequestParam("pan")
            @Pattern(regexp = "^[0-9]+$", message = "pan must contain only digits")
            @Size(min = 13, max = 19)
            String pan
    ) {
        CardService.LookupResult result = cardService.lookup(pan);
        return ResponseEntity.ok(new CardLookupResponse(result.exists(), result.id()));
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CardImportResponse> importTxt(@RequestPart("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new CardImportResponse(null, null, null, 0, 0, 0, 0, "FAILED")
            );
        }

        ImportEntity imp = cardImportService.importTxt(file);

        CardImportResponse body = new CardImportResponse(
                imp.getId(),
                imp.getBatchLote(),
                imp.getDeclaredCount(),
                imp.getReceivedCount(),
                imp.getInsertedCount(),
                imp.getDuplicateCount(),
                imp.getInvalidCount(),
                imp.getStatus()
        );

        return ResponseEntity.ok(body);
    }
}
