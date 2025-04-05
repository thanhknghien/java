package com.bookstore.BUS;

import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.gui.panel.UserManagementPanel;
import com.bookstore.model.Role;
import com.bookstore.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class UserManagementBUS {
    private UserManagementPanel panel;
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    
    public UserManagementBUS(UserManagementPanel panel) {
        this.panel = panel;
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
    }
    
    public void handleAdd() {
        panel.clearForm();
    }
    
    public void handleEdit(int userId) {
        try {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                panel.setUserFormData(user);
                panel.setCurrentUser(user);
                panel.setEditing(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải thông tin người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleDelete(int userId) {
        try {
            userDAO.deleteUser(userId);
            panel.clearForm();
            panel.loadUserData();
            JOptionPane.showMessageDialog(null, 
                "Đã ngưng hoạt động người dùng thành công", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi ngưng hoạt động người dùng: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleSave(String username, String password, Integer roleId, boolean status) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (panel.isEditing() && panel.getCurrentUser() != null) {
                // Update existing user
                User currentUser = panel.getCurrentUser();
                currentUser.setUsername(username);
                currentUser.setPassword(password);
                currentUser.setRoleId(roleId);
                currentUser.setStatus(status);
                userDAO.updateUser(currentUser);
                JOptionPane.showMessageDialog(null, "Cập nhật người dùng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Add new user
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setRoleId(roleId);
                newUser.setStatus(status);
                userDAO.addUser(newUser);
                JOptionPane.showMessageDialog(null, "Thêm người dùng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Refresh data and clear form
            panel.loadUserData();
            panel.clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleClear() {
        panel.clearForm();
        panel.setCurrentUser(null);
        panel.setEditing(false);
    }
    
    public void handleSearch(String keyword) {
        try {
            List<User> users = userDAO.searchUsers(keyword);
            panel.loadUserData();
            panel.clearForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void handleFilterRole(int roleId) {
        try {
            List<User> allUsers = userDAO.getAllUsers();
            panel.loadUserData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lọc người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
} 