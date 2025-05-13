package com.bookstore.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.bookstore.gui.main.LoginGUI;
import com.bookstore.gui.main.MainFrame;
import com.bookstore.util.SessionManager;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.panel.*;
import java.awt.event.ActionListener;


public class MainFrameController {
    private ArrayList<ArrayList<String>> permissions;


    public MainFrameController() {
    }

    public void showButton(MainFrame mainFrame) {
        SessionManager sessionManager = SessionManager.getInstance();
    
        try {
            ArrayList<ArrayList<String>> permissions = sessionManager.getAllPermissions();
    
            String[] modules = {
                "User Management",
                "Product Management",
                "Order Management",
                "Statistics Management",
                "Category Management",
                "Customer Management",
                "Permission Management"
            };
    
            // In log kiểm tra quyền
            for (int i = 0; i < permissions.size(); i++) {
                System.out.println("[" + modules[i] + "]: " + permissions.get(i));
            }
    
            // Hiện button tương ứng nếu có quyền (dựa trên quyền 'view' hoặc 'manage_permissions')
            if (permissions.get(0).contains("view")) {
                mainFrame.addUserButton();
            }
    
            if (permissions.get(1).contains("view")) {
                mainFrame.addProductButton();
            }
    
            if (permissions.get(2).contains("view")) {
                mainFrame.addPosGUI();
            }
    
            if (permissions.get(3).contains("view")) {
                mainFrame.addStatisticalButton();
            }
    
            if (permissions.get(4).contains("view")) {
                mainFrame.addCategoryButton();
            }
    
            if (permissions.get(5).contains("view")) {
                mainFrame.addCustomerButton();
            }
    
            if (permissions.get(6).contains("manage_permissions")) {
                System.out.println("Người dùng có quyền manage_permissions, hiển thị nút Quản lý quyền");
                mainFrame.addPermissonButton();
            } else {
                System.out.println("Người dùng KHÔNG có quyền manage_permissions");
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy quyền người dùng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ActionListener addActionListener(JPanel panel, JPanel centerPanel) {
        return e -> {
            centerPanel.removeAll();
            centerPanel.add(panel);
            centerPanel.revalidate();
            centerPanel.repaint();
        };
    }

    public ActionListener addActionListener(JFrame frame, MainFrame mainFrame) {
        return e -> {
            frame.setVisible(true);
            mainFrame.setVisible(false);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainFrame.setVisible(true);
                }
    
                @Override
                public void windowClosing(WindowEvent e) {
                    mainFrame.dispose(); // cần thiết để gọi windowClosed
                }
            });
        };
    }

    public ActionListener logoutActionListener(MainFrame frame) {
    return e -> {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            JFrame loginFrame = new LoginGUI();
            loginFrame.setVisible(true);
            frame.dispose();
        }
    };
}


}
