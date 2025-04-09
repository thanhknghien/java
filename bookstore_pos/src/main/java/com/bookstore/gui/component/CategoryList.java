package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class CategoryList extends JList<String> {
    public CategoryList(DefaultListModel<String> model) {
        super(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setBackground(ColorScheme.BACKGROUND_SECONDARY); // Slightly Darker Gray
        setForeground(ColorScheme.TEXT_PRIMARY); // Dark Gray
        setFont(new Font("Roboto", Font.PLAIN, 16));
        setSelectionBackground(ColorScheme.PRIMARY); // Deep Blue
        setSelectionForeground(ColorScheme.TEXT_LIGHT); // White
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                
                Button button = new Button(value.toString());
                button.setSelected(isSelected);
                
                setPreferredSize(null); 
                
                return button;
            }
        });
    }
}