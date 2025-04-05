package com.bookstore.gui.util;

import java.awt.Color;
import javax.swing.*;

import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.PanelCover;

public class ColorScheme {
    // Màu chính - Xanh dương đậm
    public static final Color PRIMARY = new Color(33, 150, 243); // Deep Blue

    // Màu phụ - Xanh lá nhạt
    public static final Color SECONDARY = new Color(129, 199, 132); // Light Green

    // Màu nhấn - Cam nhạt
    public static final Color ACCENT = new Color(255, 183, 77); // Soft Orange

    // Màu nền - Xám nhạt
    public static final Color BACKGROUND = new Color(245, 245, 245); // Light Gray

    // Màu viền - Xám trung
    public static final Color BORDER = new Color(189, 189, 189); // Medium Gray

    // Màu chữ sáng
    public static final Color TEXT_LIGHT = new Color(255, 255, 255); // White

    // Màu chữ tối
    public static final Color TEXT_DARK = new Color(33, 33, 33); // Dark Gray

    // Gradient chính (Xanh dương đậm -> Xanh lá nhạt)
    public static final Color[] GRADIENT_PRIMARY = {
        new Color(33, 150, 243),  // Deep Blue
        new Color(129, 199, 132)  // Light Green
    };

    // Gradient phụ (Xanh lá nhạt -> Cam nhạt)
    public static final Color[] GRADIENT_SECONDARY = {
        new Color(129, 199, 132),  // Light Green
        new Color(255, 183, 77)    // Soft Orange
    };

    // Gradient nền (Xám nhạt -> Trắng)
    public static final Color[] GRADIENT_BACKGROUND = {
        new Color(245, 245, 245),  // Light Gray
        new Color(255, 255, 255),  // White
        new Color(245, 245, 245)   // Light Gray
    };

    // Màu nền
    public static final Color BACKGROUND_MAIN = new Color(245, 245, 245); // Light Gray
    public static final Color BACKGROUND_SECONDARY = new Color(230, 230, 230); // Slightly Darker Gray
    public static final Color SURFACE = new Color(255, 255, 255); // White

    // Màu text
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33); // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(97, 97, 97); // Medium Gray

    // Màu trạng thái
    public static final Color SUCCESS = new Color(129, 199, 132); // Light Green
    public static final Color WARNING = new Color(255, 183, 77); // Soft Orange
    public static final Color ERROR = new Color(239, 83, 80); // Red
    public static final Color INFO = PRIMARY; // Deep Blue

    // Màu border
    public static final Color BORDER_FOCUS = PRIMARY; // Deep Blue

    // Phương thức styling cho components
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_MAIN);
    }

    public static void styleButton(Button button, boolean isAccent) {
        button.setBackground(isAccent ? ACCENT : PRIMARY);
        button.setForeground(TEXT_LIGHT);
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
        label.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 14));
    }

    public static void styleTitleLabel(JLabel label) {
        label.setForeground(TEXT_DARK);
        label.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 24));
    }
}