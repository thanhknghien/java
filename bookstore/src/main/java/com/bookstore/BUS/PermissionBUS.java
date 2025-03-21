package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DAO.RolePermissionDAO;
import com.bookstore.model.DTO.AccountDTO;
import com.bookstore.model.DTO.PermissionDTO;

public class PermissionBUS {
    private AccountDAO dao;
    private RolePermissionDAO rolePermissionDAO;
    
    public PermissionBUS(){
        dao = new AccountDAO();
        rolePermissionDAO = new RolePermissionDAO();

    }

    public int getRoleByAccountID (int accountID) throws SQLException{
        AccountDTO acc = dao.getAccountByID(accountID);
        return acc.getRoleId();
    }

    public ArrayList<String> getPermissionsForRole(int roleId) throws SQLException {
        ArrayList<String> permissions = new ArrayList<>();
        for( PermissionDTO p : rolePermissionDAO.getPermissionsByRoleID(roleId)){
            permissions.add(p.getPermissionName());
        }
        return permissions;
    }
}
