package com.bookstore.controller;

import com.bookstore.BUS.PermissionService;
import com.bookstore.gui.panel.PermissionPanel;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionController {
    private PermissionPanel panel;
    private PermissionService service;
    private SessionManager sessionManager;
    
    public PermissionController(PermissionPanel panel, PermissionService service) {
        this.panel = panel;
        this.service = service;
        this.sessionManager = SessionManager.getInstance();
        initializeListeners();
        loadInitialData();
    }

    private void initializeListeners() {
        panel.getSearchButton().addActionListener(e -> searchUsers());
        panel.getFilterButton().addActionListener(e -> filterUsersByRole());
        panel.getUserTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadPermissionsForSelectedUser();
            }
        });
        panel.getModuleCombo().addActionListener(e -> loadPermissionsForSelectedUser());
        panel.getSaveButton().addActionListener(e -> savePermissions());
    }

    private void loadInitialData() {
        try {
            List<Role> roles = service.getAllRoles();
            panel.updateRoles(roles);
            List<User> users = service.getAllUsers();
            Map<Integer, Role> roleMap = new HashMap<>();
            for (Role role : roles) {
                roleMap.put(role.getId(), role);
            }
            panel.updateUsers(users, roleMap);
        } catch (SQLException e) {
            panel.showMessage("Lỗi tải dữ liệu ban đầu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchUsers() {
        String query = panel.getSearchField().getText().trim();
        try {
            List<User> users = service.searchUsers(query);
            Map<Integer, Role> roleMap = new HashMap<>();
            for (Role role : service.getAllRoles()) {
                roleMap.put(role.getId(), role);
            }
            panel.updateUsers(users, roleMap);
        } catch (SQLException e) {
            panel.showMessage("Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterUsersByRole() {
        String selectedRole = (String) panel.getRoleFilterCombo().getSelectedItem();
        try {
            List<User> users;
            Map<Integer, Role> roleMap = new HashMap<>();
            for (Role role : service.getAllRoles()) {
                roleMap.put(role.getId(), role);
            }
            if (selectedRole == null || selectedRole.equals("Tất cả")) {
                users = service.getAllUsers();
            } else {
                int roleId = Integer.parseInt(selectedRole.substring(selectedRole.indexOf("(") + 1, selectedRole.indexOf(")")));
                users = service.getUsersByRoleId(roleId);
            }
            panel.updateUsers(users, roleMap);
        } catch (SQLException e) {
            panel.showMessage("Lỗi lọc người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPermissionsForSelectedUser() {
        int selectedRow = panel.getUserTable().getSelectedRow();
        if (selectedRow == -1) {
            panel.updatePermissions(new HashMap<>(), new HashMap<>());
            return;
        }
        int userId = (int) panel.getUserTableModel().getValueAt(selectedRow, 0);
        String module = (String) panel.getModuleCombo().getSelectedItem();
        Map<String, String> descriptions = getPermissionDescriptions(module);
        try {
            Map<String, Boolean> permissions = service.getPermissions(userId, module);
            panel.updatePermissions(permissions, descriptions);
        } catch (SQLException e) {
            panel.showMessage("Lỗi tải quyền: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePermissions() {
        if (!sessionManager.hasPermission("manage_permissions")) {
            panel.showMessage("Bạn không có quyền quản lý phân quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = panel.getUserTable().getSelectedRow();
        if (selectedRow == -1) {
            panel.showMessage("Vui lòng chọn một người dùng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userId = (int) panel.getUserTableModel().getValueAt(selectedRow, 0);
        String module = (String) panel.getModuleCombo().getSelectedItem();

        Map<String, Boolean> updatedPermissions = new HashMap<>();
        for (int i = 0; i < panel.getPermissionTableModel().getRowCount(); i++) {
            String action = (String) panel.getPermissionTableModel().getValueAt(i, 0);
            boolean allowed = (boolean) panel.getPermissionTableModel().getValueAt(i, 2);
            updatedPermissions.put(action, allowed);
        }

        try {
            boolean updated = service.updatePermissions(userId, module, updatedPermissions);
            if (updated) {
                panel.showMessage("Cập nhật quyền thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                panel.showMessage("Cập nhật quyền thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            panel.showMessage("Lỗi cập nhật quyền: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Map<String, String> getPermissionDescriptions(String module) {
        Map<String, String> descriptions = new HashMap<>();
        switch (module) {
            case "user_management":
                descriptions.put("add", "Thêm người dùng mới");
                descriptions.put("edit", "Chỉnh sửa thông tin người dùng");
                descriptions.put("delete", "Xóa người dùng");
                descriptions.put("view", "Xem danh sách người dùng");
                break;
            case "order_management":
                descriptions.put("view", "Xem danh sách đơn hàng");
                break;
            case "product_management":
                descriptions.put("add", "Thêm sản phẩm mới");
                descriptions.put("edit", "Chỉnh sửa sản phẩm");
                descriptions.put("delete", "Xóa sản phẩm");
                descriptions.put("view", "Xem danh sách sản phẩm");
                break;
            case "permission_management":
                descriptions.put("manage_permissions", "Quản lý phân quyền của người dùng");
                break;
            case "statistics_management":
                descriptions.put("view", "Xem thống kê");
                break;
            case "category_management":
                descriptions.put("add", "Thêm danh mục mới");
                descriptions.put("edit", "Chỉnh sửa danh mục");
                descriptions.put("delete", "Xóa danh mục");
                descriptions.put("view", "Xem danh sách danh mục");
                break;
            case "customer_management":
                descriptions.put("add", "Thêm khách hàng mới");
                descriptions.put("edit", "Chỉnh sửa thông tin khách hàng");
                descriptions.put("delete", "Xóa khách hàng");
                descriptions.put("view", "Xem danh sách khách hàng");
                break;
        }
        return descriptions;
    }
}