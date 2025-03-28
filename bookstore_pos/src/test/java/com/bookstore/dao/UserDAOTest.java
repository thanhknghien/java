package com.bookstore.dao;

import com.bookstore.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        try {
            userDAO.deleteUser(999); // Xóa dữ liệu cũ nếu có
            User user = new User();
            user.setId(999);
            user.setUsername("testuser");
            user.setPassword("testpass");
            user.setRoleId(1); // Giả định role_id = 1 tồn tại
            userDAO.addUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllUsers() throws SQLException {
        List<User> users = userDAO.getAllUsers();
        assertFalse(users.isEmpty(), "Danh sách người dùng không được rỗng");
    }

    @Test
    void testAddUser() throws SQLException {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("newpass");
        user.setRoleId(1);
        userDAO.addUser(user);

        List<User> users = userDAO.searchUsers("newuser");
        assertFalse(users.isEmpty(), "Người dùng vừa thêm phải tồn tại");
        assertEquals("newuser", users.get(0).getUsername());
    }

    @Test
    void testUpdateUser() throws SQLException {
        List<User> users = userDAO.searchUsers("testuser");
        User user = users.get(0);
        user.setPassword("newpass");
        userDAO.updateUser(user);

        users = userDAO.searchUsers("testuser");
        assertEquals("newpass", users.get(0).getPassword(), "Mật khẩu người dùng phải được cập nhật");
    }

    @Test
    void testDeleteUser() throws SQLException {
        userDAO.deleteUser(999);
        List<User> users = userDAO.searchUsers("testuser");
        assertTrue(users.isEmpty(), "Người dùng phải được xóa");
    }

    @Test
    void testSearchUsers() throws SQLException {
        List<User> users = userDAO.searchUsers("testuser");
        assertFalse(users.isEmpty(), "Phải tìm thấy người dùng với từ khóa 'testuser'");
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    void testLogin() throws SQLException {
        User user = userDAO.login("testuser", "testpass");
        assertNotNull(user, "Đăng nhập phải thành công");
        assertEquals("testuser", user.getUsername());
    }
}