/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.controller.dto;

/**
 *
 * @author nikolaismith
 */

public class CardLookupResponse {

    private boolean exists;
    private String id;

    public CardLookupResponse() {}

    public CardLookupResponse(boolean exists, String id) {
        this.exists = exists;
        this.id = id;
    }

    public boolean isExists() { return exists; }
    public void setExists(boolean exists) { this.exists = exists; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
