package com.bookstore.controller;

import com.bookstore.BUS.LoginBUS;
import com.bookstore.gui.panel.UserManagementPanel;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;
import com.bookstore.gui.main.MainFrame;

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
                MainFrame mainFrame = new MainFrame(SessionManager.getInstance().getCurrentUser());

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
