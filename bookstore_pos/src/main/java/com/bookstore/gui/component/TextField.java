package com.bookstore.gui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import com.bookstore.gui.util.ColorScheme;

public class TextField extends JTextField {
    private Color backgroundColor = ColorScheme.SURFACE;
    private Color borderColor = ColorScheme.BORDER;
    private Color focusColor = ColorScheme.BORDER_FOCUS;
    private Color textColor = ColorScheme.TEXT_PRIMARY;
    private Color placeholderColor = ColorScheme.TEXT_SECONDARY;
    private String placeholder = "";
    private boolean isFocused = false;
    private int cornerRadius = 10;
    private int borderWidth = 2;

    public TextField() {
        this("");
    }

    public TextField(String text) {
        super(text);
        init();
    }

    private void init() {
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(textColor);
        setCaretColor(textColor);
        
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

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setFocusColor(Color color) {
        this.focusColor = color;
        repaint();
    }

    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Create shapes
        RoundRectangle2D.Float background = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        RoundRectangle2D.Float border = new RoundRectangle2D.Float(borderWidth/2, borderWidth/2, 
            getWidth() - borderWidth - 1, getHeight() - borderWidth - 1, cornerRadius, cornerRadius);
        
        // Paint background
        g2d.setColor(backgroundColor);
        g2d.fill(background);
        
        // Paint border
        g2d.setColor(isFocused ? focusColor : borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.draw(border);
        
        // Paint text
        super.paintComponent(g2d);
        
        // Paint placeholder
        if (getText().isEmpty() && !isFocused && !placeholder.isEmpty()) {
            g2d.setColor(placeholderColor);
            g2d.setFont(getFont().deriveFont(Font.ITALIC));
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(placeholder, g2d);
            int x = getInsets().left;
            int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(placeholder, x, y);
        }
        
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 40);
    }
}
