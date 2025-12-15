package com.mahsan.librarymanagementsystem.service;

import com.mahsan.librarymanagementsystem.cli.LibraryItemSelector;
import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.RentRecord;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

import java.util.List;
import java.util.Scanner;

public class BorrowReturnHandler {

    private static final String BORROW_OPERATION_NAME = "Borrow";
    private static final String RETURN_OPERATION_NAME = "Return";

    private final LibraryManager manager;
    private final Scanner scanner;
    private final FileHandler fileHandler;
    private final LibraryItemSelector selector;

    public BorrowReturnHandler(LibraryManager manager, Scanner scanner, FileHandler fileHandler, LibraryItemSelector selector) {
        this.manager = manager;
        this.scanner = scanner;
        this.fileHandler = fileHandler;
        this.selector = selector;
    }

    public void borrowItem() {
        fileHandler.logAction("Prompt", "--- Borrow books ---");

        LibraryItem itemToBorrow = selector.selectItemBySearch(BORROW_OPERATION_NAME);

        if (itemToBorrow == null) {
            fileHandler.logAction("Borrow Canceled", "Item doesn't exists!");
            return;
        }

        if (itemToBorrow.getAvailableCopies() <= 0) {
            fileHandler.logAction("Borrow Failed", "This book hasn't enough copies!");
            itemToBorrow.getBorrowingStatus(fileHandler);
            return;
        }

        if (!manager.borrowItem(itemToBorrow.getUUID()))
            fileHandler.logAction("Borrow Failed", "Borrow Failed!");
    }

    public void returnItem() {
        fileHandler.logAction("Prompt", "--- Return book ---");

        LibraryItem itemToReturn = selector.selectItemBySearch(RETURN_OPERATION_NAME);

        if (itemToReturn == null) {
            fileHandler.logAction("Return Canceled", "There is no item returned.");
            return;
        }

        List<RentRecord> activeRecords = itemToReturn.getRentRecords().stream()
                .filter(rentRecord -> rentRecord.getReturnDate() == null)
                .toList();

        if (activeRecords.isEmpty()) {
            fileHandler.logAction("Return Failed", itemToReturn.getTitle() + " There is no active rent record.");
            return;
        }

        fileHandler.logAction("Selection Required", "Choose active records for: " + itemToReturn.getTitle());
        for (int i = 0; i < activeRecords.size(); i++) {
            RentRecord record = activeRecords.get(i);
            fileHandler.logAction("Active Record",
                    String.format("[%d] Record ID: %d | Borrow Date: %s",
                            i + 1, record.getRecordId(), record.getBorrowDate()));
        }

        fileHandler.logAction("Prompt", "Enter the Record ID for return (or 0 to cancel):");
        String recordIdString = scanner.nextLine().trim();
        int recordId;

        try {
            recordId = Integer.parseInt(recordIdString);
            if (recordId == 0) return;

            if (recordId > 0 && recordId <= activeRecords.size())
                recordId = activeRecords.get(recordId - 1).getRecordId();

        } catch (NumberFormatException e) {
            fileHandler.logAction("Return failed", "Wrong record ID format! Return cancelled.");
            return;
        }

        if (!manager.returnItem(itemToReturn.getUUID(), recordId))
            fileHandler.logAction("Return Failed", "Error while recording the return or the Record ID was not found.");
    }
}
