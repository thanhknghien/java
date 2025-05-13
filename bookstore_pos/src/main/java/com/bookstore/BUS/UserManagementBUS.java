package com.bookstore.BUS;

import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.dao.UserManagementDAO;
import com.bookstore.dao.ProductManagementDAO;
import com.bookstore.dao.OrderManagementDAO;
import com.bookstore.dao.StatisticsManagementDAO;
import com.bookstore.dao.PermissionManagementDAO;
import com.bookstore.dao.RolePermissionConfigDAO;
import com.bookstore.dao.CategoryManagementDAO;
import com.bookstore.dao.CustomerManagementDAO;
import com.bookstore.model.OrderManagement;
import com.bookstore.model.ProductManagement;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.model.UserManagement;
import com.bookstore.model.StatisticsManagement;
import com.bookstore.model.CategoryManagement;
import com.bookstore.model.CustomerManagement;
import com.bookstore.util.SessionManager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserManagementBUS {
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private UserManagementDAO userManagementDAO;
    private ProductManagementDAO productManagementDAO;
    private OrderManagementDAO orderManagementDAO;
    private StatisticsManagementDAO statisticsManagementDAO;
    private PermissionManagementDAO permissionManagementDAO;
    private RolePermissionConfigDAO rolePermissionConfigDAO;
    private CategoryManagementDAO categoryManagementDAO;
    private CustomerManagementDAO customerManagementDAO;
    
    public UserManagementBUS() throws SQLException {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
        this.userManagementDAO = new UserManagementDAO();
        this.productManagementDAO = new ProductManagementDAO();
        this.orderManagementDAO = new OrderManagementDAO();
        this.statisticsManagementDAO = new StatisticsManagementDAO();
        this.permissionManagementDAO = new PermissionManagementDAO();
        this.rolePermissionConfigDAO = new RolePermissionConfigDAO();
        this.categoryManagementDAO = new CategoryManagementDAO();
        this.customerManagementDAO = new CustomerManagementDAO();
    }
    
    public void loadRoleData(JComboBox<String> comboBox) throws SQLException {
        List<Role> roles = roleDAO.getAllRoles();
        comboBox.removeAllItems();
        comboBox.addItem("Tất cả");
        for (Role role : roles) {
            comboBox.addItem(role.getId() + ": " + role.getName());
        }
    }
    
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
    
    public User getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }
    
    public List<User> searchUsers(String keyword) throws SQLException {
        return userDAO.searchUsers(keyword);
    }
    
    public List<User> filterUsersByRole(Object selectedItem) throws SQLException {
        if ("Tất cả".equals(selectedItem)) {
            return getAllUsers();
        }
        
        String roleIdStr = selectedItem.toString().split(":")[0];
        int roleId = Integer.parseInt(roleIdStr);
        
        return getAllUsers().stream()
            .filter(user -> user.getRoleId() != null && user.getRoleId() == roleId)
            .collect(Collectors.toList());
    }
    
    public List<User> filterUsersByStatus(Object selectedItem) throws SQLException {
        if ("Tất cả".equals(selectedItem)) {
            return getAllUsers();
        }
        
        boolean status = "Hoạt động".equals(selectedItem);
        return getAllUsers().stream()
            .filter(user -> user.isStatus() == status)
            .collect(Collectors.toList());
    }
    
    public void addUser(User user) throws SQLException {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập và mật khẩu không được để trống");
        }
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        
        userDAO.addUser(user);
        User newUser = userDAO.getUserByUsername(user.getUsername());
        if (newUser != null) {
            setupUserPermissions(newUser.getId(), newUser.getRoleId());
        }
    }

    private void setupUserPermissions(int userId, Integer roleId) throws SQLException {
        // Không thiết lập quyền nếu userId không tồn tại
        if (userDAO.getUserById(userId) == null) {
            throw new SQLException("Người dùng với ID " + userId + " không tồn tại");
        }
        // Không thiết lập quyền nếu roleId không hợp lệ
        if (roleId != null && roleDAO.getRoleById(roleId) == null) {
            throw new SQLException("Vai trò với ID " + roleId + " không tồn tại");
        }

        // Xóa toàn bộ quyền cũ của người dùng
        userManagementDAO.delete(userId);
        productManagementDAO.delete(userId);
        orderManagementDAO.delete(userId);
        statisticsManagementDAO.delete(userId);
        permissionManagementDAO.delete(userId);
        categoryManagementDAO.delete(userId);
        customerManagementDAO.delete(userId);

        // Thiết lập quyền mới
        Map<String, Map<String, Boolean>> permissions = roleId != null ? 
            rolePermissionConfigDAO.getPermissionsForRole(roleId) : new HashMap<>();
        
        setupUserManagementPermissions(userId, permissions.getOrDefault("user_management", new HashMap<>()));
        setupProductManagementPermissions(userId, permissions.getOrDefault("product_management", new HashMap<>()));
        setupOrderManagementPermissions(userId, permissions.getOrDefault("order_management", new HashMap<>()));
        setupStatisticsManagementPermissions(userId, permissions.getOrDefault("statistics_management", new HashMap<>()));
        setupPermissionManagementPermissions(userId, permissions.getOrDefault("permission_management", new HashMap<>()));
        setupCategoryManagementPermissions(userId, permissions.getOrDefault("category_management", new HashMap<>()));
        setupCustomerManagementPermissions(userId, permissions.getOrDefault("customer_management", new HashMap<>()));
    }

    private void setupUserManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        UserManagement userManagement = new UserManagement(
            userId,
            perms.getOrDefault("add", false),
            perms.getOrDefault("edit", false),
            perms.getOrDefault("delete", false),
            perms.getOrDefault("view", true) // Default: all roles can view
        );
        userManagementDAO.add(userManagement);
    }

    private void setupProductManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        ProductManagement productManagement = new ProductManagement(
            userId,
            perms.getOrDefault("add", false),
            perms.getOrDefault("edit", false),
            perms.getOrDefault("delete", false),
            perms.getOrDefault("view", true) // Default: all roles can view
        );
        productManagementDAO.add(productManagement);
    }

    private void setupOrderManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        OrderManagement orderManagement = new OrderManagement(
            userId,
            perms.getOrDefault("view", true) // Default: all roles can view
        );
        orderManagementDAO.add(orderManagement);
    }
    
    private void setupStatisticsManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        StatisticsManagement statisticsManagement = new StatisticsManagement(
            userId,
            perms.getOrDefault("view", false)
        );
        statisticsManagementDAO.add(statisticsManagement);
    }
    
    private void setupPermissionManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        boolean canManagePermissions = perms.getOrDefault("manage_permissions", false);
        permissionManagementDAO.upsert(userId, canManagePermissions);
    }

    private void setupCategoryManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        CategoryManagement categoryManagement = new CategoryManagement(
            userId,
            perms.getOrDefault("add", false),
            perms.getOrDefault("edit", false),
            perms.getOrDefault("delete", false),
            perms.getOrDefault("view", true) // Default: all roles can view
        );
        categoryManagementDAO.add(categoryManagement);
    }

    private void setupCustomerManagementPermissions(int userId, Map<String, Boolean> perms) throws SQLException {
        CustomerManagement customerManagement = new CustomerManagement(
            userId,
            perms.getOrDefault("add", false),
            perms.getOrDefault("edit", false),
            perms.getOrDefault("delete", false),
            perms.getOrDefault("view", true) // Default: all roles can view
        );
        customerManagementDAO.add(customerManagement);
    }
    
    public ArrayList<String> getAllPermissions(int userId) throws SQLException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        SessionManager sessionManager = SessionManager.getInstance();
        User currentUser = sessionManager.getCurrentUser();
        sessionManager.setCurrentUser(user);
        
        try {
            ArrayList<ArrayList<String>> allPermissions = sessionManager.getAllPermissions();
            return allPermissions.get(0); // user_management permissions
        } finally {
            sessionManager.setCurrentUser(currentUser);
        }
    }
    
    public void updateUser(User user) throws SQLException {
        // Kiểm tra người dùng tồn tại
        if (userDAO.getUserById(user.getId()) == null) {
            throw new SQLException("Người dùng với ID " + user.getId() + " không tồn tại");
        }
        
        // Kiểm tra username và password
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        // Kiểm tra username không trùng
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            throw new SQLException("Tên đăng nhập đã tồn tại");
        }

        userDAO.updateUser(user);
        setupUserPermissions(user.getId(), user.getRoleId());
    }
    
    public void deleteUser(int userId) throws SQLException {
        userDAO.deleteUser(userId);
    }
    
    public String getRoleName(Integer roleId) throws SQLException {
        if (roleId == null) {
            return null;
        }
        Role role = roleDAO.getRoleById(roleId);
        return role != null ? role.getName() : null;
    }
    
    
}