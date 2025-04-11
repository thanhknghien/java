package com.bookstore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HtmlTemplateUtil {

    public static String readHtmlTemplate(String resourcePath) throws IOException {
        // Sử dụng ClassLoader để đọc file từ resources
        ClassLoader classLoader = HtmlTemplateUtil.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
        
        if (inputStream == null) {
            throw new IOException("Không tìm thấy file: " + resourcePath);
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

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