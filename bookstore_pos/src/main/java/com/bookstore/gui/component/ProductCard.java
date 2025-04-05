package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.Product;
import com.bookstore.util.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProductCard extends JPanel {
    private Product product;
    private JLabel imageLabel;

    public ProductCard(Product product) {
        this.product = product;
        setLayout(new BorderLayout(5, 5));
        setBackground(ColorScheme.SURFACE); // Màu nền trắng
        setBorder(BorderFactory.createLineBorder(ColorScheme.BORDER)); // Viền xám trung
        setPreferredSize(new Dimension(120, 150)); // Tăng chiều cao để chứa ảnh

        // Ảnh sản phẩm
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        loadProductImage();
        add(imageLabel, BorderLayout.NORTH);

        // Tên sản phẩm
        JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        nameLabel.setForeground(ColorScheme.TEXT_PRIMARY); // Chữ xám đậm
        add(nameLabel, BorderLayout.CENTER);

        // Giá sản phẩm
        JLabel priceLabel = new JLabel(NumberUtil.formatNumber(product.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        priceLabel.setForeground(ColorScheme.SECONDARY); // Chữ xanh lá nhạt
        add(priceLabel, BorderLayout.SOUTH);

        // Hiệu ứng hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(ColorScheme.SECONDARY); // Màu xanh lá nhạt khi hover
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(ColorScheme.SURFACE); // Trở lại màu trắng
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void loadProductImage() {
        String imagePath = product.getImagePath();
        ImageIcon imageIcon;

        if (imagePath != null && !imagePath.isEmpty() && new File(imagePath).exists()) {
            imageIcon = new ImageIcon(imagePath);
        } else {
            // Ảnh mặc định nếu không có ảnh sản phẩm
            imageIcon = new ImageIcon("/product/default_img.png"); // Đường dẫn đến ảnh mặc định
        }

        // Resize ảnh để vừa với kích thước của ProductCard
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(imageIcon);
    }

    public Product getProduct() {
        return product;
    }
}