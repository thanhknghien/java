package com.bookstore.test;

import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.model.Role;
import com.bookstore.model.User;

import java.sql.SQLException;
import java.util.List;

public class DAOTest {
    public static void main(String[] args) {
        try {
            // Test RoleDAO
            System.out.println("=== Testing RoleDAO ===");
            testRoleDAO();
            
            // Test UserDAO
            System.out.println("\n=== Testing UserDAO ===");
            testUserDAO();
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testRoleDAO() throws SQLException {
        RoleDAO roleDAO = new RoleDAO();
        
        // Test getAllRoles
        System.out.println("Getting all roles:");
        List<Role> roles = roleDAO.getAllRoles();
        for (Role role : roles) {
            System.out.println("Role ID: " + role.getId() + ", Name: " + role.getName());
        }
        
        // Test addRole
        System.out.println("\nAdding a new role:");
        Role newRole = new Role();
        newRole.setName("Test Role");
        roleDAO.addRole(newRole);
        System.out.println("New role added: " + newRole.getName());
        
        // Test searchRoles
        System.out.println("\nSearching for roles with 'Test':");
        List<Role> searchResults = roleDAO.searchRoles("Test");
        for (Role role : searchResults) {
            System.out.println("Found role: " + role.getName());
        }
        
        // Test getRoleById
        if (!searchResults.isEmpty()) {
            int roleId = searchResults.get(0).getId();
            System.out.println("\nGetting role by ID: " + roleId);
            Role foundRole = roleDAO.getRoleById(roleId);
            if (foundRole != null) {
                System.out.println("Found role: " + foundRole.getName());
                
                // Test updateRole
                System.out.println("\nUpdating role:");
                foundRole.setName("Updated Test Role");
                roleDAO.updateRole(foundRole);
                System.out.println("Role updated to: " + foundRole.getName());
                
                // Test deleteRole
                System.out.println("\nDeleting role:");
                roleDAO.deleteRole(roleId);
                System.out.println("Role deleted with ID: " + roleId);
            }
        }
    }
    
    private static void testUserDAO() throws SQLException {
        UserDAO userDAO = new UserDAO();
        
        // Test getAllUsers
        System.out.println("Getting all users:");
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            System.out.println("User ID: " + user.getId() + ", Username: " + user.getUsername() + 
                              ", Role ID: " + user.getRoleId() + ", Status: " + user.isStatus());
        }
        
        // Test addUser
        System.out.println("\nAdding a new user:");
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password123");
        newUser.setRoleId(1); // Assuming role ID 1 exists
        newUser.setStatus(true);
        userDAO.addUser(newUser);
        System.out.println("New user added: " + newUser.getUsername());
        
        // Test searchUsers
        System.out.println("\nSearching for users with 'test':");
        List<User> searchResults = userDAO.searchUsers("test");
        for (User user : searchResults) {
            System.out.println("Found user: " + user.getUsername());
        }
        
        // Test getUserById
        if (!searchResults.isEmpty()) {
            int userId = searchResults.get(0).getId();
            System.out.println("\nGetting user by ID: " + userId);
            User foundUser = userDAO.getUserById(userId);
            if (foundUser != null) {
                System.out.println("Found user: " + foundUser.getUsername());
                
                // Test updateUser
                System.out.println("\nUpdating user:");
                foundUser.setUsername("updated_testuser");
                userDAO.updateUser(foundUser);
                System.out.println("User updated to: " + foundUser.getUsername());
                
                // Test deleteUser
                System.out.println("\nDeleting user:");
                userDAO.deleteUser(userId);
                System.out.println("User deleted with ID: " + userId);
            }
        }
        
        // Test login
        System.out.println("\nTesting login:");
        User loggedInUser = userDAO.login("admin", "admin123"); // Replace with valid credentials
        if (loggedInUser != null) {
            System.out.println("Login successful for user: " + loggedInUser.getUsername());
        } else {
            System.out.println("Login failed");
        }
        
        // Close connection
        userDAO.closeConnection();
    }
} 