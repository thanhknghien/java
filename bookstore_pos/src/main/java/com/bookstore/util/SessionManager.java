package com.bookstore.util;

import com.bookstore.model.User;
import com.bookstore.dao.*;
import com.bookstore.model.InvoiceManagement;
import com.bookstore.model.OrderManagement;
import com.bookstore.model.ProductManagement;
import com.bookstore.model.StatisticsManagement;
import com.bookstore.model.UserManagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private PermissionManagementDAO permissionManagementDAO;
    private UserManagementDAO userManagementDAO;
    private InvoiceManagementDAO invoiceManagementDAO;
    private OrderManagementDAO orderManagementDAO;
    private ProductManagementDAO productManagementDAO;
    private StatisticsManagementDAO statisticsManagementDAO;

    private SessionManager() {
        try {
            permissionManagementDAO = new PermissionManagementDAO();
            userManagementDAO = new UserManagementDAO();
            invoiceManagementDAO = new InvoiceManagementDAO();
            orderManagementDAO = new OrderManagementDAO();
            productManagementDAO = new ProductManagementDAO();
            statisticsManagementDAO = new StatisticsManagementDAO();
        } catch (SQLException e) {
            
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
                case "invoice_management":
                    InvoiceManagement im = invoiceManagementDAO.getById(currentUser.getId());
                    if (im != null) {
                        switch (action) {
                            case "add": return im.isCanAdd();
                            case "edit": return im.isCanEdit();
                            case "delete": return im.isCanDelete();
                            case "view": return im.isCanView();
                        }
                    }
                    break;
                case "order_management":
                    OrderManagement om = orderManagementDAO.getById(currentUser.getId());
                    if (om != null) {
                        switch (action) {
                            case "add": return om.isCanAdd();
                            case "edit": return om.isCanEdit();
                            case "delete": return om.isCanDelete();
                            case "view": return om.isCanView();
                        }
                    }
                    break;
                case "product_management":
                    ProductManagement pm = productManagementDAO.getById(currentUser.getId());
                    if (pm != null) {
                        switch (action) {
                            case "add": return pm.isCanAdd();
                            case "edit": return pm.isCanEdit();
                            case "delete": return pm.isCanDelete();
                            case "view": return pm.isCanView();
                        }
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
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra quyền: " + e.getMessage());
        }
        return false;
    }

    public String[][] getAllPermissions() throws SQLException {
        // Initialize result with 6 empty arrays for each module
        String[][] permissions = new String[6][];
        permissions[0] = new String[0]; // user_management
        permissions[1] = new String[0]; // invoice_management
        permissions[2] = new String[0]; // product_management
        permissions[3] = new String[0]; // order_management
        permissions[4] = new String[0]; // statistics_management
        permissions[5] = new String[0]; // permission_management

        if (currentUser == null) {
            return permissions;
        }

        int userId = currentUser.getId();

        // User Management
        List<String> umPermissions = new ArrayList<>();
        UserManagement um = userManagementDAO.getById(userId);
        if (um != null) {
            if (um.isCanAdd()) umPermissions.add("add");
            if (um.isCanEdit()) umPermissions.add("edit");
            if (um.isCanDelete()) umPermissions.add("delete");
            if (um.isCanView()) umPermissions.add("view");
        }
        permissions[0] = umPermissions.toArray(new String[0]);

        // Invoice Management
        List<String> imPermissions = new ArrayList<>();
        InvoiceManagement im = invoiceManagementDAO.getById(userId);
        if (im != null) {
            if (im.isCanAdd()) imPermissions.add("add");
            if (im.isCanEdit()) imPermissions.add("edit");
            if (im.isCanDelete()) imPermissions.add("delete");
            if (im.isCanView()) imPermissions.add("view");
        }
        permissions[1] = imPermissions.toArray(new String[0]);

        // Product Management
        List<String> pmPermissions = new ArrayList<>();
        ProductManagement pm = productManagementDAO.getById(userId);
        if (pm != null) {
            if (pm.isCanAdd()) pmPermissions.add("add");
            if (pm.isCanEdit()) pmPermissions.add("edit");
            if (pm.isCanDelete()) pmPermissions.add("delete");
            if (pm.isCanView()) pmPermissions.add("view");
        }
        permissions[2] = pmPermissions.toArray(new String[0]);

        // Order Management
        List<String> omPermissions = new ArrayList<>();
        OrderManagement om = orderManagementDAO.getById(userId);
        if (om != null) {
            if (om.isCanAdd()) omPermissions.add("add");
            if (om.isCanEdit()) omPermissions.add("edit");
            if (om.isCanDelete()) omPermissions.add("delete");
            if (om.isCanView()) omPermissions.add("view");
        }
        permissions[3] = omPermissions.toArray(new String[0]);

        // Statistics Management
        List<String> smPermissions = new ArrayList<>();
        StatisticsManagement sm = statisticsManagementDAO.getById(userId);
        if (sm != null && sm.isCanView()) {
            smPermissions.add("view");
        }
        permissions[4] = smPermissions.toArray(new String[0]);

        // Permission Management
        List<String> permPermissions = new ArrayList<>();
        boolean canManagePermissions = permissionManagementDAO.getCanManagePermissions(userId);
        if (canManagePermissions) {
            permPermissions.add("manage_permissions");
        }
        permissions[5] = permPermissions.toArray(new String[0]);

        return permissions;
    }

    public void closeConnections() {
        permissionManagementDAO.closeConnection();
        
    }
}