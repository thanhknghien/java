package com.bookstore.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TableUtil {
    // Làm mới bảng và thêm dữ liệu
    public static <T> void refreshTable(JTable table, String[] columns, List<T> data, RowDataExtractor<T> extractor) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (T item : data) {
            Object[] rowData = new Object[columns.length];
            extractor.extractRowData(item, rowData);
            model.addRow(rowData);
        }
        table.setModel(model);
    }

    // Interface để trích xuất dữ liệu từ đối tượng sang hàng bảng
    @FunctionalInterface
    public interface RowDataExtractor<T> {
        void extractRowData(T item, Object[] rowData);
    }
}