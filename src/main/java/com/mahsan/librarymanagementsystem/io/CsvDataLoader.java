package com.mahsan.librarymanagementsystem.io;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.dto.*;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

import java.io.*;
import java.util.UUID;

public class CsvDataLoader {

    private static final String INPUT_FILE = "books_input.csv";
    private final Logger logger;
    private final CsvRequestRegistry csvRequestRegistry;

    public CsvDataLoader(Logger logger) {
        this.logger = logger;
        this.csvRequestRegistry = new CsvRequestRegistry(logger);
    }

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
                        logger.logAction("Error", "Invalid line format: " + line);
                        continue;
                    }

                    String typeString = allData[0].trim().toLowerCase();
                    LibraryItemType type = LibraryItemType.valueOf(typeString.toUpperCase());
                    String[] details = new String[allData.length - 1];
                    System.arraycopy(allData, 1, details, 0, details.length);

                    LibraryItemCreateRequest request = csvRequestRegistry.buildRequest(type, details);
                    if (request == null) {
                        logger.logAction("Error in create item", "Skipping line due to invalid data: " + line);
                        continue;
                    }

                    String uuid = UUID.randomUUID().toString();
                    LibraryItem item = request.createItem(uuid);
                    libraryManager.addItem(uuid, item);
                    logger.logAction("Add item", item.getTitle() + " (" + type + ") added to library successfully");
                } catch (MissingParametersException e) {
                    logger.logAction("Error in create item", "Missing parameters: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    logger.logAction("Error in create item", "Invalid parameters: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            logger.logAction("Load error", "File not found: " + INPUT_FILE);
            throw new com.mahsan.librarymanagementsystem.exception.FileNotFoundException("File not found");
        } catch (IOException e) {
            logger.logAction("Error", "IO error: " + e.getMessage());
            throw new com.mahsan.librarymanagementsystem.exception.IOException("Error loading file: " + e.getMessage());
        }
    }
}
