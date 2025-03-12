package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.AccountDTO;

public class AccountDAO {
    private Connection conn ;

    public AccountDAO() throws SQLException{
        conn = DBConnect.getConnection();
    }

    public AccountDTO login(String username, String password) throws SQLException {
       String sql = "SELECT * FROM Account WHERE Username = ? AND Password = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, username, password);
            if (rs.next()) {
                return new AccountDTO(
                    rs.getInt("AccountID"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getInt("RoleID"),
                    rs.getInt("CustomerID"),
                    rs.getInt("EmployeeID")
                );
            }
            return null;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
    }

    public AccountDTO getAccountByID(int accountID) throws SQLException {
        String sql = "SELECT * FROM Account WHERE AccountID = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, accountID);
            if (rs.next()) {
                return new AccountDTO(
                    rs.getInt("AccountID"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getInt("RoleID"),
                    rs.getInt("CustomerID"),
                    rs.getInt("EmployeeID")
                );
            }
            return null;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
    }

    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Account WHERE Username = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, username);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
    }
    
    public void addAccount(AccountDTO account) throws SQLException {
        // Kiểm tra Username trùng
        if (isUsernameExists(account.getUsername())) {
            throw new SQLException("Username already exists.");
        }

        // Thêm tài khoản
        String sql = "INSERT INTO Account (Username, Password, RoleID, CustomerID, EmployeeID) " +
                    "VALUES (?, ?, ?, ?, ?)";
        DBConnect.executeUpdate(sql,
            account.getUsername(),
            account.getPassword(),
            account.getRoleID(),
            account.getCustomerID(),
            account.getEmployeeID()
        );
    }
    
}
