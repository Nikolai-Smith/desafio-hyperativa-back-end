/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.controller.dto;

/**
 *
 * @author nikolaismith
 */

public class CardImportResponse {

    private String importId;
    private String lote;
    private Integer declaredCount;

    private int received;
    private int inserted;
    private int duplicate;
    private int invalid;

    private String status;

    public CardImportResponse() {}

    public CardImportResponse(String importId, String lote, Integer declaredCount, int received, int inserted, int duplicate, int invalid, String status) {
        this.importId = importId;
        this.lote = lote;
        this.declaredCount = declaredCount;
        this.received = received;
        this.inserted = inserted;
        this.duplicate = duplicate;
        this.invalid = invalid;
        this.status = status;
    }

    public String getImportId() { return importId; }
    public void setImportId(String importId) { this.importId = importId; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public Integer getDeclaredCount() { return declaredCount; }
    public void setDeclaredCount(Integer declaredCount) { this.declaredCount = declaredCount; }

    public int getReceived() { return received; }
    public void setReceived(int received) { this.received = received; }

    public int getInserted() { return inserted; }
    public void setInserted(int inserted) { this.inserted = inserted; }

    public int getDuplicate() { return duplicate; }
    public void setDuplicate(int duplicate) { this.duplicate = duplicate; }

    public int getInvalid() { return invalid; }
    public void setInvalid(int invalid) { this.invalid = invalid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
