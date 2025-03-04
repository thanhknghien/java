package com.bookstore.model.DTO;

public class RolePermissionDTO {
    private int roleID;
    private int permissionID;
    
    public RolePermissionDTO(int roleID, int permissionID) {
        this.roleID = roleID;
        this.permissionID = permissionID;
    }
    public int getRoleID() {
        return roleID;
    }
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    public int getPermissionID() {
        return permissionID;
    }
    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    
}
