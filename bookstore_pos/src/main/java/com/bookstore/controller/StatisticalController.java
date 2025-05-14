package com.bookstore.controller;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.bookstore.BUS.StatisticalBUS;
import com.bookstore.gui.panel.StatisticalPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import com.bookstore.util.TimeUtil;
import com.bookstore.util.PDFExporter;


public class StatisticalController {
    private StatisticalBUS statisticalBUS;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public StatisticalController() {
        statisticalBUS = new StatisticalBUS();
    }

    public ActionListener createAllRadio(StatisticalPanel view) {
        return e -> {
            view.removeChartTableRadio();
            view.removeTablePanel();
            view.addSearchField();
        };
    }

    public ActionListener createTopRadio(StatisticalPanel view) {
        return e -> {
            view.removeSearchField();
            view.removeChartTableRadio();
            view.removeTablePanel();
        };
    }

    public ActionListener handleTypeSelection(StatisticalPanel view) {
        return e -> {
            view.getChartRadio().setEnabled(true);
            view.getTablePanel().setEnabled(true);
            view.getChartRadio().setSelected(true);

        // Reset searchPanel
            view.resetSearchPanel();
            view.removeChartTableRadio();
            view.removeTablePanel();
        };
    }
    
    public ActionListener createDateRadio(StatisticalPanel view) {
        return e -> {
            view.removeMonthComboBox();
            view.removeFourMonthComboBox();
            view.removeThreeMonthComboBox();          
            view.addDate();
            view.addSearchButton();
        };
    }
    
    public ActionListener createThreeMonthRadio(StatisticalPanel view) {
        return e -> {
            view.removeFourMonthComboBox();
            view.removeDate();  
            view.addThreeMonthComboBox();
            view.addMonthComboBox();
            view.addSearchButton();
        };
    }
    
    public ActionListener createFourMonthRadio(StatisticalPanel view) {
        return e -> {
            view.removeThreeMonthComboBox();
            view.removeDate();
            view.addFourMonthComboBox();
            view.addMonthComboBox();
            view.addSearchButton();
        };
    }
    
    public ActionListener createSearchButton(StatisticalPanel view) {
        return e -> {
            buttonSearch(view);
        };
    }

    public ActionListener createChartRadio(StatisticalPanel view) {
        return e -> {
            TimeUtil.start();
            if (view.getProductRadio().isSelected()) {
                loadProductsChart(view);
            } else if (view.getCustomerRadio().isSelected()) {
                loadCustomersChart(view);
            } else if (view.getUserRadio().isSelected()) {
                loadUsersChart(view);
            }
            TimeUtil.stop("loadChartRadio");
        };
    }

    public ActionListener createTableRadio(StatisticalPanel view) {
        return e -> {
            TimeUtil.start();
            if (view.getProductRadio().isSelected()) {
                loadProductsTable(view);
            } else if (view.getCustomerRadio().isSelected()) {
                loadCustomersTable(view);
            } else if (view.getUserRadio().isSelected()) {
                loadUsersTable(view);
            }
            TimeUtil.stop("loadTableRadio");
        };
    }


    public ActionListener exportPDF(StatisticalPanel view) {
        return e -> {

            try {
                TimeUtil.start();
                Object[][] data;
                String fileName;
                String reportType;

                PDFExporter pdfExporter = new PDFExporter();
                // Lấy dữ liệu dựa trên kiểu xuất
                if (view.getTopRadio().isSelected()) {
                    // Lấy top 5
                    if (view.getCustomerRadio().isSelected()) {
                        data = statisticalBUS.processCustomersResults(); // Phương thức lấy top 5 khách hàng   
                        fileName = "top_customers_report_" + TimeUtil.getCurrentTime() + ".pdf";
                        reportType = "top_customer";
                        pdfExporter.exportToPDFGiveTop(data, fileName, reportType, dateReport());
                    } else if (view.getProductRadio().isSelected()) {
                        data = statisticalBUS.processProductResults(); // Phương thức lấy top 5 sản phẩm
                        fileName = "top_products_report_" + TimeUtil.getCurrentTime() + ".pdf";
                        reportType = "top_product";
                        pdfExporter.exportToPDFGiveTop(data, fileName, reportType, dateReport());
                    } else if (view.getUserRadio().isSelected()) {
                        data = statisticalBUS.processUsersResults(); // Phương thức lấy top 5 nhân viên
                        fileName = "top_employees_report_" + TimeUtil.getCurrentTime() + ".pdf";
                        reportType = "top_employee";
                        pdfExporter.exportToPDFGiveTop(data, fileName, reportType, dateReport());
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Vui lòng chọn loại báo cáo", 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // Xuất một mục cụ thể
                    if (view.getCustomerRadio().isSelected()) {
                        data = statisticalBUS.searchCustomer(view.getDateFromValue(), view.getDateToValue(), view.getSearchField().getText()); // Lấy dữ liệu khách hàng
                        fileName = "customer_report_" + TimeUtil.getCurrentTime() + ".pdf";
                        reportType = "customer";
                        pdfExporter.exportToPDFGiveAll(data, 
                        statisticalBUS.totalSpent(),
                        statisticalBUS.searchCustomer(view.getSearchField().getText()),
                        fileName, reportType, dateReport());
                    } else if (view.getProductRadio().isSelected()) {
                        data = statisticalBUS.searchProduct(view.getDateFromValue(), view.getDateToValue(), view.getSearchField().getText()); // Lấy dữ liệu sản phẩm
                        fileName = "product_report_" + TimeUtil.getCurrentTime() + ".pdf";
                        reportType = "product";
                        pdfExporter.exportToPDFGiveAll(data, 
                        "",
                        statisticalBUS.searchProduct(data),
                        fileName, reportType, dateReport());
                    } else if (view.getUserRadio().isSelected()) {
                        data = statisticalBUS.searchUser(view.getDateFromValue(), view.getDateToValue(), view.getSearchField().getText()); // Lấy dữ liệu nhân viên
                        fileName = "employee_report_" + TimeUtil.getCurrentTime() + ".pdf";
                        reportType = "employee";
                        pdfExporter.exportToPDFGiveAll(data, 
                        statisticalBUS.totalRevenue(),
                        statisticalBUS.searchUser(view.getSearchField().getText()),
                        fileName, reportType, dateReport());
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Vui lòng chọn loại báo cáo", 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(null, 
                    "Xuất PDF thành công!\nFile: " + fileName, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);

                TimeUtil.stop("exportPDF");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, 
                    "Lỗi khi xuất PDF: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        };
    }
    // ===== Top =====
    // ===== Biểu đồ =====
    // ===== Khách hàng =====
    public void loadCustomersChart(StatisticalPanel view) {
        System.out.println("Bang khach hang bieu do");
        view.updateTableCustomersChart(statisticalBUS.createDatasetGiveCustomersPoints(), 
        statisticalBUS.createDatasetGiveCustomersTotalSpent());
    }
    // ===== Sản phẩm =====
    public void loadProductsChart(StatisticalPanel view) {
        System.out.println("Bang san pham bieu do");
        view.updateTableProductsChart(statisticalBUS.createDatasetGiveProductsQuantity(),
        statisticalBUS.createDatasetGiveProductsRevenue(), statisticalBUS.createDatasetGiveCategory());
    }
    // ===== Nhân viên =====
    public void loadUsersChart(StatisticalPanel view) {
        view.updateTableUsersChart(statisticalBUS.createDatasetGiveUser());
    }

    // ===== Bảng =====
    // ===== Khách hàng =====
    public void loadCustomersTable(StatisticalPanel view) {
        System.out.println("bang khach hang");
        view.updateTableCustomersTable(statisticalBUS.processCustomersResults());
    }
    // ===== Sản phẩm =====
    public void loadProductsTable(StatisticalPanel view) {
        TimeUtil.start();
        System.out.println("bang san pham");
        view.updateTableProductsTable(statisticalBUS.processProductResults(),
         statisticalBUS.processCategorysResults());
        TimeUtil.stop("loadProductsTable");
    }
    // ===== Nhân viên =====
    public void loadUsersTable(StatisticalPanel view) {
        long start = System.nanoTime();
        System.out.println("bang nhan vien");
        view.updateTableUsersTable(statisticalBUS.processUsersResults());
        long end = System.nanoTime();
        long duration = end - start;

        System.out.println("Thời gian chạy: " + duration / 1_000_000 + " ms");
    }
    
    // ===== Tìm kiếm =====
    public void buttonSearch(StatisticalPanel view) {
            TimeUtil.start();
        if (view.getProductRadio().isSelected()) {
            // Đang chọn sản phẩm
            System.out.println("Bang san pham");
            if (view.getTopRadio().isSelected()) {
                // Đang chọn top
                System.out.println("Top");
                searchIfChooseTop(view);
                loadProductsChart(view);
            } else if (view.getAllRadio().isSelected()) {
                // Đang chọn tất cả
                searchIfChooseAll(view);
            }
        } else if (view.getCustomerRadio().isSelected()) {
            // Đang chọn khách hàng
            if (view.getTopRadio().isSelected()) {
                // Đang chọn top
                searchIfChooseTop(view);
                loadCustomersChart(view);
            } else if (view.getAllRadio().isSelected()) {
                // Đang chọn tất cả
                searchIfChooseAll(view);
            }
        } else if (view.getUserRadio().isSelected()) {
            // Đang chọn nhân viên
            if (view.getTopRadio().isSelected()) {
                // Đang chọn top
                searchIfChooseTop(view);
                loadUsersChart(view);
            } else if (view.getAllRadio().isSelected()) {
                // Đang chọn tất cả
                searchIfChooseAll(view);
            }
        }
        TimeUtil.stop("buttonSearch");
    }

    // ===== Tìm kiếm theo Top =====
    public void searchIfChooseTop(StatisticalPanel view) {
        fromDate = LocalDateTime.now();
        toDate = LocalDateTime.now();
        if (view.getRbNgay().isSelected()) {
            // Đang chọn ngày
            fromDate = view.getDateFromValue();
            toDate = view.getDateToValue();
        } else if (view.getRbKy().isSelected()) {
            // Đang chọn kỳ
            String selectedQuarter = (String) view.getFourMonthComboBox().getSelectedItem();
            System.out.println(selectedQuarter + "ky");
            String selectedYearStr = (String) view.getMonthComboBox().getSelectedItem();
            int year = Integer.parseInt(selectedYearStr);
            switch (selectedQuarter) {
                case "Kỳ 1":
                    fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 4, 30, 23, 59);
                    break;
                case "Kỳ 2":
                    fromDate = LocalDateTime.of(year, 5, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 8, 31, 23, 59);
                    break;
                case "Kỳ 3":
                    fromDate = LocalDateTime.of(year, 9, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                    break;
                case "Không":
                default:
                    fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                    break;
            }

        } else if (view.getRbQuy().isSelected()) {
            // Đang chọn quý
            String selectedTerm = (String) view.getThreeMonthComboBox().getSelectedItem();
            System.out.println(selectedTerm+ "quy");
            String selectedYearStr = (String) view.getMonthComboBox().getSelectedItem();
            int year = Integer.parseInt(selectedYearStr);
            switch (selectedTerm) {
                case "Quý 1":
                    fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 3, 31, 23, 59);
                    break;
                case "Quý 2":
                    fromDate = LocalDateTime.of(year, 4, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 6, 30, 23, 59);
                    break;
                case "Quý 3":
                    fromDate = LocalDateTime.of(year, 7, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 9, 30, 23, 59);
                    break;
                case "Quý 4":
                    fromDate = LocalDateTime.of(year, 10, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                    break;
                case "Không":
                default:
                    fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                    break;    
            }
        }
        System.out.println(fromDate + "  ||  " + toDate);
        searchByDay(view, fromDate, toDate);
        // view.setSearchPerformed(true);
    }

    // ===== Tìm kiếm theo All =====
    public void searchIfChooseAll(StatisticalPanel view) {
        if (view.getSearchField().getText().isEmpty()) {
            // Nếu không có từ khóa tìm kiếm
            System.out.println("Vui lòng nhập từ khóa tìm kiếm.");
            return;
        } else {
            // Nếu có từ khóa tìm kiếm
            String keyword = view.getSearchField().getText();
            fromDate = LocalDateTime.now();
            toDate = LocalDateTime.now();
            if (view.getRbNgay().isSelected()) {
                // Đang chọn ngày
                fromDate = view.getDateFromValue();
                toDate = view.getDateToValue();
            } else if (view.getRbKy().isSelected()) {
                // Đang chọn kỳ
                String selectedQuarter = (String) view.getFourMonthComboBox().getSelectedItem();
                String selectedYearStr = (String) view.getMonthComboBox().getSelectedItem();
                int year = Integer.parseInt(selectedYearStr);
                switch (selectedQuarter) {
                    case "Kỳ 1":
                        fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 4, 30, 23, 59);
                        break;
                    case "Kỳ 2":
                        fromDate = LocalDateTime.of(year, 5, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 8, 31, 23, 59);
                        break;
                    case "Kỳ 3":
                        fromDate = LocalDateTime.of(year, 9, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                        break;
                    case "Không":
                    default:
                        fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                        break;
                }
    
            } else if (view.getRbQuy().isSelected()) {
                // Đang chọn quý
                String selectedTerm = (String) view.getThreeMonthComboBox().getSelectedItem();
                String selectedYearStr = (String) view.getMonthComboBox().getSelectedItem();
                int year = Integer.parseInt(selectedYearStr);
                switch (selectedTerm) {
                    case "Quý 1":
                        fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 3, 31, 23, 59);
                        break;
                    case "Quý 2":
                        fromDate = LocalDateTime.of(year, 4, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 6, 30, 23, 59);
                        break;
                    case "Quý 3":
                        fromDate = LocalDateTime.of(year, 7, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 9, 30, 23, 59);
                        break;
                    case "Quý 4":
                        fromDate = LocalDateTime.of(year, 10, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                        break;
                    case "Không":
                    default:
                        fromDate = LocalDateTime.of(year, 1, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 12, 31, 23, 59);
                        break;
                }
            }
            System.out.println(fromDate + "  ||  " + toDate);
            searchByDay(view, keyword, fromDate, toDate);
            // view.setSearchPerformed(true);
        }
    }

    // ===== Tìm kiếm theo ngày =====
    public void searchByDay(StatisticalPanel view, LocalDateTime fromDate, LocalDateTime toDate) {
        
            view.addChartTableRadio();
            if (view.getProductRadio().isSelected()){
                statisticalBUS.searchProductRevenueMap(fromDate, toDate);
                statisticalBUS.searchProductSalesMap(fromDate, toDate);
                statisticalBUS.searchCategorySalesMap(fromDate, toDate);
                view.getChartRadio().setSelected(true);
                loadProductsChart(view);
            } else if (view.getCustomerRadio().isSelected()) {
                statisticalBUS.searchTotalSpentMap(fromDate, toDate);
                view.getChartRadio().setSelected(true);
                loadCustomersChart(view);
            } else if (view.getUserRadio().isSelected()) {
                statisticalBUS.searchTotalRevenueMap(fromDate, toDate);
                view.getChartRadio().setSelected(true);
                loadUsersChart(view);
            }
    }

    public void searchByDay(StatisticalPanel view, String keyword, LocalDateTime fromDate, LocalDateTime toDate) {

        view.removeChartTableRadio();
        if (view.getProductRadio().isSelected()){
            if (statisticalBUS.searchProduct(keyword).size()== 0){
                JOptionPane.showMessageDialog(null, "Không tìm thấy " + keyword , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            view.printTableProduct(statisticalBUS.searchProduct(statisticalBUS.searchProduct(fromDate, toDate, keyword)));
        } else if (view.getCustomerRadio().isSelected()) {
            if (statisticalBUS.searchCustomer(keyword).size()== 0) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy " + keyword , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            view.printTableCustomer(statisticalBUS.searchCustomer(fromDate, toDate, keyword),
            statisticalBUS.totalSpent(), 
            statisticalBUS.searchCustomer(keyword));
        } else if (view.getUserRadio().isSelected()) {
            if (statisticalBUS.searchUser(keyword).size()== 0) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy " + keyword , "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            view.printTableUser(statisticalBUS.searchUser(fromDate, toDate, keyword), 
            statisticalBUS.totalRevenue(), 
            statisticalBUS.searchUser(keyword));
        }
        view.addExportPDF();
    }

    public void displayQuantityCustomer(JPanel view) {
        JPanel box = new JPanel();
        view.add(Box.createVerticalStrut(10));
        box.setBackground(new Color(114, 233, 129));
        JLabel label = new JLabel("Số khách hàng: " + statisticalBUS.getQuantityCustomer());
        label.setFont(new Font(null, Font.PLAIN, 16));
        box.add(label);
        view.add(box);
        view.add(Box.createVerticalStrut(10));
    }

    public void displayQuantityOrder(JPanel view) {
        JPanel box = new JPanel();
        view.add(Box.createVerticalStrut(10));
        box.setBackground(new Color(114, 233, 129));
        JLabel label = new JLabel("Số hóa đơn: " + statisticalBUS.getQuantityOrder());
        label.setFont(new Font(null, Font.PLAIN, 16));
        box.add(label);
        view.add(box);
        view.add(Box.createVerticalStrut(10));
    }

    public void displayQuantityProduct(JPanel view) {
        JPanel box = new JPanel();
        view.add(Box.createVerticalStrut(10));
        box.setBackground(new Color(114, 233, 129));
        JLabel label = new JLabel("Số sản phẩm: " + statisticalBUS.getQuantityProduct());
        label.setFont(new Font(null, Font.PLAIN, 16));
        box.add(label);
        view.add(box);
        view.add(Box.createVerticalStrut(10));
    }

    private String dateReport(){
        return "Từ " + fromDate + " đến " + toDate;
    }
}
