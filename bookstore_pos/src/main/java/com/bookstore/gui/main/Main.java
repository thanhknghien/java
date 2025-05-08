/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.gui.main;



import com.bookstore.BUS.PermissionService;
import com.bookstore.BUS.UserManagementBUS;
import com.bookstore.controller.PermissionController;
import javax.swing.*;

import com.bookstore.gui.panel.PermissionPanel;


import com.bookstore.model.User;





import com.bookstore.util.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Lớp khởi động ứng dụng
 * @author HP
 */
public class Main {
    public static void main(String[] args) {
        SessionManager sessionManager = SessionManager.getInstance();
        User adminUser = new User(3, "admin1", "123", 3, true);
        sessionManager.setCurrentUser(adminUser);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý phân quyền");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            PermissionPanel panel = new PermissionPanel();
            PermissionService service = new PermissionService();
            new PermissionController(panel, service);
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        
       /* SessionManager session = SessionManager.getInstance();
        User adminUser = new User(1, "admin1", "123", 3, true);
        session.setCurrentUser(adminUser);
try {
    UserManagementBUS bus = new UserManagementBUS();
    int userId = 1; // admin1
    ArrayList<String> permissions = bus.getAllPermissions(userId);
    System.out.println("Quyền user_management của người dùng " + userId + ":");
    if (permissions.isEmpty()) {
        System.out.println("[]");
    } else {
        System.out.println("[" + String.join(", ", permissions) + "]");
    }
} catch (SQLException e) {
    System.err.println("Lỗi lấy quyền: " + e.getMessage());
}*/
    }
}
