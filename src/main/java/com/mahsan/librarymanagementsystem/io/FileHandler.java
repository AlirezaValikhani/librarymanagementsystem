package com.mahsan.librarymanagementsystem.io;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.factory.LibraryItemFactory;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.exception.LogWriteException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileHandler {

    private static final String INPUT_FILE = "books_input.csv";
    private static final String OUTPUT_FILE = "log_output.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LibraryItemFactory libraryItemFactory = new LibraryItemFactory();

    public void loadBooksFromFile(LibraryManager libraryManager) {

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] allData = line.split(",");

                if (allData.length < 4) {
                    logAction("Error", "Invalid line format: " + line);
                    continue;
                }

                String type = allData[0].trim().toLowerCase();
                String[] details = new String[allData.length - 1];
                System.arraycopy(allData, 1, details, 0, details.length);

                String uuid = UUID.randomUUID().toString();
                LibraryItem item = libraryItemFactory.createItem(type, details, uuid);
                libraryManager.addItem(uuid, item);
                logAction("Add item", item.getTitle() + " (" + type + ") added to library successfully");
            }
        } catch (FileNotFoundException e) {
            logAction("Load error", "File not found: " + INPUT_FILE);
            throw new com.mahsan.librarymanagementsystem.exception.FileNotFoundException("File not found");
        } catch (IOException e) {
            logAction("Error", "IO error: " + e.getMessage());
            throw new com.mahsan.librarymanagementsystem.exception.IOException("Error loading file: " + e.getMessage());
        } catch (MissingParametersException e) {
            logAction("Error in create item", "Missing parameters: " + e.getMessage());
        }
    }

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
