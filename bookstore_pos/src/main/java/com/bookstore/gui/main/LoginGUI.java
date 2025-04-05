package com.bookstore.gui.main;

import com.bookstore.gui.panel.LoginPanel;
import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private LoginPanel loginPanel;

    public LoginGUI() {
        initComponents();
        setupLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bookstore POS - Login");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        loginPanel = new LoginPanel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}
