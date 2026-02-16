/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.service;

import com.hyperativa.cardapi.domain.entity.ImportEntity;
import com.hyperativa.cardapi.repository.ImportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nikolaismith
 */

@Service
public class CardImportService {

    private static final Logger log = LoggerFactory.getLogger(CardImportService.class);

    private final ImportRepository importRepository;
    private final CardService cardService;
    private final PanHashService panHashService;

    public CardImportService(ImportRepository importRepository, CardService cardService, PanHashService panHashService) {
        this.importRepository = importRepository;
        this.cardService = cardService;
        this.panHashService = panHashService;
    }

    @Transactional
    public ImportEntity importTxt(MultipartFile file) {
        String originalFilename = (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank())
                ? "upload.txt"
                : file.getOriginalFilename();

        ImportEntity imp = new ImportEntity();
        imp.setOriginalFilename(originalFilename);
        imp.setStatus("RUNNING");
        imp.setReceivedCount(0);
        imp.setInsertedCount(0);
        imp.setDuplicateCount(0);
        imp.setInvalidCount(0);

        imp = importRepository.save(imp);

        String loteFromHeader = null;
        Integer declaredFromHeader = null;

        String loteFromFooter = null;
        Integer declaredFromFooter = null;

        Set<String> seenHashesInFile = new HashSet<>(1024);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            long lineNo = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;

                if (line.isBlank()) {
                    continue;
                }

                // Header
                if (line.startsWith("DESAFIO-HYPERATIVA")) {
                    loteFromHeader = trimSafeSub(line, 37, 45);
                    declaredFromHeader = parseIntOrNull(trimSafeSub(line, 45, 51));
                    continue;
                }

                // Footer
                if (line.startsWith("LOTE")) {
                    loteFromFooter = trimSafeSub(line, 0, 8);
                    declaredFromFooter = parseIntOrNull(trimSafeSub(line, 8, 14));
                    continue;
                }

                // Card record
                if (line.startsWith("C")) {
                    imp.setReceivedCount(imp.getReceivedCount() + 1);

                    String panField = trimSafeSub(line, 7, 26); // cols 08-26 (19 chars)
                    String tail = (line.length() > 26) ? line.substring(26).trim() : "";

                    if (panField.isBlank() || !isDigitsOnly(panField) || panField.length() < 13 || panField.length() > 19 || !tail.isEmpty()) {
                        imp.setInvalidCount(imp.getInvalidCount() + 1);
                        log.debug("IMPORT: invalid card line at {} (lineNo={})", imp.getId(), lineNo);
                        continue;
                    }

                    // De-dupe inside file first (cheap)
                    String hashKey = bytesToHex(panHashService.sha256Bytes(panField));
                    if (!seenHashesInFile.add(hashKey)) {
                        imp.setDuplicateCount(imp.getDuplicateCount() + 1);
                        continue;
                    }

                    CardService.CreateResult result = cardService.create(panField);
                    if (result.duplicate()) {
                        imp.setDuplicateCount(imp.getDuplicateCount() + 1);
                    } else {
                        imp.setInsertedCount(imp.getInsertedCount() + 1);
                    }

                    continue;
                }

                // Any other line type: ignore (but keep debug)
                log.debug("IMPORT: ignored line at {} (lineNo={})", imp.getId(), lineNo);
            }

            // Choose lote/declaredCount
            String lote = firstNonBlank(loteFromHeader, loteFromFooter);
            Integer declared = (declaredFromHeader != null) ? declaredFromHeader : declaredFromFooter;

            imp.setBatchLote(lote);
            imp.setDeclaredCount(declared);

            imp.setStatus("SUCCESS");
            imp.setFinishedAt(LocalDateTime.now());
            return importRepository.save(imp);

        } catch (Exception e) {
            imp.setStatus("FAILED");
            imp.setFinishedAt(LocalDateTime.now());
            importRepository.save(imp);
            throw new IllegalStateException("Import failed (importId=" + imp.getId() + ")", e);
        }
    }

    private static String trimSafeSub(String s, int start, int endExclusive) {
        if (s == null) return "";
        int len = s.length();
        if (start >= len) return "";
        int end = Math.min(endExclusive, len);
        return s.substring(start, end).trim();
    }

    private static Integer parseIntOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception ignored) {
            return null;
        }
    }

    private static boolean isDigitsOnly(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
