package com.bookstore.gui.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;

public class CustomTable extends JTable {
    public CustomTable(String[] columns) {
        super(createModel(columns));
        setFillsViewportHeight(true);
        setFont(new Font("Roboto", Font.PLAIN, 14));
        getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        setRowHeight(30);
        setColumnWidthForAction("Hành động",150);
        
    }

    // Hàm tạo model không cho phép sửa ô
    private static DefaultTableModel createModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return getColumnName(column).equals("Hành động");
            }
        };
    }

    // Method to set column width if column name matches "Hành động"
    public void setColumnWidthForAction(String columnName, int width) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equals(columnName)) {
                TableColumn column = getColumnModel().getColumn(i);
                column.setPreferredWidth(width);
                break;
            }
        }
    }

    public void refreshTable(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}