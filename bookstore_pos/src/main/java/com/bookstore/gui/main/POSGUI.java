package com.bookstore.gui.main;

import com.bookstore.controller.POSController;
import com.bookstore.gui.component.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.Button;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class POSGUI extends JFrame {
    // Biến dữ liệu tạm thời
    private POSController controller;
    private Map<Integer, OrderDetail> cart;
    private Order currentOrder;
    private ArrayList<Customer> listCustomers;
    private Customer selectedCustomer;
    private Map<String, ArrayList<Product>> productFilterByCategory;

    // Left Panel
    private CategoryList categoryList;
    private DefaultListModel<String> categoryListModel;

    // Main Panel 
    private TextField searchProductField;
    private ProductGridPanel productGridPanel;

    // Right Panel 
    private CustomComboBox<String> searchCustomerField;
    private Button addCustomerButton;
    private CustomTable selectedProductsTable;
    private DefaultTableModel selectedProductsTableModel;

    // Bottom Panel 
    private CustomLabel totalLabel;
    private CustomLabel totalBooksLabel;
    private Button checkoutButton;


    public POSGUI() throws Exception {
        initializeData();
        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        FrameUtils.setupFrame(this, "POS SYSTEM", FrameUtils.screenWidth, FrameUtils.screenHeight);

        // Left Panel
        categoryListModel = new DefaultListModel<>();
        displayCategory(productFilterByCategory);
        categoryList = new CategoryList(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    onCategorySelected(selectedCategory);
                }
            }
        });
        JScrollPane categoryScrollPane = new JScrollPane(categoryList);
        categoryScrollPane.setPreferredSize(new Dimension(200, 0));
        categoryScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        add(categoryScrollPane, BorderLayout.WEST);

        // Main Panel
        JPanel productMainPanel = new JPanel(new BorderLayout());
        productMainPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);

        JPanel searchProductPanel = new JPanel(new BorderLayout());
        searchProductPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);

        searchProductField = new TextField();
        searchProductField.setPlaceholder("Tìm kiếm sản phẩm...");
        ColorScheme.styleTextField(searchProductField);
       // searchProductField.addActionListener(e -> onSearchProduct());
        searchProductPanel.add(searchProductField, BorderLayout.CENTER);

        productMainPanel.add(searchProductPanel, BorderLayout.NORTH);
        productGridPanel = new ProductGridPanel();
        displayProduct(productFilterByCategory, productFilterByCategory.keySet().iterator().next());
        productGridPanel.setBackground(ColorScheme.SURFACE);
        JScrollPane productScrollPane = new JScrollPane(productGridPanel);
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

        JPanel searchCustomerPanel = new JPanel(new BorderLayout());
        searchCustomerPanel.setBackground(ColorScheme.BACKGROUND_SECONDARY);

        searchCustomerField = new CustomComboBox();
        searchCustomerField.setPlaceholder("Tìm kiếm khách hàng...");
        //searchCustomerField.addActionListener(e -> onSearchCustomer());
        searchCustomerPanel.add(searchCustomerField, BorderLayout.CENTER);

        customerPanel.add(displayCustomerSelected(selectedCustomer), BorderLayout.SOUTH);

        customerPanel.add(searchCustomerPanel, BorderLayout.CENTER);

        addCustomerButton = new Button("Thêm khách hàng");
        ColorScheme.styleButton(addCustomerButton, false);
        //addCustomerButton.addActionListener(e -> onAddCustomer());
        customerPanel.add(addCustomerButton, BorderLayout.EAST);

        rightPanel.add(customerPanel, BorderLayout.NORTH);

        String[] columns = {"Tên", "Số lượng", "Đơn giá", "Thành tiền", "Hành động"};
        selectedProductsTable = new CustomTable(columns);
        selectedProductsTable.getTableHeader().setReorderingAllowed(false);
        selectedProductsTableModel = (DefaultTableModel) selectedProductsTable.getModel();
        selectedProductsTable.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        selectedProductsTable.getColumn("Hành động").setCellEditor(new ButtonEditor());
        JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
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

        totalLabel = new CustomLabel("Tổng tiền: 0.00");
        totalBooksLabel = new CustomLabel("Tổng sách: 0");
        checkoutButton = new Button("Thanh toán");
        ColorScheme.styleButton(checkoutButton, true);
        //checkoutButton.addActionListener(e -> onCheckout());

        // Add Componenents
        bottomPanel.add(exitBtn);
        bottomPanel.add(totalLabel);
        bottomPanel.add(totalBooksLabel);
        bottomPanel.add(checkoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeData() throws Exception{
        this.controller = new POSController(this);
        this.selectedCustomer = new Customer();
        this.listCustomers = controller.loadingDataCustomers();
        this.productFilterByCategory = controller.loadingDataProducts();
        this.cart = new HashMap<>();
    }

    // Display when select category
    private void displayCategory(Map<String, ArrayList<Product>> list){
        categoryListModel.clear();
        for(String key : list.keySet()){
            categoryListModel.addElement(key);
        }
    }

    // Display with category
    private void displayProduct(Map<String, ArrayList<Product>> productFilterByCategory, String categoryName) {
        if (!productFilterByCategory.containsKey(categoryName)) {
            System.out.println("⚠ Không tìm thấy danh mục trong map!");
            productGridPanel.add(new Label("Danh mục rỗng!"));
        }
    
        ArrayList<Product> listProducts = productFilterByCategory.get(categoryName);
        System.out.println("Số sản phẩm tìm được: " + (listProducts == null ? "null" : listProducts.size()));
    
        productGridPanel.removeAll();
        if (listProducts != null) {
            for (Product product : listProducts) {
                ProductCard card = new ProductCard(product);
                card.addAddToCartListener(e -> handleAddToCart(e)); 
                productGridPanel.add(card);
            }
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }
    
    // For filter product
    private void displayProduct(ArrayList<Product> listProducts){
        productGridPanel.removeAll();
        if (listProducts != null) {
            for (Product product : listProducts) {
                productGridPanel.add(new ProductCard(product));
            }
        }else{
            productGridPanel.add(new JLabel("Danh sách rỗng!"));
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }
    
    // Display Cart
    private void displayCart(){
        selectedProductsTableModel.setRowCount(0); 
        for (OrderDetail detail : cart.values()) {
            Product product = detail.getProduct();
            int quantity = detail.getQuantity();
            double price = detail.getPrice();
            double total = quantity * price;

            selectedProductsTableModel.addRow(new Object[]{
                product.getName(),
                quantity,
                String.format("%.0f", price),
                String.format("%.0f", total),
                "Xóa" 
            });
        }
        
    }
    
    private void onCategorySelected(String selected){
        displayProduct(productFilterByCategory, selected);
    }

    private void handleAddToCart(ActionEvent e){
        ProductCard source = (ProductCard) e.getSource();
        Product item = source.getProduct();
        int productId = item.getId();
        if (cart.containsKey(productId)) {
            OrderDetail detail = cart.get(productId);
            detail.setQuantity(detail.getQuantity() + 1);
        } else {
            OrderDetail newDetail = new OrderDetail(null, null, item, 1, item.getPrice());
            cart.put(productId, newDetail);
        }
        displayCart();
        onChangeCart();
    } 

    private void onChangeCart(){
        totalBooksLabel.removeAll();
        totalLabel.removeAll();
        double total = 0.0;
        int totalQuantity = 0;
        for (Map.Entry<Integer, OrderDetail> entry : this.cart.entrySet()) {
            total += entry.getValue().getSubtotal();
            totalQuantity += entry.getValue().getQuantity();
        }
        totalLabel.setText("Tổng tiền: "+ total +" Đ");
        totalBooksLabel.setText("Tổng sách: " + totalQuantity + " cuốn");
    }

    private JPanel displayCustomerSelected(Customer customer){
        JPanel selectedCustomerLabel = new JPanel();
        selectedCustomerLabel.setBackground(ColorScheme.BACKGROUND_SECONDARY);
        if(customer.getName() == null){
            selectedCustomerLabel.add(new JLabel("Chưa có khách hàng được chọn !"));
            return selectedCustomerLabel;
        }else{
            
            CustomLabel nameCustomerSelectedLabel = new CustomLabel("Khách hàng: "+ customer.getName());
            CustomLabel phoneCustomerSelectedLabel = new CustomLabel("SĐT: "+ customer.getPhone());
            Button deleteSelectedCustomerBtn = new Button("X");
            ColorScheme.styleButton(deleteSelectedCustomerBtn, true);
        
            selectedCustomerLabel.add(nameCustomerSelectedLabel);
            selectedCustomerLabel.add(phoneCustomerSelectedLabel);
            selectedCustomerLabel.add(deleteSelectedCustomerBtn);

            return selectedCustomerLabel;
        }
    }

    // Exit POS
    public void exitFrame(){
        dispose();
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

    public static void main(String[] args) throws Exception {
        new POSGUI();
    }
}