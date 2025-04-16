package com.bookstore.controller;

import com.bookstore.BUS.LoginBUS;
import com.bookstore.gui.panel.UserManagementPanel;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginController {
    private LoginBUS loginBUS;
    private JPanel loginPanel;

    public LoginController(JPanel loginPanel) {
        this.loginPanel = loginPanel;
        this.loginBUS = new LoginBUS();
    }
    
    /**
     * Mở màn hình chính của ứng dụng sau khi đăng nhập thành công
     */
    private void openMainApplication() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo frame chính
                JFrame mainFrame = new JFrame("Quản Lý Cửa Hàng Sách");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                // Tạo panel quản lý người dùng và controller
                UserManagementPanel userManagementPanel = new UserManagementPanel();
                UserManagementController controller = new UserManagementController(userManagementPanel);
                
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
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Lỗi khởi tạo ứng dụng: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    public ActionListener createLoginActionListener(JTextField usernameField, JPasswordField passwordField) {
        return e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                User user = loginBUS.validateLogin(username, password);
                // Thiết lập người dùng hiện tại vào SessionManager
                SessionManager.getInstance().setCurrentUser(user);
                
                JOptionPane.showMessageDialog(loginPanel,
                    "Đăng nhập thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Đóng cửa sổ đăng nhập
                SwingUtilities.getWindowAncestor(loginPanel).dispose();
                
                // Mở màn hình chính
                openMainApplication();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(loginPanel,
                    ex.getMessage(),
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(loginPanel,
                    "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginPanel,
                    "Lỗi trong quá trình đăng nhập: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        };
    }
}
