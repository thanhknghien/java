package com.bookstore.dao;

import com.bookstore.model.Permission;
import com.bookstore.model.PermissionGroup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionDAOTest {
    private PermissionDAO permissionDAO;
    private PermissionGroupDAO permissionGroupDAO;

    @BeforeEach
    void setUp() {
        permissionDAO = new PermissionDAO();
        permissionGroupDAO = new PermissionGroupDAO();
        try {
            // Thêm nhóm quyền mẫu
            permissionGroupDAO.deletePermissionGroup(999);
            PermissionGroup group = new PermissionGroup();
            group.setId(999);
            group.setName("Test Group");
            permissionGroupDAO.addPermissionGroup(group);

            // Thêm quyền mẫu
            permissionDAO.deletePermission(999);
            Permission permission = new Permission();
            permission.setId(999);
            permission.setGroupId(999);
            permission.setName("Thêm");
            permissionDAO.addPermission(permission);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllPermissions() throws SQLException {
        List<Permission> permissions = permissionDAO.getAllPermissions();
        assertFalse(permissions.isEmpty(), "Danh sách quyền không được rỗng");
    }

    @Test
    void testGetPermissionsByGroupId() throws SQLException {
        List<Permission> permissions = permissionDAO.getPermissionsByGroupId(999);
        assertFalse(permissions.isEmpty(), "Phải tìm thấy quyền của nhóm quyền 999");
        assertEquals("Thêm", permissions.get(0).getName());
    }

    @Test
    void testAddPermission() throws SQLException {
        Permission permission = new Permission();
        permission.setGroupId(999);
        permission.setName("Sửa");
        permissionDAO.addPermission(permission);

        List<Permission> permissions = permissionDAO.getPermissionsByGroupId(999);
        assertTrue(permissions.stream().anyMatch(p -> p.getName().equals("Sửa")), "Quyền vừa thêm phải tồn tại");
    }

    @Test
    void testUpdatePermission() throws SQLException {
        List<Permission> permissions = permissionDAO.getPermissionsByGroupId(999);
        Permission permission = permissions.get(0);
        permission.setName("Xóa");
        permissionDAO.updatePermission(permission);

        permissions = permissionDAO.getPermissionsByGroupId(999);
        assertEquals("Xóa", permissions.get(0).getName(), "Quyền phải được cập nhật");
    }

    @Test
    void testDeletePermission() throws SQLException {
        permissionDAO.deletePermission(999);
        List<Permission> permissions = permissionDAO.getPermissionsByGroupId(999);
        assertTrue(permissions.isEmpty(), "Quyền phải được xóa");
    }

    @Test
    void testSearchPermissions() throws SQLException {
        List<Permission> permissions = permissionDAO.searchPermissions("Thêm");
        assertFalse(permissions.isEmpty(), "Phải tìm thấy quyền với từ khóa 'Thêm'");
        assertEquals("Thêm", permissions.get(0).getName());
    }
}