package com.bookstore.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static long startTime;
    private static final String LOG_FILE = "timing.log";

    public static void start() {
        startTime = System.nanoTime();
    }

    public static void stop(String taskName) {
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        String message = String.format("⏱️ [%s] Thời gian thực thi: %.3f ms", taskName, duration);
        System.out.println(message);

        // Ghi log vào file
        //logToFile(taskName, duration);
    }

    private static void logToFile(String taskName, double duration) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String line = String.format("[%s] %s - %.3f ms", timestamp, taskName, duration);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Không thể ghi log vào file: " + e.getMessage());
        }
    }
    
    public static String getCurrentTime() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
}
