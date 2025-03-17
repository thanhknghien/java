/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model.DTO;

/**
 *
 * @author HP
 */
public class EmployeeDTO {
    private int employeeId;
    private String name;
    private String status;
    private String phone;
    private String email;

    public EmployeeDTO() {
    }

    public EmployeeDTO(int employeeId, String name, String status, String phone, String email) {
        this.employeeId = employeeId;
        this.name = name;
        this.status = status;
        this.phone = phone;
        this.email = email;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" + "employeeId=" + employeeId + ", name=" + name + ", status=" + status + ", phone=" + phone + ", email=" + email + '}';
    }
}
