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
    private JSplitPane jsplitpanel ;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel westPanel;
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
    
    private boolean isAdding = false;
    
    public UserManagementPanel() {
        super();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadInitialData();
        
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
    }
    
    private void initializeComponents() {
        // Initialize DAOs and BUS
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
        bus = new UserManagementBUS(this);

        // Initialize panels
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2)); // Divide horizontally into 2 equal parts
        
        westPanel = new JPanel();
        eastPanel = new JPanel();
        northPanel.add(westPanel);
        northPanel.add(eastPanel);
        
        southPanel = new JPanel();
        
        // Initialize north panel components with adjusted sizes
        addButton = new Button("Thêm");
        editButton = new Button("Sửa");
        deleteButton = new Button("Xóa");
        filterRoleComboBox = new JComboBox<>();
        filterRoleComboBox.setPreferredSize(new Dimension(150, 30));
        filterStatusComboBox = new JComboBox<>(new String[]{"Tất cả", STATUS_ACTIVE, STATUS_INACTIVE});
        filterStatusComboBox.setPreferredSize(new Dimension(150, 30));
        searchField = new TextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchButton = new Button("Tìm kiếm");
        
        // Initialize center panel components
        usernameField = new TextField();
        passwordField = new JPasswordField();
        roleComboBox = new JComboBox<>();
        statusCheckBox = new JCheckBox("Hoạt động", true);
        saveButton = new Button("Lưu");
        clearButton = new Button("Xóa form");
        
        // Initialize table components
        String[] columnNames = {"ID", "Tên đăng nhập", "Mật khẩu", "Vai trò", "Trạng thái"};
        userTable = new CustomTable(columnNames);
        tableModel = (DefaultTableModel) userTable.getModel();
        tableModel.setRowCount(0);
        
        // Load role data
        loadRoleComboBox(roleComboBox);
        loadRoleComboBox(filterRoleComboBox);
        filterRoleComboBox.insertItemAt("Tất cả vai trò", 0);
        filterRoleComboBox.setSelectedIndex(0);
        
        // Make the table non-editable
        userTable.setDefaultEditor(Object.class, null);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Setup west panel (left side of north panel)
        westPanel.setLayout(new BorderLayout(10, 5));
       
        westPanel.setPreferredSize(new Dimension(590, 250)); // Increased height
        westPanel.setMinimumSize(new Dimension(590, 250)); // Increased height
        
        // Setup east panel (right side of north panel)
        eastPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));
        eastPanel.setPreferredSize(new Dimension(590, 250)); // Increased height
        eastPanel.setMinimumSize(new Dimension(590, 250)); // Increased height
        
        // Add components to west panel (left side)
        eastPanel.add(addButton);
        eastPanel.add(editButton);
        eastPanel.add(deleteButton);
        
        // Add components to east panel (right side)
        eastPanel.add(new JLabel("Vai trò:"));
        eastPanel.add(filterRoleComboBox);
        eastPanel.add(Box.createHorizontalStrut(10));
        eastPanel.add(new JLabel("Trạng thái:"));
        eastPanel.add(filterStatusComboBox);
        eastPanel.add(Box.createHorizontalStrut(20));
        eastPanel.add(new JLabel("Tìm kiếm:"));
        eastPanel.add(searchField);
        eastPanel.add(searchButton);
        
        // Setup form components in westPanel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin người dùng"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roleComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(statusCheckBox, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Add form panel to westPanel in the center
        westPanel.add(formPanel, BorderLayout.CENTER);
        
        // Setup south panel (table section)
        southPanel.setLayout(new BorderLayout());
        southPanel.setBorder(BorderFactory.createTitledBorder("Danh sách người dùng"));
        southPanel.setPreferredSize(new Dimension(1180, 450)); // Reduced height to make room for top panel
        southPanel.setMinimumSize(new Dimension(1180, 450)); // Reduced height to make room for top panel
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(1180, 450)); // Adjusted to match southPanel
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add all panels to main panel
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        // Add button listener
        addButton.addActionListener(e -> {
            isAdding = true;
            isEditing = false;
            clearForm();
            enableForm(true);
            saveButton.setEnabled(true);
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            userTable.setEnabled(false);
        });
        
        // Edit button listener
        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                isAdding = false;
                isEditing = true;
                int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                bus.handleEdit(userId);
                enableForm(true);
                saveButton.setEnabled(true);
                addButton.setEnabled(false);
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                userTable.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Vui lòng chọn một người dùng để sửa", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
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
            if (isAdding) {
                // Xử lý thêm mới
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Integer roleId = null;
                String selectedRole = (String) roleComboBox.getSelectedItem();
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    roleId = Integer.parseInt(selectedRole.split(":")[0]);
                }
                boolean status = statusCheckBox.isSelected();
                
                try {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setRoleId(roleId);
                    newUser.setStatus(status);
                    
                    userDAO.addUser(newUser);
                    loadUserData(); // Tải lại dữ liệu
                    
                    JOptionPane.showMessageDialog(this,
                        "Thêm người dùng thành công",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reset form và trạng thái
                    resetFormState();
                } catch (SQLException ex) {
                    handleError("Lỗi khi thêm người dùng", ex);
                }
            } else if (isEditing) {
                // Xử lý cập nhật (giữ nguyên code cũ)
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                Integer roleId = null;
                String selectedRole = (String) roleComboBox.getSelectedItem();
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    roleId = Integer.parseInt(selectedRole.split(":")[0]);
                }
                boolean status = statusCheckBox.isSelected();
                bus.handleSave(username, password, roleId, status);
            }
        });
        
        // Clear button listener
        clearButton.addActionListener(e -> {
            clearForm();
            resetFormState();
        });
        
        // Search button listener
        searchButton.addActionListener(e -> applyFilters());
        
        // Filter role combobox listener
        filterRoleComboBox.addActionListener(e -> applyFilters());
        
        // Filter status combobox listener
        filterStatusComboBox.addActionListener(e -> applyFilters());
    }
    
    private void loadInitialData() {
        loadUserData();
    }
    
    private void loadRoleComboBox(JComboBox<String> comboBox) {
        try {
            List<Role> roles = roleDAO.getAllRoles();
            for (Role role : roles) {
                comboBox.addItem(role.getId() + ": " + role.getName());
            }
        } catch (SQLException e) {
            handleError("Lỗi khi tải danh sách vai trò", e);
        }
    }
    
    public void loadUserData() {
        try {
            List<User> users = userDAO.getAllUsers();
            updateTableWithUsers(users);
        } catch (SQLException e) {
            handleError("Lỗi khi tải dữ liệu người dùng", e);
        }
    }
    
    public void updateTableWithUsers(List<User> users) {
        tableModel.setRowCount(0);
        for (User user : users) {
            String roleName = "N/A";
            if (user.getRoleId() != null) {
                try {
                    Role role = roleDAO.getRoleById(user.getRoleId());
                    if (role != null) {
                        roleName = role.getName();
                    }
                } catch (SQLException e) {
                    // Log error but continue processing other users
                    System.err.println("Lỗi khi lấy thông tin vai trò: " + e.getMessage());
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
        } catch (SQLException e) {
            handleError("Lỗi khi lọc dữ liệu", e);
        }
    }
    
    private void handleError(String title, Exception e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Đã xảy ra lỗi không xác định";
        }
        JOptionPane.showMessageDialog(this, 
            title + ": " + message, 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
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
    
    private void enableForm(boolean enable) {
        usernameField.setEnabled(enable);
        passwordField.setEnabled(enable);
        roleComboBox.setEnabled(enable);
        statusCheckBox.setEnabled(enable);
        saveButton.setEnabled(enable);
        clearButton.setEnabled(enable);
    }
    
    private void resetFormState() {
        isAdding = false;
        isEditing = false;
        clearForm();
        enableForm(false);
        saveButton.setEnabled(false);
        addButton.setEnabled(true);
        editButton.setEnabled(true);
        deleteButton.setEnabled(true);
        userTable.setEnabled(true);
    }
}
    