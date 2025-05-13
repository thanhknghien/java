package com.bookstore.util;

import java.text.Normalizer;

public class NomalizeVietnamese {
    public static String normalizeVietnamese(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{M}", ""); // Loại bỏ dấu
        return str;
    }
}
