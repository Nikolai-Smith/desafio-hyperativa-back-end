/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author nikolaismith
 */

@Entity
@Table(name = "imports")
public class ImportEntity {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "batch_lote")
    private String batchLote;

    @Column(name = "declared_count")
    private Integer declaredCount;

    @Column(name = "received_count", nullable = false)
    private int receivedCount;

    @Column(name = "inserted_count", nullable = false)
    private int insertedCount;

    @Column(name = "duplicate_count", nullable = false)
    private int duplicateCount;

    @Column(name = "invalid_count", nullable = false)
    private int invalidCount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        if (startedAt == null) startedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getBatchLote() { return batchLote; }
    public void setBatchLote(String batchLote) { this.batchLote = batchLote; }

    public Integer getDeclaredCount() { return declaredCount; }
    public void setDeclaredCount(Integer declaredCount) { this.declaredCount = declaredCount; }

    public int getReceivedCount() { return receivedCount; }
    public void setReceivedCount(int receivedCount) { this.receivedCount = receivedCount; }

    public int getInsertedCount() { return insertedCount; }
    public void setInsertedCount(int insertedCount) { this.insertedCount = insertedCount; }

    public int getDuplicateCount() { return duplicateCount; }
    public void setDuplicateCount(int duplicateCount) { this.duplicateCount = duplicateCount; }

    public int getInvalidCount() { return invalidCount; }
    public void setInvalidCount(int invalidCount) { this.invalidCount = invalidCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
}
