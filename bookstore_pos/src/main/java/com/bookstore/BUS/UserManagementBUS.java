package com.bookstore.BUS;

import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.dao.UserManagementDAO;
import com.bookstore.dao.InvoiceManagementDAO;
import com.bookstore.dao.ProductManagementDAO;
import com.bookstore.dao.OrderManagementDAO;
import com.bookstore.model.InvoiceManagement;
import com.bookstore.model.OrderManagement;
import com.bookstore.model.ProductManagement;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.model.UserManagement;
import com.bookstore.util.SessionManager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserManagementBUS {
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private UserManagementDAO userManagementDAO;
    private InvoiceManagementDAO invoiceManagementDAO;
    private ProductManagementDAO productManagementDAO;
    private OrderManagementDAO orderManagementDAO;
    
    public UserManagementBUS() throws SQLException {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
        this.userManagementDAO = new UserManagementDAO();
        this.invoiceManagementDAO = new InvoiceManagementDAO();
        this.productManagementDAO = new ProductManagementDAO();
        this.orderManagementDAO = new OrderManagementDAO();
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
        // Add user first
        userDAO.addUser(user);
        
        // Get the newly added user's ID
        User newUser = userDAO.getUserByUsername(user.getUsername());
        if (newUser != null) {
            // Set up permissions based on role
            setupUserPermissions(newUser.getId(), newUser.getRoleId());
        }
    }

    private void setupUserPermissions(int userId, Integer roleId) throws SQLException {
        if (roleId == null) return;

        // Set up user management permissions
        setupUserManagementPermissions(userId, roleId);
        
        // Set up invoice management permissions
        setupInvoiceManagementPermissions(userId, roleId);
        
        // Set up product management permissions
        setupProductManagementPermissions(userId, roleId);
        
        // Set up order management permissions
        setupOrderManagementPermissions(userId, roleId);
    }

    private void setupUserManagementPermissions(int userId, int roleId) throws SQLException {
        boolean canView = true; // All roles can view
        boolean canAdd = false, canEdit = false, canDelete = false;
        if(roleId>1){
            canAdd  = true; // manage or admin can add
            canEdit = true; // manage or admin can edit
            if(roleId>2){
                canDelete =  true; // Only admin can delete
            }
        }
        

        UserManagement userManagement = new UserManagement(userId, canAdd, canEdit, canDelete, canView);
        userManagementDAO.add(userManagement);
    }

    private void setupInvoiceManagementPermissions(int userId, int roleId) throws SQLException {
        boolean canView = true; // All roles can view
        boolean canAdd = false, canEdit = false, canDelete = false;        
        if(roleId>1){
            canAdd  = true; // manage or admin can add
            canEdit = true; // manage or admin can edit
            if(roleId>2){
                canDelete =  true; // Only admin can delete
            }
        }
        InvoiceManagement invoiceManagement = new InvoiceManagement(userId, canAdd, canEdit, canDelete, canView);
        invoiceManagementDAO.add(invoiceManagement);
    }

    private void setupProductManagementPermissions(int userId, int roleId) throws SQLException {
        boolean canView = true; // All roles can view
        boolean canAdd = false, canEdit = false, canDelete = false;        
        if(roleId>1){
            canAdd  = true; // manage or admin can add
            canEdit = true; // manage or admin can edit
            if(roleId>2){
                canDelete  = true; // Only admin can delete
            }
        }

        ProductManagement productManagement = new ProductManagement(userId, canAdd, canEdit, canDelete, canView);
        productManagementDAO.add(productManagement);
    }

    private void setupOrderManagementPermissions(int userId, int roleId) throws SQLException {
        boolean canView = true; // All roles can view
        boolean canAdd = false, canEdit = false, canDelete = false;        
        if(roleId>1){
            canAdd  = true; // manage or admin can add
            canEdit = true; // manage or admin can edit
            if(roleId>2){
                canDelete =  true; // Only admin can delete
            }
        }

        OrderManagement orderManagement = new OrderManagement(userId, canAdd, canEdit, canDelete, canView);
        orderManagementDAO.add(orderManagement);
    }
    
    /**
     * Kiểm tra người dùng hiện tại có quyền thêm không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canAdd() {
        return SessionManager.getInstance().canAdd();
    }
    
    /**
     * Kiểm tra người dùng hiện tại có quyền chỉnh sửa không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canEdit() {
        return SessionManager.getInstance().canEdit();
    }
    
    /**
     * Kiểm tra người dùng hiện tại có quyền xóa không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canDelete() {
        return SessionManager.getInstance().canDelete();
    }
    
    /**
     * Kiểm tra người dùng hiện tại có quyền xem không
     * @return true nếu có quyền, false nếu không
     */
    public boolean canView() {
        return SessionManager.getInstance().canView();
    }
    
    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
        // Update permissions when role changes
        setupUserPermissions(user.getId(), user.getRoleId());
    }
    
    public void deleteUser(int userId) throws SQLException {
        userDAO.deleteUser(userId);
    }
}