package com.bookstore.BUS;

import java.sql.SQLException;

import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DTO.AccountDTO;

public class AuthBUS {
    private AccountDAO accountDAO;

    public AuthBUS() {
        accountDAO = new AccountDAO();
    }

    public boolean login(String username, String password) throws SQLException {
        AccountDTO acc = new AccountDTO();
        acc = accountDAO.getAccountByUsername(username);
        return (acc != null && acc.getPassword().equals(password));
    }

    public boolean register(String fullName, String username, String password,String confirmPassword, String email, String phone ) throws SQLException{
        if(!password.equals(confirmPassword)) return false;
        if(accountDAO.getAccountByUsername(username) != null){
            return false;
        }
       
        AccountDTO newAcc = new AccountDTO(12, username, password, null, null, null);

        if(accountDAO.insertAccount(newAcc)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        AuthBUS authBUS = new AuthBUS();
        try {
            System.out.println(authBUS.register("thanh", "thanh1234", "thanh111", "thanh111", "thanh", "123"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
         
    }
}
