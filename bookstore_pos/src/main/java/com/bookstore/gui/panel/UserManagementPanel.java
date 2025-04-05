package com.bookstore.gui.panel;

import com.bookstore.BUS.UserManagementBUS;
import com.bookstore.dao.RoleDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.PanelCover;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.Role;
import com.bookstore.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class UserManagementPanel extends PanelCover {
    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel southPanel;
    
    // North components
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private JComboBox<String> filterRoleComboBox;
    private JComboBox<String> filterStatusComboBox;
    private TextField searchField;
    private Button searchButton;
    
    // Center components
    private TextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JCheckBox statusCheckBox;
    private Button saveButton;
    private Button clearButton;
    
    // South components
    private CustomTable userTable;
    private DefaultTableModel tableModel;
    
    // Data access objects
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    
    // Business logic handler
    private UserManagementBUS bus;
    
    // Current user for editing
    private User currentUser;
    private boolean isEditing = false;
    
    // Constants for status
    private static final String STATUS_ACTIVE = "Hoạt động";
    private static final String STATUS_INACTIVE = "Ngưng hoạt động";
    
    public UserManagementPanel() {
        super();
        initData();
        initComponents();
        setupLayout();
        setupListeners();
        loadUserData();
        
        // Set fixed size
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
    }
    
    private void initData() {
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
        currentUser = null;
        bus = new UserManagementBUS(this);
    }
    
    private void initComponents() {
        // Initialize panels
        northPanel = new JPanel();
        centerPanel = new JPanel();
        southPanel = new JPanel();
        
        // North components
        addButton = new Button("Thêm");
        editButton = new Button("Sửa");
        deleteButton = new Button("Xóa");
        
        filterRoleComboBox = new JComboBox<>();
        filterRoleComboBox.addItem("Tất cả vai trò");
        loadRoleComboBox(filterRoleComboBox);
        
        filterStatusComboBox = new JComboBox<>();
        filterStatusComboBox.addItem("Tất cả");
        filterStatusComboBox.addItem(STATUS_ACTIVE);
        filterStatusComboBox.addItem(STATUS_INACTIVE);
        
        searchField = new TextField();
        searchField.setPlaceholder("Tìm kiếm theo tên người dùng");
        searchField.setPreferredSize(new Dimension(200, 35));
        
        searchButton = new Button("Tìm kiếm");
        
        // Center components
        usernameField = new TextField();
        usernameField.setPlaceholder("Tên người dùng");
        usernameField.setPreferredSize(new Dimension(300, 35));
        
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 35));
        
        roleComboBox = new JComboBox<>();
        roleComboBox.setPreferredSize(new Dimension(300, 35));
        loadRoleComboBox(roleComboBox);
        
        statusCheckBox = new JCheckBox(STATUS_ACTIVE);
        statusCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCheckBox.setSelected(true);
        
        saveButton = new Button("Lưu");
        clearButton = new Button("Hủy");
        
        // South components
        String[] columnNames = {"ID", "Tên người dùng", "Mật khẩu", "Vai trò", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new CustomTable(columnNames);
        userTable.setModel(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // North Panel (MenuBar)
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        northPanel.setBackground(ColorScheme.PRIMARY);
        
        northPanel.add(addButton);
        northPanel.add(editButton);
        northPanel.add(deleteButton);
        northPanel.add(new JLabel("Vai trò:"));
        northPanel.add(filterRoleComboBox);
        northPanel.add(new JLabel("Trạng thái:"));
        northPanel.add(filterStatusComboBox);
        northPanel.add(searchField);
        northPanel.add(searchButton);
        
        // Center Panel (Form)
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Tên người dùng:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(passwordField, gbc);
        
        // Role combobox
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Vai trò:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(roleComboBox, gbc);
        
        // Status checkbox
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        centerPanel.add(statusCheckBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        centerPanel.add(buttonPanel, gbc);
        
        // South Panel (Table)
        southPanel.setLayout(new BorderLayout());
        southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(userTable);
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to main panel
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        // Add button listener
        addButton.addActionListener(e -> bus.handleAdd());
        
        // Edit button listener
        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                bus.handleEdit(userId);
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một người dùng để sửa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Delete button listener
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                String username = userTable.getValueAt(selectedRow, 1).toString();
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Bạn có chắc chắn muốn ngưng hoạt động người dùng '" + username + "'?", 
                    "Xác nhận ngưng hoạt động", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    bus.handleDelete(userId);
                }
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Vui lòng chọn một người dùng để ngưng hoạt động", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Save button listener
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            Integer roleId = null;
            String selectedRole = (String) roleComboBox.getSelectedItem();
            if (selectedRole != null && !selectedRole.isEmpty()) {
                roleId = Integer.parseInt(selectedRole.split(":")[0]);
            }
            boolean status = statusCheckBox.isSelected();
            bus.handleSave(username, password, roleId, status);
        });
        
        // Clear button listener
        clearButton.addActionListener(e -> bus.handleClear());
        
        // Search button listener
        searchButton.addActionListener(e -> applyFilters());
        
        // Filter role combobox listener
        filterRoleComboBox.addActionListener(e -> applyFilters());
        
        // Filter status combobox listener
        filterStatusComboBox.addActionListener(e -> applyFilters());
    }
    
    private void loadRoleComboBox(JComboBox<String> comboBox) {
        try {
            List<Role> roles = roleDAO.getAllRoles();
            for (Role role : roles) {
                comboBox.addItem(role.getId() + ": " + role.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách vai trò: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadUserData() {
        try {
            List<User> users = userDAO.getAllUsers();
            tableModel.setRowCount(0);
            
            for (User user : users) {
                String roleName = "N/A";
                if (user.getRoleId() != null) {
                    Role role = roleDAO.getRoleById(user.getRoleId());
                    if (role != null) {
                        roleName = role.getName();
                    }
                }
                
                Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    roleName,
                    user.isStatus() ? STATUS_ACTIVE : STATUS_INACTIVE
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu người dùng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setUserFormData(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        statusCheckBox.setSelected(user.isStatus());
        
        if (user.getRoleId() != null) {
            for (int i = 0; i < roleComboBox.getItemCount(); i++) {
                String item = roleComboBox.getItemAt(i);
                if (item.startsWith(user.getRoleId() + ":")) {
                    roleComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    public void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        statusCheckBox.setSelected(true);
        if (roleComboBox.getItemCount() > 0) {
            roleComboBox.setSelectedIndex(0);
        }
        isEditing = false;
        currentUser = null;
    }
    
    private void applyFilters() {
        try {
            String searchKeyword = searchField.getText().trim();
            String selectedRole = (String) filterRoleComboBox.getSelectedItem();
            String selectedStatus = (String) filterStatusComboBox.getSelectedItem();
            
            List<User> allUsers = userDAO.getAllUsers();
            tableModel.setRowCount(0);
            
            for (User user : allUsers) {
                // Filter by search keyword
                if (!searchKeyword.isEmpty() && !user.getUsername().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    continue;
                }
                
                // Filter by role
                if (!"Tất cả vai trò".equals(selectedRole)) {
                    // Extract role name from the selected item (format: "ID: Name")
                    String selectedRoleName = selectedRole.substring(selectedRole.indexOf(":") + 2);
                    Role role = roleDAO.getRoleById(user.getRoleId());
                    if (role == null || !role.getName().equals(selectedRoleName)) {
                        continue;
                    }
                }
                
                // Filter by status
                if (!selectedStatus.equals("Tất cả")) {
                    boolean isActive = selectedStatus.equals(STATUS_ACTIVE);
                    if (user.isStatus() != isActive) {
                        continue;
                    }
                }
                
                // Display user
                String roleName = "N/A";
                if (user.getRoleId() != null) {
                    Role role = roleDAO.getRoleById(user.getRoleId());
                    if (role != null) {
                        roleName = role.getName();
                    }
                }
                
                Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    roleName,
                    user.isStatus() ? STATUS_ACTIVE : STATUS_INACTIVE
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Getters and setters
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    public boolean isEditing() {
        return isEditing;
    }
    
    public void setEditing(boolean editing) {
        isEditing = editing;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void updateTableWithUsers(List<User> users) {
        try {
            tableModel.setRowCount(0);
            
            for (User user : users) {
                String roleName = "N/A";
                if (user.getRoleId() != null) {
                    Role role = roleDAO.getRoleById(user.getRoleId());
                    if (role != null) {
                        roleName = role.getName();
                    }
                }
                
                Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    roleName,
                    user.isStatus() ? STATUS_ACTIVE : STATUS_INACTIVE
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu người dùng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    