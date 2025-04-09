package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomComboBox<E> extends JComboBox<E> {
    private String placeholder;

    public CustomComboBox() {
        super();
        initialize();
    }

    public CustomComboBox(E[] items) {
        super(items);
        initialize();
    }

    private void initialize() {
        setEditable(true); // Cho phép nhập liệu
        setBackground(ColorScheme.SURFACE);
        setForeground(ColorScheme.TEXT_PRIMARY);
        setFont(new Font("Roboto", Font.PLAIN, 14));
        setBorder(new LineBorder(ColorScheme.BORDER, 1));
        setUI(new CustomComboBoxUI());
        customizeEditor();
    }

    private void customizeEditor() {
        Component editorComponent = getEditor().getEditorComponent();
        if (editorComponent instanceof JTextField textField) {
            textField.setForeground(ColorScheme.TEXT_SECONDARY);
            textField.setBackground(ColorScheme.SURFACE);
            textField.setBorder(null);
            textField.setFont(new Font("Roboto", Font.PLAIN, 14));

            textField.setText(placeholder != null ? placeholder : "");

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(ColorScheme.TEXT_PRIMARY);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (textField.getText().isEmpty()) {
                        textField.setText(placeholder);
                        textField.setForeground(ColorScheme.TEXT_SECONDARY);
                    }
                }
            });
        }
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        customizeEditor(); // Áp lại placeholder cho editor
    }

    private static class CustomComboBoxUI extends javax.swing.plaf.basic.BasicComboBoxUI {
        @Override
        protected Button createArrowButton() {
            Button button = new Button("▼");
            button.setBorder(null);
            button.setBackground(ColorScheme.SURFACE);
            button.setForeground(ColorScheme.TEXT_SECONDARY);
            button.setFocusable(false);
            return button;
        }
    }
}
