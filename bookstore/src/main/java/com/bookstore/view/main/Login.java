package com.bookstore.view.main;

import javax.swing.*;
import java.awt.*;


public class Login extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel loginPanel;
    private JPanel signupPanel;

    public Login(){
        initComponents();        
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        
        // Login Panel
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField loginUsername = new JTextField(20);
        loginPanel.add(loginUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField loginPassword = new JPasswordField(20);
        loginPanel.add(loginPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);

        // Signup Panel
        signupPanel = new JPanel(new GridBagLayout());
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        signupPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        JTextField fullName = new JTextField(20);
        signupPanel.add(fullName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        signupPanel.add(new JLabel("Email / Username:"), gbc);

        gbc.gridx = 1;
        JTextField signupUsername = new JTextField(20);
        signupPanel.add(signupUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        signupPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField signupPassword = new JPasswordField(20);
        signupPanel.add(signupPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        signupPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField confirmPassword = new JPasswordField(20);
        signupPanel.add(confirmPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton signupButton = new JButton("Sign Up");
        signupPanel.add(signupButton, gbc);

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Sign Up", signupPanel);

        add(tabbedPane, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setVisible(true);
    }
}
