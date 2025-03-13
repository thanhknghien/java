package com.bookstore.controller;

import java.sql.SQLException;

import com.bookstore.BUS.AuthBUS;

public class AuthController {
    private AuthBUS authBUS;

    public AuthController(){
        authBUS = new AuthBUS();
    }

    public boolean handleLogin(String username, String password) throws SQLException{
        return authBUS.login(username, password);
    }

    public boolean handleRegister(String fullName, String username, String password,String confirmPassword, String email, String phone){
        try {
            return authBUS.register(fullName, username, password, confirmPassword, email, phone);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
