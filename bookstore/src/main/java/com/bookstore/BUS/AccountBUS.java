package com.bookstore.BUS;

import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DTO.AccountDTO;
import java.sql.SQLException;
import java.util.List;

public class AccountBUS {
    private AccountDAO accountDAO;

    public AccountBUS() {
        accountDAO = new AccountDAO();
    }

    // Thêm tài khoản
    public boolean addAccount(AccountDTO account) throws SQLException {
        // Các logic nghiệp vụ (validate,...)
        return accountDAO.addAccount(account);
    }

    // Cập nhật tài khoản
    public boolean updateAccount(AccountDTO account) throws SQLException {
        return accountDAO.updateAccount(account);
    }

    // Xóa tài khoản theo ID
    public boolean deleteAccount(int accountId) throws SQLException {
        return accountDAO.deleteAccount(accountId);
    }

    // Tìm kiếm tài khoản (ví dụ trả về danh sách)
    public List<AccountDTO> searchAccounts(String keyword) {
        // TODO: Thực hiện tìm kiếm qua DAO
        return null;
    }
}
