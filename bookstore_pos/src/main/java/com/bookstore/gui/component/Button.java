package com.bookstore.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D;

public class Button extends JButton {
    private Color backgroundColor;
    private Color foregroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int radius = 10;
    private Icon icon;
    private int iconTextGap = 10;
    private int horizontalPadding = 15;
    private int verticalPadding = 8;
    private Color[] gradient;
    private boolean useGradient = false;
    
    public Button(String text) {
        super(text);
        init();
    }
    
    public Button(String text, Icon icon) {
        super(text);
        this.icon = icon;
        init();
    }
    
    private void init() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Màu mặc định
        backgroundColor = new Color(66, 66, 66);
        foregroundColor = Color.WHITE;
        hoverColor = new Color(77, 77, 77);
        pressedColor = new Color(55, 55, 55);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Vẽ nền
        if (useGradient && gradient != null && gradient.length >= 2) {
            GradientPaint gp = new GradientPaint(
                0, 0, isPressed ? gradient[0].darker() : (isHovered ? gradient[1] : gradient[0]),
                getWidth(), getHeight(), isPressed ? gradient[1].darker() : (isHovered ? gradient[0] : gradient[1])
            );
            g2d.setPaint(gp);
        } else {
            if (!isEnabled()) {
                g2d.setColor(backgroundColor.darker());
            } else if (isPressed) {
                g2d.setColor(pressedColor);
            } else if (isHovered) {
                g2d.setColor(hoverColor);
            } else {
                g2d.setColor(backgroundColor);
            }
        }
        
        RoundRectangle2D.Float background = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius * 2, radius * 2);
        g2d.fill(background);
        
        // Vẽ text và icon
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(getText(), g2d);
        
        int textX = (getWidth() - (int) rect.getWidth()) / 2;
        int textY = (getHeight() - (int) rect.getHeight()) / 2 + fm.getAscent();
        
        if (icon != null) {
            int iconX = textX - (icon.getIconWidth() + iconTextGap) / 2;
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2d, iconX, iconY);
            textX += (icon.getIconWidth() + iconTextGap) / 2;
        }
        
        g2d.setColor(isEnabled() ? foregroundColor : foregroundColor.darker());
        g2d.drawString(getText(), textX, textY);
        
        g2d.dispose();
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        int width = size.width + horizontalPadding * 2;
        int height = size.height + verticalPadding * 2;
        if (icon != null) {
            width += icon.getIconWidth() + iconTextGap;
        }
        return new Dimension(width, height);
    }
    
    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        this.hoverColor = new Color(
            Math.min(color.getRed() + 20, 255),
            Math.min(color.getGreen() + 20, 255),
            Math.min(color.getBlue() + 20, 255)
        );
        this.pressedColor = new Color(
            Math.max(color.getRed() - 20, 0),
            Math.max(color.getGreen() - 20, 0),
            Math.max(color.getBlue() - 20, 0)
        );
        repaint();
    }
    
    public void setForegroundColor(Color color) {
        this.foregroundColor = color;
        repaint();
    }
    
    public void setButtonIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }
    
    public void setIconTextGap(int gap) {
        this.iconTextGap = gap;
        repaint();
    }
    
    public void setPadding(int horizontal, int vertical) {
        this.horizontalPadding = horizontal;
        this.verticalPadding = vertical;
        revalidate();
        repaint();
    }
    
    public void setGradient(Color[] colors) {
        this.gradient = colors;
        this.useGradient = (colors != null && colors.length >= 2);
        repaint();
    }
}
