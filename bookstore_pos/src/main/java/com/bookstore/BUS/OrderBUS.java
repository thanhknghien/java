package com.bookstore.BUS;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bookstore.dao.OrderDAO;
import com.bookstore.dao.OrderDetailDAO;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.model.User;
import com.bookstore.util.HtmlTemplateUtil;
import com.itextpdf.html2pdf.HtmlConverter;

public class OrderBUS {
    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;

    public OrderBUS(){
        orderDAO = new OrderDAO();
        orderDetailDAO = new OrderDetailDAO();
    }

    // Calculate Total Amount
    public double calculateTotal(ArrayList<OrderDetail> list){
        double total = 0;
        for (OrderDetail detail : list) {
            total += detail.getQuantity() * detail.getPrice();
        }
        return total;
    }

    // Create New Order
    public void createOrder(ArrayList<OrderDetail> details, User employee, Customer customer)throws Exception{
        if (details == null || details.isEmpty()) {
            throw new Exception("Danh sách sản phẩm không được rỗng!");
        }
        if (employee == null) {
            throw new Exception("Nhân viên không được rỗng!");
        }
        for (OrderDetail detail : details) {
            if (detail.getProduct() == null) {
                throw new Exception("Sản phẩm trong chi tiết đơn hàng không được rỗng!");
            }
            if (detail.getQuantity() <= 0) {
                throw new Exception("Số lượng sản phẩm phải lớn hơn 0!");
            }
            if (detail.getPrice() < 0) {
                throw new Exception("Giá sản phẩm không được âm!");
            }
        }

        double total = calculateTotal(details);

        Order order = new Order();
        order.setDate(LocalDateTime.now()); 
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setTotal(total);

        int orderId = orderDAO.addOrder(order);

        for (OrderDetail detail : details) {
            detail.setOrderId(orderId);
            orderDetailDAO.addOrderDetail(detail);
        }
    }

    // Get All Order
    public ArrayList<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    // Search Order
    public ArrayList<Order> searchOrders(Map<String, String> criteria) throws SQLException {
        return orderDAO.searchOrders(criteria);
    }

    // Print PDF
    public void printReceipt(int orderId, String templatePath, String outputPath, double moneyReceived) throws Exception {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("id", String.valueOf(orderId));
        ArrayList<Order> orders = searchOrders(criteria);
        if (orders.isEmpty()) {
            throw new SQLException("Không tìm thấy đơn hàng với ID: " + orderId);
        }

        Order order = orders.get(0);
        ArrayList<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrderId(orderId);

        double moneyBack = moneyReceived - order.getTotal();
        if (moneyBack < 0) {
            throw new Exception("Số tiền khách đưa không đủ để thanh toán!");
        }

        String htmlContent = HtmlTemplateUtil.readHtmlTemplate(templatePath);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("orderId", String.valueOf(order.getId()));
        placeholders.put("orderDate", order.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        placeholders.put("customerName", order.getCustomer() != null ? order.getCustomer().getName() : "Khách lẻ");
        placeholders.put("customerPhone", order.getCustomer() != null ? order.getCustomer().getPhone() : "N/A");
        placeholders.put("employeeName", order.getEmployee().getUsername());

        StringBuilder itemsHtml = new StringBuilder();
        for (OrderDetail detail : details) {
            double itemTotal = detail.getQuantity() * detail.getPrice();
            itemsHtml.append("<tr>")
                    .append("<td>").append(detail.getProduct().getName()).append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(String.format("%,.0f", detail.getPrice())).append("</td>")
                    .append("<td class=\"right\">").append(String.format("%,.0f", itemTotal)).append("</td>")
                    .append("</tr>");
        }
        placeholders.put("items", itemsHtml.toString());

        placeholders.put("totalAmount", String.format("%,.0f", order.getTotal()));
        placeholders.put("moneyReceived", String.format("%,.0f", moneyReceived));
        placeholders.put("moneyBack", String.format("%,.0f", moneyBack));

        // Thay thế các placeholder trong HTML
        String finalHtml = HtmlTemplateUtil.replacePlaceholders(htmlContent, placeholders);

        HtmlConverter.convertToPdf(finalHtml, new FileOutputStream(outputPath));
        System.out.println("Hóa đơn đã được tạo tại: " + outputPath);
    }

    // Change Cart to Order
    // public Order makeOrderFromCart(Map<Integer, OrderDetail> cart){


    // }

    public static void main(String[] args) throws Exception {
        OrderBUS bus = new OrderBUS();
        ArrayList<OrderDetail> list = new ArrayList<>();
        // list.add(new OrderDetail(null, null, new Product(1, null, 123, 1, null, "abc", null), 12, 123));
        // try {
        //     bus.createOrder(list, new User(1,"thanh", "thanh",1, true), null);
        // } catch (Exception e) {-
        //     System.out.println(e);
        // }
        // ArrayList<Order> list2 = new ArrayList<>();
        // Map<String,String> criteria = new HashMap<>();
        //     criteria.put("customer_name", "");
        //     //criteria.put("total_max", "4000");
        // list2 = bus.searchOrders(criteria);
        // for( Order l : list2){
        //     System.out.println(l.getId());
        // }
        bus.printReceipt(4, "templates/order.html", "test-printPDF.pdf", 1000000);

    }
}
