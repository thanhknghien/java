package com.bookstore.model.DTO;

public class PermissionDTO {
    private int permissionID;
    private String permissionName;

    public PermissionDTO() {
    }

    public PermissionDTO(int permissionID, String permissionName) {
        this.permissionID = permissionID;
        this.permissionName = permissionName;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
