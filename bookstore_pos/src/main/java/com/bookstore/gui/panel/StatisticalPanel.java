package com.bookstore.gui.panel;

import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.util.ColorScheme;
import javax.swing.*;

import java.time.LocalDateTime;
import java.util.Date;

import java.awt.*;
import java.io.File;
import java.util.Calendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import com.bookstore.controller.StatisticalController;

public class StatisticalPanel extends JPanel{
    private StatisticalController controller = new StatisticalController();

    private JRadioButton customerRadio, productRadio, userRadio;

    private JPanel topAndAllPanel, searchPanel, datePanel;
    private TextField searchField;
    private JRadioButton rbNgay, rbQuy, rbKy;
    private JRadioButton top, all;
    private JLabel dateFrom, dateTo;
    private JSpinner toDatePicker, fromDatePicker;
    JComboBox<String> threeMonthComboBox, fourMonthComboBox, monthComboBox;

    private JPanel contentPanel;
    private JPanel datPanel;
    private JRadioButton chartRadio, tableRadio;
    private JButton exportPDF, searchButton;
    private JPanel tablePanel;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public StatisticalPanel() {
        setLayout(new BorderLayout());
        initComponentsCenter();
        initComponentsEast();
        initComponentsSouth();
    }

    private void initComponentsCenter() {
        JPanel hPanel = new JPanel(new BorderLayout());
        hPanel.setBackground(Color.GREEN);

        JPanel mPanel = new JPanel(new BorderLayout());
        
        mPanel.setBackground(Color.CYAN);
        

        // Chọn sản phẩm, khách hàng, nhân viên
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productRadio = new JRadioButton("Sản phẩm", true);
        customerRadio = new JRadioButton("Khách hàng");
        userRadio = new JRadioButton("Nhân viên");

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(productRadio);
        typeGroup.add(customerRadio);
        typeGroup.add(userRadio);
        
        menuPanel.add(productRadio);
        menuPanel.add(customerRadio);
        menuPanel.add(userRadio);
        menuPanel.setBackground(Color.LIGHT_GRAY);
        

        menuPanel.setPreferredSize(new Dimension(900, 50));
        mPanel.add(menuPanel, BorderLayout.NORTH);

        // Thanh tìm kiếm
        searchPanel = new JPanel(new GridLayout(3, 0));

        top = new JRadioButton("Top", true);
        all = new JRadioButton("Tất cả");
        ButtonGroup topAndAllGroup = new ButtonGroup();
        topAndAllGroup.add(top);
        topAndAllGroup.add(all);

        searchField = new TextField();
        searchField.setPlaceholder("Tìm kiếm");
        searchField.setPreferredSize(new Dimension(200, 30));
        ColorScheme.styleTextField(searchField); 

        searchButton = new Button("Tìm kiếm");


        rbNgay = new JRadioButton("Ngày", true);
        rbNgay.setBounds(50, 30, 100, 30);

        rbQuy = new JRadioButton("Quý(3 tháng)");
        rbQuy.setBounds(50, 70, 100, 30);

        rbKy = new JRadioButton("Kỳ(4 tháng)");
        rbKy.setBounds(50, 110, 100, 30);

        ButtonGroup group = new ButtonGroup();
        group.add(rbNgay);
        group.add(rbQuy);
        group.add(rbKy);

        topAndAllPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topAndAllPanel.add(top);
        topAndAllPanel.add(all);
        // Chọn ngày
        dateFrom = new JLabel("Từ ngày:");
        fromDatePicker = new JSpinner(new SpinnerDateModel());
        fromDatePicker.setEditor(new JSpinner.DateEditor(fromDatePicker, "dd/MM/yyyy"));
        fromDatePicker.setValue(new Date(110, Calendar.JANUARY, 1)); // Set default value to 01/01/2010
        
        dateTo = new JLabel("Đến ngày:");
        toDatePicker = new JSpinner(new SpinnerDateModel());
        toDatePicker.setEditor(new JSpinner.DateEditor(toDatePicker, "dd/MM/yyyy")); 
        
        // Chọn quý
        threeMonthComboBox = new JComboBox<>(new String[]{"Không", "Quý 1", "Quý 2", "Quý 3", "Quý 4"});
        threeMonthComboBox.setPreferredSize(new Dimension(200, 30));
        monthComboBox = new JComboBox<>();
        for (int year = 2025; year >= 2010; year--) {
            monthComboBox.addItem(String.valueOf(year));
        }
        monthComboBox.setPreferredSize(new Dimension(70, 30));
        // Chọn kỳ
        fourMonthComboBox = new JComboBox<>(new String[]{"Không", "Kỳ 1", "Kỳ 2", "Kỳ 3"});
        fourMonthComboBox.setPreferredSize(new Dimension(200, 30));

        JPanel rbJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbJPanel.add(rbNgay);
        rbJPanel.add(rbQuy);
        rbJPanel.add(rbKy);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(dateFrom);
        datePanel.add(fromDatePicker);
        datePanel.add(dateTo);
        datePanel.add(toDatePicker);
        datePanel.add(searchButton);

        searchPanel.add(topAndAllPanel);
        
        // searchPanel.add(searchField);
        searchPanel.add(rbJPanel);
        searchPanel.add(datePanel);
        
        searchPanel.setBackground(Color.RED);
        
        searchPanel.setPreferredSize(new Dimension(700, 250));
        mPanel.add(searchPanel, BorderLayout.CENTER);

        mPanel.setPreferredSize(new Dimension(700, 300));
        this.add(mPanel, BorderLayout.CENTER);
        
        all.addActionListener(e -> {
            System.out.println("Tất cả");
            topAndAllPanel.add(searchField);
            removeChartTableRadio();
            removeTablePanel();
            topAndAllPanel.revalidate();
            topAndAllPanel.repaint();
        });

        top.addActionListener(e -> {
            System.out.println("Top");
            topAndAllPanel.remove(searchField);
            addChartTableRadio();
            removeTablePanel();
            topAndAllPanel.revalidate();
            topAndAllPanel.repaint();
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        rbNgay.addActionListener(e -> {
            datePanel.remove(monthComboBox);
            datePanel.remove(fourMonthComboBox);
            datePanel.remove(threeMonthComboBox);          
            datePanel.add(dateFrom);
            datePanel.add(fromDatePicker);
            datePanel.add(dateTo);
            datePanel.add(toDatePicker);
            datePanel.add(searchButton);
            datePanel.revalidate();
            datePanel.repaint();
        });

        rbQuy.addActionListener(e -> {
            datePanel.remove(fourMonthComboBox);
            datePanel.remove(dateFrom);
            datePanel.remove(fromDatePicker);
            datePanel.remove(dateTo);
            datePanel.remove(toDatePicker);  
            datePanel.add(threeMonthComboBox);
            datePanel.add(monthComboBox);
            datePanel.add(searchButton);
            datePanel.revalidate();
            datePanel.repaint();
        });

        rbKy.addActionListener(e -> {
            datePanel.remove(threeMonthComboBox);
            datePanel.remove(dateFrom);
            datePanel.remove(fromDatePicker);
            datePanel.remove(dateTo);
            datePanel.remove(toDatePicker);
            datePanel.add(fourMonthComboBox);
            datePanel.add(monthComboBox);
            datePanel.add(searchButton);
            datePanel.revalidate();
            datePanel.repaint();
        });

        // Gán sự kiện
        customerRadio.addActionListener(e -> handleTypeSelection("Khách hàng"));
        productRadio.addActionListener(e -> handleTypeSelection("Sản phẩm"));
        userRadio.addActionListener(e -> handleTypeSelection("Nhân viên"));
        searchButton.addActionListener(e -> {
            controller.buttonSearch(this);
        });
    }

    private void initComponentsEast() {
        // Thống kê
        JPanel StatisticalPanel = new JPanel();
        StatisticalPanel.setLayout(new BoxLayout(StatisticalPanel, BoxLayout.Y_AXIS));
        controller.displayQuantityCustomer(StatisticalPanel);
        controller.displayQuantityOrder(StatisticalPanel);
        controller.displayQuantityProduct(StatisticalPanel);
        StatisticalPanel.setBackground(Color.YELLOW);
        
        StatisticalPanel.setPreferredSize(new Dimension(500, 0));
        this.add(StatisticalPanel, BorderLayout.EAST);
    }

    private void initComponentsSouth() {
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel chứa các radio buttons và nút export
        datPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datPanel.setBackground(Color.BLUE);
        chartRadio = new JRadioButton("Biểu đồ", true);
        tableRadio = new JRadioButton("Bảng");
        exportPDF = new JButton("Xuất PDF");

        ButtonGroup datGroup = new ButtonGroup();
        datGroup.add(chartRadio);
        datGroup.add(tableRadio);

        datPanel.add(chartRadio);
        datPanel.add(tableRadio);
        datPanel.add(exportPDF);

        // Thêm datPanel vào contentPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        contentPanel.add(datPanel, gbc);

        // Panel chứa bảng/biểu đồ
        tablePanel = new JPanel();
        tablePanel.setBackground(Color.ORANGE);
        tablePanel.setPreferredSize(new Dimension(1200, 400));
    
        // Thêm tablePanel vào contentPanel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(tablePanel, gbc);
        
        contentPanel.setPreferredSize(new Dimension(0, 500));
        this.add(contentPanel, BorderLayout.SOUTH);

        chartRadio.addActionListener(e -> {
            if (productRadio.isSelected()) {
                controller.loadProductsChart(this);
            } else if (customerRadio.isSelected()) {
                controller.loadCustomersChart(this);
            } else if (userRadio.isSelected()) {
                controller.loadUsersChart(this);
            }
        });
        tableRadio.addActionListener(e -> {
            if (productRadio.isSelected()) {
                controller.loadProductsTable(this);
            } else if (customerRadio.isSelected()) {
                controller.loadCustomersTable(this);
            } else if (userRadio.isSelected()) {
                controller.loadUsersTable(this);
            }
        });
        
    }

    // Các sự kiện
    private void handleTypeSelection(String type) {
        chartRadio.setEnabled(true);
        tableRadio.setEnabled(true);
        chartRadio.setSelected(true);

        // Reset searchPanel
        resetSearchPanel();
        removeTablePanel();
        
        // switch (type) {
        //     case "Khách hàng":
        //         controller.loadCustomersChart(this);
        //         break;
        //     case "Sản phẩm":
        //         controller.loadProductsChart(this);
        //         break;
        //     case "Nhân viên":
        //         controller.loadUsersChart(this);
        //         break;
        // }
        
    }

    public void updateTableCustomersTable(Object[][] data) {
        tablePanel.removeAll();
        String[] columnNames = {"ID", "Tên khách hàng", "Số điện thoại", "Điểm thưởng", "Tổng chi tiêu"};
        CustomTable table = new CustomTable(columnNames);
        table.refreshTable(data);
        table.setPreferredSize(new Dimension(1200, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void updateTableProductsTable(Object[][] data, Object[][] data1) {
        tablePanel.removeAll();
        String[] columnNames = {"ID", "Ảnh", "Tên sản phẩm", "Giá", "Số lượng đã bán", "Doanh thu"};
    
        // Tạo custom table với render hình ảnh
        CustomTable table = new CustomTable(columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) { // Cột hình ảnh
                    return ImageIcon.class;
                }
                return Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit
            }
        };

        // Xử lý dữ liệu và hình ảnh
        for (Object[] row : data) {
            String imagePath = (String) row[1];
            ImageIcon imageIcon;
            
            if (imagePath != null && !imagePath.isEmpty() && new File(imagePath).exists()) {
                imageIcon = new ImageIcon(imagePath);
            } else {
                imageIcon = new ImageIcon(getClass().getResource("/product/default_img.png"));
            }

            // Resize ảnh
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            row[1] = new ImageIcon(scaledImage);
        }

        // Cập nhật dữ liệu cho bảng
        table.refreshTable(data);
        
        // Set row height để hiển thị ảnh đẹp hơn
        table.setRowHeight(55);

        // Đặt kích thước cho các cột
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  // Ảnh
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Tên
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Giá
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Số lượng
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Doanh thu
        
        // Thêm table vào scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);

        String[] columnNames1 = {"ID", "Tên danh mục", "Số lượng đã bán"};
        CustomTable table1 = new CustomTable(columnNames1);
        table1.setPreferredSize(new Dimension(1200, 400));
        table1.refreshTable(data1);
        JScrollPane scrollPane1 = new JScrollPane(table1);
        tablePanel.add(scrollPane1);
        
        // Refresh giao diện
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void updateTableUsersTable(Object[][] data) {
        tablePanel.removeAll();
        String[] columnNames = {"ID", "Username", "Password", "Tổng doanh thu đã tạo"};
        CustomTable table = new CustomTable(columnNames);
        table.setPreferredSize(new Dimension(1200, 400));
        table.refreshTable(data);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void updateTableCustomersChart(DefaultCategoryDataset data1, DefaultCategoryDataset data2) {
        tablePanel.removeAll();
        JFreeChart chartPoint = ChartFactory.createBarChart(
            "Top 5 Khách hàng",
            "Điểm thưởng", 
            "Khách hàng", 
            data1
        );

        JFreeChart chartTotalSpent = ChartFactory.createBarChart(
            "Top 5 khách hàng", 
            "Tổng chi", 
            "Khách hàng", 
            data2
        );

        JPanel chartPanel1 = new ChartPanel(chartPoint);
        JPanel chartPanel2 = new ChartPanel(chartTotalSpent);
        chartPanel1.setPreferredSize(new Dimension(550, 350)); 
        chartPanel2.setPreferredSize(new Dimension(550, 350)); 
        tablePanel.add(chartPanel1);
        tablePanel.add(chartPanel2);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void updateTableProductsChart(DefaultCategoryDataset data1, DefaultCategoryDataset data2, DefaultCategoryDataset data3) {
        tablePanel.removeAll();
        JFreeChart chartRevenue = ChartFactory.createBarChart(
            "Top 5 sản phẩm", 
            "Số lượng bán", 
            "Sản phẩm", 
            data1
            );

        JFreeChart chartQuantity = ChartFactory.createBarChart(
            "Top 5 sản phẩm", 
            "Doanh thu", 
            "Sản phẩm", 
            data2
            );

        JFreeChart chartCategory = ChartFactory.createBarChart(
            "Top 5 danh mục", 
            "Số lượng bán", 
            "Thể loại", 
            data3
            );

        JPanel chartPanel1 = new ChartPanel(chartRevenue);
        JPanel chartPanel2 = new ChartPanel(chartQuantity);
        JPanel chartPanel3 = new ChartPanel(chartCategory);
        
        chartPanel1.setPreferredSize(new Dimension(550, 350)); 
        chartPanel2.setPreferredSize(new Dimension(550, 350));
        chartPanel3.setPreferredSize(new Dimension(550, 350)); 
        tablePanel.add(chartPanel1);
        tablePanel.add(chartPanel2);
        tablePanel.add(chartPanel3);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void updateTableUsersChart(DefaultCategoryDataset data) {
        tablePanel.removeAll();
        chart = ChartFactory.createBarChart(
            "Top 5 Nhân viên",       // Tiêu đề biểu đồ
            "Danh thu đã tạo",                // Nhãn trục X
            "Nhân viên",            // Nhãn trục Y
            data // Dữ liệu
        );

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400)); // Tăng kích thước
        tablePanel.add(chartPanel);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void printTableProduct(Object[][] data) {
        tablePanel.removeAll();

        String[] columnNames = {"ID", "Tên sản phẩm", "Giá", "Số lượng đã bán", "Doanh thu"};
    
        // Tạo custom table với render hình ảnh
        CustomTable table = new CustomTable(columnNames);
            
        // Cập nhật dữ liệu cho bảng
        table.refreshTable(data);

        // Refresh giao diện
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    
    public void printTableCustomer(Object[][] data, String total, String str) {
        tablePanel.removeAll();
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        String[] col = {"Sản phẩm", "Số lượng mua"};
        CustomTable table = new CustomTable(col);

        JLabel totalLabel = new JLabel("Tổng doanh thu: " + total);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel stringLabel = new JLabel(str);
        stringLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        stringLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Add stringLabel to the top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tablePanel.add(stringLabel, gbc);

        // Add table to the center
        table.refreshTable(data);
        JScrollPane scrollPane = new JScrollPane(table);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        tablePanel.add(scrollPane, gbc);

        // Add totalLabel to the bottom
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tablePanel.add(totalLabel, gbc);

        // Refresh the panel
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void printTableUser(Object[][] data, String total, String str) {
        tablePanel.removeAll();
        String[] col = {"Sản phẩm", "Số lượng bán"};
        CustomTable table = new CustomTable(col);
        table.refreshTable(data);

        JLabel totalLabel = new JLabel("Tổng doanh thu: " + total);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Thêm table vào scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(totalLabel, BorderLayout.SOUTH);

        // Refresh giao diện
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void removeContent() {
        contentPanel.removeAll();
    }

    public void resetSearchPanel() {
        searchField.setText("");
        top.setSelected(true);  
        resetDatePanel();
        addChartTableRadio();  
        topAndAllPanel.remove(searchField);
        topAndAllPanel.revalidate();
        topAndAllPanel.repaint();
    }

    public void resetDatePanel() {
        rbNgay.setSelected(true);
        datePanel.remove(monthComboBox);
        datePanel.remove(fourMonthComboBox);
        datePanel.remove(threeMonthComboBox);          
        datePanel.add(dateFrom);
        datePanel.add(fromDatePicker);
        datePanel.add(dateTo);
        datePanel.add(toDatePicker);
        datePanel.add(searchButton);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void removeChartTableRadio() {
        datPanel.remove(chartRadio);
        datPanel.remove(tableRadio);
        datPanel.add(exportPDF);
        datPanel.revalidate();
        datPanel.repaint();
    }

    public void addChartTableRadio() {
        datPanel.add(chartRadio);
        datPanel.add(tableRadio);
        datPanel.add(exportPDF);
        datPanel.revalidate();
        datPanel.repaint();
    }

    public void removeTablePanel() {
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    // Getter
    public JPanel getTablePanel() {
        return tablePanel;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
    
    public JRadioButton getProductRadio() {
        return productRadio;
    }
    
    public JRadioButton getCustomerRadio() {
        return customerRadio;
    }
    
    public JRadioButton getUserRadio() {
        return userRadio;
    }
    
    public JRadioButton getChartRadio() {
        return chartRadio;
    }

    public JRadioButton getTopRadio() {
        return top;
    }
    
    public JRadioButton getAllRadio() {
        return all;
    }

    public JRadioButton getRbNgay() {
        return rbNgay;
    }

    public JRadioButton getRbQuy() {
        return rbQuy;
    }

    public JRadioButton getRbKy() {
        return rbKy;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public LocalDateTime getDateFromValue() {
        Date date = (Date) fromDatePicker.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getDateToValue() {
        Date date = (Date) toDatePicker.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

    public JComboBox<String> getThreeMonthComboBox() {
        return threeMonthComboBox;
    }

    public JComboBox<String> getFourMonthComboBox() {
        return fourMonthComboBox;
    }

    public JComboBox<String> getMonthComboBox() {
        return monthComboBox;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Statistical Panel");
        StatisticalPanel panel = new StatisticalPanel();
        frame.add(panel);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
