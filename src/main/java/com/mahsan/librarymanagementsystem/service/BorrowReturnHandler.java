package com.mahsan.librarymanagementsystem.service;

import com.mahsan.librarymanagementsystem.cli.LibraryItemSelector;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
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
    private final SystemFileLogger logger;
    private final LibraryItemSelector selector;

    public BorrowReturnHandler(LibraryManager manager, Scanner scanner, SystemFileLogger logger, LibraryItemSelector selector) {
        this.manager = manager;
        this.scanner = scanner;
        this.logger = logger;
        this.selector = selector;
    }

    public void borrowItem() {
        logger.logAction("Prompt", "--- Borrow books ---");

        LibraryItem itemToBorrow = selector.selectItemBySearch(BORROW_OPERATION_NAME);

        if (itemToBorrow == null) {
            logger.logAction("Borrow Canceled", "Item doesn't exists!");
            return;
        }

        if (itemToBorrow.getAvailableCopies() <= 0) {
            logger.logAction("Borrow Failed", "This book hasn't enough copies!");
            itemToBorrow.getBorrowingStatus(logger);
            return;
        }

        if (!manager.borrowItem(itemToBorrow.getUUID()))
            logger.logAction("Borrow Failed", "Borrow Failed!");
    }

    public void returnItem() {
        logger.logAction("Prompt", "--- Return book ---");

        LibraryItem itemToReturn = selector.selectItemBySearch(RETURN_OPERATION_NAME);

        if (itemToReturn == null) {
            logger.logAction("Return Canceled", "There is no item returned.");
            return;
        }

        List<RentRecord> activeRecords = itemToReturn.getRentRecords().stream()
                .filter(rentRecord -> rentRecord.getReturnDate() == null)
                .toList();

        if (activeRecords.isEmpty()) {
            logger.logAction("Return Failed", itemToReturn.getTitle() + " There is no active rent record.");
            return;
        }

        logger.logAction("Selection Required", "Choose active records for: " + itemToReturn.getTitle());
        for (int i = 0; i < activeRecords.size(); i++) {
            RentRecord record = activeRecords.get(i);
            logger.logAction("Active Record",
                    String.format("[%d] Record ID: %d | Borrow Date: %s",
                            i + 1, record.getRecordId(), record.getBorrowDate()));
        }

        logger.logAction("Prompt", "Enter the Record ID for return (or 0 to cancel):");
        String recordIdString = scanner.nextLine().trim();
        int recordId;

        try {
            recordId = Integer.parseInt(recordIdString);
            if (recordId == 0) return;

            if (recordId > 0 && recordId <= activeRecords.size())
                recordId = activeRecords.get(recordId - 1).getRecordId();

        } catch (NumberFormatException e) {
            logger.logAction("Return failed", "Wrong record ID format! Return cancelled.");
            return;
        }

        if (!manager.returnItem(itemToReturn.getUUID(), recordId))
            logger.logAction("Return Failed", "Error while recording the return or the Record ID was not found.");
    }
}
