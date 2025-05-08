package com.bookstore.gui.main;

import javax.swing.*;
import com.bookstore.gui.component.Button;
import javax.swing.border.Border;
import com.bookstore.controller.MainFrameController;

import java.awt.*;
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

    private Button logout;

    private MainFrameController controller;
    public MainFrame() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setSize(1500, 800);
        setLayout(new BorderLayout());

        westPanel = new JPanel();
        this.add(westPanel, BorderLayout.WEST);
        centerPanel = new JPanel();
        this.add(centerPanel, BorderLayout.CENTER);

        westPanel.setPreferredSize(new Dimension(300, 800));
        centerPanel.setPreferredSize(new Dimension(1200, 800));

        Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
        westPanel.setBorder(blackLine);

        // Add tkPanel to westPanel
        tkPanel = new JPanel(new GridLayout(0, 1, 10, 20));
        tkPanel.setBorder(blackLine);
        username = new JLabel("UserName: ");
        logout = new Button("Đăng xuất");
        logout.setPreferredSize(new Dimension(270, 40));
        tkPanel.add(username);
        tkPanel.add(logout);

        // Add pessPanel to westPanel
        pessPanel = new JPanel(new GridLayout(0, 1, 0, 20));
        posGUI = new Button("Tạo hóa đơn");
        posGUI.setPreferredSize(new Dimension(270, 40));


        categoryButton = new Button("Quản lý danh mục");
        categoryButton.setPreferredSize(new Dimension(270, 40));
        customerButton = new Button("Quản lý khách hàng");
        customerButton.setPreferredSize(new Dimension(270, 40));
        userButton = new Button("Quản lý người dùng");
        userButton.setPreferredSize(new Dimension(270, 40));
        productButton = new Button("Quản lý sản phẩm");
        productButton.setPreferredSize(new Dimension(270, 40));
        statiscalButton = new Button("Thống kê");
        statiscalButton.setPreferredSize(new Dimension(270, 40));

        controller = new MainFrameController();
        controller.showButton(this);

        westPanel.add(tkPanel);
        westPanel.add(pessPanel);
        tkPanel.setPreferredSize(new Dimension(300, 100));
        pessPanel.setPreferredSize(new Dimension(300, 600));

    }

    public void addPosGUI() {
        posGUI = new Button("Tạo hóa đơn");
        posGUI.setPreferredSize(new Dimension(270, 40));
        pessPanel.add(posGUI);
    }

    public void addCategoryButton() {
        categoryButton = new Button("Quản lý danh mục");
        categoryButton.setPreferredSize(new Dimension(270, 40));
        pessPanel.add(categoryButton);
    }

    public void addCustomerButton() {
        customerButton = new Button("Quản lý khách hàng");
        customerButton.setPreferredSize(new Dimension(270, 40));
        pessPanel.add(customerButton);
    }

    public void addUserButton() {
        userButton = new Button("Quản lý người dùng");
        userButton.setPreferredSize(new Dimension(270, 40));
        pessPanel.add(userButton);
    }

    public void addStatisticalButton() {
        statiscalButton = new Button("Thống kê");
        statiscalButton.setPreferredSize(new Dimension(270, 40));
        pessPanel.add(statiscalButton);
    }

    public void addProductButton() {
        productButton = new Button("Quản lý sản phẩm");
        productButton.setPreferredSize(new Dimension(270, 40));
        pessPanel.add(productButton);
    }


    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);

    }
}
