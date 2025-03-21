package com.bookstore.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.BUS.PermissionBUS;
import com.bookstore.view.main.MainFrame;

public class MainController {
    private MainFrame view;
    private PermissionBUS permissionBUS;
    private int accountId;

    public MainController(MainFrame view, int accountId) {
        this.view = view;
        this.accountId = accountId;
        this.permissionBUS = new PermissionBUS();
    }

    // Lấy danh sách quyền từ BUS
    public ArrayList<String> getPermissions() throws SQLException {
        int roleId = permissionBUS.getRoleByAccountID(accountId);
        if (roleId == -1) {
            return new ArrayList<>(); 
        }
        return permissionBUS.getPermissionsForRole(roleId);
    }

    public void handleNavButtonClick(String permission) {
        view.showFunctionPanel(permission);
    }
}
