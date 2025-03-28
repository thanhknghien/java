package com.bookstore.gui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Password extends JPasswordField {
    private String hint;
    private boolean showHint;
    private Color hintColor;
    private Color textColor;
    private Color borderColor;
    private int borderWidth;
    private int borderRadius;

    public Password() {
        setBackground(new Color(0, 0, 0, 0));
        setForeground(Color.WHITE);
        setCaretColor(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setFont(new Font("sansserif", 0, 13));
        setSelectionColor(new Color(75, 175, 80));
        setSelectedTextColor(Color.WHITE);
        setEchoChar('•');
        
        hint = "";
        showHint = true;
        hintColor = new Color(180, 180, 180);
        textColor = Color.WHITE;
        borderColor = new Color(180, 180, 180);
        borderWidth = 1;
        borderRadius = 5;

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showHint) {
                    showHint = false;
                    setText("");
                    setForeground(textColor);
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().equals("")) {
                    showHint = true;
                    setText(hint);
                    setForeground(hintColor);
                }
            }
        });
    }

    public void setHint(String hint) {
        this.hint = hint;
        if (showHint) {
            setText(hint);
            setForeground(hintColor);
        }
    }

    public String getHint() {
        return hint;
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
        if (showHint) {
            setForeground(hintColor);
        }
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        if (!showHint) {
            setForeground(textColor);
        }
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        repaint();
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        g2.setColor(getBackground());
        g2.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2) - 1, getHeight() - (borderWidth * 2) - 1, borderRadius, borderRadius);
        super.paintComponent(g);
    }
}
