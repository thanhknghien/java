package com.bookstore.dao;

import com.bookstore.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleDAOTest {
    private RoleDAO roleDAO;

    @BeforeEach
    void setUp() {
        roleDAO = new RoleDAO();
        try {
            roleDAO.deleteRole(999); // Xóa dữ liệu cũ nếu có
            Role role = new Role();
            role.setId(999);
            role.setName("Test Role");
            roleDAO.addRole(role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllRoles() throws SQLException {
        List<Role> roles = roleDAO.getAllRoles();
        assertFalse(roles.isEmpty(), "Danh sách vai trò không được rỗng");
    }

    @Test
    void testAddRole() throws SQLException {
        Role role = new Role();
        role.setName("New Role");
        roleDAO.addRole(role);

        List<Role> roles = roleDAO.searchRoles("New Role");
        assertFalse(roles.isEmpty(), "Vai trò vừa thêm phải tồn tại");
        assertEquals("New Role", roles.get(0).getName());
    }

    @Test
    void testUpdateRole() throws SQLException {
        List<Role> roles = roleDAO.searchRoles("Test Role");
        Role role = roles.get(0);
        role.setName("Updated Role");
        roleDAO.updateRole(role);

        roles = roleDAO.searchRoles("Updated Role");
        assertFalse(roles.isEmpty(), "Vai trò phải được cập nhật");
        assertEquals("Updated Role", roles.get(0).getName());
    }

    @Test
    void testDeleteRole() throws SQLException {
        roleDAO.deleteRole(999);
        List<Role> roles = roleDAO.searchRoles("Test Role");
        assertTrue(roles.isEmpty(), "Vai trò phải được xóa");
    }

    @Test
    void testSearchRoles() throws SQLException {
        List<Role> roles = roleDAO.searchRoles("Test");
        assertFalse(roles.isEmpty(), "Phải tìm thấy vai trò với từ khóa 'Test'");
        assertEquals("Test Role", roles.get(0).getName());
    }
}