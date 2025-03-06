package com.bookstore.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BookStoreApp extends JFrame {
    // Khai báo các màu theo phương án 3
    private static final Color MAIN_BG = new Color(245, 245, 220);    // #F5F5DC (Kem nhạt)
    private static final Color HEADER_BG = new Color(128, 0, 32);     // #800020 (Đỏ burgundy)
    private static final Color BUTTON_BG = new Color(147, 112, 219);  // #9370DB (Tím nhạt)
    private static final Color TEXT_COLOR = new Color(51, 51, 51);    // #333333 (Xám đậm)
    private static final Color BORDER_COLOR = new Color(211, 211, 211); // #D3D3D3 (Xám nhạt)
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    public BookStoreApp() {
        setTitle("Nhà Sách Online");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Thiết lập giao diện chính
        setupUI();
    }
    
    private void setupUI() {
        // Tạo panel chính với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MAIN_BG);
        
        // Tạo phần header
        JPanel headerPanel = createHeaderPanel();
        
        // Tạo thanh menu bên trái
        JPanel menuPanel = createMenuPanel();
        
        // Tạo panel chính để chứa các trang
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(MAIN_BG);
        
        // Thêm các trang vào cardPanel
        cardPanel.add(createHomePage(), "home");
        cardPanel.add(createBookCatalogPage(), "catalog");
        cardPanel.add(createCartPage(), "cart");
        cardPanel.add(createAccountPage(), "account");
        
        // Tạo footer
        JPanel footerPanel = createFooterPanel();
        
        // Thêm các thành phần vào panel chính
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Thêm panel chính vào frame
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        
        // Logo và tên cửa hàng
        JLabel logoLabel = new JLabel("NHÀ SÁCH ONLINE");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        // Tạo thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(HEADER_BG);
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Tìm kiếm");
        styleButton(searchButton);
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(10, 1, 0, 10));
        menuPanel.setBackground(MAIN_BG);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        menuPanel.setPreferredSize(new Dimension(200, 600));
        
        // Tạo các nút menu
        String[] menuItems = {"Trang chủ", "Danh mục sách", "Sách mới", "Sách bán chạy", 
                            "Khuyến mãi", "Giỏ hàng", "Tài khoản", "Hỗ trợ", "Giới thiệu"};
        
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setHorizontalAlignment(SwingConstants.LEFT);
            menuButton.setBackground(MAIN_BG);
            menuButton.setForeground(TEXT_COLOR);
            menuButton.setFocusPainted(false);
            menuButton.setBorderPainted(false);
            menuButton.setFont(new Font("Arial", Font.PLAIN, 14));
            
            // Thêm sự kiện cho nút
            menuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String command = e.getActionCommand();
                    
                    if (command.equals("Trang chủ")) {
                        cardLayout.show(cardPanel, "home");
                    } else if (command.equals("Danh mục sách")) {
                        cardLayout.show(cardPanel, "catalog");
                    } else if (command.equals("Giỏ hàng")) {
                        cardLayout.show(cardPanel, "cart");
                    } else if (command.equals("Tài khoản")) {
                        cardLayout.show(cardPanel, "account");
                    }
                }
            });
            
            menuPanel.add(menuButton);
        }
        
        return menuPanel;
    }
    
    private JPanel createHomePage() {
        JPanel homePanel = new JPanel(new BorderLayout(10, 10));
        homePanel.setBackground(MAIN_BG);
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Banner khuyến mãi
        JPanel bannerPanel = new JPanel();
        bannerPanel.setPreferredSize(new Dimension(800, 200));
        bannerPanel.setBackground(new Color(240, 240, 220));
        bannerPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        JLabel bannerLabel = new JLabel("KHUYẾN MÃI THÁNG 3: GIẢM GIÁ ĐẾN 50% CHO SÁCH MỚI!");
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bannerLabel.setForeground(HEADER_BG);
        bannerPanel.add(bannerLabel);
        
        // Sách nổi bật
        JPanel featuredPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        featuredPanel.setBackground(MAIN_BG);
        
        // Tạo 3 sách nổi bật
        for (int i = 1; i <= 3; i++) {
            JPanel bookPanel = createBookPanel("Sách nổi bật " + i, "Tác giả " + i, 150000 + (i*20000));
            featuredPanel.add(bookPanel);
        }
        
        // Sách mới
        JPanel newBooksPanel = new JPanel(new BorderLayout());
        newBooksPanel.setBackground(MAIN_BG);
        
        JLabel newBooksTitle = new JLabel("SÁCH MỚI");
        newBooksTitle.setFont(new Font("Arial", Font.BOLD, 18));
        newBooksTitle.setForeground(TEXT_COLOR);
        
        JPanel booksGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        booksGrid.setBackground(MAIN_BG);
        
        // Tạo 8 sách mới
        for (int i = 1; i <= 8; i++) {
            JPanel bookPanel = createBookPanel("Sách mới " + i, "Tác giả " + i, 120000 + (i*10000));
            booksGrid.add(bookPanel);
        }
        
        newBooksPanel.add(newBooksTitle, BorderLayout.NORTH);
        newBooksPanel.add(booksGrid, BorderLayout.CENTER);
        
        // Thêm tất cả vào homePanel
        homePanel.add(bannerPanel, BorderLayout.NORTH);
        homePanel.add(featuredPanel, BorderLayout.CENTER);
        homePanel.add(newBooksPanel, BorderLayout.SOUTH);
        
        return homePanel;
    }
    
    private JPanel createBookCatalogPage() {
        JPanel catalogPanel = new JPanel(new BorderLayout(10, 10));
        catalogPanel.setBackground(MAIN_BG);
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tiêu đề trang
        JLabel titleLabel = new JLabel("DANH MỤC SÁCH");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        
        // Tạo thanh lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(MAIN_BG);
        
        JLabel filterLabel = new JLabel("Lọc theo: ");
        String[] categories = {"Tất cả", "Văn học", "Kinh tế", "Kỹ năng sống", "Ngoại ngữ", "Khoa học"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        
        JLabel sortLabel = new JLabel("Sắp xếp: ");
        String[] sortOptions = {"Mới nhất", "Bán chạy", "Giá tăng dần", "Giá giảm dần"};
        JComboBox<String> sortCombo = new JComboBox<>(sortOptions);
        
        filterPanel.add(filterLabel);
        filterPanel.add(categoryCombo);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(sortLabel);
        filterPanel.add(sortCombo);
        
        // Tạo lưới sách
        JPanel booksPanel = new JPanel(new GridLayout(3, 4, 20, 20));
        booksPanel.setBackground(MAIN_BG);
        
        // Tạo 12 sách
        for (int i = 1; i <= 12; i++) {
            String category = categories[i % categories.length];
            JPanel bookPanel = createBookPanel("Sách " + category + " " + i, 
                                             "Tác giả " + i, 
                                             100000 + (i*15000));
            booksPanel.add(bookPanel);
        }
        
        // Tạo panel chứa tất cả
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(MAIN_BG);
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(booksPanel, BorderLayout.CENTER);
        
        catalogPanel.add(titleLabel, BorderLayout.NORTH);
        catalogPanel.add(contentPanel, BorderLayout.CENTER);
        
        return catalogPanel;
    }
    
    private JPanel createCartPage() {
        JPanel cartPanel = new JPanel(new BorderLayout(10, 10));
        cartPanel.setBackground(MAIN_BG);
        cartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tiêu đề trang
        JLabel titleLabel = new JLabel("GIỎ HÀNG CỦA BẠN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        
        // Tạo bảng sản phẩm
        String[] columnNames = {"STT", "Tên sách", "Tác giả", "Đơn giá", "Số lượng", "Thành tiền", ""};
        Object[][] data = {
            {1, "Đắc Nhân Tâm", "Dale Carnegie", "150.000đ", 1, "150.000đ", "Xóa"},
            {2, "Nhà Giả Kim", "Paulo Coelho", "120.000đ", 2, "240.000đ", "Xóa"},
            {3, "Cây Cam Ngọt Của Tôi", "José Mauro de Vasconcelos", "130.000đ", 1, "130.000đ", "Xóa"}
        };
        
        JTable cartTable = new JTable(data, columnNames);
        cartTable.setRowHeight(40);
        cartTable.setBackground(MAIN_BG);
        cartTable.setGridColor(BORDER_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBackground(MAIN_BG);
        
        // Tạo panel tổng tiền và thanh toán
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(MAIN_BG);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JPanel pricePanel = new JPanel(new GridLayout(3, 2));
        pricePanel.setBackground(MAIN_BG);
        
        JLabel subtotalLabel = new JLabel("Tổng tiền sách:");
        JLabel subtotalValue = new JLabel("520.000đ");
        subtotalValue.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel shippingLabel = new JLabel("Phí vận chuyển:");
        JLabel shippingValue = new JLabel("30.000đ");
        shippingValue.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel totalLabel = new JLabel("Thành tiền:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel totalValue = new JLabel("550.000đ");
        totalValue.setFont(new Font("Arial", Font.BOLD, 16));
        totalValue.setHorizontalAlignment(SwingConstants.RIGHT);
        
        pricePanel.add(subtotalLabel);
        pricePanel.add(subtotalValue);
        pricePanel.add(shippingLabel);
        pricePanel.add(shippingValue);
        pricePanel.add(totalLabel);
        pricePanel.add(totalValue);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(MAIN_BG);
        
        JButton continueButton = new JButton("Tiếp tục mua sắm");
        JButton checkoutButton = new JButton("Thanh toán");
        
        styleButton(continueButton);
        styleButton(checkoutButton);
        
        buttonPanel.add(continueButton);
        buttonPanel.add(checkoutButton);
        
        summaryPanel.add(pricePanel, BorderLayout.CENTER);
        summaryPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        cartPanel.add(titleLabel, BorderLayout.NORTH);
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        cartPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        return cartPanel;
    }
    
    private JPanel createAccountPage() {
        JPanel accountPanel = new JPanel(new BorderLayout(10, 10));
        accountPanel.setBackground(MAIN_BG);
        accountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tiêu đề trang
        JLabel titleLabel = new JLabel("THÔNG TIN TÀI KHOẢN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        
        // Tạo form thông tin
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBackground(MAIN_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        JLabel nameLabel = new JLabel("Họ và tên:");
        JTextField nameField = new JTextField("Nguyễn Văn A");
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField("nguyenvana@email.com");
        
        JLabel phoneLabel = new JLabel("Số điện thoại:");
        JTextField phoneField = new JTextField("0901234567");
        
        JLabel addressLabel = new JLabel("Địa chỉ:");
        JTextField addressField = new JTextField("123 Đường ABC, Quận XYZ, TP. HCM");
        
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        JPasswordField passwordField = new JPasswordField("********");
        
        JLabel newPasswordLabel = new JLabel("Mật khẩu mới:");
        JPasswordField newPasswordField = new JPasswordField();
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(newPasswordLabel);
        formPanel.add(newPasswordField);
        
        // Tạo panel cho nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(MAIN_BG);
        
        JButton updateButton = new JButton("Cập nhật thông tin");
        styleButton(updateButton);
        
        buttonPanel.add(updateButton);
        
        // Tạo panel lịch sử đơn hàng
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(MAIN_BG);
        historyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), 
            "Lịch sử đơn hàng",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            TEXT_COLOR
        ));
        
        String[] columnNames = {"Mã đơn hàng", "Ngày mua", "Sản phẩm", "Tổng tiền", "Trạng thái"};
        Object[][] data = {
            {"DH001", "01/03/2025", "Đắc Nhân Tâm, Nhà Giả Kim", "390.000đ", "Đã giao"},
            {"DH002", "15/02/2025", "Cây Cam Ngọt Của Tôi", "130.000đ", "Đã giao"},
            {"DH003", "20/01/2025", "Tôi Thấy Hoa Vàng Trên Cỏ Xanh", "110.000đ", "Đã giao"}
        };
        
        JTable historyTable = new JTable(data, columnNames);
        historyTable.setRowHeight(40);
        historyTable.setBackground(MAIN_BG);
        historyTable.setGridColor(BORDER_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBackground(MAIN_BG);
        
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm các thành phần vào panel chính
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(MAIN_BG);
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(historyPanel, BorderLayout.SOUTH);
        
        accountPanel.add(titleLabel, BorderLayout.NORTH);
        accountPanel.add(contentPanel, BorderLayout.CENTER);
        
        return accountPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new GridLayout(1, 3));
        footerPanel.setBackground(new Color(245, 245, 230));
        footerPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, BORDER_COLOR));
        footerPanel.setPreferredSize(new Dimension(1000, 100));
        
        // Thông tin liên hệ
        JPanel contactPanel = new JPanel(new GridLayout(3, 1));
        contactPanel.setBackground(new Color(245, 245, 230));
        
        JLabel contactTitle = new JLabel("LIÊN HỆ");
        contactTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel phoneLabel = new JLabel("Điện thoại: 0901234567");
        JLabel emailLabel = new JLabel("Email: contact@nhasachonline.com");
        
        contactPanel.add(contactTitle);
        contactPanel.add(phoneLabel);
        contactPanel.add(emailLabel);
        
        // Thông tin cửa hàng
        JPanel storePanel = new JPanel(new GridLayout(3, 1));
        storePanel.setBackground(new Color(245, 245, 230));
        
        JLabel storeTitle = new JLabel("NHÀ SÁCH ONLINE");
        storeTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel addressLabel = new JLabel("Địa chỉ: 123 Đường ABC, Quận XYZ, TP. HCM");
        JLabel copyrightLabel = new JLabel("© 2025 Nhà Sách Online. All rights reserved.");
        
        storePanel.add(storeTitle);
        storePanel.add(addressLabel);
        storePanel.add(copyrightLabel);
        
        // Thông tin dịch vụ
        JPanel servicePanel = new JPanel(new GridLayout(3, 1));
        servicePanel.setBackground(new Color(245, 245, 230));
        
        JLabel serviceTitle = new JLabel("DỊCH VỤ");
        serviceTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel deliveryLabel = new JLabel("Giao hàng miễn phí cho đơn hàng từ 300.000đ");
        JLabel returnLabel = new JLabel("Đổi trả trong vòng 7 ngày");
        
        servicePanel.add(serviceTitle);
        servicePanel.add(deliveryLabel);
        servicePanel.add(returnLabel);
        
        // Thêm các panel vào footer
        footerPanel.add(contactPanel);
        footerPanel.add(storePanel);
        footerPanel.add(servicePanel);
        
        return footerPanel;
    }
    
    private JPanel createBookPanel(String title, String author, int price) {
        JPanel bookPanel = new JPanel(new BorderLayout(5, 5));
        bookPanel.setBackground(Color.WHITE);
        bookPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        // Hình ảnh sách
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(240, 240, 240));
        imagePanel.setPreferredSize(new Dimension(120, 150));
        JLabel imageLabel = new JLabel(title.substring(0, 1));
        imageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        imagePanel.add(imageLabel);
        
        // Thông tin sách
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel priceLabel = new JLabel(String.format("%,d", price) + "đ");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setForeground(HEADER_BG);
        
        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(priceLabel);
        
        // Nút mua ngay
        JButton buyButton = new JButton("Thêm vào giỏ");
        styleButton(buyButton);
        buyButton.setFont(new Font("Arial", Font.BOLD, 10));
        
        // Thêm các thành phần vào bookPanel
        bookPanel.add(imagePanel, BorderLayout.NORTH);
        bookPanel.add(infoPanel, BorderLayout.CENTER);
        bookPanel.add(buyButton, BorderLayout.SOUTH);
        
        return bookPanel;
    }
    
    private void styleButton(JButton button) {
        button.setBackground(BUTTON_BG);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_BG.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_BG);
            }
        });
    }
    
    public static void main(String[] args) {
        // Sử dụng look and feel của hệ điều hành
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookStoreApp().setVisible(true);
            }
        });
    }
}
