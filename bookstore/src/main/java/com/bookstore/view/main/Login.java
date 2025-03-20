package com.bookstore.view.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import com.bookstore.controller.AuthController;

public class Login extends JFrame {
    private JPanel panel;
    private CardLayout tab;
    private AuthController controller;

    public Login(){
        init();
        controller = new AuthController();

        tab = new CardLayout();
        panel = new JPanel(tab);


        panel.add(createRegisterPanel(), "Register");
        panel.add(createLoginPanel(), "Login");
        
        add(panel);
        tab.show(panel, "Login");
    }

    public void init(){
        setTitle("Book Store System");
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createLoginPanel(){
        JPanel loginPnl = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);

        JLabel lbUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(20);
        JLabel lbPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(20);
        JButton btnLogin = new JButton("Login");
        JButton btnSwitch = new JButton("Register");

        c.gridx = 0; c.gridy = 0; loginPnl.add(lbUsername,c);
        c.gridx = 1; c.gridy = 0; loginPnl.add(txtUsername,c);
        c.gridx = 0; c.gridy = 1; loginPnl.add(lbPassword,c);
        c.gridx = 1; c.gridy = 1; loginPnl.add(txtPassword,c);
        c.gridx = 0; c.gridy = 2; loginPnl.add(btnLogin,c);
        c.gridx = 1; c.gridy = 2; loginPnl.add(btnSwitch,c);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                try {
                    controller.handleLogin(username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
            
        btnSwitch.addActionListener(e -> tab.show(panel, "Register")); 
        return loginPnl;
    }

    private JPanel createRegisterPanel(){
        JPanel registerPnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbName = new JLabel("Full Name:");
        JLabel lbUsername = new JLabel("Username:");
        JLabel lbEmail = new JLabel("Email:");
        JLabel lbPhoneNumber = new JLabel("Phone Number:");
        JLabel lbAddress = new JLabel("Address:");
        JLabel lbPassword = new JLabel("Password:");
        JLabel lbConfirmPassword = new JLabel("Confirm Password:");

        JTextField txtName = new JTextField(20);
        JTextField txtUsername = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtPhoneNumber = new JTextField(20);
        JTextField txtAddress = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        JPasswordField txtConfirmPassword = new JPasswordField(20);

        JButton btnRegister = new JButton("Register");
        JButton btnSwitch = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; registerPnl.add(lbName, gbc);
        gbc.gridx = 1; gbc.gridy = 0; registerPnl.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; registerPnl.add(lbUsername, gbc);
        gbc.gridx = 1; gbc.gridy = 1; registerPnl.add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 2; registerPnl.add(lbEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 2; registerPnl.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 3; registerPnl.add(lbPhoneNumber, gbc);
        gbc.gridx = 1; gbc.gridy = 3; registerPnl.add(txtPhoneNumber, gbc);
        gbc.gridx = 0; gbc.gridy = 4; registerPnl.add(lbAddress, gbc);
        gbc.gridx = 1; gbc.gridy = 4; registerPnl.add(txtAddress, gbc);
        gbc.gridx = 0; gbc.gridy = 5; registerPnl.add(lbPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 5; registerPnl.add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 6; registerPnl.add(lbConfirmPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 6; registerPnl.add(txtConfirmPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 7; registerPnl.add(btnRegister, gbc);
        gbc.gridx = 1; gbc.gridy = 7; registerPnl.add(btnSwitch, gbc);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = txtName.getText();
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                String email = txtEmail.getText();
                String phoneNumber = txtPhoneNumber.getText();
                String address = txtAddress.getText();

                try {
                    controller.handleRegister(fullName, username, password, confirmPassword, email, address, phoneNumber);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnSwitch.addActionListener(e -> tab.show(panel, "Login"));
        return registerPnl;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}