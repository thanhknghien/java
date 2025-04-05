package com.bookstore.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class HtmlTemplateUtil {

    // Đọc nội dung từ file HTML và trả về dưới dạng String
    public static String readHtmlTemplate(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Thay thế các placeholder trong HTML bằng dữ liệu từ Map
    public static String replacePlaceholders(String htmlContent, Map<String, String> placeholders) {
        String result = htmlContent;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }
}