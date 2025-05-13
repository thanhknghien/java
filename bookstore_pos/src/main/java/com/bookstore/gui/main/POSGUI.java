package com.bookstore.gui.main;

import com.bookstore.controller.POSController;
import com.bookstore.gui.component.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.model.Customer;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.model.User;
import com.bookstore.util.NumberUtil;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.Button;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class POSGUI extends JFrame {
    private POSController controller;
    private Map<Integer, OrderDetail> cart;
    private Customer selectedCustomer;
    private User employee;

    // Left Panel
    private CategoryList categoryList;
    private DefaultListModel<String> categoryListModel;

    // Main Panel 
    private TextField searchProductField;
    private ProductGridPanel productGridPanel;

    // Right Panel 
    private Button searchButton;
    private Button addCustomerButton;
    private JPanel searchJPanel;
    private CustomTable selectedProductsTable;
    private DefaultTableModel selectedProductsTableModel;

    // Bottom Panel 
    private CustomLabel totalLabel;
    private CustomLabel totalBooksLabel;
    private Button checkoutButton;


    public POSGUI(User user) throws Exception {
        this.employee = user;
        this.controller = new POSController(this);
        this.cart = new HashMap<>();
        this.selectedCustomer = new Customer();
        initializeUI();
        controller.displayAllData();

    }

    private void initializeUI() {
        FrameUtils.setupFrame(this, "POS SYSTEM", FrameUtils.screenWidth, FrameUtils.screenHeight);

        // Left Panel
        this.categoryListModel = new DefaultListModel<>();
        this.categoryList = new CategoryList(this.categoryListModel);
        this.categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onCategorySelected();
        JScrollPane categoryScrollPane = new JScrollPane(categoryList);
        categoryScrollPane.setPreferredSize(new Dimension(200, 0));
        categoryScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        add(categoryScrollPane, BorderLayout.WEST);

        // Main Panel
        JPanel productMainPanel = new JPanel(new BorderLayout());
        productMainPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);

        JPanel searchProductPanel = new JPanel(new BorderLayout());
        searchProductPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);
        
        this.searchProductField = new TextField();
        this.searchProductField.setPlaceholder("Tìm kiếm sản phẩm...");
        ColorScheme.styleTextField(this.searchProductField);
        searchProductPanel.add(this.searchProductField, BorderLayout.CENTER);

        productMainPanel.add(searchProductPanel, BorderLayout.NORTH);
        this.productGridPanel = new ProductGridPanel();
        this.productGridPanel.setBackground(ColorScheme.SURFACE);
        JScrollPane productScrollPane = new JScrollPane(this.productGridPanel);
        productScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        productMainPanel.add(productScrollPane, BorderLayout.CENTER);

        productMainPanel.setPreferredSize(new Dimension(400, 0));
        add(productMainPanel, BorderLayout.CENTER);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);
        rightPanel.setPreferredSize(new Dimension(400, 0));

        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);

        this.searchButton = new Button("Tìm kiếm");
        this.addCustomerButton = new Button("Thêm khách hàng");
        ColorScheme.styleButton(addCustomerButton, false);
        ColorScheme.styleButton(searchButton, false);
        JPanel funcPanel = new JPanel();
        funcPanel.add(searchButton); funcPanel.add(addCustomerButton);
        onSearchCustomer();
        onAddCustomer();
        searchJPanel = new JPanel();
        searchJPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);
        displayCustomerSelected(selectedCustomer);
        customerPanel.add(searchJPanel, BorderLayout.SOUTH);
        customerPanel.add(funcPanel, BorderLayout.CENTER);

        rightPanel.add(customerPanel, BorderLayout.NORTH);

        String[] columns = {"Tên", "Số lượng", "Đơn giá", "Thành tiền", "Hành động"};
        this.selectedProductsTable = new CustomTable(columns);
        this.selectedProductsTable.getTableHeader().setReorderingAllowed(false);
        this.selectedProductsTableModel = (DefaultTableModel) selectedProductsTable.getModel();
        this.selectedProductsTable.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        this.selectedProductsTable.getColumn("Hành động").setCellEditor(new ButtonEditor());
        JScrollPane selectedProductsScrollPane = new JScrollPane(this.selectedProductsTable);
        selectedProductsScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        rightPanel.add(selectedProductsScrollPane, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension((int)(FrameUtils.screenHeight - FrameUtils.screenWidth*0.25 ) ,0));

        add(rightPanel, BorderLayout.EAST);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4));
        bottomPanel.setBackground(ColorScheme.BACKGROUND_MAIN);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Button exitBtn = new Button("Thoát");
        exitBtn.addActionListener(e -> exitFrame());

        this.totalLabel = new CustomLabel("Tổng tiền: 0.00 Đ");
        this.totalBooksLabel = new CustomLabel("Tổng sách: 0");
        this.checkoutButton = new Button("Thanh toán");
        ColorScheme.styleButton(checkoutButton, true);
        this.checkoutButton.addActionListener(e -> displayCheckout(e));

        bottomPanel.add(exitBtn);
        bottomPanel.add(this.totalLabel);
        bottomPanel.add(this.totalBooksLabel);
        bottomPanel.add(this.checkoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Display Category on nav-bav
    public void displayCategory(Map<String, ArrayList<Product>> list){
        this.categoryListModel.clear();
        for(String key : list.keySet()){
            this.categoryListModel.addElement(key);
        }
    }

    // Display Product with category
    public void displayProduct(Map<String, ArrayList<Product>> productFilterByCategory, String categoryName) {
        if (!productFilterByCategory.containsKey(categoryName)) {
            this.productGridPanel.add(new Label("Danh mục rỗng!"));
        }
    
        ArrayList<Product> listProducts = productFilterByCategory.get(categoryName);
        this.productGridPanel.clearItems();
        if (listProducts != null) {
            for (Product product : listProducts) {
                ProductCard card = new ProductCard(product);
                card.addAddToCartListener(e -> controller.handleAddToCart(e)); 
                productGridPanel.add(card);
            }
        }
    }

    // Display cart
    public void displayCart(){
        selectedProductsTableModel.setRowCount(0); 
        for (OrderDetail detail : this.cart.values()) {
            Product product = detail.getProduct();
            int quantity = detail.getQuantity();
            double price = detail.getPrice();
            double total = quantity * price;

            selectedProductsTableModel.addRow(new Object[]{
                product.getName(),
                quantity,
                NumberUtil.formatNumber(price) + " Đ",
                NumberUtil.formatNumber(total) + " Đ",
                "Xóa" 
            });
        }
        
    }

    // Display Customer
    public void displayCustomerSelected(Customer customer){
        this.searchJPanel.removeAll();
        this.searchJPanel.revalidate();
        this.searchJPanel.repaint();
        JLabel lbl = new JLabel("Chưa có khách hàng được chọn !");
        lbl.setForeground(ColorScheme.TEXT_SECONDARY);
        lbl.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 16));

        if(customer == null){
            searchJPanel.add(lbl);
            return;
        }

        if(customer.getFullName() == null){
            searchJPanel.add(lbl);
        }else{
            CustomLabel nameCustomerSelectedLabel = new CustomLabel("Khách hàng: "+ customer.getFullName());
            CustomLabel phoneCustomerSelectedLabel = new CustomLabel("SĐT: "+ customer.getPhoneNumber());
            Button deleteSelectedCustomerBtn = new Button("X");
            deleteSelectedCustomerBtn.addActionListener(e -> {
                setSelectedCustomer(new Customer());
                displayCustomerSelected(getSelectedCustomer());
                return;
            });
            ColorScheme.styleButton(deleteSelectedCustomerBtn, true);
        
            searchJPanel.add(nameCustomerSelectedLabel);
            searchJPanel.add(phoneCustomerSelectedLabel);
            searchJPanel.add(deleteSelectedCustomerBtn);

        }
    }

    // Display Products Result
    public void displayProduct(ArrayList<Product> listProducts){
        productGridPanel.clearItems();
        if (listProducts != null) {
            for (Product product : listProducts) {
                ProductCard card = new ProductCard(product);
                card.addAddToCartListener(e -> controller.handleAddToCart(e)); 
                productGridPanel.add(card);
            }
        }else{
            productGridPanel.add(new JLabel("Danh sách rỗng!"));
        }   
    }

    // Display Checkout Dialog
    public void displayCheckout(ActionEvent e){
        if(cart.size()== 0){
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }
        PaymentDialog paymentDialog = new PaymentDialog(this, cart);
        paymentDialog.setVisible(true);
    }

    public void onChangeCart(){
        this.totalBooksLabel.removeAll();
        this.totalLabel.removeAll();
        double total = 0.0;
        int totalQuantity = 0;
        for (Map.Entry<Integer, OrderDetail> entry : this.cart.entrySet()) {
            total += entry.getValue().getSubtotal();
            totalQuantity += entry.getValue().getQuantity();
        }
        totalLabel.setText("Tổng tiền: "+ NumberUtil.formatNumber(total) +" Đ");
        totalBooksLabel.setText("Tổng sách: " + totalQuantity + " cuốn");
    }

    // Handle selected category
    public void onCategorySelected(){
        this.categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    try {
                        controller.displayProductOnCategory(selectedCategory);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    // Handle search Customer
    public void onSearchCustomer(){
        this.searchButton.addActionListener(e -> {
            controller.handleSearchCustomer();
        });
    }

    // Handle add Customer
    public void onAddCustomer(){
        this.addCustomerButton.addActionListener(e -> {
            try {
                controller.handleAddCustomer();
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, e, "Số điện thoại sai định dạng", ABORT);
            }
        });
    }

    // Getter Cart
    public Map<Integer, OrderDetail> getCart() {
        return cart;
    }

    // Getter Search Product TextField
    public TextField getSearchProductField() {
        return searchProductField;
    }

    // Getter Customer
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    // Getter Employee
    public User getEmployee() {
        return employee;
    }

    // Setter Cart
    public void setCart(Map<Integer, OrderDetail> cart){
        this.cart = cart;
    }



    // Exit POS
    public void exitFrame(){
        dispose();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private Button increaseButton;
        private Button decreaseButton;
        private Button removeButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout());
            setBackground(ColorScheme.SURFACE);
            increaseButton = new Button("+");
            decreaseButton = new Button("-");
            removeButton = new Button("Xóa");
            ColorScheme.styleButton(increaseButton, false);
            ColorScheme.styleButton(decreaseButton, false);
            ColorScheme.styleButton(removeButton, true);
            
            add(increaseButton);
            add(decreaseButton);
            add(removeButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private Button increaseButton;
        private Button decreaseButton;
        private Button removeButton;
        private int row;

        public ButtonEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout());
            panel.setBackground(ColorScheme.SURFACE);
            increaseButton = new Button("+");
            decreaseButton = new Button("-");
            removeButton = new Button("Xóa");
            ColorScheme.styleButton(increaseButton, false);
            ColorScheme.styleButton(decreaseButton, false);
            ColorScheme.styleButton(removeButton, true);

            panel.add(increaseButton);
            panel.add(decreaseButton);
            panel.add(removeButton);

            // Handle "+"
        increaseButton.addActionListener(e -> {
            if (selectedProductsTable.getCellEditor() != null) {
                selectedProductsTable.getCellEditor().stopCellEditing();
            }
            String productName = (String) selectedProductsTableModel.getValueAt(row, 0);
            for (OrderDetail detail : cart.values()) {
                if (detail.getProduct().getName().equals(productName)) {
                    detail.setQuantity(detail.getQuantity() + 1);
                    break;
                }
            }
            displayCart();
            onChangeCart();
        });

        // Handle "-"
        decreaseButton.addActionListener(e -> {
            if (selectedProductsTable.getCellEditor() != null) {
                selectedProductsTable.getCellEditor().stopCellEditing();
            }
            String productName = (String) selectedProductsTableModel.getValueAt(row, 0);
            Iterator<Map.Entry<Integer, OrderDetail>> iterator = cart.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, OrderDetail> entry = iterator.next();
                OrderDetail detail = entry.getValue();
                if (detail.getProduct().getName().equals(productName)) {
                    int newQty = detail.getQuantity() - 1;
                    if (newQty <= 0) {
                        iterator.remove();
                    } else {
                        detail.setQuantity(newQty);
                    }
                    break;
                }
            }
            displayCart();
            onChangeCart();
        });

        // Handle "Xóa"
        removeButton.addActionListener(e -> {
            if (selectedProductsTable.getCellEditor() != null) {
                selectedProductsTable.getCellEditor().stopCellEditing();
            }
            String productName = (String) selectedProductsTableModel.getValueAt(row, 0);
            cart.entrySet().removeIf(entry -> entry.getValue().getProduct().getName().equals(productName));
            displayCart();
            onChangeCart();
        });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}