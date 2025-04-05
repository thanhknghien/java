package com.bookstore.gui.main;

import com.bookstore.gui.component.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.model.Customer;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.Button;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class POSGUI extends JFrame {
    // Biến dữ liệu tạm thời
    private List<OrderDetail> selectedProducts;
    private Customer selectedCustomer;
    private List<Product> allProducts;

    // Left Panel
    private CategoryList categoryList;
    private DefaultListModel<String> categoryListModel;

    // Main Panel 
    private TextField searchProductField;
    private ProductGridPanel productGridPanel;

    // Right Panel 
    private TextField searchCustomerField;
    private Button addCustomerButton;
    private JPanel selectedCustomerLabel;
    private CustomTable selectedProductsTable;
    private DefaultTableModel selectedProductsTableModel;

    // Bottom Panel 
    private CustomLabel totalLabel;
    private CustomLabel totalBooksLabel;
    private Button checkoutButton;


    public POSGUI() {
        
        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        FrameUtils.setupFrame(this, "POS SYSTEM", 1200, 800);

        // Left Panel
        categoryListModel = new DefaultListModel<>();
        categoryList = new CategoryList(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    //onCategorySelected(selectedCategory);
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

        Button searchProductButton = new Button("Tìm kiếm");
        ColorScheme.styleButton(searchProductButton, false);
        //searchProductButton.addActionListener(e -> onSearchProduct());
        searchProductPanel.add(searchProductButton, BorderLayout.EAST);

        productMainPanel.add(searchProductPanel, BorderLayout.NORTH);

        productGridPanel = new ProductGridPanel();
        //productGridPanel.setProductClickListener(this::onProductClicked);
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

        searchCustomerField = new TextField();
        searchCustomerField.setPlaceholder("Tìm kiếm khách hàng...");
        ColorScheme.styleTextField(searchCustomerField);
        //searchCustomerField.addActionListener(e -> onSearchCustomer());
        searchCustomerPanel.add(searchCustomerField, BorderLayout.CENTER);

        Button searchCustomerButton = new Button("Tìm kiếm");
        ColorScheme.styleButton(searchCustomerButton, false);
        //searchCustomerButton.addActionListener(e -> onSearchCustomer());
        searchCustomerPanel.add(searchCustomerButton, BorderLayout.EAST);

        customerPanel.add(searchCustomerPanel, BorderLayout.CENTER);

        addCustomerButton = new Button("Thêm khách hàng");
        ColorScheme.styleButton(addCustomerButton, false);
        //addCustomerButton.addActionListener(e -> onAddCustomer());
        customerPanel.add(addCustomerButton, BorderLayout.EAST);

        rightPanel.add(customerPanel, BorderLayout.NORTH);

        String[] columns = {"Tên", "Số lượng", "Đơn giá", "Thành tiền", "Hành động"};
        selectedProductsTable = new CustomTable(columns);
        selectedProductsTableModel = (DefaultTableModel) selectedProductsTable.getModel();
        selectedProductsTable.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        selectedProductsTable.getColumn("Hành động").setCellEditor(new ButtonEditor());
        JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
        selectedProductsScrollPane.setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER));
        rightPanel.add(selectedProductsScrollPane, BorderLayout.CENTER);

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

    public static void main(String[] args) {
        new POSGUI();
    }
}