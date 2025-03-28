package com.bookstore.model;

public class User {
    private int id;
    private String username;
    private String password;
    private Integer roleId; 
    public User() {
    }
    public User(int id, String username, String password, Integer roleId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
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
}