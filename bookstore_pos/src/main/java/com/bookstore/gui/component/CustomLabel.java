package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {
    public CustomLabel(String text) {
        super(text);
        setFont(new Font("Roboto", Font.BOLD, 16));
        setForeground(ColorScheme.PRIMARY); // Deep Blue
        setBackground(ColorScheme.BACKGROUND_MAIN); // Light Gray
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}