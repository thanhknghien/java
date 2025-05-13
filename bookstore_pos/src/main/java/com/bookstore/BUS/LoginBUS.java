package com.bookstore.BUS;

import com.bookstore.dao.UserDAO;
import com.bookstore.model.User;

import java.sql.SQLException;

public class LoginBUS {
    private UserDAO userDAO;

    public LoginBUS() {
        userDAO = new UserDAO();
    }

    public User validateLogin(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User user = userDAO.login(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!user.isStatus()) {
            throw new IllegalArgumentException("Account is inactive");
        }

        return user;
    }
}