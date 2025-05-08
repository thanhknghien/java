package com.bookstore.gui.panel;

import com.bookstore.controller.UserManagementController;
import com.bookstore.dao.RoleDAO;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.PanelCover;
import com.bookstore.gui.component.TextField;
import com.bookstore.model.Role;
import com.bookstore.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementPanel extends PanelCover {
    private JSplitPane jsplitpanel;
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
    
    // Controller
    private UserManagementController controller;
    
    // Current user for editing
    private User currentUser;
    private boolean isEditing = false;
    
    // Constants for status
    private static final String STATUS_ACTIVE = "Hoạt động";
    private static final String STATUS_INACTIVE = "Ngưng hoạt động";
    
    private boolean isAdding = false;
    
    public UserManagementPanel() throws SQLException {
        super();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadInitialData();
        
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
    }
    
    private void initializeComponents() throws SQLException {
        // Initialize panels
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2));
        
        westPanel = new JPanel();
        eastPanel = new JPanel();
        northPanel.add(westPanel);
        northPanel.add(eastPanel);
        northPanel.setSize(1180, 650);
        southPanel = new JPanel();
        
        // Initialize north panel components
        addButton = new Button("Thêm");
        editButton = new Button("Sửa");
        deleteButton = new Button("Xóa");
        filterRoleComboBox = new JComboBox<>();
        filterRoleComboBox.setPreferredSize(new Dimension(150, 30));
        filterStatusComboBox = new JComboBox<>(new String[]{"Tất cả trang thái", STATUS_ACTIVE, STATUS_INACTIVE});
        filterStatusComboBox.setPreferredSize(new Dimension(150, 30));
        searchField = new TextField();
        searchField.setPlaceholder("Tìm kiếm...");
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
        
        // Make the table non-editable
        userTable.setDefaultEditor(Object.class, null);
        
        // Initialize controller last, after all UI components are created
        controller = new UserManagementController(this);
        
        // Load role data
        controller.loadRoleData(roleComboBox);
        controller.loadRoleData(filterRoleComboBox);
        filterRoleComboBox.setSelectedIndex(0);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Setup west panel (left side of north panel)
        westPanel.setLayout(new BorderLayout(10, 5));
        westPanel.setPreferredSize(new Dimension(590, 250));
        westPanel.setMinimumSize(new Dimension(590, 250));
        
        // Setup east panel (right side of north panel)
        eastPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));
        eastPanel.setPreferredSize(new Dimension(590, 250));
        eastPanel.setMinimumSize(new Dimension(590, 250));
        
        // Add components to west panel (left side)
        eastPanel.add(addButton);
        eastPanel.add(editButton);
        eastPanel.add(deleteButton);
        
        // Add components to east panel (right side)
        
        eastPanel.add(filterRoleComboBox);
        eastPanel.add(Box.createHorizontalStrut(10));
        eastPanel.add(filterStatusComboBox);
        eastPanel.add(Box.createHorizontalStrut(20));
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
        //formPanel.add(statusCheckBox, gbc);
        
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
        southPanel.setPreferredSize(new Dimension(1180, 450));
        southPanel.setMinimumSize(new Dimension(1180, 800));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(1180, 450));
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add all panels to main panel
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
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
                controller.handleEdit(userId);
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
                    controller.handleDelete(userId);
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
            
            if (isAdding) {
                controller.handleAdd(username, password, roleId, status);
            } else if (isEditing) {
                controller.handleUpdate(currentUser.getId(), username, password, roleId, status);
            }
        });
        
        // Clear button listener
        clearButton.addActionListener(e -> {
            clearForm();
            resetFormState();
        });
        
        // Search button listener
        searchButton.addActionListener(e -> controller.handleSearch(searchField.getText().trim()));
        
        // Filter role combobox listener
        filterRoleComboBox.addActionListener(e -> controller.handleFilterRole(filterRoleComboBox.getSelectedItem()));
        
        // Filter status combobox listener
        filterStatusComboBox.addActionListener(e -> controller.handleFilterStatus(filterStatusComboBox.getSelectedItem()));
    }
    
    private void loadInitialData() {
        controller.loadUserData();
    }
    
    public void updateTableWithUsers(List<User> users) {
        try {
            tableModel.setRowCount(0);
            for (User user : users) {  
                String roleName = controller.getRoleName(user.getRoleId());
                Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    roleName != null ? roleName : "",
                    user.isStatus() ? STATUS_ACTIVE : STATUS_INACTIVE
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khi cập nhật bảng người dùng: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
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
    
    /**
     * Cập nhật hiển thị các nút dựa trên quyền của người dùng
     * @param canAdd Quyền thêm
     * @param canEdit Quyền sửa
     * @param canDelete Quyền xóa
     * @param canView Quyền xem
     */
    public void updateButtonsVisibility(boolean canAdd, boolean canEdit, boolean canDelete, boolean canView) {
        // Kiểm tra null trước khi truy cập các thành phần
        if (addButton != null) {
            // Cập nhật hiển thị nút thêm
            addButton.setEnabled(canAdd);
            addButton.setVisible(canAdd);
        }
        
        if (editButton != null) {
            // Cập nhật hiển thị nút sửa
            editButton.setEnabled(canEdit);
            editButton.setVisible(canEdit);
        }
        
        if (deleteButton != null) {
            // Cập nhật hiển thị nút xóa
            deleteButton.setEnabled(canDelete);
            deleteButton.setVisible(canDelete);
        }
        
        // Cập nhật hiển thị các thành phần tìm kiếm và lọc
        if (searchField != null) searchField.setEnabled(canView);
        if (searchButton != null) searchButton.setEnabled(canView);
        if (filterRoleComboBox != null) filterRoleComboBox.setEnabled(canView);
        if (filterStatusComboBox != null) filterStatusComboBox.setEnabled(canView);
        
        // Nếu không có quyền xem, ẩn bảng dữ liệu
        if (userTable != null) userTable.setEnabled(canView);
    }
}
    