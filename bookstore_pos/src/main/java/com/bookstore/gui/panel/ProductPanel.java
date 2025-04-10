package com.bookstore.gui.panel;

import com.bookstore.controller.ProductController;
import com.bookstore.dao.ProductDAO;
import com.bookstore.model.Product;

import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List; 
public class ProductPanel extends JPanel {

    private ProductDAO productDAO;
    private ProductController controller;
    
    private TextField productID;
    private TextField productName;
    private TextField author;
    private TextField price;
    private TextField categoryId;
    private TextField imagePath;
    private TextField searchProduct;
    private Button btnSearch, btnAdd, btnUpdate, btnDelete, btnReset, btnClear, btnImportFile, btnExportFile;
    private JComboBox<String> cboSearchType;

    private CustomTable productsTable;
    private DefaultTableModel productsTableModel;

    public ProductPanel() {
        controller = new ProductController();
        productDAO = new ProductDAO(); 
        initializeUI();
        loadProductData();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());

        // Left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(580, 0)); // Tăng chiều rộng lên 450px
        Border thickBorder = new LineBorder(ColorScheme.BORDER, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Nhập thông tin"));

        // Phần bên trái: Viền để chứa ảnh
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(220, 0)); // Chiều rộng 150px
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Viền đen đơn giản
        leftPanel.add(imagePanel, BorderLayout.WEST);

        // Phần bên phải: Các ô TextField (2 cột)
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(5, 5, 2, 5); // Giảm khoảng cách bên trái/phải
        gbcL.fill = GridBagConstraints.HORIZONTAL;

        // Cột 1
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        productID = new TextField();
        productID.setPlaceholder("ID: [Tự động tăng]");
        productID.setFocusable(false);
        productID.setPreferredSize(new Dimension(150, 25));
        productID.setBackground(new Color(224, 224, 224));
        fieldsPanel.add(productID, gbcL);

        gbcL.gridy = 1;
        productName = new TextField();
        productName.setPlaceholder("Nhập tên sản phẩm...");
        productName.setPreferredSize(new Dimension(150, 25));
        ColorScheme.styleTextField(productName);
        fieldsPanel.add(productName, gbcL);

        gbcL.gridy = 2;
        author = new TextField();
        author.setPlaceholder("Nhập tên tác giả...");
        author.setPreferredSize(new Dimension(150, 25));
        ColorScheme.styleTextField(author);
        fieldsPanel.add(author, gbcL);

        // Cột 2
        gbcL.gridx = 1;
        gbcL.gridy = 0;
        price = new TextField();
        price.setPlaceholder("Nhập giá sản phẩm...");
        price.setPreferredSize(new Dimension(150, 25));
        ColorScheme.styleTextField(price);
        fieldsPanel.add(price, gbcL);

        gbcL.gridy = 1;
        categoryId = new TextField();
        categoryId.setPlaceholder("Nhập ID danh mục...");
        categoryId.setPreferredSize(new Dimension(150, 25));
        ColorScheme.styleTextField(categoryId);
        fieldsPanel.add(categoryId, gbcL);

        gbcL.gridy = 2;
        imagePath = new TextField();
        imagePath.setPlaceholder("Nhập đường dẫn ảnh...");
        imagePath.setPreferredSize(new Dimension(150, 25));
        ColorScheme.styleTextField(imagePath);
        fieldsPanel.add(imagePath, gbcL);

        leftPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Right panel (giảm kích thước)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Thanh chức năng"));
        Dimension buttonSize = new Dimension(120, 25); // Giảm từ 150x30 xuống 120x25
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(5, 10, 2, 5); // Giảm khoảng cách
        gbcR.gridx = 0;
        gbcR.gridy = 0;
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        searchProduct = new TextField();
        searchProduct.setPlaceholder("Nhập thông tin tìm kiếm...");
        searchProduct.setPreferredSize(new Dimension(175, 25)); // Giảm từ 175 xuống 150
        ColorScheme.styleTextField(searchProduct);
        rightPanel.add(searchProduct, gbcR);

        gbcR.gridy = 2;
        gbcR.fill = GridBagConstraints.NONE;
        gbcR.anchor = GridBagConstraints.WEST;
        gbcR.gridwidth = 1;
        btnAdd = new Button("Thêm sản phẩm");
        btnAdd.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnAdd, false);
        rightPanel.add(btnAdd, gbcR);

        gbcR.gridy = 1;
        btnReset = new Button("Làm mới bảng");
        btnReset.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnReset, false);
        rightPanel.add(btnReset, gbcR);
        btnReset.addActionListener(e -> loadProductData());

        gbcR.insets = new Insets(5, 0, 2, 10); // Giảm khoảng cách
        gbcR.gridx = 1;
        gbcR.gridy = 2;
        btnUpdate = new Button("Sửa sản phẩm");
        btnUpdate.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnUpdate, false);
        rightPanel.add(btnUpdate, gbcR);

        gbcR.insets = new Insets(5, 0, 2, 10);
        cboSearchType = new JComboBox<>(new String[]{"Tìm theo ID", "Tìm theo tên", "Tìm theo tác giả"});
        gbcR.gridy = 0;
        rightPanel.add(cboSearchType, gbcR);

        gbcR.gridy = 1;
        btnClear = new Button("Xóa ô nhập liệu");
        btnClear.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnClear, false);
        rightPanel.add(btnClear, gbcR);
        btnClear.addActionListener(e -> clearTextField());

        gbcR.gridx = 2;
        gbcR.gridy = 2;
        btnDelete = new Button("Xóa sản phẩm");
        btnDelete.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnDelete, false);
        rightPanel.add(btnDelete, gbcR);

        gbcR.gridy = 1;
        btnImportFile = new Button("Nhập File 📥");
        btnImportFile.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnImportFile, false);
        rightPanel.add(btnImportFile, gbcR);

        gbcR.gridy = 0;
        btnSearch = new Button("Tìm kiếm 🔍");
        btnSearch.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnSearch, false);
        rightPanel.add(btnSearch, gbcR);

        gbcR.gridx = 3;
        gbcR.gridy = 1;
        btnExportFile = new Button("Xuất File 📤");
        btnExportFile.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnExportFile, false);
        rightPanel.add(btnExportFile, gbcR);

        northPanel.add(leftPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.EAST);
        this.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        String[] columns = {"ID", "Tên sản phẩm", "Tác giả", "Giá", "ID Danh mục", "Đường dẫn ảnh"};
        productsTable = new CustomTable(columns);
        productsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable.setModel(productsTableModel);

        JScrollPane productsScrollPane = new JScrollPane(productsTable);
        productsScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        productsScrollPane.setPreferredSize(new Dimension(800, 400));
        productsTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        productsTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên sản phẩm
        productsTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Tác giả
        productsTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Giá
        productsTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // ID Danh mục
        productsTable.getColumnModel().getColumn(5).setPreferredWidth(200);  // Đường dẫn ảnh

        centerPanel.add(productsScrollPane, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
    }
    
    public void loadProductData(){
        productsTableModel.setRowCount(0);
        List<Product> products = controller.getAllProduct();
        for(Product product : products){
            productsTableModel.addRow(new Object[]{product.getId(), product.getName(), product.getAuthor(),
                product.getPrice(), product.getCategoryId(), product.getImagePath()} );
        }
    }
    
    public void clearTextField(){
        productID.setText("");
        productName.setText("");
        author.setText("");
        price.setText("");
        categoryId.setText("");
        imagePath.setText("");
        searchProduct.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý sản phẩm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ProductPanel());
            frame.setVisible(true);
        });
    }
}