package com.bookstore.model.DTO;

public class RoleDTO {
    private int roleID;
    private String roleName;

    public RoleDTO() {
    }
    public RoleDTO(int roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }
    public int getRoleID() {
        return roleID;
    }
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @Override
    public String toString() {
        return "RoleDTO [roleID= " + roleID + ", roleName= " + roleName + "]";
    }
}
