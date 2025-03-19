/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.controller;

import com.bookstore.view.panel.AccountManagementPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author HP
 */
public class AccountManagementController implements ActionListener{
    private AccountManagementPanel accountManagementPanel;

    public AccountManagementController(AccountManagementPanel accountManagementPanel) {
        this.accountManagementPanel = accountManagementPanel;
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
       String button = e.getActionCommand();
       
       if(button.equals("D:\\QLBH\\java\\bookstore\\src\\main\\java\\com\\bookstore\\resources\\icon\\edit-icon-8x8.png")){
           
       }
    }
}
