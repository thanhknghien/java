/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model.DAO;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.EmployeeDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class EmployeeDAO {
    public boolean addEmployee(EmployeeDTO employee) throws SQLException{
        String sql = "INSERT INTO Employee ( Name, Status, Phone, Email) VALUE (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getStatus());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getEmail());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
                    if (generatedKeys.next()){
                        employee.setEmployeeId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } 
        return false;   
    }
    public EmployeeDTO getEmployeeById(int employeeId) throws SQLException{
        String sql = "SELECT * FROM Employee WHERE EmployeeId= ?";
        try(Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, employeeId);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    return new EmployeeDTO(
                       resultSet.getInt("EmployeeID"),
                       resultSet.getString("Name"),
                       resultSet.getString("Status"),
                       resultSet.getString("Phone"),
                       resultSet.getString("Email")     
                    );
                }
            }
        }
        
        return null;
        
    }
   
    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException{
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)){
            while (resultSet.next()){
                employees.add(new EmployeeDTO(
                    resultSet.getInt("EmployeeID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Status"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email")           
                ));
            }
        }
        
        return employees;
        
    }
    
    public boolean updateEmployee(EmployeeDTO employee) throws SQLException{
        String sql = "UPDATE Employee SET Name= ?, Status= ?, Phone= ?, Email= ? WHERE EmployeeID= ?";
        try( Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getStatus());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getEmail());
            stmt.setInt(5, employee.getEmployeeId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public boolean deleteEmployee(int employeeId) throws SQLException{
        String sql = "DELETE FROM Employee WHERE EmployeeID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, employeeId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected >0;
        }
    }
    
}
