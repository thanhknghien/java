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

    public ArrayList<ArrayList<String>> getAllPermissions() throws SQLException {
        ArrayList<ArrayList<String>> permissions = new ArrayList<>();
        // Initialize with 6 empty ArrayLists for each module
        for (int i = 0; i < 6; i++) {
            permissions.add(new ArrayList<>());
        }

        if (currentUser == null) {
            return permissions;
        }

        int userId = currentUser.getId();

        // User Management
        ArrayList<String> umPermissions = permissions.get(0);
        UserManagement um = userManagementDAO.getById(userId);
        if (um != null) {
            if (um.isCanAdd()) umPermissions.add("add");
            if (um.isCanEdit()) umPermissions.add("edit");
            if (um.isCanDelete()) umPermissions.add("delete");
            if (um.isCanView()) umPermissions.add("view");
        }

        // Invoice Management
        ArrayList<String> imPermissions = permissions.get(1);
        InvoiceManagement im = invoiceManagementDAO.getById(userId);
        if (im != null) {
            if (im.isCanAdd()) imPermissions.add("add");
            if (im.isCanEdit()) imPermissions.add("edit");
            if (im.isCanDelete()) imPermissions.add("delete");
            if (im.isCanView()) imPermissions.add("view");
        }

        // Product Management
        ArrayList<String> pmPermissions = permissions.get(2);
        ProductManagement pm = productManagementDAO.getById(userId);
        if (pm != null) {
            if (pm.isCanAdd()) pmPermissions.add("add");
            if (pm.isCanEdit()) pmPermissions.add("edit");
            if (pm.isCanDelete()) pmPermissions.add("delete");
            if (pm.isCanView()) pmPermissions.add("view");
        }

        // Order Management
        ArrayList<String> omPermissions = permissions.get(3);
        OrderManagement om = orderManagementDAO.getById(userId);
        if (om != null) {
            if (om.isCanAdd()) omPermissions.add("add");
            if (om.isCanEdit()) omPermissions.add("edit");
            if (om.isCanDelete()) omPermissions.add("delete");
            if (om.isCanView()) omPermissions.add("view");
        }

        // Statistics Management
        ArrayList<String> smPermissions = permissions.get(4);
        StatisticsManagement sm = statisticsManagementDAO.getById(userId);
        if (sm != null && sm.isCanView()) {
            smPermissions.add("view");
        }

        // Permission Management
        ArrayList<String> permPermissions = permissions.get(5);
        boolean canManagePermissions = permissionManagementDAO.getCanManagePermissions(userId);
        if (canManagePermissions) {
            permPermissions.add("manage_permissions");
        }

        return permissions;
    }

    public void closeConnections() {
        permissionManagementDAO.closeConnection();
        
    }
}