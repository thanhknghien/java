package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {
    private Color[] gradientColors;
    private boolean isGradient;
    
    public Button(String text, String imagePath) {
        super(text);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Roboto", Font.BOLD, 14));
        ColorScheme.styleButton(this, false); // Áp dụng style mặc định

        // Thêm icon vào nút nếu có đường dẫn ảnh hợp lệ
        if (imagePath != null && !imagePath.isEmpty()) {
            setIcon(new ImageIcon(imagePath));
        }

        // Thêm hiệu ứng hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (isGradient) {
                    setBackground(getBackground().brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ColorScheme.styleButton(Button.this, getBackground() == ColorScheme.ACCENT);
            }
        });
    }

    public Button(String text) {
        super(text);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Roboto", Font.BOLD, 14));
        ColorScheme.styleButton(this, false); // Áp dụng style mặc định
        
        // Thêm hiệu ứng hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                if (isGradient) {
                    setBackground(getBackground().brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ColorScheme.styleButton(Button.this, getBackground() == ColorScheme.ACCENT);
            }
        });
    }

    public void setGradient(Color[] gradientColors) {
        this.gradientColors = gradientColors;
        this.isGradient = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isGradient && gradientColors != null && gradientColors.length >= 2) {
            GradientPaint gradient = new GradientPaint(
                    0, 0, gradientColors[0],
                    getWidth(), getHeight(), gradientColors[1]
            );
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(getBackground());
        }

        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g2d);
        g2d.dispose();
    }
}