package com.bookstore;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;  
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.*;


public class TestThanh extends JFrame {

    public TestThanh(){
        setTitle("Đăng nhập");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Tên đăng nhập:");
        JTextField userText = new JTextField(15);
        JLabel passLabel = new JLabel("Mật khẩu:");
        JPasswordField passText = new JPasswordField(15);
        JButton loginButton = new JButton("Đăng nhập");
        JButton cancelButton = new JButton("Hủy");

        // Định vị các thành phần trên giao diện
        gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(userText, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(passText, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(loginButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(cancelButton, gbc);
        add(panel);
        setVisible(true);
    }
    public static void main(String[] args)  {
        new TestThanh();
    }
}
