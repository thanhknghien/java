package com.bookstore.controller;

import com.bookstore.BUS.LoginBUS;

import com.bookstore.model.User;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginController {
    private LoginBUS loginBUS;
    private JPanel loginPanel;

    public LoginController(JPanel loginPanel) {
        this.loginPanel = loginPanel;
        this.loginBUS = new LoginBUS();
    }

    public ActionListener createLoginActionListener(JTextField usernameField, JPasswordField passwordField) {
        return e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                User user = loginBUS.validateLogin(username, password);
                JOptionPane.showMessageDialog(loginPanel,
                    "Login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                // TODO: Navigate to main application
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(loginPanel,
                    ex.getMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(loginPanel,
                    "Error during login: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
