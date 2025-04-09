/*package com.bookstore.gui.panel;

import com.bookstore.dao.PermissionDAO;
import com.bookstore.dao.PermissionGroupDAO;
import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.RolePermissionDAO;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.PanelCover;
import com.bookstore.gui.component.TextField;
import com.bookstore.model.Permission;
import com.bookstore.model.PermissionGroup;
import com.bookstore.model.Role;
import com.bookstore.model.RolePermission;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RolePermissionPanel extends PanelCover {
    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel southPanel;
    
    // North components
    private JComboBox<String> roleComboBox;
    private Button addPermissionButton;
    private Button removePermissionButton;
    private Button refreshButton;
    
    // Center components
    private JComboBox<String> groupComboBox;
    private JComboBox<String> permissionComboBox;
    private Button saveButton;
    private Button clearButton;
    
    // South components
    private CustomTable permissionTable;
    private DefaultTableModel tableModel;
    
    // Data access objects
    private RoleDAO roleDAO;
    private PermissionDAO permissionDAO;
    private PermissionGroupDAO groupDAO;
    private RolePermissionDAO rolePermissionDAO;
    
    // Current role for editing
    private Role currentRole;
    
    public RolePermissionPanel() {
        super();
        initData();
        initComponents();
        setupLayout();
        setupListeners();
        loadData();
        
        // Set fixed size
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
    }
    
    private void initData() {
        roleDAO = new RoleDAO();
        permissionDAO = new PermissionDAO();
        groupDAO = new PermissionGroupDAO();
        rolePermissionDAO = new RolePermissionDAO();
        currentRole = null;
    }
    
    private void initComponents() {
        // Initialize panels
        northPanel = new JPanel();
        centerPanel = new JPanel();
        southPanel = new JPanel();
        
        // North components
        roleComboBox = new JComboBox<>();
        roleComboBox.setPreferredSize(new Dimension(300, 35));
        loadRoleComboBox();
        
        addPermissionButton = new Button("Thêm quyền");
        removePermissionButton = new Button("Xóa quyền");
        refreshButton = new Button("Làm mới");
        
        // Center components
        groupComboBox = new JComboBox<>();
        groupComboBox.setPreferredSize(new Dimension(300, 35));
        loadGroupComboBox();
        
        permissionComboBox = new JComboBox<>();
        permissionComboBox.setPreferredSize(new Dimension(300, 35));
        
        saveButton = new Button("Lưu");
        clearButton = new Button("Hủy");
        
        // South components
        String[] columns = {"ID", "Nhóm quyền", "Quyền"};
        
        permissionTable = new CustomTable(columns);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // North Panel
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        northPanel.setOpaque(false);
        northPanel.add(new JLabel("Vai trò:"));
        northPanel.add(roleComboBox);
        northPanel.add(addPermissionButton);
        northPanel.add(removePermissionButton);
        northPanel.add(refreshButton);
        
        // Center Panel
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Group combobox
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Nhóm quyền:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(groupComboBox, gbc);
        
        // Permission combobox
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Quyền:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(permissionComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        centerPanel.add(buttonPanel, gbc);
        
        // South Panel (Table)
        southPanel.setLayout(new BorderLayout());
        southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(permissionTable);
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to main panel
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        // Role combobox listener
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            if (selectedRole != null && !selectedRole.isEmpty()) {
                int roleId = Integer.parseInt(selectedRole.split(":")[0]);
                try {
                    currentRole = roleDAO.getRoleById(roleId);
                    loadRolePermissions(roleId);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Lỗi khi tải quyền của vai trò: " + ex.getMessage(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Group combobox listener
        groupComboBox.addActionListener(e -> {
            String selectedGroup = (String) groupComboBox.getSelectedItem();
            if (selectedGroup != null && !selectedGroup.isEmpty()) {
                int groupId = Integer.parseInt(selectedGroup.split(":")[0]);
                loadPermissionsByGroup(groupId);
            }
        });
        
        // Add permission button listener
        addPermissionButton.addActionListener(e -> {
            if (currentRole == null) {
                JOptionPane.showMessageDialog(null, 
                    "Vui lòng chọn một vai trò", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String selectedPermission = (String) permissionComboBox.getSelectedItem();
            if (selectedPermission != null && !selectedPermission.isEmpty()) {
                int permissionId = Integer.parseInt(selectedPermission.split(":")[0]);
                try {
                    RolePermission rolePermission = new RolePermission(currentRole.getId(), permissionId);
                    rolePermissionDAO.addRolePermission(rolePermission);
                    loadRolePermissions(currentRole.getId());
                    clearForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Lỗi khi thêm quyền: " + ex.getMessage(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Remove permission button listener
        removePermissionButton.addActionListener(e -> {
            if (currentRole == null) {
                JOptionPane.showMessageDialog(null, 
                    "Vui lòng chọn một vai trò", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int selectedRow = permissionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int permissionId = Integer.parseInt(permissionTable.getValueAt(selectedRow, 0).toString());
                try {
                    rolePermissionDAO.deleteRolePermission(currentRole.getId(), permissionId);
                    loadRolePermissions(currentRole.getId());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Lỗi khi xóa quyền: " + ex.getMessage(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Vui lòng chọn một quyền để xóa", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Refresh button listener
        refreshButton.addActionListener(e -> {
            loadData();
            if (currentRole != null) {
                loadRolePermissions(currentRole.getId());
            }
        });
        
        // Clear button listener
        clearButton.addActionListener(e -> clearForm());
    }
    
    private void loadData() {
        loadRoleComboBox();
        loadGroupComboBox();
    }
    
    private void loadRoleComboBox() {
        try {
            roleComboBox.removeAllItems();
            List<Role> roles = roleDAO.getAllRoles();
            for (Role role : roles) {
                roleComboBox.addItem(role.getId() + ": " + role.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi tải danh sách vai trò: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadGroupComboBox() {
        try {
            groupComboBox.removeAllItems();
            List<PermissionGroup> groups = groupDAO.getAllPermissionGroups();
            for (PermissionGroup group : groups) {
                groupComboBox.addItem(group.getId() + ": " + group.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi tải danh sách nhóm quyền: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPermissionsByGroup(int groupId) {
        try {
            permissionComboBox.removeAllItems();
            List<Permission> permissions = permissionDAO.getPermissionsByGroupId(groupId);
            for (Permission permission : permissions) {
                permissionComboBox.addItem(permission.getId() + ": " + permission.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi tải danh sách quyền: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadRolePermissions(int roleId) {
        try {
            tableModel.setRowCount(0);
            List<RolePermission> rolePermissions = rolePermissionDAO.getRolePermissionsByRoleId(roleId);
            for (RolePermission rolePermission : rolePermissions) {
                Permission permission = permissionDAO.getPermissionById(rolePermission.getPermissionId());
                PermissionGroup group = groupDAO.getPermissionGroupById(permission.getGroupId());
                tableModel.addRow(new Object[]{
                    permission.getId(),
                    group.getName(),
                    permission.getName()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi tải quyền của vai trò: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        groupComboBox.setSelectedIndex(0);
        permissionComboBox.removeAllItems();
    }
} */