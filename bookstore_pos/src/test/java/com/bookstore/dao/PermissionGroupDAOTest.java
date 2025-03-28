package com.bookstore.dao;

import com.bookstore.model.PermissionGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionGroupDAOTest {
    private PermissionGroupDAO permissionGroupDAO;

    @BeforeEach
    void setUp() {
        permissionGroupDAO = new PermissionGroupDAO();
        try {
            permissionGroupDAO.deletePermissionGroup(999); // Xóa dữ liệu cũ nếu có
            PermissionGroup group = new PermissionGroup();
            group.setId(999);
            group.setName("Test Group");
            permissionGroupDAO.addPermissionGroup(group);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllPermissionGroups() throws SQLException {
        List<PermissionGroup> groups = permissionGroupDAO.getAllPermissionGroups();
        assertFalse(groups.isEmpty(), "Danh sách nhóm quyền không được rỗng");
    }

    @Test
    void testAddPermissionGroup() throws SQLException {
        PermissionGroup group = new PermissionGroup();
        group.setName("New Group");
        permissionGroupDAO.addPermissionGroup(group);

        List<PermissionGroup> groups = permissionGroupDAO.searchPermissionGroups("New Group");
        assertFalse(groups.isEmpty(), "Nhóm quyền vừa thêm phải tồn tại");
        assertEquals("New Group", groups.get(0).getName());
    }

    @Test
    void testUpdatePermissionGroup() throws SQLException {
        List<PermissionGroup> groups = permissionGroupDAO.searchPermissionGroups("Test Group");
        PermissionGroup group = groups.get(0);
        group.setName("Updated Group");
        permissionGroupDAO.updatePermissionGroup(group);

        groups = permissionGroupDAO.searchPermissionGroups("Updated Group");
        assertFalse(groups.isEmpty(), "Nhóm quyền phải được cập nhật");
        assertEquals("Updated Group", groups.get(0).getName());
    }

    @Test
    void testDeletePermissionGroup() throws SQLException {
        permissionGroupDAO.deletePermissionGroup(999);
        List<PermissionGroup> groups = permissionGroupDAO.searchPermissionGroups("Test Group");
        assertTrue(groups.isEmpty(), "Nhóm quyền phải được xóa");
    }

    @Test
    void testSearchPermissionGroups() throws SQLException {
        List<PermissionGroup> groups = permissionGroupDAO.searchPermissionGroups("Test");
        assertFalse(groups.isEmpty(), "Phải tìm thấy nhóm quyền với từ khóa 'Test'");
        assertEquals("Test Group", groups.get(0).getName());
    }
}