package com.bookstore.model.DTO;

public class AccountDTO {
    private int accountID;
    private String username;
    private String password;
    private Integer roleID;
    private Integer CustomerID;
    private Integer employeeID;

    public AccountDTO(int accountID, String username, String password, Integer roleID, Integer customerID, Integer employeeID) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.roleID = roleID;
        this.CustomerID = customerID;
        this.employeeID = employeeID;
    }
    public AccountDTO() {
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
    public Integer getRoleID() {
        return roleID;
    }
    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }
    public Integer getCustomerID() {
        return CustomerID;
    }
    public void setCustomerID(Integer customerID) {
        CustomerID = customerID;
    }
    public Integer getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }
    @Override
    public String toString() {
        return "AccountDTO [accountID= " + accountID + ", username= " + username + ", password= " + password + ", roleID= "
                + roleID + ", CustomerID= " + CustomerID + ", employeeID= " + employeeID + "]";
    }
}
