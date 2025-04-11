package com.bookstore.controller;

import java.util.*;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bookstore.BUS.StatisticalBUS;
import com.bookstore.gui.panel.StatisticalPanel;
import com.bookstore.model.Customer;
import com.bookstore.model.Product;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;



public class StatisticalController {
    private StatisticalBUS statisticalBUS;

    public StatisticalController() {
        statisticalBUS = new StatisticalBUS();
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
        long start = System.nanoTime();
        System.out.println("bang san pham");
        view.updateTableProductsTable(statisticalBUS.processProductResults(),
         statisticalBUS.processCategorysResults());
        long end = System.nanoTime();
        long duration = end - start;

        System.out.println("Thời gian chạy: " + duration / 1_000_000 + " ms");
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
    }

    // ===== Tìm kiếm theo Top =====
    public void searchIfChooseTop(StatisticalPanel view) {
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = LocalDateTime.now();
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
                    toDate = LocalDateTime.of(year, 3, 31, 23, 59);
                    break;
                case "Kỳ 2":
                    fromDate = LocalDateTime.of(year, 4, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 6, 30, 23, 59);
                    break;
                case "Kỳ 3":
                    fromDate = LocalDateTime.of(year, 7, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 9, 30, 23, 59);
                    break;
                case "Kỳ 4":
                    fromDate = LocalDateTime.of(year, 10, 1, 0, 0);
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
                    toDate = LocalDateTime.of(year, 4, 30, 23, 59);
                    break;
                case "Quý 2":
                    fromDate = LocalDateTime.of(year, 5, 1, 0, 0);
                    toDate = LocalDateTime.of(year, 8, 31, 23, 59);
                    break;
                case "Quý 3": // Chú ý chữ "ỳ" và "ì" khác nhau!
                    fromDate = LocalDateTime.of(year, 9, 1, 0, 0);
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
            LocalDateTime fromDate = LocalDateTime.now();
            LocalDateTime toDate = LocalDateTime.now();
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
                        toDate = LocalDateTime.of(year, 3, 31, 23, 59);
                        break;
                    case "Kỳ 2":
                        fromDate = LocalDateTime.of(year, 4, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 6, 30, 23, 59);
                        break;
                    case "Kỳ 3":
                        fromDate = LocalDateTime.of(year, 7, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 9, 30, 23, 59);
                        break;
                    case "Kỳ 4":
                        fromDate = LocalDateTime.of(year, 10, 1, 0, 0);
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
                        toDate = LocalDateTime.of(year, 4, 30, 23, 59);
                        break;
                    case "Quý 2":
                        fromDate = LocalDateTime.of(year, 5, 1, 0, 0);
                        toDate = LocalDateTime.of(year, 8, 31, 23, 59);
                        break;
                    case "Quý 3": // Chú ý chữ "ỳ" và "ì" khác nhau!
                        fromDate = LocalDateTime.of(year, 9, 1, 0, 0);
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
            view.printTableProduct(statisticalBUS.searchProduct(fromDate, toDate, keyword));
        } else if (view.getCustomerRadio().isSelected()) {
            view.printTableCustomer(statisticalBUS.searchCustomer(fromDate, toDate, keyword),
            statisticalBUS.totalSpent(), 
            statisticalBUS.searchCustomer(keyword));
        } else if (view.getUserRadio().isSelected()) {
            view.printTableUser(statisticalBUS.searchUser(fromDate, toDate, keyword), 
            statisticalBUS.totalRevenue(), 
            keyword);
        }
    }

    public void displayQuantityCustomer(JPanel view) {
        JPanel box = new JPanel();
        box.add(new JLabel("Số khách hàng"));
        box.add(new JLabel(statisticalBUS.getQuantityCustomer()));
        view.add(box);
    }

    public void displayQuantityOrder(JPanel view) {
        JPanel box = new JPanel();
        box.add(new JLabel("Số hóa đơn"));
        box.add(new JLabel(statisticalBUS.getQuantityOrder()));
        view.add(box);
    }

    public void displayQuantityProduct(JPanel view) {
        JPanel box = new JPanel();
        box.add(new JLabel("Số sản phẩm"));
        box.add(new JLabel(statisticalBUS.getQuantityProduct()));
        view.add(box);
    }

    public void exportPDF() {

    }
}
