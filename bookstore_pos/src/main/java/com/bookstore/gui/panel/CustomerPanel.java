package com.bookstore.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;

public class CustomerPanel extends JFrame{
    private CustomerDAO customerDAO;
    private CustomTable customerTable;
    private DefaultTableModel customerTableModel;


    public CustomerPanel () {
        customerDAO = new CustomerDAO();
        initializeUI();
        setVisible(true);
    }
    private void initializeUI() {
        List<Object[]> originalData = new ArrayList<>();
        // Thiết lập Frame
        FrameUtils.setupFrame(this, "Quản lý khách hàng", 1200, 800);
    
// Tạo leftPanel với GridBagLayout
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(300, 100));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        // Tạo các JLabel và TextField
        JLabel idLabel = new JLabel("Số ID:");
        TextField textField1 = new TextField();
        textField1.setPreferredSize(new Dimension(200, 30));

        JLabel nameLabel = new JLabel("Tên:");
        TextField textField2 = new TextField();
        textField2.setPreferredSize(new Dimension(200, 30));

        JLabel phoneLabel = new JLabel("SĐT:");
        TextField textField3 = new TextField();
        textField3.setPreferredSize(new Dimension(200, 30));

        JLabel pointLabel = new JLabel("POINT:");
        TextField textField4 = new TextField();
        textField4.setPreferredSize(new Dimension(200, 30));

        // Cài đặt GridBagConstraints cho các JLabel và TextField
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.gridx = 0; // Cột đầu tiên dành cho JLabel
        gbc.gridy = GridBagConstraints.RELATIVE; // Xếp theo hàng

        // Thêm JLabel và TextField vào leftPanel
        leftPanel.add(idLabel, gbc); // Thêm nhãn "Số ID"
        gbc.gridx = 1; // Cột thứ hai dành cho TextField
        leftPanel.add(textField1, gbc); // Thêm ô "Số ID"

        gbc.gridx = 0; // Quay lại cột đầu tiên cho nhãn tiếp theo
        leftPanel.add(nameLabel, gbc); // Thêm nhãn "Tên"
        gbc.gridx = 1; // Cột thứ hai dành cho TextField
        leftPanel.add(textField2, gbc); // Thêm ô "Tên"

        gbc.gridx = 0; // Quay lại cột đầu tiên cho nhãn tiếp theo
        leftPanel.add(phoneLabel, gbc); // Thêm nhãn "SĐT"
        gbc.gridx = 1; // Cột thứ hai dành cho TextField
        leftPanel.add(textField3, gbc); // Thêm ô "SĐT"

        gbc.gridx = 0; // Quay lại cột đầu tiên cho nhãn tiếp theo
        leftPanel.add(pointLabel, gbc); // Thêm nhãn "POINT"
        gbc.gridx = 1; // Cột thứ hai dành cho TextField
        leftPanel.add(textField4, gbc); // Thêm ô "POINT"

        Button addBtn = new Button("Them");
        addBtn.setPreferredSize(new Dimension(260, 40));
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(addBtn,gbc);
        
        Button changeBtn = new Button("Sua");
        changeBtn.setPreferredSize(new Dimension(260, 40));
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(changeBtn,gbc);

        Button clearBtn = new Button("Lam moi");
        clearBtn.setPreferredSize(new Dimension(260, 40));
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(clearBtn,gbc);



// Tạo northPanel với GridBagLayout
        JPanel northPanel = new JPanel(new GridBagLayout());
        northPanel.setPreferredSize(new Dimension(1200, 120));
        northPanel.setBackground(Color.CYAN);
        
    // Tạo và cấu hình northLeftPanel
        JPanel northLeftPanel = new JPanel();
        northLeftPanel.setPreferredSize(new Dimension(300, 120));
        northLeftPanel.setBackground(Color.LIGHT_GRAY);
        
    // Tạo và cấu hình northCenterPanel
        JPanel northCenterPanel = new JPanel();
        northCenterPanel.setPreferredSize(new Dimension(650, 120));
        northCenterPanel.setBackground(Color.RED);
        TextField search = new TextField();
        search.setPlaceholder("Tim kiem ....");
        search.setPreferredSize(new Dimension(570, 30));
        northCenterPanel.add(search, BorderLayout.CENTER);
        Button searchBtn = new Button("🔍");
        northCenterPanel.add(searchBtn);

        
    // Tạo và cấu hình northRightPanel
        JPanel northRightPanel = new JPanel();
        northRightPanel.setPreferredSize(new Dimension(230, 120));
        northRightPanel.setBackground(Color.BLUE);
        JComboBox<String> cboSearchType = new JComboBox<String>(new String[]{"Tìm theo ID", "Tìm theo tên", "Tim theo SDT", "Tim theo Point"});
        northRightPanel.add(cboSearchType);        
    //nut SearchBtn
        searchBtn.addActionListener(e -> {
            String searchText = search.getText().trim();
            String searchType = (String) cboSearchType.getSelectedItem();
        
            customerTableModel.setRowCount(0);
        
            if (searchText.isEmpty()) {
                // Hiển thị lại toàn bộ dữ liệu gốc
                for (Object[] row : originalData) {
                    customerTableModel.addRow(row);
                }
            } else {
                // Thực hiện tìm kiếm và hiển thị dữ liệu phù hợp
                for (Object[] row : originalData) {
                    boolean match = false;
                    String id = row[0].toString();
                    String name = row[1].toString();
                    String phone = row[2].toString();
                    String points = row[3].toString();
        
                    if ("Tìm theo ID".equals(searchType) && id.contains(searchText)) {
                        match = true;
                    } else if ("Tìm theo tên".equals(searchType) && name.toLowerCase().contains(searchText.toLowerCase())) {
                        match = true;
                    } else if ("Tim theo SDT".equals(searchType) && phone.contains(searchText)) {
                        match = true;
                    } else if ("Tim theo Point".equals(searchType) && points.equals(searchText)) {
                        match = true;
                    }
        
                    if (match) {
                        customerTableModel.addRow(row);
                    }
                }
            }
        });
    

        
        // Cài đặt GridBagConstraints cho các panel con
        GridBagConstraints gbcNL = new GridBagConstraints();
        gbcNL.gridy = 0; // Đặt tất cả các panel ở hàng 0
        gbcNL.insets = new Insets(0, 0, 0, 1); // Khoảng cách giữa các panel
        gbcNL.anchor = GridBagConstraints.WEST;
        
        // Thêm northLeftPanel vào vị trí cột 0
        gbcNL.gridx = 0;
        northPanel.add(northLeftPanel, gbcNL);
        
        // Thêm northCenterPanel vào vị trí cột 1
        gbcNL.gridx = 1;
        northPanel.add(northCenterPanel, gbcNL);
        
        // Thêm northRightPanel vào vị trí cột 2
        gbcNL.gridx = 2;
        northPanel.add(northRightPanel, gbcNL);
        

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(880, 960));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0,10));
        centerPanel.setBackground(Color.ORANGE);

        String [] columns = {"ID", "Ten", "SDT", "POINT"};
        customerTable = new CustomTable(columns);

        customerTableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        customerTable.setModel(customerTableModel);
// Xu li su kien nhap chuot vao du lieu tren bang
        customerTable.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0){
                    textField1.setText(customerTableModel.getValueAt(selectedRow, 0).toString());
                    textField2.setText(customerTableModel.getValueAt(selectedRow, 1).toString());
                    textField3.setText(customerTableModel.getValueAt(selectedRow, 2).toString());
                    textField4.setText(customerTableModel.getValueAt(selectedRow, 3).toString());
                }   
                
            }
        });

        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        customerScrollPane.setPreferredSize(new Dimension(850, 900));

        customerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(200);

        centerPanel.add(customerScrollPane, BorderLayout.CENTER);

        // Thêm northPanel và leftPanel vào JFrame
        this.add(northPanel, BorderLayout.NORTH);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);

//Xu li cac nut
    //Nut lam moi
        clearBtn.addActionListener(e -> {
            customerTable.clearSelection();
            textField1.setText("");
            textField2.setText("");
            textField3.setText("");
            textField4.setText("");

            search.setText("");
            cboSearchType.setSelectedItem("Tìm theo ID");

            customerTableModel.setRowCount(0);
            for (Object [] row : originalData){
                customerTableModel.addRow(row);
            }
        });
    //Nut them
        addBtn.addActionListener(e -> {
            Map<String, String> inputData = processInput(textField1, textField2, textField3, textField4);
            if (inputData != null){
                String id = inputData.get("id");
                String name = inputData.get("name");
                String phone = inputData.get("phone");
                String point = inputData.get("point");

                customerTableModel.addRow(new Object[]{id, name, phone, point});

                originalData.add(new Object[] {id, name, phone, point});
    
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");
                textField4.setText("");
    
                JOptionPane.showMessageDialog(null, "Them thanh cong", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    

    // Nut Sua
        changeBtn.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow(); // Lấy dòng được chọn
            if (selectedRow >= 0) {
                Map<String, String> inputData = changeInput(textField1, textField2, textField3, textField4);
                if (inputData != null) {
                    // Lấy dữ liệu từ Map
                    String id = inputData.get("id");
                    String name = inputData.get("name");
                    String phone = inputData.get("phone");
                    String point = inputData.get("point");
        
                    // Cập nhật dữ liệu vào bảng
                    customerTableModel.setValueAt(id, selectedRow, 0); // Cập nhật cột ID
                    customerTableModel.setValueAt(name, selectedRow, 1); // Cập nhật cột Tên
                    customerTableModel.setValueAt(phone, selectedRow, 2); // Cập nhật cột SĐT
                    customerTableModel.setValueAt(point, selectedRow, 3); // Cập nhật cột POINT

                    originalData.set(selectedRow, new Object[] {id, name, phone, point});
        
                    JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
    }


//Ham chuc nang phu
    // Xu li du lieu dau vao nut "Them"
    public Map<String, String> processInput(JTextField textField1, JTextField textField2, JTextField textField3, JTextField textField4) {
        Map<String, String> processedData = new HashMap<>();

        String id = textField1.getText().trim();
        String name = textField2.getText().trim();
        String phone = textField3.getText().trim();
        String point = textField4.getText().trim();

        // Kiểm tra các trường bắt buộc
        if (name.isEmpty() || phone.isEmpty() || point.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Sinh ID tự động nếu để trống
        if (id.isEmpty()) {
            id = generateUniqueID();
        }

        // Kiểm tra định dạng ID
        if (!id.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "ID phải là số!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (isIDDuplicate(id)) {
            JOptionPane.showMessageDialog(null, "ID đã tồn tại!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Định dạng tên
        name = formatName(name);

        // Kiểm tra số điện thoại
        if (!phone.matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại đúng định dạng!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (isPhoneDuplicate(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        // Kiểm tra POINT là số không âm và không bắt đầu bằng số 0
        if (!point.matches("[1-9]\\d*")) { // Số dương không bắt đầu bằng 0
            JOptionPane.showMessageDialog(null, "POINT phải là số dương và không được bắt đầu bằng số 0!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    // Lưu vào Map
        processedData.put("id", id);
        processedData.put("name", name);
        processedData.put("phone", phone);
        processedData.put("point", point);

        return processedData; // Trả về dữ liệu đã được xử lý
    }

    //Sinh id tu dong
    private String generateUniqueID() {
        int maxID = 0;
        for (int i = 0; i < customerTableModel.getRowCount(); i++) {
            int currentID = Integer.parseInt(customerTableModel.getValueAt(i, 0).toString());
            if (currentID > maxID) maxID = currentID;
        }
        return String.valueOf(maxID + 1);
    }

    //Id co trung lap khong?
    private boolean isIDDuplicate(String id) {
        for (int i = 0; i < customerTableModel.getRowCount(); i++) {
            if (customerTableModel.getValueAt(i, 0).equals(id)) {
                return true;
            }
        }
        return false;
    }

    //SDT co trung khong?
    private boolean isPhoneDuplicate(String phone) {
        for (int i = 0; i < customerTableModel.getRowCount(); i++) {
            if (customerTableModel.getValueAt(i, 2).equals(phone)) {
                return true;
            }
        }
        return false;
    }

    // Xu li du lieu dau vao nut "Sua"
    public Map<String, String> changeInput(JTextField textField1, JTextField textField2, JTextField textField3, JTextField textField4) {
        int selectedRow = customerTable.getSelectedRow(); // Lấy dòng được chọn
        Map<String, String> processedData = new HashMap<>();

        String id = textField1.getText().trim();
        String name = textField2.getText().trim();
        String phone = textField3.getText().trim();
        String point = textField4.getText().trim();

        // Kiểm tra các trường bắt buộc
        if (name.isEmpty() || phone.isEmpty() || point.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Sinh ID bang ID hien tai
        if (id.isEmpty()) {
            id = customerTable.getValueAt(selectedRow, 0).toString();
        }

        // Kiểm tra định dạng ID
        if (!id.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "ID phải là số!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        // Kiểm tra trùng lặp ID (ngoại trừ dòng hiện tại)
        if (isIDDuplicateExcludingRow(id, selectedRow)) {
            JOptionPane.showMessageDialog(null, "ID đã tồn tại ở dòng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Định dạng tên
        name = formatName(name);

        // Kiểm tra số điện thoại
        if (!phone.matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại đúng định dạng!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        // Kiểm tra trùng lặp số điện thoại (ngoại trừ dòng hiện tại)
        if (isPhoneDuplicateExcludingRow(phone, selectedRow)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại ở dòng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        // Kiểm tra POINT là số không âm và không bắt đầu bằng số 0
        if (!point.matches("[1-9]\\d*")) { // Số dương không bắt đầu bằng 0
            JOptionPane.showMessageDialog(null, "POINT phải là số dương và không được bắt đầu bằng số 0!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }


        // Lưu vào Map
        processedData.put("id", id);
        processedData.put("name", name);
        processedData.put("phone", phone);
        processedData.put("point", point);

        return processedData; // Trả về dữ liệu đã được xử lý
    }

    private boolean isPhoneDuplicateExcludingRow(String phone, int excludeRow) {
        for (int i = 0; i < customerTableModel.getRowCount(); i++) {
            if (i != excludeRow && customerTableModel.getValueAt(i, 2).equals(phone)) {
                return true; // Có số điện thoại trùng ở dòng khác
            }
        }
        return false; // Không có số điện thoại trùng
    }
        
    private boolean isIDDuplicateExcludingRow(String id, int excludeRow) {
        for (int i = 0; i < customerTableModel.getRowCount(); i++) {
            if (i != excludeRow && customerTableModel.getValueAt(i, 0).equals(id)) {
                return true; // Có ID trùng ở dòng khác
            }
        }
        return false; // Không có ID trùng
    }
        
    //Viet hoa ten
    private  String formatName (String name) {
        String [] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words){
            if (!word.isEmpty()){
                formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return formattedName.toString().trim();
    }        
    
    public static void main(String[] args) {
        new CustomerPanel();
    }
}
