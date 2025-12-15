package com.mahsan.librarymanagementsystem.cli;

import com.mahsan.librarymanagementsystem.context.AppContext;
import com.mahsan.librarymanagementsystem.io.FileHandler;
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
    private final FileHandler fileHandler;
    private final LibraryItemSelector selector;
    private final LibraryItemCreator creator;
    private final LibraryItemUpdater updater;
    private final BorrowReturnHandler borrowReturnHandler;

    public LibraryCommandExecutor(AppContext context) {
        this.manager = context.getManager();
        this.scanner = context.getScanner();
        this.fileHandler = context.getFileHandler();
        this.selector = new LibraryItemSelector(manager, scanner, fileHandler);
        this.creator = new LibraryItemCreator(manager, scanner, fileHandler, context.getFactory());
        this.updater = new LibraryItemUpdater(manager, scanner, fileHandler, selector);
        this.borrowReturnHandler = new BorrowReturnHandler(manager, scanner, fileHandler, selector);
    }

    public void loadInitialData() {
        fileHandler.loadBooksFromFile(manager);
    }

    public String readInput() {
        return scanner.nextLine().trim();
    }

    public void logExit() {
        fileHandler.logAction("Exit", "User exited.");
    }

    public void logInvalidInput() {
        fileHandler.logAction("CLI wrong input", "Invalid input. Choose a number between 1 and 5.");
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
        fileHandler.logAction("Searching", "Enter anything to search in library: ");
        String query = scanner.nextLine();

        manager.search(query);
    }

    public void sortedList() {
        List<LibraryItem> sortedItems = manager.getSortedItems();
        for (LibraryItem libraryItem : sortedItems)
            libraryItem.display(fileHandler);

        fileHandler.logAction("Sorted list", "Sorted list successfully called.");
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
