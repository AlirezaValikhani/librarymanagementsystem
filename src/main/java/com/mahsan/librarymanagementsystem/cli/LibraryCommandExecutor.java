package com.mahsan.librarymanagementsystem.cli;

import com.mahsan.librarymanagementsystem.context.AppContext;
import com.mahsan.librarymanagementsystem.io.CsvDataLoader;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.service.BorrowReturnHandler;
import com.mahsan.librarymanagementsystem.service.LibraryItemCreator;
import com.mahsan.librarymanagementsystem.service.LibraryItemUpdater;

import java.util.List;
import java.util.Scanner;

public class LibraryCommandExecutor {

    private static final String REMOVE_OPERATION_NAME = "Remove";

    private final LibraryManager manager;
    private final Scanner scanner;
    private final CsvDataLoader csvDataLoader;
    private final SystemFileLogger logger;
    private final LibraryItemSelector selector;
    private final LibraryItemCreator creator;
    private final LibraryItemUpdater updater;
    private final BorrowReturnHandler borrowReturnHandler;

    public LibraryCommandExecutor(AppContext context) {
        this.manager = context.getManager();
        this.scanner = context.getScanner();
        this.csvDataLoader = context.getCsvDataLoader();
        this.logger = context.getLogger();
        this.selector = new LibraryItemSelector(manager, scanner, logger);
        this.creator = new LibraryItemCreator(manager, scanner, logger);
        this.updater = new LibraryItemUpdater(manager, scanner, logger, selector);
        this.borrowReturnHandler = new BorrowReturnHandler(manager, scanner, logger, selector);
    }

    public void loadInitialData() {
        csvDataLoader.loadBooksFromFile(manager);
    }

    public String readInput() {
        return scanner.nextLine().trim();
    }

    public void logExit() {
        logger.logAction("Exit", "User exited.");
    }

    public void logInvalidInput() {
        logger.logAction("CLI wrong input", "Invalid input. Choose a number between 1 and 5.");
    }

    public void close() {
        scanner.close();
    }

    public void addItem() {
        creator.addItem();
    }

    public void removeItem() {
        LibraryItem itemToRemove = selector.selectItemBySearch(REMOVE_OPERATION_NAME);

        if (itemToRemove == null)
            return;

        manager.removeItem(itemToRemove);
    }

    public void displayBooks() {
        manager.displayAll();
    }

    public void updateItem() {
        updater.updateItem();
    }

    public void search() {
        logger.logAction("Searching", "Enter anything to search in library: ");
        String query = scanner.nextLine();

        manager.search(query);
    }

    public void sortedList() {
        List<LibraryItem> sortedItems = manager.getSortedItems();
        for (LibraryItem libraryItem : sortedItems)
            libraryItem.display(logger);

        logger.logAction("Sorted list", "Sorted list successfully called.");
    }

    public void borrowItem() {
        borrowReturnHandler.borrowItem();
    }

    public void returnItem() {
        borrowReturnHandler.returnItem();
    }

    public void displayBorrowedItems() {
        manager.displayAllBorrowedItems();
    }
}
