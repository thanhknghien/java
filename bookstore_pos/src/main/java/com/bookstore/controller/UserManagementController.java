package com.bookstore.controller;

import com.bookstore.BUS.UserManagementBUS;
import com.bookstore.gui.panel.UserManagementPanel;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManagementController {
    private UserManagementPanel panel;
    private UserManagementBUS bus;
    
    public UserManagementController(UserManagementPanel panel) throws SQLException {
        this.panel = panel;
        this.bus = new UserManagementBUS();
        updateUIBasedOnPermissions();
    }
    
    /**
     * Cập nhật giao diện dựa trên quyền của người dùng hiện tại
     */
    public void updateUIBasedOnPermissions() {
    try {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            panel.updateButtonsVisibility(false, false, false, false);
            return;
        }
        ArrayList<String> permissions = bus.getAllPermissions(currentUser.getId());
        boolean canAdd = permissions.contains("add");
        boolean canEdit = permissions.contains("edit");
        boolean canDelete = permissions.contains("delete");
        boolean canView = permissions.contains("view");
        panel.updateButtonsVisibility(canAdd, canEdit, canDelete, canView);
    } catch (SQLException e) {
        handleError("Lỗi khi kiểm tra quyền", e);
    }
}
    
    public void loadRoleData(JComboBox<String> comboBox) {
        try {
            bus.loadRoleData(comboBox);
        } catch (SQLException e) {
            handleError("Lỗi khi tải danh sách vai trò", e);
        }
    }
    
    public void loadUserData() {
        try {
            List<User> users = bus.getAllUsers();
            panel.updateTableWithUsers(users);
        } catch (SQLException e) {
            handleError("Lỗi khi tải dữ liệu người dùng", e);
        }
    }
    
    public void handleAdd(String username, String password, Integer roleId, boolean status) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                "Vui lòng nhập đầy đủ thông tin",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRoleId(roleId);
            newUser.setStatus(status);
            
            bus.addUser(newUser);
    loadUserData();
    JOptionPane.showMessageDialog(panel, "Thêm người dùng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    panel.clearForm();
} catch (IllegalArgumentException e) {
    JOptionPane.showMessageDialog(panel, e.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
} catch (SQLException e) {
    handleError("Lỗi khi thêm người dùng", e);
}
    }
    
    public void handleEdit(int userId) {
        try {
            User user = bus.getUserById(userId);
            if (user != null) {
                panel.setUserFormData(user);
                panel.setCurrentUser(user);
                panel.setEditing(true);
            }
        } catch (SQLException e) {
            handleError("Lỗi khi tải thông tin người dùng", e);
        }
    }
    
    public void handleUpdate(int userId, String username, String password, Integer roleId, boolean status) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                "Vui lòng nhập đầy đủ thông tin",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            user.setPassword(password);
            user.setRoleId(roleId);
            user.setStatus(status);
            
            bus.updateUser(user);
            loadUserData();
            
            JOptionPane.showMessageDialog(panel,
                "Cập nhật người dùng thành công",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            
            panel.clearForm();
        } catch (SQLException e) {
            handleError("Lỗi khi cập nhật người dùng", e);
        }
    }
    
    public void handleDelete(int userId) {
        try {
            bus.deleteUser(userId);
            loadUserData();
            
            JOptionPane.showMessageDialog(panel,
                "Xóa người dùng thành công",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            
            panel.clearForm();
        } catch (SQLException e) {
            handleError("Lỗi khi xóa người dùng", e);
        }
    }
    
    public void handleSearch(String keyword) {
        try {
            List<User> users = bus.searchUsers(keyword);
            panel.updateTableWithUsers(users);
        } catch (SQLException e) {
            handleError("Lỗi khi tìm kiếm người dùng", e);
        }
    }
    
    public void handleFilterRole(Object selectedItem) {
        try {
            List<User> users = bus.filterUsersByRole(selectedItem);
            panel.updateTableWithUsers(users);
        } catch (SQLException e) {
            handleError("Lỗi khi lọc người dùng theo vai trò", e);
        }
    }
    
    public void handleFilterStatus(Object selectedItem) {
        try {
            List<User> users = bus.filterUsersByStatus(selectedItem);
            panel.updateTableWithUsers(users);
        } catch (SQLException e) {
            handleError("Lỗi khi lọc người dùng theo trạng thái", e);
        }
    }
    
    private void handleError(String title, Exception e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Đã xảy ra lỗi không xác định";
        }
        JOptionPane.showMessageDialog(panel, 
            title + ": " + message, 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
    public String getRoleName(Integer roleId) throws SQLException {
        return roleId != null ? bus.getRoleName(roleId) : null;
    }   
}