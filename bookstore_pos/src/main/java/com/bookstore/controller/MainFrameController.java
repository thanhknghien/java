package com.bookstore.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.gui.main.MainFrame;
import com.bookstore.util.SessionManager;

public class MainFrameController {
    private ArrayList<ArrayList<String>> permissions;


    public MainFrameController() {
    }

    public void showButton(MainFrame mainFrame) {
        try {
            permissions = new ArrayList<>();
            SessionManager sessionManager = SessionManager.getInstance();
            permissions = sessionManager .getAllPermissions();
            if (permissions.isEmpty()) {
                System.out.println("[]");
            } else {
                System.out.println("[" + String.join(", ", permissions.get(0)) + "]");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy quyền: " + e.getMessage());
        }

        if (permissions.get(0).contains("user_management")) {
            mainFrame.addUserButton();
        }

        if (permissions.get(2).contains("order_management")) {
            mainFrame.addPosGUI();
        }

        if (permissions.get(3).contains("product_management")) {
            mainFrame.addProductButton();
        }

        if (permissions.get(4).contains("statistics_management")) {
            mainFrame.addStatisticalButton();
        }

    }

}
