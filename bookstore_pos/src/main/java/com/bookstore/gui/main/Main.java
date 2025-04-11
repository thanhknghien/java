/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.gui.main;

import javax.swing.*;
import com.bookstore.gui.panel.UserManagementPanel;
import java.awt.Dimension;

/**
 *
 * @author HP
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Tạo frame chính
            JFrame mainFrame = new JFrame("Quản Lý Cửa Hàng Sách");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Tạo panel quản lý người dùng
            UserManagementPanel userManagementPanel = new UserManagementPanel();
            
            // Thêm panel vào frame
            mainFrame.add(userManagementPanel);
            
            // Thiết lập kích thước tối thiểu cho frame
            mainFrame.setMinimumSize(new Dimension(1200, 800));
            
            // Đóng gói frame để vừa với nội dung
            mainFrame.pack();
            
            // Đặt frame ở giữa màn hình
            mainFrame.setLocationRelativeTo(null);
            
            // Hiển thị frame
            mainFrame.setVisible(true);
        });
    }
}
