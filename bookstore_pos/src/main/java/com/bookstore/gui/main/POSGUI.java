package com.bookstore.gui.main;

import com.bookstore.gui.component.*;
import com.bookstore.gui.util.ColorScheme;
import com.bookstore.gui.util.FrameUtils;
import com.bookstore.model.Customer;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;
import com.bookstore.gui.component.TextField;
import com.bookstore.gui.component.Button;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class POSGUI extends JFrame {
    

    public POSGUI() {
        
        initializeUI();
    }

    private void initializeUI() {
        FrameUtils.setupFrame(this, "POS SYSTEM", 1200, 800);
        setLayout(new BorderLayout());

        
    }

    private void openCheckoutDialog() {
        
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private Button increaseButton;
        private Button decreaseButton;
        private Button removeButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout());
            setBackground(ColorScheme.SURFACE);
            increaseButton = new Button("+");
            decreaseButton = new Button("-");
            removeButton = new Button("Xóa");
            ColorScheme.styleButton(increaseButton, false);
            ColorScheme.styleButton(decreaseButton, false);
            ColorScheme.styleButton(removeButton, true);
            add(increaseButton);
            add(decreaseButton);
            add(removeButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private Button increaseButton;
        private Button decreaseButton;
        private Button removeButton;
        private int row;

        public ButtonEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout());
            panel.setBackground(ColorScheme.SURFACE);
            increaseButton = new Button("+");
            decreaseButton = new Button("-");
            removeButton = new Button("Xóa");
            ColorScheme.styleButton(increaseButton, false);
            ColorScheme.styleButton(decreaseButton, false);
            ColorScheme.styleButton(removeButton, true);

            panel.add(increaseButton);
            panel.add(decreaseButton);
            panel.add(removeButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new POSGUI().setVisible(true));
    }
}