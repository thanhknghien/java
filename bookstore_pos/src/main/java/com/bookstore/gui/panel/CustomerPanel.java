package com.bookstore.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.bookstore.controller.CustomerController;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.Customer;

public class CustomerPanel extends JPanel {
    private CustomerController controller;

    private TextField customerID;
    private TextField fullName;
    private TextField phoneNumber;
    private TextField points;
    private TextField search;
    private Button btnAdd, btnUpdate, btnDelete, btnSearch, btnReset;

    private ButtonGroup searchGroup;
    private JRadioButton searchById, searchByName, searchByPhone, searchByPoints;
    private TextField pointStart, pointEnd; 

    private CustomTable customerTable;
    private DefaultTableModel customerTableModel;

    public CustomerPanel() {
        controller = new CustomerController(); // Controller xử lý logic
        initializeUI(); // Xây dựng giao diện
        loadCustomerData(); // Nạp dữ liệu khách hàng
    }
    private void initializeUI() {
        this.setLayout(new BorderLayout());
    
        // Left Panel (Nhập thông tin)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(600, 200));
        Border thickBorder = new LineBorder(ColorScheme.BORDER, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Thông tin khách hàng"));
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(10, 20, 5, 5);
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        gbcL.fill = GridBagConstraints.HORIZONTAL;
    
        customerID = new TextField();
        customerID.setPlaceholder("ID: [Tự động tăng]");
        customerID.setPreferredSize(new Dimension(300, 25));
        leftPanel.add(new JLabel("ID:"), gbcL);
        gbcL.gridx = 1;
        leftPanel.add(customerID, gbcL);
    
        gbcL.gridx = 2;
        btnAdd = new Button("Thêm");
        btnAdd.setPreferredSize(new Dimension(100, 25));
        leftPanel.add(btnAdd, gbcL);
        btnAdd.addActionListener(e -> addCustomer());
    
        gbcL.gridx = 0;
        gbcL.gridy++;
        fullName = new TextField();
        fullName.setPlaceholder("Nhập họ và tên...");
        leftPanel.add(new JLabel("Họ và tên:"), gbcL);
        gbcL.gridx = 1;
        leftPanel.add(fullName, gbcL);
    
        gbcL.gridx = 2;
        btnUpdate = new Button("Sửa");
        btnUpdate.setPreferredSize(new Dimension(100, 25));
        leftPanel.add(btnUpdate, gbcL);
        btnUpdate.addActionListener(e -> updateCustomer());
    
        gbcL.gridy++;
        gbcL.gridx = 0;
        phoneNumber = new TextField();
        phoneNumber.setPlaceholder("Nhập số điện thoại...");
        leftPanel.add(new JLabel("SĐT:"), gbcL);
        gbcL.gridx = 1;
        leftPanel.add(phoneNumber, gbcL);
    
        gbcL.gridx = 2;
        btnDelete = new Button("Xóa");
        btnDelete.setPreferredSize(new Dimension(100, 25));
        leftPanel.add(btnDelete, gbcL);
        btnDelete.addActionListener(e -> deleteCustomer());
    
        gbcL.gridy++;
        gbcL.gridx = 0;
        points = new TextField();
        points.setPlaceholder("Nhập điểm...");
        leftPanel.add(new JLabel("POINT:"), gbcL);
        gbcL.gridx = 1;
        leftPanel.add(points, gbcL);
    
        gbcL.gridx = 2;
        btnReset = new Button("Làm mới");
        btnReset.setPreferredSize(new Dimension(100, 25));
        leftPanel.add(btnReset, gbcL);
        btnReset.addActionListener(e -> clearTextField());
    
        // Right Panel (Tìm kiếm và chức năng)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(580, 200));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
    
        JPanel northRPanel = new JPanel();
        northRPanel.setPreferredSize(new Dimension(690, 50));
    
        search = new TextField();
        search.setPlaceholder("Search...");
        search.setPreferredSize(new Dimension(450, 30));
        northRPanel.add(search, BorderLayout.CENTER);
        btnSearch = new Button("🔍");
        btnSearch.setPreferredSize(new Dimension(100, 30));
        northRPanel.add(btnSearch, BorderLayout.SOUTH);
    
        rightPanel.add(northRPanel, BorderLayout.NORTH);
    
        // South Panel (Radio buttons và tìm kiếm nâng cao)
        JPanel southRPanel = new JPanel(new GridBagLayout());
        southRPanel.setBorder(BorderFactory.createTitledBorder("Lựa chọn tìm kiếm"));
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(10, 10, 5, 5);
        gbcR.gridx = 0;
        gbcR.gridy = 0;
    
        // Radio Buttons Group
        searchGroup = new ButtonGroup();
        searchById = new JRadioButton("Tìm theo ID");
        searchByName = new JRadioButton("Tìm theo Tên");
        searchByPhone = new JRadioButton("Tìm theo SĐT");
        searchByPoints = new JRadioButton("Tìm theo Point");
    
        searchGroup.add(searchById);
        searchGroup.add(searchByName);
        searchGroup.add(searchByPhone);
        searchGroup.add(searchByPoints);
    
        southRPanel.add(searchById, gbcR);
        gbcR.gridx++;
        southRPanel.add(searchByName, gbcR);
        gbcR.gridx++;
        southRPanel.add(searchByPhone, gbcR);
        gbcR.gridx++;
        southRPanel.add(searchByPoints, gbcR);
    
        // TextFields for "Point Range"
        gbcR.gridy++;
        gbcR.gridx = 0;
        JLabel lblPointStart = new JLabel("Điểm từ:");
        southRPanel.add(lblPointStart, gbcR);
        gbcR.gridx = 1;
        pointStart = new TextField();
        pointStart.setPreferredSize(new Dimension(150, 25));
        southRPanel.add(pointStart, gbcR);
    
        gbcR.gridx = 2;
        JLabel lblPointEnd = new JLabel("Đến:");
        southRPanel.add(lblPointEnd, gbcR);
        gbcR.gridx = 3;
        pointEnd = new TextField();
        pointEnd.setPreferredSize(new Dimension(150, 25));
        southRPanel.add(pointEnd, gbcR);
    
        // Hiển thị TextFields 
        lblPointStart.setVisible(true);
        lblPointEnd.setVisible(true);
        pointStart.setVisible(true);
        pointEnd.setVisible(true);
        
        searchById.setSelected(true);
        pointStart.setBackground(new Color(224, 224, 224));
        pointEnd.setBackground(new Color(224, 224, 224));

        searchByPoints.addActionListener(e -> showPointFields(pointStart, pointEnd));
    
        // Ẩn TextFields khi chọn các lựa chọn khác
        searchById.addActionListener(e -> hidePointFields(lblPointStart, lblPointEnd, pointStart, pointEnd));
        searchByName.addActionListener(e -> hidePointFields(lblPointStart, lblPointEnd, pointStart, pointEnd));
        searchByPhone.addActionListener(e -> hidePointFields(lblPointStart, lblPointEnd, pointStart, pointEnd));
    
        rightPanel.add(southRPanel, BorderLayout.SOUTH);
    
        // Center Panel (Bảng dữ liệu khách hàng)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columns = {"ID", "Họ và tên", "SĐT", "POINT"};
        customerTable = new CustomTable(columns);
        customerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        customerTable.setModel(customerTableModel);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && !evt.isConsumed()) {
                    int row = customerTable.getSelectedRow();
                    if (row >= 0) {
                        fillTextFieldsFromSelectedRow(row);
                    }
                }
            }
        });
        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setPreferredSize(new Dimension(1000, 400));
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(250);
        centerPanel.add(customerScrollPane, BorderLayout.CENTER);
    
        // Thêm các panel vào giao diện chính
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(leftPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.EAST);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }
    
    // Hàm ẩn các TextField khi không chọn "Tìm theo Điểm"
    private void hidePointFields(JLabel lblPointStart, JLabel lblPointEnd, TextField pointStart, TextField pointEnd) {
        lblPointStart.setFocusable(false);
        lblPointEnd.setFocusable(false);
        pointStart.setFocusable(false);
        pointEnd.setFocusable(false);
        pointStart.setBackground(new Color(224, 224, 224));
        pointEnd.setBackground(new Color(224, 224, 224));
    }

    private void showPointFields(TextField pointStart, TextField pointEnd){
        pointStart.setFocusable(true);
        pointEnd.setFocusable(true);
        pointStart.setBackground(new Color(255, 255, 255));
        pointEnd.setBackground(new Color(255, 255, 255));
    }
    

    private void loadCustomerData() {
        customerTableModel.setRowCount(0);
        List<Customer> customers = controller.getAllCustomers();
        for (Customer customer : customers) {
            customerTableModel.addRow(new Object[]{customer.getId(), customer.getFullName(), customer.getPhoneNumber(), customer.getPoints()});
        }
    }
    
    private void fillTextFieldsFromSelectedRow(int row) {
        Object idValue = customerTableModel.getValueAt(row, 0); // Lấy ID từ cột đầu tiên
        Object nameValue = customerTableModel.getValueAt(row, 1); // Lấy tên từ cột thứ hai
        Object phoneValue = customerTableModel.getValueAt(row, 2); // Lấy SĐT từ cột thứ ba
        Object pointsValue = customerTableModel.getValueAt(row, 3); // Lấy điểm từ cột thứ tư
    
        customerID.setText(idValue.toString());
        fullName.setText(nameValue.toString());
        phoneNumber.setText(phoneValue.toString());
        points.setText(pointsValue.toString());
    }
    
    private void addCustomer() {
        String customerName = fullName.getText().trim();
        String customerPhone = phoneNumber.getText().trim();
        String customerPointsText = points.getText().trim();
    
        try {
            int customerPoints = customerPointsText.isEmpty() ? 0 : Integer.parseInt(customerPointsText); // Điểm mặc định là 0 nếu để trống
            Customer newCustomer = new Customer(0, customerName, customerPhone, customerPoints);
            boolean success = controller.addCustomer(newCustomer);
            if (success) {
                customerTableModel.addRow(new Object[]{newCustomer.getId(), newCustomer.getFullName(), newCustomer.getPhoneNumber(), newCustomer.getPoints()});
                fullName.setText("");
                phoneNumber.setText("");
                points.setText("");
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Điểm phải là một số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        String idText = customerID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            int id = Integer.parseInt(idText);
            boolean success = controller.deleteCustomer(id);
            if (success) {
                customerTableModel.setRowCount(0);
                loadCustomerData();
                customerID.setText("");
                fullName.setText("");
                phoneNumber.setText("");
                points.setText("");
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateCustomer() {
        String idText = customerID.getText().trim();
        String customerName = fullName.getText().trim();
        String customerPhone = phoneNumber.getText().trim();
        String customerPointsText = points.getText().trim();
    
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (customerName.isEmpty() || customerPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            int id = Integer.parseInt(idText);
            int customerPoints = customerPointsText.isEmpty() ? 0 : Integer.parseInt(customerPointsText);
            Customer updatedCustomer = new Customer(id, customerName, customerPhone, customerPoints);
    
            boolean success = controller.updateCustomer(updatedCustomer);
            if (success) {
                loadCustomerData();
                customerID.setText("");
                fullName.setText("");
                phoneNumber.setText("");
                points.setText("");
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID hoặc Điểm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void clearTextField(){
        customerID.setText("");
        fullName.setText("");
        phoneNumber.setText("");
        points.setText("");

        search.setText("");
        searchById.setSelected(true);
        pointStart.setBackground(new Color(224, 224, 224));
        pointEnd.setBackground(new Color(224, 224, 224));

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý khách hàng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new CustomerPanel());
            frame.setVisible(true);
        });
    }
}
