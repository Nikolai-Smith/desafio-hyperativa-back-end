/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.hyperativa.cardapi.controller.dto;

/**
 *
 * @author nikolaismith
 */

public class CardCreateResponse {

    private String id;
    private boolean duplicate;

    public CardCreateResponse() {}

    public CardCreateResponse(String id, boolean duplicate) {
        this.id = id;
        this.duplicate = duplicate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }
}
