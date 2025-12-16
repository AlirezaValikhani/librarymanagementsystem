package com.mahsan.librarymanagementsystem.cli;

import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

import java.util.List;
import java.util.Scanner;

public class LibraryItemSelector {

    private final LibraryManager manager;
    private final Scanner scanner;
    private final SystemFileLogger logger;

    public LibraryItemSelector(LibraryManager manager, Scanner scanner, SystemFileLogger logger) {
        this.manager = manager;
        this.scanner = scanner;
        this.logger = logger;
    }

    public LibraryItem selectItemBySearch(String operationName) {
        logger.logAction("Prompt", "Enter search key for " + operationName + " (title or author):");
        String searchKey = scanner.nextLine();

        List<LibraryItem> matchingItems = manager.searchLibraryItemByPartialMatch(searchKey);

        if (matchingItems.isEmpty()) {
            logger.logAction(operationName + " Failed", "No items found matching " + searchKey + ".");
            return null;
        }

        logger.logAction("Selection Required", "Found " + matchingItems.size() + " matches. Select a number for " + operationName + ":");

        for (int i = 0; i < matchingItems.size(); i++) {
            LibraryItem item = matchingItems.get(i);
            logger.logAction("Match Found", "[" + (i + 1) + "] Title: " + item.getTitle() +
                    ", Author: " + item.getAuthor() + ", Year: " + item.getYearOfPublication() + " (Type: " + item.getClass().getSimpleName() + ")");
        }

        logger.logAction("Prompt", "Enter the number of the item to " + operationName.toLowerCase() + " (or 0 to cancel):");
        int selection;
        try {
            selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            logger.logAction("Error", "Invalid selection format. " + operationName + " canceled.");
            return null;
        }

        if (selection == 0) {
            logger.logAction("Operation Canceled", operationName + " operation canceled by user.");
            return null;
        }

        if (selection < 1 || selection > matchingItems.size()) {
            logger.logAction("Error", "Selection number out of range. " + operationName + " canceled.");
            return null;
        }

        LibraryItem itemToSelect = matchingItems.get(selection - 1);
        logger.logAction("Target Selected", operationName + " target: " + itemToSelect.getTitle());
        return itemToSelect;
    }
}
