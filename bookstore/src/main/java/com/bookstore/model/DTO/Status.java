/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.bookstore.model.DTO;

import org.bouncycastle.jcajce.provider.asymmetric.EC;

/**
 *
 * @author HP
 */
public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");
    private final String description;

    private Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Status{" + "description=" + description + '}';
    }
    
}
