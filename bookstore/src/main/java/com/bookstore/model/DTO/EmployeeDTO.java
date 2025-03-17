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
    private String status;

    public EmployeeDTO() {
    }

    public EmployeeDTO(int employeeId, String status) {
        this.employeeId = employeeId;
        this.status = status;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" + "employeeId=" + employeeId + ", status=" + status + '}';
    }
    
}