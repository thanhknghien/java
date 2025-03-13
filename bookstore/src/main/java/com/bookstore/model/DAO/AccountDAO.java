package com.bookstore.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.AccountDTO;

public class AccountDAO {
    // Insert Account
    public boolean insertAccount(AccountDTO account) throws SQLException {
        String sql = "INSERT INTO Account (Username, Password, RoleID, CustomerID, EmployeeID) VALUES (?, ?, ?, ?, ?)";
        return DBConnect.executeUpdate(sql, account.getUsername(), account.getPassword(),
                                       account.getRoleID(), account.getCustomerID(), account.getEmployeeID());
    }

    // Update Account
    public boolean updateAccount(AccountDTO account) throws SQLException {
        String sql = "UPDATE Account SET Password = ?, RoleID = ?, CustomerID = ?, EmployeeID = ? WHERE Username = ?";
        return DBConnect.executeUpdate(sql, account.getPassword(), account.getRoleID(),
                                       account.getCustomerID(), account.getEmployeeID(), account.getUsername());
    }

    // Delete Account
    public boolean deleteAccount(String username) throws SQLException {
        String sql = "DELETE FROM Account WHERE Username = ?";
        return DBConnect.executeUpdate(sql, username);
    }

    // Get All Accounts
    public ArrayList<AccountDTO> getAllAccounts() throws SQLException {
        ArrayList<AccountDTO> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        ResultSet rs = DBConnect.executeQuery(sql);

        while (rs.next()) {
            accounts.add(new AccountDTO(rs.getInt("AccountID"), rs.getString("Username"), rs.getString("Password"),
                                     rs.getInt("RoleID"), rs.getInt("CustomerID"), rs.getInt("EmployeeID")));
        }

        DBConnect.closeResultSet(rs);
        return accounts;
    }

    // Get Account By Username
    public AccountDTO getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Account WHERE Username = ?";
        ResultSet rs = DBConnect.executeQuery(sql, username);

        if (rs.next()) {
            AccountDTO account = new AccountDTO(rs.getInt("AccountID"), rs.getString("Username"), rs.getString("Password"),
                                          rs.getInt("RoleID"), rs.getInt("CustomerID"), rs.getInt("EmployeeID"));
            DBConnect.closeResultSet(rs);
            return account;
        }

        DBConnect.closeResultSet(rs);
        return null;
    }

    // Get Account By ID
    public AccountDTO getAccountByID(int accountID) throws SQLException {
        String sql = "SELECT * FROM Account WHERE AccountID = ?";
        ResultSet rs = DBConnect.executeQuery(sql, accountID);

        if (rs.next()) {
            AccountDTO account = new AccountDTO(rs.getInt("AccountID"), rs.getString("Username"), rs.getString("Password"),
                                          rs.getInt("RoleID"), rs.getInt("CustomerID"), rs.getInt("EmployeeID"));
            DBConnect.closeResultSet(rs);
            return account;
        }

        DBConnect.closeResultSet(rs);
        return null;
    }
}