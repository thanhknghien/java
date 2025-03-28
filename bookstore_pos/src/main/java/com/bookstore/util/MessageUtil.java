package com.bookstore.util;

import javax.swing.*;

public class MessageUtil {
    // Hiển thị thông báo thành công
    public static void showSuccessMessage(JFrame parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // Hiển thị thông báo lỗi
    public static void showErrorMessage(JFrame parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // Hiển thị thông báo xác nhận
    public static boolean showConfirmMessage(JFrame parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}