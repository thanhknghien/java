package com.bookstore.util;

import com.bookstore.model.User;
import com.bookstore.dao.*;
import com.bookstore.model.OrderManagement;
import com.bookstore.model.ProductManagement;
import com.bookstore.model.StatisticsManagement;
import com.bookstore.model.UserManagement;
import com.bookstore.model.CategoryManagement;
import com.bookstore.model.CustomerManagement;

import java.sql.SQLException;
import java.util.ArrayList;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private PermissionManagementDAO permissionManagementDAO;
    private UserManagementDAO userManagementDAO;
    private OrderManagementDAO orderManagementDAO;
    private ProductManagementDAO productManagementDAO;
    private StatisticsManagementDAO statisticsManagementDAO;
    private CategoryManagementDAO categoryManagementDAO;
    private CustomerManagementDAO customerManagementDAO;

    private SessionManager() {
        try {
            permissionManagementDAO = new PermissionManagementDAO();
            userManagementDAO = new UserManagementDAO();
            orderManagementDAO = new OrderManagementDAO();
            productManagementDAO = new ProductManagementDAO();
            statisticsManagementDAO = new StatisticsManagementDAO();
            categoryManagementDAO = new CategoryManagementDAO();
            customerManagementDAO = new CustomerManagementDAO();
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo DAO: " + e.getMessage());
        }
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean hasPermission(String permission) {
        if (currentUser == null) {
            return false;
        }
        if (permission.equals("manage_permissions")) {
            try {
                return permissionManagementDAO.getCanManagePermissions(currentUser.getId());
            } catch (SQLException e) {
                System.err.println("Lỗi kiểm tra quyền: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public boolean hasPermission(String module, String action) {
        if (currentUser == null) {
            return false;
        }
        try {
            switch (module) {
                case "user_management":
                    UserManagement um = userManagementDAO.getById(currentUser.getId());
                    if (um != null) {
                        switch (action) {
                            case "add": return um.isCanAdd();
                            case "edit": return um.isCanEdit();
                            case "delete": return um.isCanDelete();
                            case "view": return um.isCanView();
                        }
                    }
                    break;
                case "order_management":
                    OrderManagement om = orderManagementDAO.getById(currentUser.getId());
                    if (om != null) {
                        switch (action) {
                            case "view": return om.isCanView();
                        }
                    }
                    break;
                case "product_management":
                ProductManagement pm = productManagementDAO.getById(currentUser.getId());
                if (pm != null) {
                    // In ra thông tin quyền để kiểm tra
                    System.out.println("Permissions for Product Management: ");
                    System.out.println("Can add: " + pm.isCanAdd());
                    System.out.println("Can edit: " + pm.isCanEdit());
                    System.out.println("Can delete: " + pm.isCanDelete());
                    System.out.println("Can view: " + pm.isCanView());
                    
                    // Kiểm tra quyền 'add'
                    if (action.equals("add")) {
                        return pm.isCanAdd(); // Nếu có quyền add, trả về true
                    }
                    // Các quyền khác
                }
                break;
                case "statistics_management":
                    StatisticsManagement sm = statisticsManagementDAO.getById(currentUser.getId());
                    if (sm != null) {
                        switch (action) {
                            case "view": return sm.isCanView();
                        }
                    }
                    break;
                case "category_management":
                    CategoryManagement cm = categoryManagementDAO.getById(currentUser.getId());
                    if (cm != null) {
                        switch (action) {
                            case "add": return cm.isCanAdd();
                            case "edit": return cm.isCanEdit();
                            case "delete": return cm.isCanDelete();
                            case "view": return cm.isCanView();
                        }
                    }
                    break;
                case "customer_management":
                    CustomerManagement custm = customerManagementDAO.getById(currentUser.getId());
                    if (custm != null) {
                        switch (action) {
                            case "add": return custm.isCanAdd();
                            case "edit": return custm.isCanEdit();
                            case "delete": return custm.isCanDelete();
                            case "view": return custm.isCanView();
                        }
                    }
                    break;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra quyền: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<ArrayList<String>> getAllPermissions() throws SQLException {
        ArrayList<ArrayList<String>> permissions = new ArrayList<>();
        // Initialize with 7 empty ArrayLists for each module (6 modules + permission_management)
        for (int i = 0; i < 7; i++) {
            permissions.add(new ArrayList<>());
        }

        if (currentUser == null) {
            return permissions;
        }

        int userId = currentUser.getId();

        // User Management (index 0)
        ArrayList<String> umPermissions = permissions.get(0);
        UserManagement um = userManagementDAO.getById(userId);
        if (um != null) {
            if (um.isCanAdd()) umPermissions.add("add");
            if (um.isCanEdit()) umPermissions.add("edit");
            if (um.isCanDelete()) umPermissions.add("delete");
            if (um.isCanView()) umPermissions.add("view");
        }

        // Product Management (index 1)
        ArrayList<String> pmPermissions = permissions.get(1);
        ProductManagement pm = productManagementDAO.getById(userId);
        if (pm != null) {
            if (pm.isCanAdd()) pmPermissions.add("add");
            if (pm.isCanEdit()) pmPermissions.add("edit");
            if (pm.isCanDelete()) pmPermissions.add("delete");
            if (pm.isCanView()) pmPermissions.add("view");
        }

        // Order Management (index 2)
        ArrayList<String> omPermissions = permissions.get(2);
        OrderManagement om = orderManagementDAO.getById(userId);
        if (om != null) {
            if (om.isCanView()) omPermissions.add("view");
        }

        // Statistics Management (index 3)
        ArrayList<String> smPermissions = permissions.get(3);
        StatisticsManagement sm = statisticsManagementDAO.getById(userId);
        if (sm != null && sm.isCanView()) {
            smPermissions.add("view");
        }

        // Category Management (index 4)
        ArrayList<String> cmPermissions = permissions.get(4);
        CategoryManagement cm = categoryManagementDAO.getById(userId);
        if (cm != null) {
            if (cm.isCanAdd()) cmPermissions.add("add");
            if (cm.isCanEdit()) cmPermissions.add("edit");
            if (cm.isCanDelete()) cmPermissions.add("delete");
            if (cm.isCanView()) cmPermissions.add("view");
        }

        // Customer Management (index 5)
        ArrayList<String> custmPermissions = permissions.get(5);
        CustomerManagement custm = customerManagementDAO.getById(userId);
        if (custm != null) {
            if (custm.isCanAdd()) custmPermissions.add("add");
            if (custm.isCanEdit()) custmPermissions.add("edit");
            if (custm.isCanDelete()) custmPermissions.add("delete");
            if (custm.isCanView()) custmPermissions.add("view");
        }

        // Permission Management (index 6)
        ArrayList<String> permPermissions = permissions.get(6);
        boolean canManagePermissions = permissionManagementDAO.getCanManagePermissions(userId);
        if (canManagePermissions) {
            permPermissions.add("manage_permissions");
        }

        return permissions;
    }

    
}