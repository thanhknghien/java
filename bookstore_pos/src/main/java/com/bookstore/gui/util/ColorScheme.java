package com.bookstore.gui.util;

import java.awt.Color;
import javax.swing.*;

import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.PanelCover;

public class ColorScheme {
    // Màu chính - Hồng pastel
    public static final Color PRIMARY = new Color(255, 182, 193);
    
    // Màu phụ - Xanh mint
    public static final Color SECONDARY = new Color(152, 251, 152);
    
    // Màu nhấn - Vàng pastel
    public static final Color ACCENT = new Color(255, 218, 185);
    
    // Màu nền - Trắng kem
    public static final Color BACKGROUND = new Color(255, 253, 245);
    
    // Màu viền - Xám nhạt
    public static final Color BORDER = new Color(230, 230, 230);
    
    // Màu chữ sáng
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);
    
    // Màu chữ tối
    public static final Color TEXT_DARK = new Color(51, 51, 51);
    
    // Gradient chính (Hồng -> Xanh mint)
    public static final Color[] GRADIENT_PRIMARY = {
        new Color(255, 182, 193),  // Hồng pastel
        new Color(255, 218, 185),  // Vàng pastel
        new Color(152, 251, 152)   // Xanh mint
    };
    
    // Gradient phụ (Xanh mint -> Hồng)
    public static final Color[] GRADIENT_SECONDARY = {
        new Color(152, 251, 152),  // Xanh mint
        new Color(255, 218, 185),  // Vàng pastel
        new Color(255, 182, 193)   // Hồng pastel
    };
    
    // Gradient nền (Trắng kem -> Hồng nhạt)
    public static final Color[] GRADIENT_BACKGROUND = {
        new Color(255, 253, 245),  // Trắng kem
        new Color(255, 240, 245),  // Hồng nhạt
        new Color(255, 253, 245)   // Trắng kem
    };

    // Màu nền
    public static final Color BACKGROUND_MAIN = new Color(255, 253, 245);    // Trắng kem
    public static final Color BACKGROUND_SECONDARY = new Color(255, 240, 245); // Hồng nhạt
    public static final Color SURFACE = new Color(255, 255, 255);    // Trắng
    
    // Màu text
    public static final Color TEXT_PRIMARY = new Color(51, 51, 51);     // Đen nhạt
    public static final Color TEXT_SECONDARY = new Color(128, 128, 128); // Xám
    
    // Màu trạng thái
    public static final Color SUCCESS = new Color(152, 251, 152);    // Xanh mint
    public static final Color WARNING = new Color(255, 218, 185);    // Vàng pastel
    public static final Color ERROR = new Color(255, 182, 193);      // Hồng pastel
    public static final Color INFO = PRIMARY;                        // Hồng pastel
    
    // Màu border
    public static final Color BORDER_FOCUS = PRIMARY;                // Hồng pastel
    
    // Phương thức styling cho components
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_MAIN);
    }

    public static void styleButton(Button button, boolean isAccent) {
        button.setBackground(isAccent ? ACCENT : PRIMARY);
        button.setForeground(TEXT_DARK);
        if (isAccent) {
            button.setGradient(GRADIENT_SECONDARY);
        } else {
            button.setGradient(GRADIENT_PRIMARY);
        }
    }

    public static void styleTextField(TextField field) {
        field.setBackground(SURFACE);
        field.setForeground(TEXT_PRIMARY);
        field.setBorderColor(BORDER);
        field.setFocusColor(BORDER_FOCUS);
        field.setPlaceholderColor(TEXT_SECONDARY);
    }

    public static void stylePanelCover(PanelCover panel) {
        panel.setBackground(PRIMARY);
        panel.setGradientColors(GRADIENT_PRIMARY);
        panel.setBorderColor(BORDER);
    }

    public static void styleLabel(JLabel label, boolean isPrimary) {
        label.setForeground(isPrimary ? TEXT_PRIMARY : TEXT_SECONDARY);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    }

    public static void styleTitleLabel(JLabel label) {
        label.setForeground(TEXT_DARK);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
    }
}