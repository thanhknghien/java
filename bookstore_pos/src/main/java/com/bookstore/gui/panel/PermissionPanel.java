package com.bookstore.gui.panel;

import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.PanelCover;
import com.bookstore.gui.component.TextField;
import com.bookstore.model.Role;
import com.bookstore.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PermissionPanel extends PanelCover {
    private TextField searchField;
    private JComboBox<String> roleFilterCombo;
    private Button searchButton;
    private Button filterButton;
    private CustomTable userTable;
    private DefaultTableModel userTableModel;
    private JComboBox<String> moduleCombo;
    private CustomTable permissionTable;
    private DefaultTableModel permissionTableModel;
    private Button saveButton;

    public PermissionPanel() throws SQLException{
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
        initializeComponents();
    }

    private void initializeComponents() {
        // Phần 1: Thanh bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        searchField = new TextField();
        searchField.setPlaceholder("Tìm kiếm...");
        searchField.setPreferredSize(new Dimension(200, 30));
        searchButton = new Button("Tìm kiếm");
        roleFilterCombo = new JComboBox<>();
        filterButton = new Button("Lọc");

        topPanel.add(new JLabel("Tìm kiếm :"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Lọc theo vai trò:"));
        topPanel.add(roleFilterCombo);
        topPanel.add(filterButton);

        // Phần 2: Bảng người dùng
        String[] userColumns = {"ID", "Tên", "Vai trò"};
        userTableModel = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new CustomTable(userColumns);
        userTable.setModel(userTableModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        userScrollPane.setPreferredSize(new Dimension(1180, 200));

        // Phần 3: Quyền chi tiết
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        moduleCombo = new JComboBox<>(new String[]{
                "user_management", "order_management", 
                "product_management", "statistics_management", 
                "category_management", "customer_management", 
                "permission_management"
        });
        String[] permissionColumns = {"Tên quyền", "Mô tả", "Cấp quyền"};
        permissionTableModel = new DefaultTableModel(permissionColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };
        permissionTable = new CustomTable(permissionColumns);
        permissionTable.setModel(permissionTableModel);
        JScrollPane permissionScrollPane = new JScrollPane(permissionTable);
        permissionScrollPane.setPreferredSize(new Dimension(1180, 150));
        
        saveButton = new Button("Lưu thay đổi");

        JPanel modulePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modulePanel.add(new JLabel("Chọn module:"));
        modulePanel.add(moduleCombo);

        JPanel permissionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        permissionPanel.add(saveButton);

        bottomPanel.add(modulePanel);
        bottomPanel.add(permissionScrollPane);
        bottomPanel.add(permissionPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(userScrollPane, BorderLayout.CENTER);
        
        
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.add(bottomPanel);
        
        add(southPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public TextField getSearchField() {
        return searchField;
    }

    public JComboBox<String> getRoleFilterCombo() {
        return roleFilterCombo;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getFilterButton() {
        return filterButton;
    }

    public JTable getUserTable() {
        return userTable;
    }

    public DefaultTableModel getUserTableModel() {
        return userTableModel;
    }

    public JComboBox<String> getModuleCombo() {
        return moduleCombo;
    }

    public JTable getPermissionTable() {
        return permissionTable;
    }

    public DefaultTableModel getPermissionTableModel() {
        return permissionTableModel;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void updateRoles(List<Role> roles) {
        roleFilterCombo.removeAllItems();
        roleFilterCombo.addItem("Tất cả");
        for (Role role : roles) {
            roleFilterCombo.addItem(role.getName() + " (" + role.getId() + ")");
        }
    }

    public void updateUsers(List<User> users, Map<Integer, Role> roleMap) {
        userTableModel.setRowCount(0);
        for (User user : users) {
            Role role = roleMap.get(user.getRoleId());
            userTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    role != null ? role.getName() + " (" + role.getId() + ")" : "N/A"
            });
        }
    }

    public void updatePermissions(Map<String, Boolean> permissions, Map<String, String> descriptions) {
        permissionTableModel.setRowCount(0);
        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            permissionTableModel.addRow(new Object[]{
                    entry.getKey(),
                    descriptions.getOrDefault(entry.getKey(), "Không có mô tả"),
                    entry.getValue()
            });
        }
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}