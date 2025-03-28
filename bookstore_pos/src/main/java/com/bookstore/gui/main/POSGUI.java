package com.bookstore.gui.main;

import javax.swing.*;
import java.awt.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.PanelCover;

public class POSGUI extends JFrame {
    private PanelCover mainPanel;
    private PanelCover leftPanel;
    private PanelCover rightPanel;
    private PanelCover topPanel;
    
    // Components
    private JLabel titleLabel;
    private Button searchButton;
    private TextField searchField;
    private JTable productTable;
    private JTable cartTable;
    private Button checkoutButton;
    private JLabel totalLabel;
    
    public POSGUI() {
        initComponents();
        setupLayout();
        setupStyling();
        FrameUtils.setupFrame(this, "Hệ Thống Bán Hàng", 1200, 800);
    }
    
    private void initComponents() {
        // Khởi tạo các panel chính
        mainPanel = new PanelCover();
        leftPanel = new PanelCover();
        rightPanel = new PanelCover();
        topPanel = new PanelCover();
        
        // Khởi tạo các components
        titleLabel = new JLabel("Hệ Thống Bán Hàng");
        searchField = new TextField();
        searchButton = new Button("Tìm Kiếm");
        productTable = new JTable();
        cartTable = new JTable();
        checkoutButton = new Button("Thanh Toán");
        totalLabel = new JLabel("Tổng Tiền: 0đ");
        
        // Thêm các components vào panel
        topPanel.add(titleLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        
        leftPanel.add(new JScrollPane(productTable));
        
        rightPanel.add(new JScrollPane(cartTable));
        rightPanel.add(totalLabel);
        rightPanel.add(checkoutButton);
    }
    
    private void setupLayout() {
        // Layout chính
        setLayout(new BorderLayout());
        
        // Layout cho top panel
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        // Layout cho left panel
        leftPanel.setLayout(new BorderLayout());
        
        // Layout cho right panel
        rightPanel.setLayout(new BorderLayout());
        
        // Thêm các panel vào main panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Thêm main panel vào frame
        add(mainPanel);
    }
    
    private void setupStyling() {
        // Styling cho frame
        ColorScheme.styleFrame(this);
        
        // Styling cho các components
        ColorScheme.styleTitleLabel(titleLabel);
        ColorScheme.styleTextField(searchField);
        ColorScheme.styleButton(searchButton, false);
        ColorScheme.styleButton(checkoutButton, true);
        
        // Styling cho các panel
        leftPanel.setBackground(ColorScheme.SURFACE);
        rightPanel.setBackground(ColorScheme.SURFACE);
        topPanel.setBackground(ColorScheme.PRIMARY);
        
        // Styling cho tables
        productTable.setBackground(ColorScheme.SURFACE);
        productTable.setForeground(ColorScheme.TEXT_PRIMARY);
        productTable.setGridColor(ColorScheme.BORDER);
        productTable.setSelectionBackground(ColorScheme.PRIMARY);
        productTable.setSelectionForeground(ColorScheme.TEXT_LIGHT);
        
        cartTable.setBackground(ColorScheme.SURFACE);
        cartTable.setForeground(ColorScheme.TEXT_PRIMARY);
        cartTable.setGridColor(ColorScheme.BORDER);
        cartTable.setSelectionBackground(ColorScheme.PRIMARY);
        cartTable.setSelectionForeground(ColorScheme.TEXT_LIGHT);
        
        // Styling cho total label
        ColorScheme.styleLabel(totalLabel, true);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }

    public static void main(String[] args) {
        // Chạy ứng dụng trong Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                POSGUI pos = new POSGUI();
                pos.setVisible(true);
            }
        });
    }
}