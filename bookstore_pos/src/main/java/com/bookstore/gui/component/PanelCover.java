package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class PanelCover extends JPanel {
    private Color[] gradientColors;
    private Color borderColor;

    public PanelCover() {
        setOpaque(false);
        ColorScheme.stylePanelCover(this); // Áp dụng style từ ColorScheme
    }

    public void setGradientColors(Color[] gradientColors) {
        this.gradientColors = gradientColors;
        repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gradientColors != null && gradientColors.length >= 2) {
            GradientPaint gradient = new GradientPaint(
                    0, 0, gradientColors[0],
                    getWidth(), getHeight(), gradientColors[1]
            );
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(getBackground());
        }

        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        if (borderColor != null) {
            g2d.setColor(borderColor);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }

        g2d.dispose();
    }
}