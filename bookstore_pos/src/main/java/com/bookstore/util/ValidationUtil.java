package com.bookstore.util;

public class ValidationUtil {
    // Kiểm tra chuỗi không rỗng
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Kiểm tra số không âm
    public static boolean isNonNegative(double value) {
        return value >= 0;
    }

    // Kiểm tra số điện thoại (đơn giản: 10 chữ số)
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }
}