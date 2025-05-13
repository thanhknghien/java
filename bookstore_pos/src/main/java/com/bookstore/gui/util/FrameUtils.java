package com.bookstore.gui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameUtils {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int screenWidth = screenSize.width;
    public static int screenHeight = screenSize.height;
    
    public static void setResizable(JFrame frame) {
        frame.setResizable(false);
    }

    public static void setCenterScreen(JFrame frame) {
        frame.setLocationRelativeTo(null);
    }
    
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

    public static void setupFrame(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());
        setResizable(frame);
        setCenterScreen(frame);
        setCloseButton(frame, "Xác nhận thoát", "Bạn có chắc chắn muốn thoát chương trình?");
    }
} 