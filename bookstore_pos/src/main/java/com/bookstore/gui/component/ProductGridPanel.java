package com.bookstore.gui.component;

import javax.swing.*;
import java.awt.*;

public class ProductGridPanel extends JPanel {
    private static final int COLUMNS = 4;
    private int itemCount = 0;

    public ProductGridPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
    }

    @Override
    public Component add(Component comp) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = itemCount % COLUMNS; 
        gbc.gridy = itemCount / COLUMNS; 
        gbc.insets = new Insets(10, 10, 10, 10);

        itemCount++;
        super.add(comp, gbc);
        revalidate();
        repaint();
        return comp;
    }

    public void clearItems() {
        removeAll();
        itemCount = 0;
        revalidate();
        repaint();
    }
}
