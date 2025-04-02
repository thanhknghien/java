package com.bookstore.gui.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.PanelCover;

public class POSGUI extends JFrame {
    // Panels chính
    private PanelCover mainPanel;
    private PanelCover leftPanel;      // Panel danh mục
    private PanelCover centerPanel;    // Panel sản phẩm
    private PanelCover rightPanel;     // Panel giỏ hàng
    
    // Components cho panel danh mục
    private JList<String> categoryList;
    private DefaultListModel<String> categoryModel;
    
    // Components cho panel sản phẩm
    private TextField searchField;
    private Button searchButton;
    private JTable productTable;
    private DefaultTableModel productModel;
    
    // Components cho panel giỏ hàng
    private TextField customerSearchField;
    private Button addCustomerButton;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel totalItemsLabel;
    private JLabel totalAmountLabel;
    private Button checkoutButton;
    
    public POSGUI() {
        initComponents();
        setupLayout();
        setupStyling();
        setupListeners();
        FrameUtils.setupFrame(this, "Hệ Thống Bán Hàng", 1400, 800);
    }
    
    private void initComponents() {
        // Khởi tạo các panel chính
        mainPanel = new PanelCover();
        leftPanel = new PanelCover();
        centerPanel = new PanelCover();
        rightPanel = new PanelCover();
        
        // Khởi tạo danh sách danh mục
        categoryModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Khởi tạo bảng sản phẩm
        String[] productColumns = {"Mã SP", "Tên SP", "Giá", "Tồn Kho"};
        productModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(productModel);
        
        // Khởi tạo bảng giỏ hàng
        String[] cartColumns = {"Mã SP", "Tên SP", "Số Lượng", "Đơn Giá", "Thành Tiền", "Xóa"};
        cartModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 5; // Chỉ cho phép sửa số lượng và xóa
            }
        };
        cartTable = new JTable(cartModel);
        
        // Khởi tạo các components khác
        searchField = new TextField();
        searchButton = new Button("Tìm Kiếm");
        customerSearchField = new TextField();
        addCustomerButton = new Button("Thêm KH");
        totalItemsLabel = new JLabel("Tổng số sản phẩm: 0");
        totalAmountLabel = new JLabel("Tổng tiền: 0đ");
        checkoutButton = new Button("Thanh Toán");
        
        // Thêm dữ liệu mẫu
        addSampleData();
    }
    
    private void setupLayout() {
        // Layout chính
        setLayout(new BorderLayout());
        
        // Layout cho panel danh mục (leftPanel)
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(new JLabel("Danh Mục"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(categoryList), BorderLayout.CENTER);
        
        // Layout cho panel sản phẩm (centerPanel)
        centerPanel.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        
        // Layout cho panel giỏ hàng (rightPanel)
        rightPanel.setLayout(new BorderLayout());
        
        // Panel tìm kiếm khách hàng
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        customerPanel.add(customerSearchField);
        customerPanel.add(addCustomerButton);
        rightPanel.add(customerPanel, BorderLayout.NORTH);
        
        // Panel giỏ hàng
        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        
        // Panel tổng tiền và thanh toán
        JPanel totalPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        totalPanel.add(totalItemsLabel);
        totalPanel.add(totalAmountLabel);
        totalPanel.add(checkoutButton);
        rightPanel.add(totalPanel, BorderLayout.SOUTH);
        
        // Thêm các panel vào main panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        // Thêm main panel vào frame
        add(mainPanel);
    }
    
    private void setupStyling() {
        // Styling cho frame
        ColorScheme.styleFrame(this);
        
        // Styling cho các components
        ColorScheme.styleTextField(searchField);
        ColorScheme.styleTextField(customerSearchField);
        ColorScheme.styleButton(searchButton, false);
        ColorScheme.styleButton(addCustomerButton, false);
        ColorScheme.styleButton(checkoutButton, true);
        
        // Styling cho các panel
        leftPanel.setBackground(ColorScheme.SURFACE);
        centerPanel.setBackground(ColorScheme.SURFACE);
        rightPanel.setBackground(ColorScheme.SURFACE);
        
        // Styling cho tables
        styleTable(productTable);
        styleTable(cartTable);
        
        // Styling cho labels
        ColorScheme.styleLabel(totalItemsLabel, true);
        ColorScheme.styleLabel(totalAmountLabel, true);
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }
    
    private void styleTable(JTable table) {
        table.setBackground(ColorScheme.SURFACE);
        table.setForeground(ColorScheme.TEXT_PRIMARY);
        table.setGridColor(ColorScheme.BORDER);
        table.setSelectionBackground(ColorScheme.PRIMARY);
        table.setSelectionForeground(ColorScheme.TEXT_LIGHT);
        table.getTableHeader().setBackground(ColorScheme.PRIMARY);
        table.getTableHeader().setForeground(ColorScheme.TEXT_LIGHT);
    }
    
    private void setupListeners() {
        // Xử lý sự kiện chọn danh mục
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    loadProductsByCategory(selectedCategory);
                }
            }
        });
        
        // Xử lý sự kiện tìm kiếm sản phẩm
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            searchProducts(searchText);
        });
        
        // Xử lý sự kiện thêm khách hàng
        addCustomerButton.addActionListener(e -> {
            showAddCustomerDialog();
        });
        
        // Xử lý sự kiện thanh toán
        checkoutButton.addActionListener(e -> {
            showCheckoutDialog();
        });
    }
    
    private void addSampleData() {
        // Thêm danh mục mẫu
        categoryModel.addElement("Sách Văn Học");
        categoryModel.addElement("Sách Kinh Tế");
        categoryModel.addElement("Sách Kỹ Thuật");
        categoryModel.addElement("Sách Thiếu Nhi");
        
        // Thêm sản phẩm mẫu
        productModel.addRow(new Object[]{"SP001", "Đắc Nhân Tâm", 89000, 50});
        productModel.addRow(new Object[]{"SP002", "Nhà Giả Kim", 79000, 30});
        productModel.addRow(new Object[]{"SP003", "7 Thói Quen Hiệu Quả", 99000, 25});
    }
    
    private void loadProductsByCategory(String category) {
        // TODO: Implement loading products by category
        System.out.println("Loading products for category: " + category);
    }
    
    private void searchProducts(String searchText) {
        // TODO: Implement product search
        System.out.println("Searching for: " + searchText);
    }
    
    private void showAddCustomerDialog() {
        // TODO: Implement add customer dialog
        JOptionPane.showMessageDialog(this, "Chức năng thêm khách hàng đang phát triển");
    }
    
    private void showCheckoutDialog() {
        // TODO: Implement checkout dialog
        JOptionPane.showMessageDialog(this, "Chức năng thanh toán đang phát triển");
    }

    public static void main(String[] args) {
        // Chạy ứng dụng trong Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                POSGUI pos = new POSGUI();
                pos.setVisible(true);
            }
        });
    }
}