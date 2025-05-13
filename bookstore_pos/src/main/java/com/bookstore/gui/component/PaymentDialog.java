package com.bookstore.gui.component;

import com.bookstore.controller.POSController;
import com.bookstore.gui.main.POSGUI;
import com.bookstore.model.OrderDetail;
import com.bookstore.util.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class PaymentDialog extends JDialog {
    private JPanel centerPanel;
    private JPanel keypadPanel;
    private CustomLabel totalLabel;
    private CustomLabel paidLabel;
    private JButton payButton;
    

    private double totalAmount;
    private double paidAmount = 0;

    public PaymentDialog(POSGUI parent, Map<Integer, OrderDetail> orderDetailMap) {
        super(parent, "Thanh toán", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Tính tổng tiền từ OrderDetail
        totalAmount = orderDetailMap.values().stream()
                .mapToDouble(od -> od.getPrice() * od.getQuantity())
                .sum();

        // Center panel: danh sách sản phẩm thu gọn
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        for (OrderDetail od : orderDetailMap.values()) {
            CustomLabel itemLabel = new CustomLabel(od.getProduct().getName() + " x" + od.getQuantity() + "  = " + od.getPrice() * od.getQuantity() + " đ");
            centerPanel.add(itemLabel);
        }

        // Keypad panel: nhập số tiền khách đưa
        keypadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "X", "C"};
        for (String key : keys) {
            JButton btn = new Button(key);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleKeyPress(key);
                }
            });
            keypadPanel.add(btn);
        }
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        keypadPanel.setBackground(Color.WHITE);
        keypadPanel.setOpaque(false);
        keypadPanel.setPreferredSize(new Dimension(200, 200));

        // Thông tin thanh toán
        totalLabel = new CustomLabel("Tổng tiền: " + NumberUtil.formatNumber(totalAmount) + " đ");
        paidLabel = new CustomLabel("Khách đưa: 0đ");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(paidLabel, BorderLayout.CENTER);

        payButton = new Button("Thanh toán");
        payButton.addActionListener(e -> {
            if (paidAmount >= totalAmount) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công! Thối lại: " + NumberUtil.formatNumber((paidAmount - totalAmount)) + "đ");
                try {
                    POSController controller = new POSController(parent);
                    controller.handleCheckout(paidAmount);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(parent, "Có lỗi xảy ra! " + e1);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Khách đưa chưa đủ tiền!");
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(keypadPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        add(payButton, BorderLayout.NORTH);
    }

    private void handleKeyPress(String key) {
        if (key.equals("C")) {
            paidAmount = 0;
        } else if (key.equals("X")) {
            paidAmount /= 10;
        } else {
            paidAmount = paidAmount * 10 + Integer.parseInt(key);
        }
        paidLabel.setText("Khách đưa: " + NumberUtil.formatNumber(paidAmount) + " đ");
    }
}