/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.gui.main;

import javax.swing.*;
import com.bookstore.gui.main.LoginGUI;

/**
 * Lớp khởi động ứng dụng
 * @author HP
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // Khởi động ứng dụng bằng cách mở màn hình đăng nhập
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Lỗi khởi tạo ứng dụng: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    
}
