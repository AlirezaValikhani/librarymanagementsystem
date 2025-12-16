package com.mahsan.librarymanagementsystem.io;

import com.mahsan.librarymanagementsystem.exception.LogWriteException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemFileLogger implements Logger {

    private static final String OUTPUT_FILE = "log_output.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void logAction(String operation, String result) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String log = String.format("[%s] Operation : %s | Result : %s", timestamp, operation, result);

        System.out.println(log);

        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
                bw.write(log);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new LogWriteException("Error in writing output file!");
        }
    }
}
