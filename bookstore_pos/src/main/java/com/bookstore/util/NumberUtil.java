package com.bookstore.util;

import java.text.DecimalFormat;

public class NumberUtil {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0");

    // Định dạng số (giá tiền, tổng tiền)
    public static String formatNumber(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    // Chuyển String sang double
    public static double parseNumber(String numberStr) throws NumberFormatException {
        return Double.parseDouble(numberStr.replace(",", ""));
    }
}