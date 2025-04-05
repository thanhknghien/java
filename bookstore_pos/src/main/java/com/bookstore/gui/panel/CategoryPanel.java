package com.bookstore.gui.panel;

import com.bookstore.dao.CategoryDAO;
import com.bookstore.model.Category;

import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CategoryPanel extends JFrame {
    private CategoryDAO categoryDAO;
    
    private TextField categoryID;
    private TextField name;
    private TextField searchCategory;
    private Button btnSearch, btnAdd, btnUpdate, btnDelete, btnReset, btnClear, btnImportFile, btnExportFile;
    private JComboBox<String> cboSearchType;
    
    private CustomTable categorysTable;
    private DefaultTableModel categorysTableModel;

    public CategoryPanel() {
        categoryDAO = new CategoryDAO();
        initializeUI();
        loadCategoriesData();
        setVisible(true);
    }

    private void initializeUI() {
        FrameUtils.setupFrame(this, "Quản lý danh mục", 1200, 800);
        JPanel northPanel = new JPanel(new BorderLayout());

        // left panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(400, 0));
        Border thickBorder = new LineBorder(ColorScheme.BORDER, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Nhập thông tin"));
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(10, 20, 5, 5); // Khoảng cách giữa các thành phần
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        gbcL.fill = GridBagConstraints.HORIZONTAL; // Kéo giãn theo chiều ngang

        categoryID = new TextField();
        categoryID.setPlaceholder("ID: [Tự động tăng]");
        categoryID.setFocusable(false);
        categoryID.setPreferredSize(new Dimension(200, 25));
        categoryID.setBackground(new Color(224, 224, 224));
//        ColorScheme.styleTextField(categoryID);
        leftPanel.add(categoryID, gbcL);

        gbcL.gridy = 1; // Xuống dòng tiếp theo
        name = new TextField();
        name.setPlaceholder("Nhập tên danh mục...");
        name.setPreferredSize(new Dimension(200, 25));
        ColorScheme.styleTextField(name);
        leftPanel.add(name, gbcL);

        //right panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder(thickBorder,"Thanh chức năng"));
        Dimension buttonSize = new Dimension(150, 30);
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(10, 20, 5, 0);
        gbcR.gridx = 0;
        gbcR.gridy = 0;
        gbcR.fill = GridBagConstraints.HORIZONTAL;
        
        searchCategory = new TextField();
        searchCategory.setPlaceholder("Nhập thông tin tìm kiếm...");
        searchCategory.setPreferredSize(new Dimension(175, 25));
        ColorScheme.styleTextField(searchCategory);
        rightPanel.add(searchCategory, gbcR);
        
        gbcR.gridy = 2;
        gbcR.fill = GridBagConstraints.NONE; // Không kéo giãn button
        gbcR.anchor = GridBagConstraints.WEST; // Giữ lề trái
        gbcR.gridwidth = 1; // Giữ nguyên kích thước
        btnAdd = new Button("Thêm danh mục");
        btnAdd.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnAdd, false);
        rightPanel.add(btnAdd, gbcR);
        btnAdd.addActionListener(e -> addCategory());
        
        gbcR.gridy = 1;
        btnReset = new Button("Làm mới bảng");
        btnReset.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnReset, false);
        rightPanel.add(btnReset, gbcR);
        
        gbcR.insets = new Insets(10, 0, 5, 25);
        gbcR.gridx = 1;
        gbcR.gridy = 2;
        btnUpdate = new Button("Sửa danh mục");
        btnUpdate.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnUpdate, false);
        rightPanel.add(btnUpdate, gbcR);
        
        gbcR.insets = new Insets(10, 0, 5, 25);
        cboSearchType = new JComboBox<String>(new String[]{"Tìm theo ID", "Tìm theo tên"});
        gbcR.gridy = 0;
        rightPanel.add(cboSearchType);
        
        gbcR.gridy = 1;
        btnClear = new Button("Xóa ô nhập liệu");
        btnClear.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnClear, false);
        rightPanel.add(btnClear, gbcR);
        
        gbcR.gridx = 2;
        gbcR.gridy = 2;
        btnDelete = new Button("Xóa danh mục");
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
        // THÊM VÀO NORTH PANEL
        northPanel.add(leftPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.EAST);
        this.add(northPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250)); // Lề trái & phải 100px

        String[] columns = {"ID", "Tên danh mục"};
        categorysTable = new CustomTable(columns);
        categorysTableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Không cột nào được sửa
            }
        };
categorysTable.setModel(categorysTableModel);
        categorysTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && !evt.isConsumed()) { // Kiểm tra double-click
                    int row = categorysTable.getSelectedRow();
                    if (row >= 0) { // Đảm bảo có hàng được chọn
                        fillTextFieldsFromSelectedRow(row);
                    }
                }
            }
        });

        JScrollPane categorysScrollPane = new JScrollPane(categorysTable);
        categorysScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        categorysScrollPane.setPreferredSize(new Dimension(800, 400));

        // Căn lại độ rộng từng cột
        categorysTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // Cột ID nhỏ
        categorysTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Cột Tên lớn

        centerPanel.add(categorysScrollPane, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);

    }
    
    private void loadCategoriesData() {
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            // Add rows to the table
            categorysTableModel.addRow(new Object[]{category.getCategoryID(), category.getName()});
        }
    }
    private void fillTextFieldsFromSelectedRow(int row) {
        Object idValue = categorysTableModel.getValueAt(row, 0); // Lấy ID từ cột 0
        Object nameValue = categorysTableModel.getValueAt(row, 1); // Lấy Name từ cột 1
        
        categoryID.setText(idValue.toString()); // Điền ID
        name.setText(nameValue.toString()); // Điền Name
    }
    private void addCategory() {
        // Lấy tên danh mục từ text field
        String categoryName = name.getText().trim();

        // Kiểm tra input
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên danh mục!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng Category mới
        Category newCategory = new Category();
        newCategory.setName(categoryName);

        // Thêm vào database qua DAO
        boolean success = categoryDAO.insertCategory(newCategory);

        if (success) {
            // Thêm vào bảng hiển thị
            categorysTableModel.addRow(new Object[]{
                newCategory.getCategoryID(), // Lấy ID được sinh tự động từ database
                newCategory.getName()
            });

            // Reset form
            name.setText("");

            JOptionPane.showMessageDialog(this, 
                "Thêm danh mục thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Không thể thêm danh mục. Vui lòng thử lại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void deleteCategory(){
        
    }

    public static void main(String[] args) {
        new CategoryPanel();
    }
}
