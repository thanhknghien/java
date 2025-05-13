package com.bookstore;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.bookstore.gui.main.LoginGUI;

public class Main {

  public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}
