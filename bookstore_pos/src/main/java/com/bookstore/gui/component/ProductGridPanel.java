package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;
import com.bookstore.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductGridPanel extends JPanel {
    private List<ProductCard> productCards;
    private Consumer<Product> productClickListener;

    public ProductGridPanel() {
        setLayout(new GridLayout(0, 3, 10, 10)); // 3 cột, khoảng cách 10px
        setBackground(ColorScheme.SURFACE); // Màu nền trắng
        productCards = new ArrayList<>();
    }

    // Đặt listener cho sự kiện nhấn vào sản phẩm
    public void setProductClickListener(Consumer<Product> listener) {
        this.productClickListener = listener;
    }

    // Cập nhật danh sách sản phẩm
    public void setProducts(List<Product> products) {
        removeAll(); // Xóa các ProductCard hiện tại
        productCards.clear();

        for (Product product : products) {
            ProductCard card = new ProductCard(product);
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (productClickListener != null) {
                        productClickListener.accept(product);
                    }
                }
            });
            productCards.add(card);
            add(card);
        }

        revalidate();
        repaint();
    }
}