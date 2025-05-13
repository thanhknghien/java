package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class CategoryList extends JList<String> {
    public CategoryList(DefaultListModel<String> model) {
        super(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setBackground(ColorScheme.BACKGROUND_SECONDARY);
        setForeground(ColorScheme.TEXT_PRIMARY);
        setFont(new Font("Roboto", Font.PLAIN, 16));
        setSelectionBackground(ColorScheme.PRIMARY);
        setSelectionForeground(ColorScheme.TEXT_LIGHT);
        setFixedCellHeight(40);
        
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(isSelected ? ColorScheme.PRIMARY : ColorScheme.BACKGROUND_SECONDARY);
                label.setForeground(isSelected ? ColorScheme.TEXT_LIGHT : ColorScheme.TEXT_PRIMARY);
                label.setFont(new Font("Roboto", Font.PLAIN, 16));
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(200, Integer.MAX_VALUE); 
    }
}
