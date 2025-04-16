package com.bookstore.gui.panel;

import com.bookstore.controller.ProductController;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductPanel extends JPanel {

    private ProductController controller;
    
    private TextField productID;
    private TextField productName;
    private TextField author;
    private TextField price;
    private TextField categoryId;
    private TextField imagePath;
    private TextField searchProduct;
    private Button btnSearch, btnAdd, btnUpdate, btnDelete, btnReset, btnClear, btnImportFile, btnExportFile, btnImagePath;
    private JComboBox<String> cboSearchType;

    private CustomTable productsTable;
    private DefaultTableModel productsTableModel;
    private JLabel imagePreviewLabel;
    private static final String PRODUCT_IMAGE_PATH = "product/";
    private static final String IMAGE_EXTENSION = ".jpg";
    private String tempImagePath;

    public ProductPanel() {
        controller = new ProductController();
        tempImagePath = "";
        initializeUI();
        loadProductData();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());

        // Left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(570, 0)); 
        Border thickBorder = new LineBorder(ColorScheme.BORDER, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Nhập thông tin"));

        // Phần bên trái: Viền để chứa ảnh
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(150, 100)); 
        imagePanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1));
        imagePanel.setLayout(new BorderLayout()); // Để JLabel căn giữa
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imagePreviewLabel, BorderLayout.CENTER);
        leftPanel.add(imagePanel, BorderLayout.WEST);

        // Phần bên phải: Các ô TextField 
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(5, 2, 2, 2); 
        gbcL.fill = GridBagConstraints.HORIZONTAL;

        // Cột 1
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        productID = new TextField();
        productID.setPlaceholder("ID: [Tự động tăng]");
        productID.setFocusable(false);
        productID.setPreferredSize(new Dimension(150, 25));
        productID.setBackground(new java.awt.Color(224, 224, 224));
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
        imagePath.setText(PRODUCT_IMAGE_PATH); // Mặc định là "product/"
        imagePath.setFocusable(true); // Không cho phép chỉnh sửa trực tiếp
        ColorScheme.styleTextField(imagePath);
        fieldsPanel.add(imagePath, gbcL);
        
        gbcL.gridx = 2;
        btnImagePath = new Button("File");
        btnImagePath.setPreferredSize(new Dimension(60, 25));
        ColorScheme.styleButton(btnImagePath, false);
        fieldsPanel.add(btnImagePath, gbcL);
        leftPanel.add(fieldsPanel, BorderLayout.CENTER);
        btnImagePath.addActionListener(e -> openFileExplorer());
        

        // Right panel (giảm kích thước)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Thanh chức năng"));
        Dimension buttonSize = new Dimension(120, 25);
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(5, 5, 2, 5);
        gbcR.gridx = 0;
        gbcR.gridy = 0;
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        searchProduct = new TextField();
        searchProduct.setPlaceholder("Nhập thông tin tìm kiếm...");
        searchProduct.setPreferredSize(new Dimension(175, 25));
        ColorScheme.styleTextField(searchProduct);
        rightPanel.add(searchProduct, gbcR);

        gbcR.gridy = 2;
        gbcR.fill = GridBagConstraints.NONE;
        gbcR.anchor = GridBagConstraints.WEST;
        gbcR.gridwidth = 1;
        btnAdd = new Button("Thêm sản phẩm");
        btnAdd.setPreferredSize(new Dimension(155, 25));
        ColorScheme.styleButton(btnAdd, false);
        rightPanel.add(btnAdd, gbcR);
        btnAdd.addActionListener(e -> addProduct());

        gbcR.gridy = 1;
        btnReset = new Button("Làm mới bảng");
        btnReset.setPreferredSize(new Dimension(155, 25));
        ColorScheme.styleButton(btnReset, false);
        rightPanel.add(btnReset, gbcR);
        btnReset.addActionListener(e -> loadProductData());

        gbcR.insets = new Insets(5, 0, 2, 10);
        gbcR.gridx = 1;
        gbcR.gridy = 2;
        btnUpdate = new Button("Sửa sản phẩm");
        btnUpdate.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnUpdate, false);
        rightPanel.add(btnUpdate, gbcR);
        btnUpdate.addActionListener(e -> updateProduct());

        gbcR.insets = new Insets(5, 0, 2, 10);
        cboSearchType = new JComboBox<>(new String[]{"Tìm theo ID", "Tìm theo tên", "Tìm theo tác giả", "Tìm theo ID danh mục"});
        
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
        btnDelete.addActionListener(e -> deleteProduct());

        gbcR.gridy = 1;
        btnImportFile = new Button("Nhập File 📥");
        btnImportFile.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnImportFile, false);
        rightPanel.add(btnImportFile, gbcR);
        btnImportFile.addActionListener(e -> importFromExcel());

        gbcR.gridy = 0;
        btnSearch = new Button("Tìm kiếm 🔍");
        btnSearch.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnSearch, false);
        rightPanel.add(btnSearch, gbcR);
        btnSearch.addActionListener(e -> searchProduct());

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
        productsTable.getTableHeader().setReorderingAllowed(false);
        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && !evt.isConsumed()) { // Nhấp đúp
                    int row = productsTable.getSelectedRow();
                    if (row >= 0) {
                        fillTextFieldsFromSelectedRow(row);
                    }
                }
            }
        });
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
            String imagePathWithId = PRODUCT_IMAGE_PATH + product.getId() + IMAGE_EXTENSION;
            productsTableModel.addRow(new Object[]{product.getId(), product.getName(), product.getAuthor(),
                product.getPrice(), product.getCategoryId(), imagePathWithId} );
        }
    }
    
    public void fillTextFieldsFromSelectedRow(int row){
        Object productIdValue = productsTableModel.getValueAt(row, 0);
        Object nameValue = productsTableModel.getValueAt(row, 1);
        Object authorValue = productsTableModel.getValueAt(row,  2);
        Object priceValue = productsTableModel.getValueAt(row, 3);
        Object categoryIdValue = productsTableModel.getValueAt(row, 4);
        Object imagePathValue = productsTableModel.getValueAt(row, 5);
        
        productID.setText(productIdValue.toString());
        productName.setText(nameValue.toString());
        author.setText(authorValue.toString());
        price.setText(priceValue.toString());
        categoryId.setText(categoryIdValue.toString());
        imagePath.setText("product/default_img.png");
        
        if (imagePathValue != null && !imagePathValue.toString().isEmpty()) {
            // Tạo đường dẫn ảnh theo định dạng product/{id}.jpg
            String imageResourcePath = PRODUCT_IMAGE_PATH + productIdValue + IMAGE_EXTENSION;
            imagePath.setText(PRODUCT_IMAGE_PATH + productIdValue + IMAGE_EXTENSION );
            ImageIcon scaledIcon = loadImage(imageResourcePath);
            imagePreviewLabel.setIcon(scaledIcon);
        } else {
            imagePreviewLabel.setIcon(loadImage(null)); // Hiển thị placeholder
        }
    }
    
    public void clearTextField(){
        productID.setText("");
        productName.setText("");
        author.setText("");
        price.setText("");
        categoryId.setText("");
        imagePath.setText(PRODUCT_IMAGE_PATH); // Đặt lại mặc định
        searchProduct.setText("");
        tempImagePath = "";
        imagePreviewLabel.setIcon(null);
        imagePreviewLabel.revalidate();
        imagePreviewLabel.repaint();
    }
    
    public void addProduct() {
        String productName = this.productName.getText().trim();
        String author = this.author.getText().trim();
        String priceText = this.price.getText().trim();
        String categoryIdText = this.categoryId.getText().trim();

        // Kiểm tra đầu vào
        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên tác giả!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (categoryIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ID danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int categoryId = Integer.parseInt(categoryIdText);

            // Tạo đối tượng Product
            Product newProduct = new Product(0, productName, author, price, categoryId, PRODUCT_IMAGE_PATH + IMAGE_EXTENSION); // ID = 0 vì tự tăng

            // Gọi controller
            int newProductId = controller.addProduct(newProduct);
            
            // Cập nhật imagePath trong CSDL với ID mới
            String updatedImagePath = PRODUCT_IMAGE_PATH + newProductId + IMAGE_EXTENSION;
            Product updatedProduct = new Product(newProductId, productName, author, price, categoryId, updatedImagePath);
            boolean updateSuccess = controller.updateProduct(updatedProduct);
            if (!updateSuccess) {
                throw new SQLException("Không thể cập nhật imagePath cho sản phẩm ID: " + newProductId);
            }
            
            // Nếu người dùng chọn ảnh mới, sao chép ảnh vào thư mục resources
            if (!tempImagePath.isEmpty()) {
                String destPath = "src/main/resources/" + PRODUCT_IMAGE_PATH + newProductId + IMAGE_EXTENSION;
                File destFile = new File(destPath);
                if (destFile.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "File ảnh product/" + newProductId + ".jpg đã tồn tại. Bạn có muốn ghi đè không?",
                        "Xác nhận ghi đè",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        Files.copy(new File(tempImagePath).toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                    }
                } else {
                    Files.copy(new File(tempImagePath).toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Làm mới bảng
            loadProductData();
            // Xóa các ô nhập liệu
            clearTextField();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công! ID: " + newProductId, "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá hoặc ID danh mục không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sao chép file ảnh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteProduct() {
        String idText = productID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            boolean success = controller.deleteProduct(id);
            if (success) {
                // Xóa file ảnh nếu tồn tại
                String imageFilePath = "src/main/resources/" + PRODUCT_IMAGE_PATH + id + IMAGE_EXTENSION;
                File imageFile = new File(imageFilePath);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
                
                productsTableModel.setRowCount(0);
                loadProductData();
                clearTextField();
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateProduct(){
        String idText = productID.getText().trim();
        String nameText = productName.getText().trim();
        String priceText = price.getText().trim();
        String authorText = author.getText().trim();
        String categoryIdText = categoryId.getText().trim();
        
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nameText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (authorText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên tác giả không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá sản phẩm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (categoryIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã danh mục không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText); 
            String name = nameText; 
            String authorValue = authorText;
            double priceValue = Double.parseDouble(priceText); 
            int categoryIdValue = Integer.parseInt(categoryIdText); 
            String image = PRODUCT_IMAGE_PATH; // Luôn giữ đường dẫn chung

            // Tạo đối tượng Product mới
            Product updatedProduct = new Product(id, name, authorValue, priceValue, categoryIdValue, image);

            // Nếu có ảnh mới được chọn, sao chép ảnh vào thư mục resources
            if (!tempImagePath.isEmpty()) {
                String destPath = "src/main/resources/" + PRODUCT_IMAGE_PATH + id + IMAGE_EXTENSION;
                File destFile = new File(destPath);
                if (destFile.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "File ảnh product/" + id + ".jpg đã tồn tại. Bạn có muốn ghi đè không?",
                        "Xác nhận ghi đè",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        Files.copy(new File(tempImagePath).toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                    }
                } else {
                    Files.copy(new File(tempImagePath).toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // Gọi controller để cập nhật sản phẩm
            boolean success = controller.updateProduct(updatedProduct);
            if (success) {
                loadProductData(); // Làm mới bảng
                clearTextField();  // Xóa các ô nhập
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID, giá hoặc mã danh mục không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sao chép file ảnh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchProduct(){
        String searchText = searchProduct.getText();
        String searchType = (String) cboSearchType.getSelectedItem();
        if(searchText.isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin để tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        productsTableModel.setRowCount(0);
        try{
           List<Product> results;
           if("Tìm theo ID".equals(searchType)){
               int id = Integer.parseInt(searchText);
               Product product = controller.searchById(id);
               if(product != null){
                   productsTableModel.addRow(new Object[]{
                       product.getId(),
                       product.getName(),
                       product.getAuthor(),
                       product.getPrice(),
                       product.getCategoryId(),
                       product.getImagePath()
                   });
               }else{
                   JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với ID: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
               }
           }else if("Tìm theo tên".equals(searchType)){
               results = controller.searchByName(searchText);
               if (results != null && !results.isEmpty()){
                   for(Product product : results){
                       productsTableModel.addRow(new Object[]{
                            product.getId(),
                            product.getName(),
                            product.getAuthor(),
                            product.getPrice(),
                            product.getCategoryId(),
                            product.getImagePath()
                       });
                   }
               }else{
                   JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với tên sản phẩm: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
               }
           }else if("Tìm theo tác giả".equals(searchType)){
               results = controller.searchByAuthor(searchText);
               if (results != null && !results.isEmpty()){
                   for(Product product : results){
                       productsTableModel.addRow(new Object[]{
                            product.getId(),
                            product.getName(),
                            product.getAuthor(),
                            product.getPrice(),
                            product.getCategoryId(),
                            product.getImagePath()
                       });
                   }
               }else{
                   JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với tên tác giả: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
               }
           }else if("Tìm theo ID danh mục".equals(searchType)){
               int categoryId = Integer.parseInt(searchText);
               results = controller.searchByCategoryId(categoryId);
               if (results != null && !results.isEmpty()){
                   for(Product product : results){
                       productsTableModel.addRow(new Object[]{
                            product.getId(),
                            product.getName(),
                            product.getAuthor(),
                            product.getPrice(),
                            product.getCategoryId(),
                            product.getImagePath()
                       });
                   }
               }else{
                   JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với categoryID: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
               }
           }
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void openFileExplorer() {
        FileDialog fileDialog = new FileDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn ảnh", FileDialog.LOAD);
        fileDialog.setFile("*.jpg;*.png;*.jpeg;*.gif");
        fileDialog.setFilenameFilter((dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".jpg") || lowercaseName.endsWith(".png") || 
                   lowercaseName.endsWith(".jpeg") || lowercaseName.endsWith(".gif");
        });

        fileDialog.setVisible(true);

        String directory = fileDialog.getDirectory();
        String file = fileDialog.getFile();

        if (directory != null && file != null) {
            String filePath = directory + file;
            tempImagePath = filePath; // Lưu đường dẫn tạm thời để dùng khi thêm/sửa sản phẩm
            imagePath.setText(PRODUCT_IMAGE_PATH); // Hiển thị đường dẫn chung trong TextField

            try {
                // Tải ảnh từ đường dẫn ngoài
                ImageIcon originalIcon = new ImageIcon(filePath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                imagePreviewLabel.setIcon(scaledIcon);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể tải ảnh: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                imagePreviewLabel.setIcon(loadImage(null)); // Hiển thị placeholder
            }
        }
    }
    
    private ImageIcon loadImage(String path) {
        final int WIDTH = 150;
        final int HEIGHT = 100;
        final String DEFAULT_IMAGE_PATH = "product/default_img.png";
    
        try {
            String finalPath = path;

            if (path == null || path.trim().isEmpty()) {
                finalPath = DEFAULT_IMAGE_PATH;
            }

            // Tải ảnh trực tiếp từ hệ thống file
            String absolutePath = "src/main/resources/" + finalPath;
            File imageFile = new File(absolutePath);
            
            if (!imageFile.exists()) {
                // Nếu không tồn tại, thử tải default image từ hệ thống file
                absolutePath = "src/main/resources/" + DEFAULT_IMAGE_PATH;
                imageFile = new File(absolutePath);
            }

            if (!imageFile.exists()) {
                // Nếu không tìm thấy ảnh, hiển thị placeholder
                BufferedImage emptyImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = emptyImage.createGraphics();
                g2d.setColor(java.awt.Color.GRAY);
                g2d.fillRect(0, 0, WIDTH, HEIGHT);
                g2d.setColor(java.awt.Color.BLACK);
                g2d.drawString("No Image", WIDTH/2 - 30, HEIGHT/2);
                g2d.dispose();
                return new ImageIcon(emptyImage);
            }

            // Tải ảnh từ file
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image scaledImage = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
    
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            BufferedImage emptyImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = emptyImage.createGraphics();
            g2d.setColor(java.awt.Color.GRAY);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            g2d.setColor(java.awt.Color.BLACK);
            g2d.drawString("No Image", WIDTH/2 - 30, HEIGHT/2);
            g2d.dispose();
            return new ImageIcon(emptyImage);
        }
    }
    
    public void importFromExcel() {
        FileDialog fileDialog = new FileDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn file Excel", FileDialog.LOAD);
        fileDialog.setFile("*.xlsx;*.xls");
        fileDialog.setFilenameFilter((dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".xlsx") || lowercaseName.endsWith(".xls");
        });

        fileDialog.setVisible(true);

        String directory = fileDialog.getDirectory();
        String file = fileDialog.getFile();

        if (directory != null && file != null) {
            String excelFilePath = directory + file;
            try (FileInputStream fis = new FileInputStream(excelFilePath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = 0;

                for (Row row : sheet) {
                    if (rowCount == 0) {
                        rowCount++;
                        continue;
                    }

                    String name = getCellValueAsString(row.getCell(1));
                    String author = getCellValueAsString(row.getCell(2));
                    String priceText = getCellValueAsString(row.getCell(3));
                    String categoryIdText = getCellValueAsString(row.getCell(4));
                    String imagePathText = getCellValueAsString(row.getCell(5));

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống (dòng " + (rowCount + 1) + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    if (author.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tên tác giả không được để trống (dòng " + (rowCount + 1) + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    if (priceText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Giá sản phẩm không được để trống (dòng " + (rowCount + 1) + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    if (categoryIdText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "ID danh mục không được để trống (dòng " + (rowCount + 1) + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    try {
                        double price = Double.parseDouble(priceText);
                        int categoryId = Integer.parseInt(categoryIdText);

                        // Sử dụng đường dẫn mặc định
                        String imagePathToSave = PRODUCT_IMAGE_PATH;

                        Product product = new Product(0, name, author, price, categoryId, imagePathToSave);
                        int newProductId = controller.addProduct(product);

                        // Nếu có đường dẫn ảnh trong file Excel, sao chép ảnh vào thư mục resources
                        if (!imagePathText.isEmpty()) {
                            File imageFile = new File(imagePathText);
                            if (imageFile.exists()) {
                                String destPath = "src/main/resources/" + PRODUCT_IMAGE_PATH + newProductId + IMAGE_EXTENSION;
                                File destFile = new File(destPath);
                                if (destFile.exists()) {
                                    int confirm = JOptionPane.showConfirmDialog(
                                        this,
                                        "File ảnh product/" + newProductId + ".jpg đã tồn tại. Bạn có muốn ghi đè không?",
                                        "Xác nhận ghi đè",
                                        JOptionPane.YES_NO_OPTION
                                    );
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        Files.copy(imageFile.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                                    }
                                } else {
                                    Files.copy(imageFile.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "File ảnh không tồn tại: " + imagePathText + " (dòng " + (rowCount + 1) + ")", 
                                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                            }
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Giá hoặc ID danh mục không hợp lệ (dòng " + (rowCount + 1) + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu (dòng " + (rowCount + 1) + "): " + e.getMessage(), 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Lỗi khi sao chép file ảnh (dòng " + (rowCount + 1) + "): " + e.getMessage(), 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    rowCount++;
                }

                loadProductData();
                JOptionPane.showMessageDialog(this, "Nhập dữ liệu từ Excel thành công! Đã thêm " + (rowCount - 1) + " sản phẩm.", 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi đọc file Excel: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((int) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
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