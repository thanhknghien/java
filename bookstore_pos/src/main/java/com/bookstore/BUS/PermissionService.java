package com.bookstore.BUS;

import com.bookstore.dao.*;
import com.bookstore.model.*;
import java.sql.SQLException;
import java.util.*;

public class PermissionService {
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private UserManagementDAO userManagementDAO;

    private OrderManagementDAO orderManagementDAO;
    private ProductManagementDAO productManagementDAO;
    private PermissionManagementDAO permissionManagementDAO;
    private StatisticsManagementDAO statisticsManagementDAO;

    public PermissionService() {
        try {
            userDAO = new UserDAO();
            roleDAO = new RoleDAO();
            userManagementDAO = new UserManagementDAO();
            
            orderManagementDAO = new OrderManagementDAO();
            productManagementDAO = new ProductManagementDAO();
            permissionManagementDAO = new PermissionManagementDAO();
            statisticsManagementDAO = new StatisticsManagementDAO();
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo DAO: " + e.getMessage());
        }
    }

    public void closeConnections() {
        userDAO.closeConnection();
        permissionManagementDAO.closeConnection();
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public List<User> searchUsers(String query) throws SQLException {
        return userDAO.searchUsers(query);
    }

    public List<User> getUsersByRoleId(int roleId) throws SQLException {
        List<User> users = userDAO.getAllUsers();
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getRoleId() != null && user.getRoleId() == roleId) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    public List<Role> getAllRoles() throws SQLException {
        return roleDAO.getAllRoles();
    }

    public Role getRoleById(Integer roleId) throws SQLException {
        if (roleId == null) {
            return null;
        }
        return roleDAO.getRoleById(roleId);
    }

    public Map<String, Boolean> getPermissions(int userId, String module) throws SQLException {
        Map<String, Boolean> permissions = new HashMap<>();
        switch (module) {
            case "user_management":
                permissions.put("add", false);
                permissions.put("edit", false);
                permissions.put("delete", false);
                permissions.put("view", false);
                UserManagement userManagement = userManagementDAO.getById(userId);
                if (userManagement != null) {
                    permissions.put("add", userManagement.isCanAdd());
                    permissions.put("edit", userManagement.isCanEdit());
                    permissions.put("delete", userManagement.isCanDelete());
                    permissions.put("view", userManagement.isCanView());
                }
                break;
            case "order_management":
                permissions.put("add", false);
                permissions.put("edit", false);
                permissions.put("delete", false);
                permissions.put("view", false);
                OrderManagement orderManagement = orderManagementDAO.getById(userId);
                if (orderManagement != null) {
                    
                    permissions.put("view", orderManagement.isCanView());
                }
                break;
            case "product_management":
                permissions.put("add", false);
                permissions.put("edit", false);
                permissions.put("delete", false);
                permissions.put("view", false);
                ProductManagement productManagement = productManagementDAO.getById(userId);
                if (productManagement != null) {
                    permissions.put("add", productManagement.isCanAdd());
                    permissions.put("edit", productManagement.isCanEdit());
                    permissions.put("delete", productManagement.isCanDelete());
                    permissions.put("view", productManagement.isCanView());
                }
                break;
            case "permission_management":
                permissions.put("manage_permissions", permissionManagementDAO.getCanManagePermissions(userId));
                break;
            case "statistics_management":
                permissions.put("view", false);
                StatisticsManagement statisticsManagement = statisticsManagementDAO.getById(userId);
                if (statisticsManagement != null) {
                    permissions.put("view", statisticsManagement.isCanView());
                }
                break;
        }
        return permissions;
    }

    public boolean updatePermissions(int userId, String module, Map<String, Boolean> permissions) throws SQLException {
        switch (module) {
            case "user_management":
                UserManagement userManagement = new UserManagement(
                        userId,
                        permissions.getOrDefault("add", false),
                        permissions.getOrDefault("edit", false),
                        permissions.getOrDefault("delete", false),
                        permissions.getOrDefault("view", false)
                );
                if (userManagementDAO.getById(userId) == null) {
                    return userManagementDAO.add(userManagement);
                } else {
                    return userManagementDAO.update(userManagement);
                }
            case "order_management":
                OrderManagement orderManagement = new OrderManagement(
                        userId,
                        permissions.getOrDefault("view", false)
                );
                if (orderManagementDAO.getById(userId) == null) {
                    return orderManagementDAO.add(orderManagement);
                } else {
                    return orderManagementDAO.update(orderManagement);
                }
            case "product_management":
                ProductManagement productManagement = new ProductManagement(
                        userId,
                        permissions.getOrDefault("add", false),
                        permissions.getOrDefault("edit", false),
                        permissions.getOrDefault("delete", false),
                        permissions.getOrDefault("view", false)
                );
                if (productManagementDAO.getById(userId) == null) {
                    return productManagementDAO.add(productManagement);
                } else {
                    return productManagementDAO.update(productManagement);
                }
            case "permission_management":
                if (permissions.containsKey("manage_permissions")) {
                    return permissionManagementDAO.upsert(userId, permissions.get("manage_permissions"));
                }
                return false;
            case "statistics_management":
                StatisticsManagement statisticsManagement = new StatisticsManagement(
                        userId,
                        permissions.getOrDefault("view", false)
                );
                if (statisticsManagementDAO.getById(userId) == null) {
                    return statisticsManagementDAO.add(statisticsManagement);
                } else {
                    return statisticsManagementDAO.update(statisticsManagement);
                }
            default:
                return false;
        }
    }
}