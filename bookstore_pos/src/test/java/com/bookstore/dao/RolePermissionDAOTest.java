package com.bookstore.dao;

import com.bookstore.model.Permission;
import com.bookstore.model.PermissionGroup;
import com.bookstore.model.Role;
import com.bookstore.model.RolePermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RolePermissionDAOTest {
    private RolePermissionDAO rolePermissionDAO;
    private RoleDAO roleDAO;
    private PermissionDAO permissionDAO;
    private PermissionGroupDAO permissionGroupDAO;

    @BeforeEach
    void setUp() {
        rolePermissionDAO = new RolePermissionDAO();
        roleDAO = new RoleDAO();
        permissionDAO = new PermissionDAO();
        permissionGroupDAO = new PermissionGroupDAO();
        try {
            // Thêm vai trò mẫu
            roleDAO.deleteRole(999);
            Role role = new Role();
            role.setId(999);
            role.setName("Test Role");
            roleDAO.addRole(role);

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

            // Thêm ánh xạ quyền-vai trò mẫu
            rolePermissionDAO.deleteRolePermission(999, 999);
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(999);
            rolePermission.setPermissionId(999);
            rolePermissionDAO.addRolePermission(rolePermission);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllRolePermissions() throws SQLException {
        List<RolePermission> rolePermissions = rolePermissionDAO.getAllRolePermissions();
        assertFalse(rolePermissions.isEmpty(), "Danh sách ánh xạ quyền-vai trò không được rỗng");
    }

    @Test
    void testGetRolePermissionsByRoleId() throws SQLException {
        List<RolePermission> rolePermissions = rolePermissionDAO.getRolePermissionsByRoleId(999);
        assertFalse(rolePermissions.isEmpty(), "Phải tìm thấy quyền của vai trò 999");
        assertEquals(999, rolePermissions.get(0).getPermissionId());
    }

    @Test
    void testAddRolePermission() throws SQLException {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(999);
        rolePermission.setPermissionId(999);
        rolePermissionDAO.addRolePermission(rolePermission);

        List<RolePermission> rolePermissions = rolePermissionDAO.getRolePermissionsByRoleId(999);
        assertTrue(rolePermissions.stream().anyMatch(rp -> rp.getPermissionId() == 999), "Ánh xạ vừa thêm phải tồn tại");
    }

    @Test
    void testDeleteRolePermission() throws SQLException {
        rolePermissionDAO.deleteRolePermission(999, 999);
        List<RolePermission> rolePermissions = rolePermissionDAO.getRolePermissionsByRoleId(999);
        assertTrue(rolePermissions.isEmpty(), "Ánh xạ quyền-vai trò phải được xóa");
    }

    @Test
    void testDeleteRolePermissionsByRoleId() throws SQLException {
        rolePermissionDAO.deleteRolePermissionsByRoleId(999);
        List<RolePermission> rolePermissions = rolePermissionDAO.getRolePermissionsByRoleId(999);
        assertTrue(rolePermissions.isEmpty(), "Tất cả quyền của vai trò phải được xóa");
    }
}