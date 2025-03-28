package com.bookstore.gui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameUtils {
    
    /**
     * Thiết lập frame không thể thay đổi kích thước
     * @param frame Frame cần thiết lập
     */
    public static void setResizable(JFrame frame) {
        frame.setResizable(false);
    }
    
    /**
     * Thiết lập frame luôn hiển thị ở giữa màn hình
     * @param frame Frame cần thiết lập
     */
    public static void setCenterScreen(JFrame frame) {
        frame.setLocationRelativeTo(null);
    }
    
    /**
     * Thiết lập nút đóng frame với xác nhận
     * @param frame Frame cần thiết lập
     * @param title Tiêu đề dialog xác nhận
     * @param message Nội dung dialog xác nhận
     */
    public static void setCloseButton(JFrame frame, String title, String message) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                    frame,
                    message,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (option == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }
    
    /**
     * Thiết lập tất cả các thuộc tính cơ bản cho frame
     * @param frame Frame cần thiết lập
     * @param title Tiêu đề frame
     * @param width Chiều rộng frame
     * @param height Chiều cao frame
     */
    public static void setupFrame(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        setResizable(frame);
        setCenterScreen(frame);
        setCloseButton(frame, "Xác nhận thoát", "Bạn có chắc chắn muốn thoát chương trình?");
    }
} 