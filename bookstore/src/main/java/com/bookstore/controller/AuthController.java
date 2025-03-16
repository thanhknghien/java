package com.bookstore.controller;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.bookstore.BUS.AuthBUS;
import com.bookstore.utils.ShowException;
import com.bookstore.view.main.Login;

public class AuthController {
    private AuthBUS bus;
    private Login view;

    public AuthController(){
        bus = new AuthBUS();
    }

    public void handleLogin(String username, String password) throws SQLException{
        try {
            if(bus.login(username, password)){
                JOptionPane.showMessageDialog(view, "Login Successful");
            }
        } catch (ShowException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleRegister(String fullName, String username, String password,String confirmPassword, String email, String address,String phone) throws SQLException{
    try {
        if(bus.register(fullName, username, password, confirmPassword, email, address, phone)){
            JOptionPane.showMessageDialog(view, "Register Successful");
        }
    } catch (ShowException e) {
        JOptionPane.showMessageDialog(view, e.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);
    }
    }
}
