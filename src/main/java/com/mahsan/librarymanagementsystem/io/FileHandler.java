package com.mahsan.librarymanagementsystem.io;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.factory.LibraryItemFactory;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.exception.LogWriteException;
import com.mahsan.librarymanagementsystem.model.dto.*;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileHandler implements Logger {

    private static final String INPUT_FILE = "books_input.csv";
    private static final String OUTPUT_FILE = "log_output.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LibraryItemFactory libraryItemFactory = new LibraryItemFactory();

    public void loadBooksFromFile(LibraryManager libraryManager) {

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] allData = line.split(",");

                    if (allData.length < 4) {
                        logAction("Error", "Invalid line format: " + line);
                        continue;
                    }

                    String typeString = allData[0].trim().toLowerCase();
                    LibraryItemType type = LibraryItemType.valueOf(typeString.toUpperCase());
                    String[] details = new String[allData.length - 1];
                    System.arraycopy(allData, 1, details, 0, details.length);

                    LibraryItemCreateRequest request = buildRequest(type, details);

                    if (request == null) {
                        logAction("Error in create item", "Skipping line due to invalid data: " + line);
                        continue;
                    }

                    String uuid = UUID.randomUUID().toString();
                    LibraryItem item = libraryItemFactory.createItem(request, uuid);
                    libraryManager.addItem(uuid, item);
                    logAction("Add item", item.getTitle() + " (" + type + ") added to library successfully");
                } catch (MissingParametersException e) {
                    logAction("Error in create item", "Missing parameters: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    logAction("Error in create item", "Invalid parameters: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            logAction("Load error", "File not found: " + INPUT_FILE);
            throw new com.mahsan.librarymanagementsystem.exception.FileNotFoundException("File not found");
        } catch (IOException e) {
            logAction("Error", "IO error: " + e.getMessage());
            throw new com.mahsan.librarymanagementsystem.exception.IOException("Error loading file: " + e.getMessage());
        }
    }

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

    private LibraryItemCreateRequest buildRequest(LibraryItemType type, String[] details) {
        try {
            String title = details[0].trim();
            String author = details[1].trim();
            int year = Integer.parseInt(details[2].trim());
            int totalCopies = Integer.parseInt(details[3].trim());

            return switch (type) {
                case BOOK -> {
                    BookState state = BookState.fromValue(Integer.parseInt(details[4].trim()));
                    String isbn = details[5].trim();
                    String publisher = details[6].trim();
                    yield new BookCreateRequest(title, author, year, totalCopies, state, isbn, publisher);
                }
                case MAGAZINE -> {
                    String issn = details[4].trim();
                    int volume = Integer.parseInt(details[5].trim());
                    int issue = Integer.parseInt(details[6].trim());
                    yield new MagazineCreateRequest(title, author, year, totalCopies, issn, volume, issue);
                }
                case THESIS -> {
                    String university = details[4].trim();
                    String degree = details[5].trim();
                    String advisor = details[6].trim();
                    yield new ThesisCreateRequest(title, author, year, totalCopies, university, degree, advisor);
                }
                case REFERENCE -> {
                    String refIsbn = details[4].trim();
                    int edition = Integer.parseInt(details[5].trim());
                    boolean lendable = Boolean.parseBoolean(details[6].trim());
                    yield new ReferenceBookCreateRequest(title, author, year, totalCopies, refIsbn, edition, lendable);
                }
                default -> null;
            };
        } catch (Exception e) {
            logAction("Error", "Invalid parameters: " + e.getMessage());
            return null;
        }
    }
}
