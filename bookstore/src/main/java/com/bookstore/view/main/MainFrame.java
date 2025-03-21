package com.bookstore.view.main;

import javax.swing.*;

import com.bookstore.controller.MainController;
import com.bookstore.model.DTO.PermissionDTO;

import java.awt.*;
import java.security.Permission;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel navBarPanel; // Thanh điều hướng
    private JPanel mainPanel;   // Panel chức năng chính
    private MainController controller;

    public MainFrame(int accountId) throws SQLException {
        // Khởi tạo controller
        controller = new MainController(this, accountId);

        // Thiết lập giao diện chính
        setTitle("BookStore - Giao diện chính");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thiết lập layout chính
        setLayout(new BorderLayout());

        // Tạo thanh điều hướng (nav-bar)
        navBarPanel = createNavBar();
        add(navBarPanel, BorderLayout.WEST);

        // Tạo panel chức năng chính
        mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    // Tạo thanh điều hướng (nav-bar)
    private JPanel createNavBar() throws SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setBackground(new Color(50, 50, 50)); // Màu nền tối cho nav-bar

        // Tiêu đề nav-bar
        JLabel lblTitle = new JLabel("BookStore");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(20));

        // Lấy danh sách quyền từ controller
        ArrayList<String> permissions = controller.getPermissions();

        // Tạo các nút dựa trên quyền
        for (String permission : permissions) {
            JButton button = new JButton(permission);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            button.setBackground(new Color(70, 130, 180)); // Màu nền nút
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.addActionListener(e -> controller.handleNavButtonClick(permission));
            panel.add(button);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    // Tạo panel chức năng chính
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());

        // Thêm label mặc định
        JLabel lblDefault = new JLabel("Chọn một chức năng từ thanh điều hướng", SwingConstants.CENTER);
        lblDefault.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lblDefault, BorderLayout.CENTER);

        return panel;
    }

    // Hiển thị panel tương ứng với chức năng được chọn
    public void showFunctionPanel(String permission) {
        mainPanel.removeAll(); // Xóa nội dung hiện tại của mainPanel

        // Tạo panel mới dựa trên quyền (chỉ là placeholder)
        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new BorderLayout());
        JLabel lblFunction = new JLabel("Chức năng: " + permission, SwingConstants.CENTER);
        lblFunction.setFont(new Font("Arial", Font.BOLD, 20));
        functionPanel.add(lblFunction, BorderLayout.CENTER);

        mainPanel.add(functionPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame(2).setVisible(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
}