package com.bookstore.BUS;

import java.sql.SQLException;

import com.bookstore.model.DAO.AccountDAO;
import com.bookstore.model.DTO.AccountDTO;
import com.bookstore.utils.ShowException;
import com.bookstore.utils.ValidateUtils;

public class AuthBUS {
    private AccountDAO accountDAO;

    public AuthBUS() {
        accountDAO = new AccountDAO();
    }

    public boolean login(String username, String password) throws SQLException, ShowException {
        if(ValidateUtils.isEmptyString(username)){
            throw new ShowException("Empty username!");
        }
        if(ValidateUtils.isEmptyString(password)){
            throw new ShowException("Empty password!");
        }
        AccountDTO acc = new AccountDTO();
        acc = accountDAO.getAccountByUsername(username);
        return (acc != null && acc.getPassword().equals(password));
    }

    public boolean register(String fullName, String username, String password,String confirmPassword, String email, String address,String phone ) throws SQLException, ShowException{
        if(ValidateUtils.isEmptyString(fullName)){
            throw new ShowException("Empty fullName!");
        }
        if(ValidateUtils.isEmptyString(username)){
            throw new ShowException("Empty username!");
        }
        if(ValidateUtils.isEmptyString(password)){
            throw new ShowException("Empty password!");
        }
        if(!confirmPassword.equals(password)){
            throw new ShowException("Wrong Confirm Password!");
        }
        if(ValidateUtils.isEmptyString(email)){
            throw new ShowException("Empty email!");
        }
        if(ValidateUtils.isEmptyString(phone)){
            throw new ShowException("Empty phone!");
        }
        if(ValidateUtils.isEmptyString(address)){
            throw new ShowException("Empty address!");
        }
        if(ValidateUtils.isValidEmail(email)){
            throw new ShowException("Wrong email!");
        }
        if(ValidateUtils.isValidPhoneNumber(phone)){
            throw new ShowException("Wrong phone!");
        }
        if(accountDAO.getAccountByUsername(username) != null){
            throw new ShowException("Username already exists!");
        }
       
        AccountDTO newAcc = new AccountDTO(99, username, password, fullName, email, phone, address, 2, null);

        if(accountDAO.addAccount(newAcc)){
            return true;
        }else{
            return false;
        }
    }
}