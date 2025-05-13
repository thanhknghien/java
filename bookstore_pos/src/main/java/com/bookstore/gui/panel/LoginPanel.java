package com.bookstore.gui.panel;

import com.bookstore.controller.LoginController;
import com.bookstore.dao.UserDAO;
import com.bookstore.gui.component.PanelCover;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPanel extends PanelCover {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserDAO userDAO;

    public LoginPanel() {
        super( );
        initComponents();
        setupLayout();
        setupListeners();
        userDAO = new UserDAO();
    }

    private void initComponents() {
        // Initialize components with modern styling
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        loginButton.setFocusPainted(false);
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        gbc.gridy = 1;
        add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridy = 3;
        add(passwordField, gbc);

        // Login button
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 10, 5, 10);
        add(loginButton, gbc);
    }

    private void setupListeners() {
        LoginController loginController = new LoginController(this);
        loginButton.addActionListener(loginController.createLoginActionListener(usernameField, passwordField));
    }
}