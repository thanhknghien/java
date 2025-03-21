package com.bookstore.view.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
//import javax.swing.table.DefaultCellEditor;
import java.awt.*;
import java.awt.event.*;
import com.bookstore.utils.Status;  // Enum Status được định nghĩa trong com.bookstore.model
import com.bookstore.utils.Status;

public class AccountManagementPanel extends JPanel {
    
    // --- Phần North ---
    private JButton addButton;
    private JMenuBar menuBarFilter;
    private JMenu menuFilter;
    private JMenuItem filterNhanVien, filterKhachHang;
    private JButton searchButton;
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchExecuteButton;
    
    // --- Phần CENTER – Left: Form nhập thông tin để thêm tài khoản ---
    private JPanel addFormPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JComboBox<String> comboRole;
    private JPanel statusPanel;
    private JComboBox<Status> comboStatus;
    private JButton confirmAddButton;
    
    // --- Phần CENTER – Right: Bảng hiển thị danh sách tài khoản ---
    private JTable accountTable;
    private DefaultTableModel tableModel;
    
    public AccountManagementPanel() {
        setLayout(new BorderLayout());
        initNorthComponents();
        initCenterComponents();
    }
    
    // Khởi tạo các thành phần phần North
    private void initNorthComponents() {
        // Button "Thêm"
        addButton = new JButton("Thêm");
        
        // Menu Filter
        menuBarFilter = new JMenuBar();
        menuBarFilter.setBorderPainted(false);
        menuBarFilter.setOpaque(false);
        menuFilter = new JMenu("Filter");
        filterNhanVien = new JMenuItem("Tài khoản nhân viên");
        filterKhachHang = new JMenuItem("Tài khoản khách hàng");
        menuFilter.add(filterNhanVien);
        menuFilter.add(filterKhachHang);
        menuBarFilter.add(menuFilter);
        
        // Button "Search" và panel nhập từ khóa
        searchButton = new JButton("Search");
        searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchExecuteButton = new JButton("Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchExecuteButton);
        searchPanel.setVisible(false);
        
        // Sự kiện bật/ẩn panel tìm kiếm khi nhấn button "Search"
        searchButton.addActionListener(e -> {
            searchPanel.setVisible(!searchPanel.isVisible());
            revalidate();
            repaint();
        });
    }
    
    // Tạo panel North kết hợp
    private JPanel createNorthPanel() {
        JPanel north = new JPanel(new BorderLayout());
        JPanel leftNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftNorth.add(addButton);
        leftNorth.add(menuBarFilter);
        leftNorth.add(searchButton);
        north.add(leftNorth, BorderLayout.WEST);
        north.add(searchPanel, BorderLayout.EAST);
        return north;
    }
    
    // Khởi tạo các thành phần trong phần Center
    private void initCenterComponents() {
        // Form thêm tài khoản (bên trái)
        initAddFormPanel();
        // Bảng danh sách tài khoản (bên phải)
        initTablePanel();
    }
    
    // Tạo form nhập thông tin để thêm tài khoản
    private void initAddFormPanel() {
        addFormPanel = new JPanel(new GridBagLayout());
        addFormPanel.setBorder(BorderFactory.createTitledBorder("Thêm Tài Khoản"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Hàng 0: Tên người dùng
        gbc.gridx = 0; gbc.gridy = 0;
        addFormPanel.add(new JLabel("Tên người dùng:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        addFormPanel.add(txtUsername, gbc);
        
        // Hàng 1: Mật khẩu
        gbc.gridx = 0; gbc.gridy = 1;
        addFormPanel.add(new JLabel("Mật khẩu:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        addFormPanel.add(txtPassword, gbc);
        
        // Hàng 2: Họ và tên
        gbc.gridx = 0; gbc.gridy = 2;
        addFormPanel.add(new JLabel("Họ và tên:"), gbc);
        txtFullName = new JTextField(15);
        gbc.gridx = 1;
        addFormPanel.add(txtFullName, gbc);
        
        // Hàng 3: Email
        gbc.gridx = 0; gbc.gridy = 3;
        addFormPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        addFormPanel.add(txtEmail, gbc);
        
        // Hàng 4: Phone
        gbc.gridx = 0; gbc.gridy = 4;
        addFormPanel.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(15);
        gbc.gridx = 1;
        addFormPanel.add(txtPhone, gbc);
        
        // Hàng 5: Address
        gbc.gridx = 0; gbc.gridy = 5;
        addFormPanel.add(new JLabel("Address:"), gbc);
        txtAddress = new JTextField(15);
        gbc.gridx = 1;
        addFormPanel.add(txtAddress, gbc);
        
        // Hàng 6: RoleID
        gbc.gridx = 0; gbc.gridy = 6;
        addFormPanel.add(new JLabel("RoleID:"), gbc);
        String[] roles = {"Khách hàng (1)", "Nhân viên (2)"};
        comboRole = new JComboBox<>(roles);
        gbc.gridx = 1;
        addFormPanel.add(comboRole, gbc);
        
        // Hàng 7: Status (chỉ nếu chọn Nhân viên)
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(Status.values());
        statusPanel.add(comboStatus);
        statusPanel.setVisible(false);
        addFormPanel.add(statusPanel, gbc);
        
        // Sự kiện thay đổi Role: nếu "Nhân viên" thì hiện panel Status
        comboRole.addActionListener(e -> {
            boolean isEmp = comboRole.getSelectedIndex() == 1;
            
            statusPanel.setVisible(isEmp);
            addFormPanel.revalidate();
            addFormPanel.repaint();
        });
        
        // Hàng 8: Button "Xác nhận thêm"
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        confirmAddButton = new JButton("Xác nhận thêm");
        addFormPanel.add(confirmAddButton, gbc);
        
        // Sự kiện xác nhận thêm (có thể gọi Controller/DAO)
        confirmAddButton.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String fullName = txtFullName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            int roleId = comboRole.getSelectedIndex() == 0 ? 1 : 2;
            Status status = (roleId == 2) ? (Status) comboStatus.getSelectedItem() : null;
            // Demo: Hiển thị thông báo
            JOptionPane.showMessageDialog(this, "Thêm tài khoản: " + username + "\nRole: " + roleId + (status != null ? "\nStatus: " + status : ""));
            
            // Sau khi thêm, bạn có thể reset form hoặc thêm dòng mới vào table
        });
    }
    
    // Tạo bảng hiển thị danh sách tài khoản (bên phải)
    private JPanel initTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách tài khoản"));
        String[] columnNames = {"STT", "Tên người dùng", "Họ và tên", "Mật khẩu", "Email", "Phone", "Address", "Status", "Xóa"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Cho phép chỉnh sửa chỉ ở cột "Xóa" để hiển thị nút xóa
                return column == 8;
            }
        };
        accountTable = new JTable(tableModel);
        
        // Thêm custom renderer và editor cho cột "Xóa"
        accountTable.getColumn("Xóa").setCellRenderer(new ButtonRenderer());
        accountTable.getColumn("Xóa").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(accountTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Tải dữ liệu mẫu
        loadDummyData();
        // Thêm sự kiện double-click để sửa: Controller sẽ gắn thêm MouseListener
        accountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && accountTable.getSelectedRow() != -1) {
                    int row = accountTable.getSelectedRow();
                    Object[] rowData = new Object[tableModel.getColumnCount()];
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        rowData[i] = tableModel.getValueAt(row, i);
                    }
                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(AccountManagementPanel.this);
                    // Mở dialog chỉnh sửa (EditAccountDialog ở package com.bookstore.view.dialog)
                    com.bookstore.view.dialog.EditAccountDialog editDialog = new com.bookstore.view.dialog.EditAccountDialog(parent, rowData);
                    editDialog.setVisible(true);
                    // Sau khi sửa, bạn có thể cập nhật lại dòng tương ứng trong bảng
                }
            }
        });
        // Bạn có thể bổ sung các sự kiện khác theo yêu cầu.
        
        // Trả về panel chứa bảng
        tablePanel.validate();
        return tablePanel;
    }
    
    // Tạo panel Center bằng cách sử dụng JSplitPane chia làm 2
    public JPanel createCenterPanel() {
        JPanel leftPanel = addFormPanel;
        JPanel rightPanel = initTablePanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.4);
        JPanel center = new JPanel(new BorderLayout());
        center.add(splitPane, BorderLayout.CENTER);
        return center;
    }
    
    // Hàm giả lập dữ liệu mẫu: STT, username, fullName, password, email, phone, address, status, "Xóa"
    private void loadDummyData() {
        Object[][] dummy = {
            {1, "user1", "Nguyen Van A", "pwd1", "a@gmail.com", "0123456789", "Address1", "ACTIVE", "Xóa"},
            {2, "user2", "Tran Thi B", "pwd2", "b@gmail.com", "0987654321", "Address2", "INACTIVE", "Xóa"}
        };
        for (Object[] row : dummy) {
            tableModel.addRow(row);
        }
    }
    
    // --- Custom Renderer cho cột "Xóa" ---
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton deleteButton;
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = new JButton(new ImageIcon("delete_icon.png")); // đảm bảo có file ảnh "delete_icon.png"
            add(deleteButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // --- Custom Editor cho cột "Xóa" ---
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton deleteButton;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = new JButton(new ImageIcon("delete_icon.png"));
            panel.add(deleteButton);
            deleteButton.addActionListener(e -> {
                int row = accountTable.getEditingRow();
                deleteAccount(row);
                fireEditingStopped();
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Xóa";
        }
    }
    
    // Xử lý xóa tài khoản từ bảng
    private void deleteAccount(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }
    
    // Các getter cho các đối tượng trong view (để Controller sử dụng)
    public JButton getAddButton() { return addButton; }
    public JButton getSearchExecuteButton() { return searchExecuteButton; }
    public JTextField getSearchField() { return searchField; }
    public JTable getAccountTable() { return accountTable; }
    public void addAccountToTable(Object[] rowData) { tableModel.addRow(rowData); }
    
    // --- Hàm main để test độc lập ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Tài Khoản");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            AccountManagementPanel amp = new AccountManagementPanel();
            frame.add(amp.createNorthPanel(), BorderLayout.NORTH);
            frame.add(amp.createCenterPanel(), BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}