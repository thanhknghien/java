package com.bookstore.model.DTO;

public class AccountDTO {
    private int accountID;
    private String username;
    private String password;
    private int roleID;
    private int CustomerID;
    private int employeeID;

    public AccountDTO(int accountID, String username, String password, int roleID, int customerID, int employeeID) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.roleID = roleID;
        CustomerID = customerID;
        this.employeeID = employeeID;
    }
    public int getAccountID() {
        return accountID;
    }
    public void setAccountID(int accountID) {
        this.accountID = accountID;
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
    public int getRoleID() {
        return roleID;
    }
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    public int getCustomerID() {
        return CustomerID;
    }
    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }
    public int getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
}
