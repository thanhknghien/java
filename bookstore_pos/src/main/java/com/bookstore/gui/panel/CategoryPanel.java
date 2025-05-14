package com.bookstore.gui.panel;

//import com.bookstore.dao.CategoryDAO;
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
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.awt.FileDialog;
import java.io.FileOutputStream;
import javax.swing.border.EmptyBorder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CategoryPanel extends JPanel {
    private CategoryController controller;

    private TextField categoryID;
    private TextField name;
    private TextField searchCategory;
    private Button btnSearch, btnAdd, btnUpdate, btnDelete, btnReset, btnClear, btnImportFile, btnExportFile;
    private JComboBox<String> cboSearchType;

    private CustomTable categorysTable;
    private DefaultTableModel categorysTableModel;

    public CategoryPanel() {
//        controller = new CategoryController();
        initializeUI();
        loadCategoriesData();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
//        setBorder(new EmptyBorder(10, 10, 10, 10));
        // Panel đệm để tạo khoảng cách 300px từ mép trái
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Căn trái 300px

        // north panel - Sử dụng FlowLayout để kéo leftPanel và rightPanel sát nhau
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // left panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(570, 118)); // Tăng chiều cao để chứa nội dung
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
        categoryID.setBackground(new java.awt.Color(224, 224, 224));
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
        Dimension buttonSize = new Dimension(120, 25); // Kích thước cho các nút thông thường
        Dimension addResetButtonSize = new Dimension(155, 25); // Kích thước cho btnAdd và btnReset
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(5, 5, 2, 5); // Insets giống Product
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
        btnAdd = new Button("Thêm➕");
        btnAdd.setPreferredSize(addResetButtonSize); // Kích thước 155x25
        ColorScheme.styleButton(btnAdd, false);
        rightPanel.add(btnAdd, gbcR);
        btnAdd.addActionListener(e -> addCategory());

        gbcR.gridy = 1;
        gbcR.insets = new Insets(5, 5, 2, 5);
        btnReset = new Button("Làm mới bảng🔄");
        btnReset.setPreferredSize(addResetButtonSize); // Kích thước 155x25
        ColorScheme.styleButton(btnReset, false);
        rightPanel.add(btnReset, gbcR);
        btnReset.addActionListener(e -> loadCategoriesData());

        gbcR.insets = new Insets(5, 0, 2, 10); 
        gbcR.gridx = 1;
        gbcR.gridy = 2;
        btnUpdate = new Button("Sửa ✏️ ");
        btnUpdate.setPreferredSize(buttonSize); // Kích thước 120x25
        ColorScheme.styleButton(btnUpdate, false);
        rightPanel.add(btnUpdate, gbcR);
        btnUpdate.addActionListener(e -> updateCategory());

        gbcR.gridy = 0;
        cboSearchType = new JComboBox<>(new String[]{"Tìm theo ID", "Tìm theo tên"});
        rightPanel.add(cboSearchType, gbcR);

        gbcR.gridy = 1;
        btnClear = new Button("Clear 🗑️");
        btnClear.setPreferredSize(buttonSize); // Kích thước 120x25
        ColorScheme.styleButton(btnClear, false);
        rightPanel.add(btnClear, gbcR);
        btnClear.addActionListener(e -> clearTextField());

        gbcR.gridx = 2;
        gbcR.gridy = 2;
        btnDelete = new Button("Xóa 🚫️");
        btnDelete.setPreferredSize(buttonSize); // Kích thước 120x25
        ColorScheme.styleButton(btnDelete, false);
        rightPanel.add(btnDelete, gbcR);
        btnDelete.addActionListener(e -> deleteCategory());

        gbcR.gridy = 1;
        btnImportFile = new Button("Nhập File 📥");
        btnImportFile.setPreferredSize(buttonSize); // Kích thước 120x25
        ColorScheme.styleButton(btnImportFile, false);
        rightPanel.add(btnImportFile, gbcR);
        btnImportFile.addActionListener(e -> importFromExcel());

        gbcR.gridy = 0;
        btnSearch = new Button("Tìm kiếm 🔍");
        btnSearch.setPreferredSize(buttonSize); // Kích thước 120x25
        ColorScheme.styleButton(btnSearch, false);
        rightPanel.add(btnSearch, gbcR);
        btnSearch.addActionListener(e -> searchCategory());

        gbcR.gridx = 3;
        gbcR.gridy = 2;
        btnExportFile = new Button("Xuất File 📤");
        btnExportFile.setPreferredSize(buttonSize); // Kích thước 120x25
        ColorScheme.styleButton(btnExportFile, false);
        rightPanel.add(btnExportFile, gbcR);
        btnExportFile.addActionListener(e -> exportToExcel());

        northPanel.add(leftPanel);
        northPanel.add(rightPanel);
        wrapperPanel.add(northPanel, BorderLayout.CENTER);
        this.add(wrapperPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(thickBorder, "Danh sách danh mục"));

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
        controller = new CategoryController(this);

        JScrollPane categorysScrollPane = new JScrollPane(categorysTable);
        categorysScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        categorysScrollPane.setPreferredSize(new Dimension(1200, 600));
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

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống (dòng " + (rowCount + 1) + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Tạo đối tượng Category với ID là 0 (tự tăng)
                    Category category = new Category(0, name);
                    // Gọi controller để thêm danh mục và nhận ID mới
                    controller.addCategory(category);

                    rowCount++;
                }

                loadCategoriesData();
                JOptionPane.showMessageDialog(this, "Nhập dữ liệu từ Excel thành công! Đã thêm " + (rowCount - 1) + " danh mục.", 
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
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    public void exportToExcel() {
        // Kiểm tra nếu bảng rỗng
        if (categorysTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng danh mục trống! Không có dữ liệu để xuất.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hiển thị FileDialog để người dùng chọn nơi lưu file
        FileDialog fileDialog = new FileDialog((Frame) SwingUtilities.getWindowAncestor(this), "Lưu file Excel", FileDialog.SAVE);
        fileDialog.setFile("DanhMuc.xlsx"); // Tên file mặc định
        fileDialog.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".xlsx"));

        fileDialog.setVisible(true);

        String directory = fileDialog.getDirectory();
        String file = fileDialog.getFile();

        if (directory != null && file != null) {
            // Đảm bảo tên file có đuôi .xlsx
            if (!file.toLowerCase().endsWith(".xlsx")) {
                file += ".xlsx";
            }
            String excelFilePath = directory + file;

            try (Workbook workbook = new XSSFWorkbook()) {
                // Tạo sheet
                Sheet sheet = workbook.createSheet("Danh mục");

                // Tạo dòng tiêu đề
                Row headerRow = sheet.createRow(0);
                String[] columns = {"ID", "Tên danh mục"};
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                }

                // Ghi dữ liệu từ bảng
                for (int rowIndex = 0; rowIndex < categorysTable.getRowCount(); rowIndex++) {
                    Row row = sheet.createRow(rowIndex + 1); // Bắt đầu từ dòng 1 (sau tiêu đề)
                    for (int colIndex = 0; colIndex < categorysTable.getColumnCount(); colIndex++) {
                        Cell cell = row.createCell(colIndex);
                        Object value = categorysTable.getValueAt(rowIndex, colIndex);
                        if (value != null) {
                            if (colIndex == 0) { // Cột ID (số nguyên)
                                cell.setCellValue(Integer.parseInt(value.toString()));
                            } else { // Cột Tên danh mục (chuỗi)
                                cell.setCellValue(value.toString());
                            }
                        }
                    }
                }

                // Tự động điều chỉnh kích thước cột
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Ghi file Excel
                try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(this, "Xuất dữ liệu sang Excel thành công! File được lưu tại: " + excelFilePath, 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi ghi file Excel: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void clearTextField(){
        categoryID.setText("");
        name.setText("");
    }
    public void updateButtonsVisibility(boolean canAdd, boolean canEdit, boolean canDelete) {
        if (btnAdd != null) {
            // Cập nhật hiển thị nút thêm
            btnAdd.setVisible(canAdd);
            btnImportFile.setVisible(canAdd);
            
        }
        
        if (btnUpdate != null) {
            // Cập nhật hiển thị nút sửa
            btnUpdate.setVisible(canEdit);
        }
        
        if (btnDelete != null) {
            // Cập nhật hiển thị nút xóa
            btnDelete.setVisible(canDelete);
            
        }
        

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

