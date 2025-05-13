package com.bookstore.gui.util;

import java.awt.Color;
import javax.swing.*;

import com.bookstore.gui.component.Button;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.PanelCover;

public class ColorScheme {
    // ===== MÀU CHÍNH - NÂU NHẸ =====
    public static final Color PRIMARY = new Color(193, 154, 107);       // #C19A6B – Nâu nhẹ (coffee milk)
    public static final Color SECONDARY = new Color(222, 184, 135);     // #DEB887 – Beige nâu sáng
    public static final Color ACCENT = new Color(255, 236, 179);        // #FFECB3 – Vàng kem

    // ===== MÀU NỀN & VIỀN =====
    public static final Color BACKGROUND = new Color(250, 245, 240);    // #FAF5F0 – nền sáng
    public static final Color BORDER = new Color(210, 180, 140);        // #D2B48C – nhẹ và ấm

    // ===== MÀU CHỮ =====
    public static final Color TEXT_LIGHT = Color.WHITE;                 // Trắng sáng
    public static final Color TEXT_DARK = new Color(80, 50, 20);        // #503214 – Nâu đậm

    // ===== GRADIENT =====
    public static final Color[] GRADIENT_PRIMARY = {
        new Color(193, 154, 107),  // PRIMARY
        new Color(222, 184, 135)   // SECONDARY
    };

    public static final Color[] GRADIENT_SECONDARY = {
        new Color(222, 184, 135),
        new Color(255, 236, 179)
    };

    // ===== NỀN CHÍNH =====
    public static final Color BACKGROUND_MAIN = BACKGROUND;
    public static final Color BACKGROUND_SECONDARY = new Color(245, 238, 230); // hơi đậm hơn
    public static final Color SURFACE = Color.WHITE;

    // ===== MÀU CHỮ (CHÍNH & PHỤ) =====
    public static final Color TEXT_PRIMARY = TEXT_DARK;
    public static final Color TEXT_SECONDARY = new Color(120, 90, 60); // Nâu nhẹ

      // vàng kem nhấn
    public static final Color ERROR = new Color(198, 40, 40);           // đỏ sẫm

    // ===== MÀU BORDER KHI FOCUS =====
    public static final Color BORDER_FOCUS = PRIMARY;

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
}
