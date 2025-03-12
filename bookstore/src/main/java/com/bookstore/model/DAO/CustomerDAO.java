/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model.DAO;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.CustomerDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class CustomerDAO {
     // Method to add a new customer
    public boolean addCustomer(CustomerDTO customer) throws SQLException {
        String sql = "INSERT INTO Customer (Name, Address, Phone, Email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            int rowsAffected = stmt.executeUpdate();
            
            // Get the generated customer ID
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setCustomerId(generatedKeys.getInt(1));
                    }
                }
                return true; // Return true if the customer was added successfully
            }
        }
        return false; // Return false if the customer was not added
    }

    // Method to get a customer by ID
    public CustomerDTO getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new CustomerDTO(
                        resultSet.getInt("CustomerID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Email")
                    );
                }
            }
        }
        
        return null;
    }

    // Method to get all customers
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        ArrayList<CustomerDTO> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                customers.add(new CustomerDTO(
                    resultSet.getInt("CustomerID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Address"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email")
                ));
            }
        }
        return customers;
    }

    // Method to update a customer
    public boolean updateCustomer(CustomerDTO customer) throws SQLException {
        String sql = "UPDATE Customer SET Name = ?, Address = ?, Phone = ?, Email = ? WHERE CustomerID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            stmt.setInt(5, customer.getCustomerId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the customer was updated successfully
        }
    }

    // Method to delete a customer
    public boolean deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the customer was deleted successfully
        }
    }

    
}
