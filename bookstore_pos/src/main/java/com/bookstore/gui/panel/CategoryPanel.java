package com.bookstore.gui.panel;

import com.bookstore.dao.CategoryDAO;
import com.bookstore.model.Category;
import com.bookstore.controller.CategoryController;

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

public class CategoryPanel extends JPanel {
    private CategoryDAO categoryDAO;
    private CategoryController controller;

    private TextField categoryID;
    private TextField name;
    private TextField searchCategory;
    private Button btnSearch, btnAdd, btnUpdate, btnDelete, btnReset, btnClear, btnImportFile, btnExportFile;
    private JComboBox<String> cboSearchType;

    private CustomTable categorysTable;
    private DefaultTableModel categorysTableModel;

    public CategoryPanel() {
        categoryDAO = new CategoryDAO();
        controller = new CategoryController();
        initializeUI();
        loadCategoriesData();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());

        // left panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(400, 0));
        Border thickBorder = new LineBorder(ColorScheme.BORDER, 2);
        leftPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Nhập thông tin"));
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(10, 20, 5, 5);
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        gbcL.fill = GridBagConstraints.HORIZONTAL;

        categoryID = new TextField();
        categoryID.setPlaceholder("ID: [Tự động tăng]");
        categoryID.setFocusable(false);
        categoryID.setPreferredSize(new Dimension(200, 25));
        categoryID.setBackground(new Color(224, 224, 224));
        leftPanel.add(categoryID, gbcL);

        gbcL.gridy = 1;
        name = new TextField();
        name.setPlaceholder("Nhập tên danh mục...");
        name.setPreferredSize(new Dimension(200, 25));
        ColorScheme.styleTextField(name);
        leftPanel.add(name, gbcL);

        // right panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Thanh chức năng"));
        Dimension buttonSize = new Dimension(150, 30);
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(10, 20, 5, 5);
        gbcR.gridx = 0;
        gbcR.gridy = 0;
        gbcR.fill = GridBagConstraints.HORIZONTAL;

        searchCategory = new TextField();
        searchCategory.setPlaceholder("Nhập thông tin tìm kiếm...");
        searchCategory.setPreferredSize(new Dimension(175, 25));
        ColorScheme.styleTextField(searchCategory);
        rightPanel.add(searchCategory, gbcR);

        gbcR.gridy = 2;
        gbcR.fill = GridBagConstraints.NONE;
        gbcR.anchor = GridBagConstraints.WEST;
        gbcR.gridwidth = 1;
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
        btnReset.addActionListener(e -> loadCategoriesData());

        gbcR.insets = new Insets(10, 0, 5, 25);
        gbcR.gridx = 1;
        gbcR.gridy = 2;
        btnUpdate = new Button("Sửa danh mục");
        btnUpdate.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnUpdate, false);
        rightPanel.add(btnUpdate, gbcR);
        btnUpdate.addActionListener(e -> updateCategory());

        gbcR.insets = new Insets(10, 0, 5, 25);
        cboSearchType = new JComboBox<>(new String[]{"Tìm theo ID", "Tìm theo tên"});
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
        btnDelete = new Button("Xóa danh mục");
        btnDelete.setPreferredSize(buttonSize);
        ColorScheme.styleButton(btnDelete, false);
        rightPanel.add(btnDelete, gbcR);
        btnDelete.addActionListener(e -> deleteCategory());

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
        btnSearch.addActionListener(e -> searchCategory());

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
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250));

        String[] columns = {"ID", "Tên danh mục"};
        categorysTable = new CustomTable(columns);
        categorysTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        categorysTable.setModel(categorysTableModel);
        categorysTable.getTableHeader().setReorderingAllowed(false);
        categorysTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && !evt.isConsumed()) {
                    int row = categorysTable.getSelectedRow();
                    if (row >= 0) {
                        fillTextFieldsFromSelectedRow(row);
                    }
                }
            }
        });

        JScrollPane categorysScrollPane = new JScrollPane(categorysTable);
        categorysScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        categorysScrollPane.setPreferredSize(new Dimension(800, 400));
        categorysTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        categorysTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        centerPanel.add(categorysScrollPane, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void loadCategoriesData() {
        categorysTableModel.setRowCount(0);
        List<Category> categories = controller.getAllCategories();
        for (Category category : categories) {
            categorysTableModel.addRow(new Object[]{category.getCategoryID(), category.getName()});
        }
    }

    private void fillTextFieldsFromSelectedRow(int row) {
        Object idValue = categorysTableModel.getValueAt(row, 0);
        Object nameValue = categorysTableModel.getValueAt(row, 1);
        categoryID.setText(idValue.toString());
        name.setText(nameValue.toString());
    }

    private void addCategory() {
        String categoryName = name.getText().trim();
        try {
            Category newCategory = new Category(0, categoryName);
            boolean success = controller.addCategory(newCategory);
            if (success) {
                categorysTableModel.addRow(new Object[]{newCategory.getCategoryID(), newCategory.getName()});
                name.setText("");
                JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        String idText = categoryID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            boolean success = controller.deleteCategory(id);
            if (success) {
                categorysTableModel.setRowCount(0);
                loadCategoriesData();
                categoryID.setText("");
                name.setText("");
                JOptionPane.showMessageDialog(this, "Xóa danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateCategory() {
        String idText = categoryID.getText().trim();
        String categoryName = name.getText().trim();

        // Kiểm tra nếu ID trống
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra nếu tên danh mục trống
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText); // Chuyển ID từ String sang int
            Category updatedCategory = new Category(id, categoryName); // Tạo đối tượng Category mới

            // Gọi controller để cập nhật danh mục
            boolean success = controller.updateCategory(updatedCategory);
            if (success) {
                // Làm mới bảng
                loadCategoriesData();
                // Xóa các ô nhập liệu sau khi cập nhật
                categoryID.setText("");
                name.setText("");
                JOptionPane.showMessageDialog(this, "Cập nhật danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void searchCategory(){
        String searchText = searchCategory.getText().trim();
        String searchType = (String) cboSearchType.getSelectedItem();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin để tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        categorysTableModel.setRowCount(0);

        try {
            List<Category> results;
            if ("Tìm theo ID".equals(searchType)) {
                int id = Integer.parseInt(searchText);
                Category category = controller.getCategoryById(id);
                if (category != null) {
                    categorysTableModel.addRow(new Object[]{category.getCategoryID(), category.getName()});
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục với ID: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if ("Tìm theo tên".equals(searchType)) {
                results = controller.searchCategoriesByName(searchText);
                if (results != null && !results.isEmpty()) {
                    for (Category category : results) {
                        categorysTableModel.addRow(new Object[]{category.getCategoryID(), category.getName()});
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục với tên: " + searchText, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            if (categorysTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID phải là một số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void clearTextField(){
        categoryID.setText("");
        name.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý danh mục");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new CategoryPanel());
            frame.setVisible(true);
        });
    }
}

