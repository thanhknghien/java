package com.bookstore.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileUtil {
    // Export danh sách dữ liệu ra file Excel
    public static <T> void exportToExcel(String filePath, String[] headers, List<T> data, RowDataExtractor<T> extractor) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Thêm dữ liệu
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                extractor.extractData(data.get(i), row);
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    // Interface để trích xuất dữ liệu từ đối tượng sang hàng Excel
    @FunctionalInterface
    public interface RowDataExtractor<T> {
        void extractData(T item, Row row);
    }
}