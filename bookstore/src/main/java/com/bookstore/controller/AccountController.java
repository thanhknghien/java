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
public class AccountController implements ActionListener{
    AccountManagementPanel accountManagementPanel = new AccountManagementPanel();

    public AccountController() {
    }
    public AccountController(AccountManagementPanel accountManagementPanel ) {
        this.accountManagementPanel = accountManagementPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();
        if(button.equals("Nhân viên (2)")){
            //this.accountManagementPanel.Xem(1);
        }
    }
    
}
