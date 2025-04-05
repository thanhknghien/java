package com.bookstore.gui.component;

import com.bookstore.gui.util.ColorScheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomTable extends JTable {
    public CustomTable(String[] columns) {
        super(new DefaultTableModel(columns, 0));
        setFillsViewportHeight(true);
        setBackground(ColorScheme.SURFACE); // White
        setForeground(ColorScheme.TEXT_PRIMARY); // Dark Gray
        setGridColor(ColorScheme.BORDER); // Medium Gray
        setFont(new Font("Roboto", Font.PLAIN, 14));
        getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        getTableHeader().setBackground(ColorScheme.PRIMARY); // Deep Blue
        getTableHeader().setForeground(ColorScheme.TEXT_LIGHT); // White
        setRowHeight(30);
        setSelectionBackground(ColorScheme.SECONDARY); // Light Green
        setSelectionForeground(ColorScheme.TEXT_DARK); // Dark Gray
    }

    public void refreshTable(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}