package com.bookstore.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.geom.*;

import com.bookstore.gui.util.ColorScheme;

public class PanelCover extends JPanel {
    private Color backgroundColor = ColorScheme.PRIMARY;
    private Color borderColor = ColorScheme.BORDER;
    private int cornerRadius = 20;
    private int borderWidth = 1;
    private boolean isHovered = false;
    private Color[] gradient = ColorScheme.GRADIENT_PRIMARY;
    private boolean useGradient = true;
    private JLabel titleLabel;
    private JLabel messageLabel;
    private Button actionButton;

    public PanelCover() {
        this("Welcome!", "Get Started", true);
    }

    public PanelCover(String title, String buttonText, boolean showButton) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        initComponents(title, buttonText, showButton);
    }

    private void initComponents(String title, String buttonText, boolean showButton) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);
        
        // Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(50, 30, 20, 30);
        add(titleLabel, gbc);
        
        // Message (optional)
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 30, 50, 30);
        add(messageLabel, gbc);
        
        // Button
        actionButton = new Button(buttonText);
        actionButton.setBackground(ColorScheme.ACCENT);
        actionButton.setForeground(Color.WHITE);
        actionButton.setVisible(showButton);
        gbc.insets = new Insets(20, 50, 50, 50);
        add(actionButton, gbc);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setButtonText(String text) {
        actionButton.setText(text);
    }

    public void setButtonVisible(boolean visible) {
        actionButton.setVisible(visible);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        repaint();
    }

    public void setGradientColors(Color[] colors) {
        this.gradient = colors;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Create shape
        RoundRectangle2D.Float background = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
        
        // Paint background with gradient
        if (useGradient && gradient != null && gradient.length >= 2) {
            GradientPaint gp = new GradientPaint(0, 0, gradient[0], width, height, gradient[2]);
            g2d.setPaint(gp);
        } else {
            g2d.setColor(backgroundColor);
        }
        g2d.fill(background);
        
        // Paint border
        if (borderWidth > 0) {
            g2d.setColor(isHovered ? borderColor.brighter() : borderColor);
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.draw(background);
        }
        
        super.paintComponent(g2d);
        g2d.dispose();
    }

    public void addButtonListener(ActionListener listener) {
        actionButton.addActionListener(listener);
    }
}