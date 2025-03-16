package com.bookstore.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.AccountDTO;

public class AccountDAO {

    // Thêm tài khoản mới
    public boolean insertAccount(AccountDTO account) throws SQLException {
        String sql = "INSERT INTO Account (Username, Password, FullName, Email, Phone, Address, RoleID, EmployeeID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return DBConnect.executeUpdate(sql, 
                account.getUsername(), account.getPassword(), 
                account.getFullName(), account.getEmail(), account.getPhone(), 
                account.getAddress(), account.getRoleID(), account.getEmployeeID());
    }

    // Cập nhật tài khoản
    public boolean updateAccount(AccountDTO account) throws SQLException {
        String sql = "UPDATE Account SET Password = ?, FullName = ?, Email = ?, Phone = ?, Address = ?, RoleID = ?, EmployeeID = ? " +
                     "WHERE Username = ?";
        return DBConnect.executeUpdate(sql, 
                account.getPassword(), account.getFullName(), account.getEmail(), 
                account.getPhone(), account.getAddress(), account.getRoleID(), 
                account.getEmployeeID(), account.getUsername());
    }

    // Xóa tài khoản theo Username
    public boolean deleteAccount(String username) throws SQLException {
        String sql = "DELETE FROM Account WHERE Username = ?";
        return DBConnect.executeUpdate(sql, username);
    }

    // Lấy danh sách tất cả tài khoản
    public ArrayList<AccountDTO> getAllAccounts() throws SQLException {
        ArrayList<AccountDTO> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        ResultSet rs = DBConnect.executeQuery(sql);

        while (rs.next()) {
            accounts.add(new AccountDTO(
                rs.getInt("AccountID"), rs.getString("Username"), rs.getString("Password"),
                rs.getString("FullName"), rs.getString("Email"), rs.getString("Phone"),
                rs.getString("Address"), rs.getInt("RoleID"), rs.getInt("EmployeeID")));
        }

        DBConnect.closeResultSet(rs);
        return accounts;
    }

    // Lấy tài khoản theo Username
    public AccountDTO getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Account WHERE Username = ? LIMIT 1";
        ResultSet rs = DBConnect.executeQuery(sql, username);

        if (rs.next()) {
            AccountDTO account = new AccountDTO(
                rs.getInt("AccountID"), rs.getString("Username"), rs.getString("Password"),
                rs.getString("FullName"), rs.getString("Email"), rs.getString("Phone"),
                rs.getString("Address"), rs.getInt("RoleID"), rs.getInt("EmployeeID"));
            DBConnect.closeResultSet(rs);
            return account;
        }

        DBConnect.closeResultSet(rs);
        return null;
    }

    // Lấy tài khoản theo ID
    public AccountDTO getAccountByID(int accountID) throws SQLException {
        String sql = "SELECT * FROM Account WHERE AccountID = ? LIMIT 1";
        ResultSet rs = DBConnect.executeQuery(sql, accountID);

        if (rs.next()) {
            AccountDTO account = new AccountDTO(
                rs.getInt("AccountID"), rs.getString("Username"), rs.getString("Password"),
                rs.getString("FullName"), rs.getString("Email"), rs.getString("Phone"),
                rs.getString("Address"), rs.getInt("RoleID"), rs.getInt("EmployeeID"));
            DBConnect.closeResultSet(rs);
            return account;
        }

        DBConnect.closeResultSet(rs);
        return null;
    }

    // Kiểm tra tài khoản đã tồn tại hay chưa
    public boolean isAccountExist(String username) throws SQLException {
        String sql = "SELECT 1 FROM Account WHERE Username = ? LIMIT 1";
        ResultSet rs = DBConnect.executeQuery(sql, username);
        boolean exists = rs.next();
        DBConnect.closeResultSet(rs);
        return exists;
    }
}