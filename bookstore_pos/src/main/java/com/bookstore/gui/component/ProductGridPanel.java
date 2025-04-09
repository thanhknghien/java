package com.bookstore.gui.component;

import com.bookstore.model.Product;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProductGridPanel extends JPanel {
    private ActionListener cartListener;

    public ProductGridPanel() {
        this.cartListener = cartListener;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Color.WHITE);
    }

    public void displayProducts(ArrayList<Product> products) {
        removeAll();
        for (Product p : products) {
            ProductCard card = new ProductCard(p);
            card.addAddToCartListener(cartListener);
            add(card);
        }
        revalidate();
        repaint();
    }
}