package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.util.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InvoicePanel extends JPanel {
    private JTextArea invoiceText;

    public InvoicePanel(String customerName, List<OrderDetail> orderDetails, List<Product> products) {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND_MAIN); // Light Gray

        invoiceText = new JTextArea();
        invoiceText.setEditable(false);
        invoiceText.setFont(new Font("Roboto", Font.PLAIN, 14));
        invoiceText.setForeground(ColorScheme.TEXT_PRIMARY); // Dark Gray
        invoiceText.setBackground(ColorScheme.SURFACE); // White
        invoiceText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(invoiceText), BorderLayout.CENTER);

        displayInvoice(customerName, orderDetails, products);
    }

    private void displayInvoice(String customerName, List<OrderDetail> orderDetails, List<Product> products) {
        StringBuilder invoice = new StringBuilder();
        invoice.append("HÓA ĐƠN BÁN HÀNG\n");
        invoice.append("Khách hàng: ").append(customerName != null ? customerName : "Khách lẻ").append("\n");
        invoice.append("----------------------------------------\n");
        double total = 0;
        for (OrderDetail detail : orderDetails) {
            Product product = products.stream()
                    .filter(p -> p.getId() == detail.getProductId())
                    .findFirst()
                    .orElse(null);
            if (product != null) {
                double amount = detail.getQuantity() * detail.getPrice();
                total += amount;
                invoice.append(product.getName()).append(" x ").append(detail.getQuantity())
                        .append(" - ").append(NumberUtil.formatNumber(amount)).append("\n");
            }
        }
        invoice.append("----------------------------------------\n");
        invoice.append("Tổng tiền: ").append(NumberUtil.formatNumber(total));
        invoiceText.setText(invoice.toString());
    }
}