package com.bookstore.controller;

import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.gui.panel.UserManagementPanel;
import com.bookstore.model.Role;
import com.bookstore.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class UserManagementController {
    private UserManagementPanel panel;
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    
    public UserManagementController(UserManagementPanel panel) {
        this.panel = panel;
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
    }
    
    public ActionListener createAddButtonListener(JTextField usernameField, JPasswordField passwordField, JComboBox<String> roleComboBox, JCheckBox statusCheckBox) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm(usernameField, passwordField, roleComboBox, statusCheckBox);
            }
        };
    }
    
    public ActionListener createEditButtonListener(JTable userTable, JTextField usernameField, JPasswordField passwordField, JComboBox<String> roleComboBox, JCheckBox statusCheckBox) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                        User user = userDAO.getUserById(userId);
                        if (user != null) {
                            usernameField.setText(user.getUsername());
                            passwordField.setText(user.getPassword());
                            statusCheckBox.setSelected(user.isStatus());
                            
                            // Set selected role
                            for (int i = 0; i < roleComboBox.getItemCount(); i++) {
                                String item = roleComboBox.getItemAt(i);
                                if (item.startsWith(user.getRoleId() + ":")) {
                                    roleComboBox.setSelectedIndex(i);
                                    break;
                                }
                            }
                            
                            panel.setCurrentUser(user);
                            panel.setEditing(true);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tải thông tin người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một người dùng để sửa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
    }
    
    public ActionListener createDeleteButtonListener(JTable userTable) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa người dùng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                            userDAO.deleteUser(userId);
                            panel.clearForm();
                            panel.loadUserData();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Lỗi khi xóa người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một người dùng để xóa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
    }
    
    public ActionListener createSaveButtonListener(JTextField usernameField, JPasswordField passwordField, JComboBox<String> roleComboBox, JCheckBox statusCheckBox) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUser(usernameField, passwordField, roleComboBox, statusCheckBox);
            }
        };
    }
    
    public ActionListener createClearButtonListener(JTextField usernameField, JPasswordField passwordField, JComboBox<String> roleComboBox, JCheckBox statusCheckBox) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm(usernameField, passwordField, roleComboBox, statusCheckBox);
            }
        };
    }
    
    public ActionListener createSearchButtonListener(JTextField searchField) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText().trim();
                try {
                    List<User> users = userDAO.searchUsers(keyword);
                    panel.updateTableWithUsers(users);
                    panel.clearForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
    public ActionListener createFilterRoleListener(JComboBox<String> filterRoleComboBox) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) filterRoleComboBox.getSelectedItem();
                
                if ("Tất cả".equals(selectedItem)) {
                    panel.loadUserData();
                    return;
                }
                
                // Extract role ID from selected item (format: "ID: Name")
                int roleId = Integer.parseInt(selectedItem.split(":")[0]);
                
                try {
                    List<User> allUsers = userDAO.getAllUsers();
                    panel.updateTableWithUsers(allUsers.stream()
                        .filter(user -> user.getRoleId() != null && user.getRoleId() == roleId)
                        .collect(java.util.stream.Collectors.toList()));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi lọc người dùng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
    private void clearForm(JTextField usernameField, JPasswordField passwordField, JComboBox<String> roleComboBox, JCheckBox statusCheckBox) {
        usernameField.setText("");
        passwordField.setText("");
        statusCheckBox.setSelected(true);
        if (roleComboBox.getItemCount() > 0) {
            roleComboBox.setSelectedIndex(0);
        }
        panel.setCurrentUser(null);
        panel.setEditing(false);
    }
    
    private void saveUser(JTextField usernameField, JPasswordField passwordField, JComboBox<String> roleComboBox, JCheckBox statusCheckBox) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get selected role ID
        Integer roleId = null;
        String selectedRole = (String) roleComboBox.getSelectedItem();
        if (selectedRole != null && !selectedRole.isEmpty()) {
            roleId = Integer.parseInt(selectedRole.split(":")[0]);
        }
        
        try {
            if (panel.isEditing() && panel.getCurrentUser() != null) {
                // Update existing user
                User currentUser = panel.getCurrentUser();
                currentUser.setUsername(username);
                currentUser.setPassword(password);
                currentUser.setRoleId(roleId);
                currentUser.setStatus(statusCheckBox.isSelected());
                userDAO.updateUser(currentUser);
                JOptionPane.showMessageDialog(null, "Cập nhật người dùng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Add new user
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setRoleId(roleId);
                newUser.setStatus(statusCheckBox.isSelected());
                userDAO.addUser(newUser);
                JOptionPane.showMessageDialog(null, "Thêm người dùng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Refresh data and clear form
            panel.loadUserData();
            clearForm(usernameField, passwordField, roleComboBox, statusCheckBox);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}