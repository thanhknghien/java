package com.bookstore.gui.main;

import com.bookstore.BUS.PermissionService;
import javax.swing.*;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.panel.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.User;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.bookstore.controller.MainFrameController;
import com.bookstore.controller.PermissionController;

import java.awt.*;
import java.sql.SQLException;
public class MainFrame extends JFrame {
    private JPanel westPanel;
    private JPanel centerPanel;

    private JPanel tkPanel;
    private JLabel username;

    private JPanel pessPanel;
    private Button posGUI;
    private Button categoryButton;
    private Button customerButton;
    private Button userButton;
    private Button statiscalButton;
    private Button productButton;
    private Button permissionButton;

    private JFrame PosGUI;
    private JPanel statiscalPanel;
    private JPanel categoryPanel;
    private JPanel customerPanel;
    private JPanel userPanel;
    private JPanel productPanel;
    private JPanel permissionPanel;
    
    private Button logout;

    private MainFrameController controller;
    


    public MainFrame(User user) throws SQLException{
        initComponents(user);
        setTitle("Quản lý bán hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents(User user) throws SQLException {
        setSize(1550, 850);
        setResizable(false);
        setLayout(new BorderLayout());

        westPanel = new JPanel();
        this.add(westPanel, BorderLayout.WEST);
        centerPanel = new JPanel();
        this.add(centerPanel, BorderLayout.CENTER);

        westPanel.setPreferredSize(new Dimension(300, 800));
        centerPanel.setPreferredSize(new Dimension(1200, 800));

        Border thickBorder = new LineBorder(ColorScheme.BORDER, 2);
        

        tkPanel = new JPanel();
        tkPanel.setLayout(new BoxLayout(tkPanel, BoxLayout.Y_AXIS));

        tkPanel.setPreferredSize(new Dimension(300, 100));

        tkPanel.setBorder(thickBorder);
        JPanel userPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        userPanel1.setPreferredSize(new Dimension(270, 40));
        username = new JLabel("Xin chào: " + user.getUsername());
        username.setFont(new Font("Arial", Font.BOLD, 20));
        userPanel1.add(username);
        logout = new Button("Đăng xuất");
        setupButton(logout);
        
        tkPanel.add(Box.createVerticalStrut(10));
        tkPanel.add(userPanel1);
        tkPanel.add(Box.createVerticalStrut(10));
        tkPanel.add(logout);
        
        pessPanel = new JPanel();
        pessPanel.setPreferredSize(new Dimension(300, 700));
        pessPanel.setBorder(thickBorder);
        pessPanel.setLayout(new BoxLayout(pessPanel, BoxLayout.Y_AXIS));

        posGUI = new Button("Tạo hóa đơn");
        setupButton(posGUI);
        categoryButton = new Button("Quản lý danh mục");
        setupButton(categoryButton);
        customerButton = new Button("Quản lý khách hàng");
        setupButton(customerButton);
        userButton = new Button("Quản lý người dùng");
        setupButton(userButton);
        statiscalButton = new Button("Thống kê");
        setupButton(statiscalButton);
        productButton = new Button("Quản lý sản phẩm");
        setupButton(productButton);
       // ImageIcon icon = new ImageIcon("path/to/your/image.png"); // Thay đường dẫn ảnh của bạn
        permissionButton = new Button("Quản lý quyền", "D:QLBH2/java/bookstore_pos/src/main/java/com/bookstore/gui/util/PhanQuyen.png");
        setupButton(permissionButton);

        try {
            PosGUI = new POSGUI(user);
        }catch (Exception e) {
            System.out.println("Lỗi khởi tạo POSGUI: " + e.getMessage());
        }
        statiscalPanel = new StatisticalPanel();
        categoryPanel = new CategoryPanel();
        customerPanel = new CustomerPanel();
        try {
            userPanel = new UserManagementPanel();
        }catch (Exception e) {
            System.out.println("Lỗi khởi tạo USERMANAGEMENTPANEL: " + e.getMessage());
        }
        productPanel = new ProductPanel();
        try {
            permissionPanel = new PermissionPanel();
            PermissionService permissionService = new PermissionService();
            new PermissionController((PermissionPanel) permissionPanel, permissionService);
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo PermissionPanel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể tải panel Quản lý quyền: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            permissionPanel = new JPanel(); // Tạo panel rỗng để tránh lỗi null
        }
        
        
        westPanel.add(tkPanel);
        westPanel.add(pessPanel);

        controller = new MainFrameController();
        addActionListenerLogoutButton();
        controller.showButton(this);

    }

    public void addPosGUI() {   
        pessPanel.add(Box.createVerticalStrut(10));   
        pessPanel.add(posGUI);
        posGUI.addActionListener(controller.addActionListener(PosGUI, this));
    }

    public void addCategoryButton() { 
        pessPanel.add(Box.createVerticalStrut(10));
        pessPanel.add(categoryButton);
        categoryButton.addActionListener(controller.addActionListener(categoryPanel, centerPanel));
    }

    public void addCustomerButton() {
        pessPanel.add(Box.createVerticalStrut(10));
        pessPanel.add(customerButton);
        customerButton.addActionListener(controller.addActionListener(customerPanel, centerPanel));
    }

    public void addUserButton() {
        pessPanel.add(Box.createVerticalStrut(10));
        pessPanel.add(userButton);
        userButton.addActionListener(controller.addActionListener(userPanel, centerPanel));
    }

    public void addStatisticalButton() {
        pessPanel.add(Box.createVerticalStrut(10));
        pessPanel.add(statiscalButton);
        statiscalButton.addActionListener(controller.addActionListener(statiscalPanel, centerPanel));
    }

    public void addProductButton() {   
        pessPanel.add(Box.createVerticalStrut(10));
        pessPanel.add(productButton);
        productButton.addActionListener(controller.addActionListener(productPanel, centerPanel));    
    }

    public void addPermissonButton() {
        pessPanel.add(Box.createVerticalStrut(10));
        
        pessPanel.add(permissionButton);
        System.out.println("Đã thêm nút Quản lý quyền");
        permissionButton.addActionListener(controller.addActionListener(permissionPanel, centerPanel));   
    }

    public void addActionListenerLogoutButton() {
        logout.addActionListener(controller.logoutActionListener(this));
    }

    private void setupButton(Button button) {
        Dimension size = new Dimension(270, 40);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

}
