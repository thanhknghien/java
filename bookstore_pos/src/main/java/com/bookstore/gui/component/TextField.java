package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TextField extends JTextField {
    private String placeholder;
    private Color borderColor;
    private Color focusColor;
    private Color placeholderColor;
    private boolean isFocused;

    public TextField() {
        setFont(new Font("Roboto", Font.PLAIN, 14));
        ColorScheme.styleTextField(this); // Áp dụng style từ ColorScheme
        this.placeholder = "";
        this.isFocused = false;

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
        repaint();
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && !isFocused) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(placeholderColor != null ? placeholderColor : ColorScheme.TEXT_SECONDARY);
            g2d.setFont(getFont().deriveFont(Font.ITALIC));
            g2d.drawString(placeholder, getInsets().left, g.getFontMetrics().getAscent() + getInsets().top);
            g2d.dispose();
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(isFocused ? focusColor : borderColor);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
        g2d.dispose();
    }
}