package com.bookstore.gui.panel;

import com.bookstore.gui.component.CustomTable;
import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.component.PanelCover;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.border.Border;
import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import com.bookstore.controller.StatisticalController;

public class StatisticalPanel extends PanelCover {
    private StatisticalController controller = new StatisticalController();

    private JRadioButton customerRadio, productRadio, userRadio;

    private JPanel topAndAllPanel, searchPanel, datePanel;
    private TextField searchField;
    private JRadioButton rbNgay, rbQuy, rbKy;
    private JRadioButton top, all;
    private JLabel dateFrom, dateTo;
    private JSpinner toDatePicker, fromDatePicker;
    private JComboBox<String> threeMonthComboBox, fourMonthComboBox, monthComboBox;

    private JPanel contentPanel;
    private JPanel datPanel;
    private JRadioButton chartRadio, tableRadio;
    private JButton exportPDF, searchButton;
    private JPanel tablePanel;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public StatisticalPanel() {
        super();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponentsCenter();
        initComponentsEast();
        initComponentsSouth();
    }

    private void initComponentsCenter() {
        JPanel mPanel = new JPanel(new BorderLayout());

        // Chọn sản phẩm, khách hàng, nhân viên
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        productRadio = new JRadioButton("Sản phẩm", true);
        productRadio.setFont(new Font(null, Font.PLAIN, 18));
        customerRadio = new JRadioButton("Khách hàng");
        customerRadio.setFont(new Font(null, Font.PLAIN, 18));
        userRadio = new JRadioButton("Nhân viên");
        userRadio.setFont(new Font(null, Font.PLAIN, 18));

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(productRadio);
        typeGroup.add(customerRadio);
        typeGroup.add(userRadio);
        
        menuPanel.add(productRadio);
        menuPanel.add(customerRadio);
        menuPanel.add(userRadio);

        menuPanel.setPreferredSize(new Dimension(900, 50));
        mPanel.add(menuPanel, BorderLayout.NORTH);

        // Thanh tìm kiếm
        searchPanel = new JPanel(new GridLayout(3, 0));

        top = new JRadioButton("Top", true);
        top.setFont(new Font(null, Font.PLAIN, 18));
        all = new JRadioButton("Tất cả");
        all.setFont(new Font(null, Font.PLAIN, 18));
        ButtonGroup topAndAllGroup = new ButtonGroup();
        topAndAllGroup.add(top);
        topAndAllGroup.add(all);

        searchField = new TextField();
        searchField.setPlaceholder("Tìm kiếm theo ID");
        searchField.setPreferredSize(new Dimension(200, 30));
        ColorScheme.styleTextField(searchField); 

        searchButton = new Button("Tìm kiếm");
        searchButton.setFont(new Font(null, Font.PLAIN, 18));

        rbNgay = new JRadioButton("Ngày", true);
        rbNgay.setFont(new Font(null, Font.PLAIN, 18));
        rbNgay.setBounds(50, 30, 100, 30);

        rbQuy = new JRadioButton("Quý(3 tháng)");
        rbQuy.setFont(new Font(null, Font.PLAIN, 18));
        rbQuy.setBounds(50, 70, 100, 30);

        rbKy = new JRadioButton("Kỳ(4 tháng)");
        rbKy.setFont(new Font(null, Font.PLAIN, 18));
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
        dateFrom.setFont(new Font(null, Font.PLAIN, 18));
        fromDatePicker = new JSpinner(new SpinnerDateModel());
        fromDatePicker.setEditor(new JSpinner.DateEditor(fromDatePicker, "dd/MM/yyyy"));
        fromDatePicker.setValue(new Date(110, Calendar.JANUARY, 1)); 
        fromDatePicker.setPreferredSize(new Dimension(150, 30));
        fromDatePicker.setFont(new Font(null, Font.PLAIN, 18));
        
        dateTo = new JLabel("Đến ngày:");
        dateTo.setFont(new Font(null, Font.PLAIN, 18));
        toDatePicker = new JSpinner(new SpinnerDateModel());
        toDatePicker.setEditor(new JSpinner.DateEditor(toDatePicker, "dd/MM/yyyy")); 
        toDatePicker.setPreferredSize(new Dimension(150, 30));
        toDatePicker.setFont(new Font(null, Font.PLAIN, 18));
        
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
        //searchPanel.setBorder(blackLine);
        searchPanel.setPreferredSize(new Dimension(690, 250));
        mPanel.add(searchPanel, BorderLayout.CENTER);

        mPanel.setPreferredSize(new Dimension(690, 300));
        this.add(mPanel, BorderLayout.CENTER);

        // Gán sự kiện
        all.addActionListener(controller.createAllRadio(this));
        top.addActionListener(controller.createTopRadio(this));
        rbNgay.addActionListener(controller.createDateRadio(this));
        rbQuy.addActionListener(controller.createThreeMonthRadio(this));
        rbKy.addActionListener(controller.createFourMonthRadio(this));      
        customerRadio.addActionListener(controller.handleTypeSelection(this));
        productRadio.addActionListener(controller.handleTypeSelection(this));
        userRadio.addActionListener(controller.handleTypeSelection(this));
        searchButton.addActionListener(controller.createSearchButton(this));
    }

    private void initComponentsEast() {
        // Thống kê
        JPanel StatisticalPanel = new JPanel();
        StatisticalPanel.setLayout(new BoxLayout(StatisticalPanel, BoxLayout.Y_AXIS));
        controller.displayQuantityCustomer(StatisticalPanel);
        controller.displayQuantityOrder(StatisticalPanel);
        controller.displayQuantityProduct(StatisticalPanel);
        Border blackLine = BorderFactory.createLineBorder(new Color(193, 154, 107));
        StatisticalPanel.setBorder(blackLine);
        StatisticalPanel.setPreferredSize(new Dimension(490, 0));
        StatisticalPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        this.add(StatisticalPanel, BorderLayout.EAST);
    }

    private void initComponentsSouth() {
        contentPanel = new JPanel(new GridBagLayout());
        Border blackLine = BorderFactory.createLineBorder(new Color(193, 154, 107));
        contentPanel.setBorder(blackLine);
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel chứa các radio buttons và nút export
        datPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chartRadio = new JRadioButton("Biểu đồ", true);
        tableRadio = new JRadioButton("Bảng");
        exportPDF = new Button("Xuất PDF");
        exportPDF.addActionListener(controller.exportPDF(this));

        ButtonGroup datGroup = new ButtonGroup();
        datGroup.add(chartRadio);
        datGroup.add(tableRadio);

        // Thêm datPanel vào contentPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        contentPanel.add(datPanel, gbc);

        // Panel chứa bảng/biểu đồ
        tablePanel = new JPanel();
        //tablePanel.setPreferredSize(new Dimension(1200, 400));
    
        // Thêm tablePanel vào contentPanel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(tablePanel, gbc);
        
        contentPanel.setPreferredSize(new Dimension(0, 470));
        this.add(contentPanel, BorderLayout.SOUTH);

        chartRadio.addActionListener(controller.createChartRadio(this));
        tableRadio.addActionListener(controller.createTableRadio(this));
        
    }

    public void updateTableCustomersTable(Object[][] data) {
        tablePanel.removeAll();
        String[] columnNames = {"ID", "Tên khách hàng", "Số điện thoại", "Điểm thưởng", "Tổng chi tiêu"};
        CustomTable table = new CustomTable(columnNames);
        table.refreshTable(data);
        table.getTableHeader().setReorderingAllowed(false);
        table.setPreferredSize(new Dimension(1200, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void updateTableProductsTable(Object[][] data, Object[][] data1) {
        tablePanel.removeAll();
        String[] columnNames = {"ID", "Ảnh", "Tên sản phẩm", "Giá", "Số lượng đã bán", "Doanh thu"};
        
        CustomTable table = new CustomTable(columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) { 
                    return ImageIcon.class;
                }
                return Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
    
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Map<String, ImageIcon> imageCache = new HashMap<>();

                for (Object[] row : data) {
                    String imagePath = "/" + (String) row[1];
                    System.out.println("Image Path: " + imagePath);
                    ImageIcon imageIcon;

                    // Kiểm tra cache để tránh load lại ảnh
                    if (imageCache.containsKey(imagePath)) {
                        imageIcon = imageCache.get(imagePath);
                    } else {
                        java.net.URL imageUrl = getClass().getResource(imagePath);
                        if (imageUrl != null) {
                            imageIcon = createScaledIconFromResource(imagePath);
                            imageCache.put(imagePath, imageIcon);
                        } else {
                            System.out.println("Không tìm thấy hình ảnh chính, sử dụng hình ảnh mặc định.");
                            imageIcon = createScaledIconFromResource("/product/default_img.png");
                        }
                    }

                    final ImageIcon finalImageIcon = imageIcon;
                    final Object[] finalRow = row;
                    SwingUtilities.invokeLater(() -> finalRow[1] = finalImageIcon);
                }

                return null;
            }

            private ImageIcon createScaledIconFromResource(String resourcePath) {
                ImageIcon icon = new ImageIcon(getClass().getResource(resourcePath));
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
            
            @Override
            protected void done() {

                table.refreshTable(data);
                table.setRowHeight(55);
    
                table.getColumnModel().getColumn(0).setPreferredWidth(50);  
                table.getColumnModel().getColumn(1).setPreferredWidth(60);  
                table.getColumnModel().getColumn(2).setPreferredWidth(200); 
                table.getColumnModel().getColumn(3).setPreferredWidth(100); 
                table.getColumnModel().getColumn(4).setPreferredWidth(100); 
                table.getColumnModel().getColumn(5).setPreferredWidth(100); 
                
                JScrollPane scrollPane = new JScrollPane(table);
                tablePanel.add(scrollPane);
    
                String[] columnNames1 = {"ID", "Tên danh mục", "Số lượng đã bán"};
                CustomTable table1 = new CustomTable(columnNames1);
                table1.getTableHeader().setReorderingAllowed(false);
                table1.setPreferredSize(new Dimension(1200, 390));
                table1.refreshTable(data1);
                JScrollPane scrollPane1 = new JScrollPane(table1);
                tablePanel.add(scrollPane1);
                
                tablePanel.revalidate();
                tablePanel.repaint();
            }
        }.execute();
    }

    public void updateTableUsersTable(Object[][] data) {
        tablePanel.removeAll();
        String[] columnNames = {"ID", "Username", "Tổng doanh thu đã tạo"};
        CustomTable table = new CustomTable(columnNames);
        table.getTableHeader().setReorderingAllowed(false);
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

        JPanel chartPanel1 = new ChartPanel(chartPoint){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(550, 350);
            }
        };
        JPanel chartPanel2 = new ChartPanel(chartTotalSpent){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(550, 350);
            }
        };
        // chartPanel1.setPreferredSize(new Dimension(550, 350)); 
        // chartPanel2.setPreferredSize(new Dimension(550, 350)); 
        SwingUtilities.invokeLater(() -> {
                    tablePanel.removeAll();
                    tablePanel.setLayout(new GridLayout(1, 3));
                    tablePanel.add(chartPanel1);
                    tablePanel.add(chartPanel2);
                    tablePanel.revalidate();
                    tablePanel.repaint();
                });
        // tablePanel.add(chartPanel1);
        // tablePanel.add(chartPanel2);
        // tablePanel.revalidate();
        // tablePanel.repaint();
    }

    public void updateTableProductsChart(DefaultCategoryDataset data1, DefaultCategoryDataset data2, DefaultCategoryDataset data3) {
        // Sử dụng SwingWorker để load biểu đồ trong background
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Tạo biểu đồ trong background
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
    
                // Tối ưu hiệu suất cho biểu đồ
                chartRevenue.setAntiAlias(true);
                chartQuantity.setAntiAlias(true);
                chartCategory.setAntiAlias(true);
    
                // Tạo các panel chứa biểu đồ
                JPanel chartPanel1 = new ChartPanel(chartRevenue) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(400, 350);
                    }
                };
                
                JPanel chartPanel2 = new ChartPanel(chartQuantity) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(400, 350);
                    }
                };
                
                JPanel chartPanel3 = new ChartPanel(chartCategory) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(400, 350);
                    }
                };
    
                // Cập nhật UI trong EDT
                SwingUtilities.invokeLater(() -> {
                    tablePanel.removeAll();
                    tablePanel.setLayout(new GridLayout(1, 3));
                    tablePanel.add(chartPanel1);
                    tablePanel.add(chartPanel2);
                    tablePanel.add(chartPanel3);
                    tablePanel.revalidate();
                    tablePanel.repaint();
                });
    
                return null;
            }
        }.execute();
    }

    public void updateTableUsersChart(DefaultCategoryDataset data) {
        tablePanel.removeAll();
        chart = ChartFactory.createBarChart(
            "Top 5 Nhân viên",      
            "Danh thu đã tạo",            
            "Nhân viên",         
            data 
        );

        chartPanel = new ChartPanel(chart){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(500, 400);
            }
        };
        SwingUtilities.invokeLater(() -> {
                    tablePanel.removeAll();
                    tablePanel.setLayout(new GridLayout(1, 3));
                    tablePanel.add(chartPanel);
                    tablePanel.revalidate();
                    tablePanel.repaint();
                });
        // chartPanel.setPreferredSize(new Dimension(1200, 400)); // Tăng kích thước
        // tablePanel.add(chartPanel);
        // tablePanel.revalidate();
        // tablePanel.repaint();
    }

    public void printTableProduct(ArrayList<String> data) {
        tablePanel.removeAll();
        tablePanel.setLayout(new GridLayout(2,1));
        tablePanel.setBackground(Color.WHITE);
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
            JLabel string = new JLabel("ID: " + data.get(0) + "                      Tên: " + data.get(1) + "                      Danh mục: " + data.get(2));
            string.setFont(new Font(null, Font.PLAIN, 20));
            labelPanel.add(string);
            tablePanel.add(labelPanel);

            JPanel labelPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
            JLabel string1 = new JLabel("Giá: " + data.get(3) + "                      Số lượng: " + data.get(4) + "                      Doanh thu: " + data.get(5));
            string1.setFont(new Font(null, Font.PLAIN, 20));
            labelPanel1.add(string1);
            tablePanel.add(labelPanel1);

        // Refresh giao diện
        tablePanel.revalidate();
        tablePanel.repaint();

    }
    
    public void printTableCustomer(Object[][] data, String total, ArrayList<String> str) {
        tablePanel.removeAll();
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
    
        // Tên các cột
        String[] col = {"ID", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tổng tiền"};
        JTable table = new JTable(data, col);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
    
        // Tạo panel thông tin khách hàng
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel idLabel = new JLabel("ID: " + str.get(0));
        idLabel.setFont(new Font(null, Font.PLAIN, 16));
        JLabel nameLabel = new JLabel("Tên: " + str.get(1));
        nameLabel.setFont(new Font(null, Font.PLAIN, 16));
        JLabel phoneLabel = new JLabel("Số điện thoại: " + str.get(2));
        phoneLabel.setFont(new Font(null, Font.PLAIN, 16));
        JLabel pointLabel = new JLabel("Điểm thưởng: " + str.get(3));
        pointLabel.setFont(new Font(null, Font.PLAIN, 16));
        infoPanel.add(idLabel);
        infoPanel.add(nameLabel);
        infoPanel.add(phoneLabel);
        infoPanel.add(pointLabel);
    
        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
    
        // Label tổng doanh thu
        JLabel totalLabel = new JLabel("Tổng doanh thu: " + total);
        totalLabel.setFont(new Font(null, Font.PLAIN, 16));
        totalLabel.setHorizontalAlignment(JLabel.CENTER);
    
        // Thêm các thành phần vào panel chính
        tablePanel.add(infoPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(totalLabel, BorderLayout.SOUTH);
    
        // Làm mới giao diện
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    
    public void printTableUser(Object[][] data, String total, ArrayList<String> str) {
        tablePanel.removeAll();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setLayout(new BorderLayout(10, 10));
    
        // Tạo các cột
        String[] col = {"ID", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tổng tiền"};
        JTable table = new JTable(data, col);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true); // Cho đẹp hơn
    
        // Tạo panel chứa thông tin người dùng
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel idLabel = new JLabel("ID: " + str.get(0));
        idLabel.setFont(new Font(null, Font.PLAIN, 16));
        JLabel nameLabel = new JLabel("Username: " + str.get(1));
        nameLabel.setFont(new Font(null, Font.PLAIN, 16));
        infoPanel.add(idLabel);
        infoPanel.add(nameLabel);
    
        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
    
        // Label tổng doanh thu
        JLabel totalLabel = new JLabel("Tổng doanh thu: " + total);
        totalLabel.setFont(new Font(null, Font.PLAIN, 16));
        totalLabel.setHorizontalAlignment(JLabel.CENTER);
    
        // Thêm các thành phần vào panel chính
        tablePanel.add(infoPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(totalLabel, BorderLayout.SOUTH);
    
        // Refresh giao diện
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    
    public void removeContent() {
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void resetSearchPanel() {
        searchField.setText("");
        top.setSelected(true);  
        resetDatePanel();
        addChartTableRadio();  
        topAndAllPanel.remove(searchField);
        topAndAllPanel.revalidate();
        topAndAllPanel.repaint();
        // searchPerformed = false;
    }

    public void resetDatePanel() {
        rbNgay.setSelected(true);
        removeMonthComboBox();
        removeFourMonthComboBox();
        removeThreeMonthComboBox();         
        addDate();
        addSearchButton();
    }

    public void removeChartTableRadio() {
        datPanel.remove(chartRadio);
        datPanel.remove(tableRadio);
        datPanel.remove(exportPDF);
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
    
    public void addDate() {
        datePanel.add(dateFrom);
        datePanel.add(fromDatePicker);
        datePanel.add(dateTo);
        datePanel.add(toDatePicker);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void removeDate() {
        datePanel.remove(dateFrom);
        datePanel.remove(fromDatePicker);
        datePanel.remove(dateTo);
        datePanel.remove(toDatePicker);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void addSearchButton() {
        datePanel.add(searchButton);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void removeSearchButton() {
        datePanel.remove(searchButton);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void addThreeMonthComboBox() {
        datePanel.add(threeMonthComboBox);
        datePanel.revalidate();
        datePanel.repaint();
    }
    
    public void removeThreeMonthComboBox() {
        datePanel.remove(threeMonthComboBox);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void addFourMonthComboBox() {
        datePanel.add(fourMonthComboBox);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void removeFourMonthComboBox() {
        datePanel.remove(fourMonthComboBox);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void addMonthComboBox() {
        datePanel.add(monthComboBox);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void removeMonthComboBox() {
        datePanel.remove(monthComboBox);
        datePanel.revalidate();
        datePanel.repaint();
    }

    public void removeSearchField() {
        topAndAllPanel.remove(searchField);
        topAndAllPanel.revalidate();
        topAndAllPanel.repaint();
    }

    public void addSearchField() {
        topAndAllPanel.add(searchField);
        topAndAllPanel.revalidate();
        topAndAllPanel.repaint();
    }
    
    public void addExportPDF() {
        datPanel.add(exportPDF);
        datePanel.revalidate();
        datPanel.repaint();
    }

    public void removeExportPDF() {
        datPanel.remove(exportPDF);
        datePanel.revalidate();
        datPanel.repaint();
    }

    public void removeDatPanel() {
        contentPanel.remove(datePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void addDatPanel() {
        contentPanel.add(datePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    } 

    public void removeAllDatPanel() {
        datPanel.removeAll();
        datePanel.revalidate();
        datPanel.repaint(); 
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

    public JRadioButton getTableRadio() {
        return tableRadio;
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
