package com.bookstore.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.AccountDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class AccountDAO {
    
    public boolean addAccount(AccountDTO account) throws SQLException{
        String sql = "INSERT INTO Account (Username, Password, FullName, Email, Phone, Address, RoleID, EmployeeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getFullName());
            stmt.setString(4, account.getEmail());
            stmt.setString(5, account.getPhone());
            stmt.setString(6, account.getAddress());
            stmt.setInt(7, account.getRoleId());
            stmt.setInt(8, account.getEmployeeId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setAccountId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    public AccountDTO getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM Account WHERE AccountID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new AccountDTO(
                        resultSet.getInt("AccountID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Email"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Address"),
                        resultSet.getInt("RoleID"),
                        resultSet.getInt("EmployeeID")
                    );
                }
            }
        }
        return null;
    }
    // Lấy tất cả tài khoản
    public ArrayList<AccountDTO> getAllAccounts() throws SQLException {
        ArrayList<AccountDTO> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                accounts.add(new AccountDTO(
                    resultSet.getInt("AccountID"),
                    resultSet.getString("Username"),
                    resultSet.getString("Password"),
                    resultSet.getString("FullName"),
                    resultSet.getString("Email"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Address"),
                    resultSet.getInt("RoleID"),
                    resultSet.getInt("EmployeeID")
                ));
            }
        }
        return accounts;
    }
    // Cập nhật thông tin tài khoản
    public boolean updateAccount(AccountDTO account) throws SQLException {
        String sql = "UPDATE Account SET Username = ?, Password = ?, FullName = ?, Email = ?, Phone = ?, Address = ?, RoleID = ?, EmployeeID = ? WHERE AccountID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getFullName());
            stmt.setString(4, account.getEmail());
            stmt.setString(5, account.getPhone());
            stmt.setString(6, account.getAddress());
            stmt.setInt(7, account.getRoleId());
            stmt.setInt(8, account.getEmployeeId());
            stmt.setInt(9, account.getAccountId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Xóa tài khoản
    public boolean deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM Account WHERE AccountID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    // Lấy thông tin tài khoản theo tên đăng nhập
    public AccountDTO getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Account WHERE Username = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new AccountDTO(
                        resultSet.getInt("AccountID"),
                        resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Email"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Address"),
                        resultSet.getInt("RoleID"),
                        resultSet.getInt("EmployeeID")
                    );
                }
            }
        }
        return null;
    }
}