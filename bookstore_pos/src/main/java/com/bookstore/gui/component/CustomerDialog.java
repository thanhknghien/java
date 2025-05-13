package com.bookstore.gui.component;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.*;
import java.awt.*;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.model.Customer;
import com.bookstore.util.ValidationUtil;

public class CustomerDialog extends JDialog {
    private Customer selectedCustomer;
    private JTextField searchField;
    private JButton searchButton;
    private JList<Customer> resultList;
    private DefaultListModel<Customer> listModel;

    private ArrayList<Customer> customerList;

    // Constructor tìm kiếm khách hàng
    public CustomerDialog(Frame parent, ArrayList<Customer> customerList) {
        super(parent, "Tìm kiếm khách hàng", true);
        this.customerList = customerList;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Thanh tìm kiếm
        searchField = new JTextField();
        searchButton = new JButton("Tìm");

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        searchPanel.add(new JLabel("Nhập tên hoặc số điện thoại:"), BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Danh sách kết quả
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Nút chọn
        JButton selectButton = new JButton("Chọn");
        selectButton.addActionListener(e -> {
            selectedCustomer = resultList.getSelectedValue();
            dispose();
        });

        searchButton.addActionListener(e -> performSearch());

        // Tìm bằng Enter
        searchField.addActionListener(e -> performSearch());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(selectButton);

        // Add các phần tử vào dialog
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // Constructor thêm mới khách hàng
    public CustomerDialog(ArrayList<Customer> customerList ,Frame parent) {
        super(parent, "Thêm khách hàng", true);
        this.customerList = customerList;
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        formPanel.add(new JLabel("Tên khách hàng:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Số điện thoại:"));
        formPanel.add(phoneField);
        add(formPanel, BorderLayout.CENTER);

        JButton btnAdd = new JButton("Thêm");
        btnAdd.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            if (name.isEmpty() || phone.isEmpty() || !ValidationUtil.isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin và không nhập sai định dạng số điện thoại!");
                return;
            }
            if(isExistCustomer(phone)){
               JOptionPane.showMessageDialog(parent, "Số điện thoại đã tồn tại trên hệ thống!");
               return; 
            }
            CustomerDAO dao = new CustomerDAO();
            int id = dao.getNextCustomerId();
            selectedCustomer = new Customer();
            selectedCustomer.setId(id);
            selectedCustomer.setName(name);
            selectedCustomer.setPhone(phone);
            selectedCustomer.setPoints(0);
            dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnAdd);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // Lọc khách hàng theo tên hoặc số điện thoại
    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        listModel.clear();

        if (keyword.isEmpty()) return;

        ArrayList<Customer> results = (ArrayList<Customer>) customerList.stream()
                .filter(c -> c.getFullName().toLowerCase().contains(keyword)
                          || c.getPhoneNumber().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        

        for (Customer c : results) {
            listModel.addElement(c);
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng.");
        }
    }

    private boolean isExistCustomer(String keyword){

        ArrayList<Customer> results = (ArrayList<Customer>) customerList.stream()
                    .filter(c -> c.getPhoneNumber().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        if(results.size() == 0){
            return false;
        }else return true;
    }

    public Customer getSelectedCustomer() {
        System.out.println(selectedCustomer.getId());
        return selectedCustomer;
    }

}
