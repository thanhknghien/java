package com.bookstore.model;

public class User {
    private int id;
    private String username;
    private String password;
    private Integer roleId;
    private boolean status;

    public User() {
        this.status = true; // Default status is active
    }

    public User(int id, String username, String password, Integer roleId, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                ", status=" + status +
                '}';
    }
}