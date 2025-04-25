package com.bookstore.util;

import com.itextpdf.html2pdf.HtmlConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;

public class PDFExporter {

    public void exportToPDFGiveTop(Object[][] data, String fileName, String reportType, String dateReport) throws Exception {
        // Đảm bảo thư mục báo cáo tồn tại
        String reportsDir = Paths.get(System.getProperty("user.dir"), "reports").toString();
        new File(reportsDir).mkdirs();
        
        // Đọc mẫu HTML dựa trên loại báo cáo
        String templatePath = getTemplatePath(reportType);
        String htmlTemplate = HtmlTemplateUtil.readHtmlTemplate(templatePath); // Sử dụng HtmlTemplateUtil
        
        // Chuẩn bị dữ liệu cho mẫu
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        
        // Thêm dữ liệu vào templateData
        populateTemplateData(templateData, data, reportType, dateReport);
        
        // Thay thế các placeholder trong mẫu
        String html = replacePlaceholders(htmlTemplate, templateData); // Sử dụng HtmlTemplateUtil
        
        // Chuyển đổi HTML sang PDF
        String outputPath = Paths.get(reportsDir, fileName).toString();
        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            HtmlConverter.convertToPdf(html, outputStream);
        }
    }

    public void exportToPDFGiveAll(Object[][] data, String total, ArrayList<String> str, String fileName, String reportType, String dateReport) throws Exception {
        String reportsDir = Paths.get(System.getProperty("user.dir"), "reports").toString();
        new File(reportsDir).mkdirs();

        String templatePath = getTemplatePath(reportType);
        String htmlTemplate = HtmlTemplateUtil.readHtmlTemplate(templatePath);

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        populateTemplateData(templateData, data, total, str, reportType, dateReport);

        String html = replacePlaceholders(htmlTemplate, templateData); 

        String outputPath = Paths.get(reportsDir, fileName).toString();
        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            HtmlConverter.convertToPdf(html, outputStream);
        }
    }

    private String getTemplatePath(String reportType) {
        switch (reportType) {
            case "customer":
                return "templates/customer_report.html";
            case "employee":
                return "templates/employee_report.html";
            case "product":
                return "templates/product_report.html";
            case "top_customer":
                return "templates/top_customers_report.html";
            case "top_employee":
                return "templates/top_employees_report.html";
            case "top_product":
                return "templates/top_products_report.html";
            default:
                throw new IllegalArgumentException("Loại báo cáo không hợp lệ: " + reportType);
        }
    }

    private void populateTemplateData(Map<String, Object> templateData, Object[][] data, String reportType, String dateReport) {
        if ("top_customer".equals(reportType)) {
            List<Map<String, Object>> customers = new ArrayList<>();
    
            for (int i = 0; i < data.length; i++) {
                Map<String, Object> customer = new HashMap<>();
                customer.put("id", data[i][0]);
                customer.put("name", data[i][1]);
                customer.put("phone", data[i][2]);
                customer.put("points", data[i][3]);
                customer.put("totalSpent", formatCurrency(Double.parseDouble(data[i][4].toString())));
                customers.add(customer);
            }
    
            templateData.put("totalCustomers", String.valueOf(data.length));
            StringBuilder customerRows = new StringBuilder();
            for (Map<String, Object> customer : customers) {
                customerRows.append("<tr>")
                            .append("<td>").append(customer.get("id")).append("</td>")
                            .append("<td>").append(customer.get("name")).append("</td>")
                            .append("<td>").append(customer.get("phone")).append("</td>")
                            .append("<td class='points'>").append(customer.get("points")).append("</td>")
                            .append("<td class='total-spent'>").append(customer.get("totalSpent")).append("</td>")
                            .append("</tr>");
            }
            templateData.put("customers", customerRows.toString());
            templateData.put("timePeriod", dateReport);
        } else if ("top_employee".equals(reportType)) {
            List<Map<String, Object>> employees = new ArrayList<>();

            for (int i = 0; i < data.length; i++) {
                Map<String, Object> employee = new HashMap<>();
                employee.put("id", data[i][0]);
                employee.put("username", data[i][1]);
                employee.put("totalRevenue", formatCurrency(Double.parseDouble(data[i][3].toString())));
                employees.add(employee);
            }

            templateData.put("totalEmployees", String.valueOf(data.length));
            StringBuilder employeeRows = new StringBuilder();
            for (Map<String, Object> employee : employees) {
                employeeRows.append("<tr>")
                            .append("<td>").append(employee.get("id")).append("</td>")
                            .append("<td>").append(employee.get("username")).append("</td>")
                            .append("<td class='revenue'>").append(employee.get("totalRevenue")).append("</td>")
                            .append("</tr>");
            }
            templateData.put("employees", employeeRows.toString());
            templateData.put("timePeriod", dateReport);
        } else if ("top_product".equals(reportType)) {
            List<Map<String, Object>> products = new ArrayList<>();
        
            for (int i = 0; i < data.length; i++) {
                Map<String, Object> product = new HashMap<>();
                product.put("id", data[i][0]);
                product.put("imagePath", data[i][1]); // Đường dẫn hình ảnh
                product.put("name", data[i][2]);
                product.put("price", formatCurrency(Double.parseDouble(data[i][3].toString())));
                product.put("quantitySold", data[i][4]);
                product.put("revenue", formatCurrency(Double.parseDouble(data[i][5].toString())));
                products.add(product);
            }
        
            templateData.put("totalProducts", String.valueOf(data.length)); // Convert to String
            StringBuilder productRows = new StringBuilder();
            for (Map<String, Object> product : products) {
                productRows.append("<tr>")
                           .append("<td>").append(product.get("id")).append("</td>")
                           .append("<td>").append(product.get("name")).append("</td>")
                           .append("<td class='price'>").append(product.get("price")).append("</td>")
                           .append("<td class='quantity'>").append(product.get("quantitySold")).append("</td>")
                           .append("<td class='revenue'>").append(product.get("revenue")).append("</td>")
                           .append("</tr>");
            }
            templateData.put("products", productRows.toString());
            templateData.put("timePeriod", dateReport); // Thay đổi theo yêu cầu
        }
    }

    private void populateTemplateData(Map<String, Object> templateData, Object[][] data, String total, ArrayList<String> str, String reportType, String dateReport) {
        if ("customer".equals(reportType)) {
            List<Map<String, Object>> purchases = new ArrayList<>();
            
            // Process customer data
            templateData.put("customerId", str.get(0));
            templateData.put("customerName", str.get(1));
            templateData.put("customerPhone", str.get(2));
            templateData.put("points", str.get(3));
            
            // Process purchase history
            for (int i = 0; i < data.length; i++) {
                Map<String, Object> purchase = new HashMap<>();
                purchase.put("productID", data[i][0]);
                purchase.put("productName", data[i][1]);
                purchase.put("price", formatCurrency(Double.parseDouble(data[i][2].toString())));
                purchase.put("quantity", data[i][3]);
                purchase.put("total", formatCurrency(Double.parseDouble(data[i][4].toString())));
                purchases.add(purchase);
            }
            
            StringBuilder purchaseRows = new StringBuilder();
            for (Map<String, Object> purchase : purchases) {
                purchaseRows.append("<tr>")
                            .append("<td>").append(purchase.get("productID")).append("</td>")
                            .append("<td>").append(purchase.get("productName")).append("</td>")
                            .append("<td>").append(purchase.get("price")).append("</td>")
                            .append("<td>").append(purchase.get("quantity")).append("</td>")
                            .append("<td>").append(purchase.get("total")).append("</td>")
                            .append("</tr>");
            }
            templateData.put("timePeriod", dateReport);
            templateData.put("purchaseRows", purchaseRows.toString());
            templateData.put("totalSpent", formatCurrency(Double.parseDouble(total)));
    
        } else if ("employee".equals(reportType)) {
            List<Map<String, Object>> sales = new ArrayList<>();
            
            // Process employee data
            templateData.put("UserId", str.get(0));
            templateData.put("UserName", str.get(1));
            
            // Process sales data
            for (int i = 0; i < data.length; i++) {
                Map<String, Object> sale = new HashMap<>();
                sale.put("productID", data[i][0]);
                sale.put("productName", data[i][1]);
                sale.put("price", formatCurrency(Double.parseDouble(data[i][2].toString())));
                sale.put("quantity", data[i][3]);
                sale.put("revenue", formatCurrency(Double.parseDouble(data[i][4].toString())));
                sales.add(sale);
            }
            
            StringBuilder salesRows = new StringBuilder();
            for (Map<String, Object> sale : sales) {
                salesRows.append("<tr>")
                        .append("<td>").append(sale.get("productID")).append("</td>")
                        .append("<td>").append(sale.get("productName")).append("</td>")
                        .append("<td>").append(sale.get("price")).append("</td>")
                        .append("<td>").append(sale.get("quantity")).append("</td>")
                        .append("<td>").append(sale.get("revenue")).append("</td>")
                        .append("</tr>");
            }
            templateData.put("timePeriod", dateReport);
            templateData.put("salesRows", salesRows.toString());
            templateData.put("totalRevenue", formatCurrency(Double.parseDouble(total)));
    
        } else if ("product".equals(reportType)) {
            
            // Process product data
            templateData.put("timePeriod", dateReport);
            templateData.put("productId", str.get(0));
            templateData.put("productName", str.get(1));
            templateData.put("category", str.get(2));
            templateData.put("price", str.get(3));
            templateData.put("quantity", str.get(4));
            templateData.put("totalRevenue", str.get(5));
        }
    }

    public static String replacePlaceholders(String htmlContent, Map<String, Object> placeholders) {
        String result = htmlContent;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }

}