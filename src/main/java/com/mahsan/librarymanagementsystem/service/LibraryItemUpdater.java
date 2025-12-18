package com.mahsan.librarymanagementsystem.service;

import com.mahsan.librarymanagementsystem.cli.LibraryItemSelector;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.*;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.BookState;

import java.util.Scanner;

public class LibraryItemUpdater {

    private static final String UPDATE_OPERATION_NAME = "Update";

    private final LibraryManager manager;
    private final Scanner scanner;
    private final SystemFileLogger logger;
    private final LibraryItemSelector selector;

    public LibraryItemUpdater(LibraryManager manager, Scanner scanner, SystemFileLogger logger, LibraryItemSelector selector) {
        this.manager = manager;
        this.scanner = scanner;
        this.logger = logger;
        this.selector = selector;
    }

    public void updateItem() {
        LibraryItem itemToUpdate = selector.selectItemBySearch(UPDATE_OPERATION_NAME);

        if (itemToUpdate == null) {
            logger.logAction("Update canceled", "No item selected for update.");
            return;
        }

        try {
            CommonDetails details = getCommonDetails(itemToUpdate);

            if (details.getYear() == 0) {
                logger.logAction("Update canceled", "Invalid year of publication.");
                return;
            }

            setCommonDetails(itemToUpdate, details);
            updateTypeSpecific(itemToUpdate);
            manager.addItem(itemToUpdate.getUUID(), itemToUpdate);
            logger.logAction("Success", "Item successfully updated: " + itemToUpdate.getTitle());
        } catch (IllegalArgumentException e) {
            logger.logAction("Update canceled", e.getMessage());
        }
    }

    private void updateTypeSpecific(LibraryItem itemToUpdate) {
        if (itemToUpdate instanceof Book)
            updateBookSpecifics((Book) itemToUpdate);
        else if (itemToUpdate instanceof Magazine)
            updateMagazineSpecifics((Magazine) itemToUpdate);
        else if (itemToUpdate instanceof Thesis)
            updateThesisSpecifics((Thesis) itemToUpdate);
        else if (itemToUpdate instanceof ReferenceBook)
            updateReferenceSpecifics((ReferenceBook) itemToUpdate);
    }

    private void setCommonDetails(LibraryItem itemToUpdate, CommonDetails details) {
        itemToUpdate.setTitle(details.getTitle());
        itemToUpdate.setAuthor(details.getAuthor());
        itemToUpdate.setYearOfPublication(details.getYear());
    }

    private CommonDetails getCommonDetails(LibraryItem  itemToUpdate) {
        logger.logAction("Prompt", "--- Updating Item: " + itemToUpdate.getTitle() + " (" + itemToUpdate.getClass().getSimpleName() + ") ---");
        String newTitle = readUpdatedText("Title", itemToUpdate.getTitle());
        String newAuthor = readUpdatedText("Author", itemToUpdate.getAuthor());
        int year = readUpdatedYear(itemToUpdate);
        int totalCopies = readUpdatedTotalCopies(itemToUpdate);

        return new CommonDetails(newTitle, newAuthor, year, totalCopies);
    }

    private String readUpdatedText(String fieldName, String currentValue) {
        logger.logAction("Prompt", "Current " + fieldName + ": " + currentValue + " | Enter new value (Leave empty to keep):");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? currentValue : input;
    }

    private int readUpdatedYear(LibraryItem itemToUpdate) {
        logger.logAction("Prompt", "Current Year: " + itemToUpdate.getYearOfPublication() + " | Enter new Year (Leave empty to keep):");
        String input = scanner.nextLine().trim();
        if (input.isEmpty())
            return itemToUpdate.getYearOfPublication();

        try {
            int year = Integer.parseInt(input);
            return year > 0 ? year : null;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int readUpdatedTotalCopies(LibraryItem itemToUpdate) {
        logger.logAction("Prompt", "Current Total Copies: " + itemToUpdate.getTotalCopies() + " | Enter new Total Copies (Leave empty to keep):");
        String input = scanner.nextLine().trim();
        if (input.isEmpty())
            return itemToUpdate.getTotalCopies();

        try {
            int totalCopies = Integer.parseInt(input);
            return totalCopies > 0 ? totalCopies : null;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateBookSpecifics(Book book) {
        logger.logAction("Prompt", "Current ISBN: " + book.getISBN() + " | Enter new ISBN (Leave empty to keep):");
        String newIsbn = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Publisher: " + book.getPublisher() + " | Enter new Publisher (Leave empty to keep):");
        String newPublisher = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Book State: " + book.getState() + " | Enter state (1=Exists, 2=Borrowed, 3=Banned, Leave empty to keep):");
        String bookStateString = scanner.nextLine().trim();

        if (!newIsbn.isEmpty())
            book.setISBN(newIsbn);
        if (!newPublisher.isEmpty())
            book.setPublisher(newPublisher);

        if (!bookStateString.isEmpty()) {
            try {
                int bookStateValue = Integer.parseInt(bookStateString);
                book.setState(BookState.fromValue(bookStateValue));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid book state value.");
            }
        }
    }

    private void updateMagazineSpecifics(Magazine magazine) {
        logger.logAction("Prompt", "Current ISSN: " + magazine.getISSN() + " | Enter new ISSN (Leave empty to keep):");
        String newISSN = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Volume: " + magazine.getVolumeNumber() + " | Enter new Volume Number (Leave empty to keep):");
        String newVolume = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Issue: " + magazine.getIssueNumber() + " | Enter new Issue Number (Leave empty to keep):");
        String newIssue = scanner.nextLine().trim();

        if (!newISSN.isEmpty())
            magazine.setISSN(newISSN);

        if (!newVolume.isEmpty()) {
            try {
                magazine.setVolumeNumber(Integer.parseInt(newVolume));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid volume number.");
            }
        }

        if (!newIssue.isEmpty()) {
            try {
                magazine.setIssueNumber(Integer.parseInt(newIssue));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid issue number.");
            }
        }
    }

    private void updateThesisSpecifics(Thesis thesis) {
        logger.logAction("Prompt", "Current University: " + thesis.getUniversityName() + " | Enter new University Name (Leave empty to keep):");
        String newUniversity = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Advisor: " + thesis.getAdvisorName() + " | Enter new Advisor Name (Leave empty to keep):");
        String newAdvisor = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Degree Level: " + thesis.getDegreeLevel() + " | Enter new Degree Level (Leave empty to keep):");
        String newDegreeLevel = scanner.nextLine().trim();

        if (!newUniversity.isEmpty())
            thesis.setUniversityName(newUniversity);
        if (!newAdvisor.isEmpty())
            thesis.setAdvisorName(newAdvisor);
        if (!newDegreeLevel.isEmpty())
            thesis.setDegreeLevel(newDegreeLevel);
    }

    private void updateReferenceSpecifics(ReferenceBook refBook) {
        logger.logAction("Prompt", "Current ISBN: " + refBook.getISBN() + " | Enter new ISBN (Leave empty to keep):");
        String newISBN = scanner.nextLine().trim();
        logger.logAction("Prompt", "Current Edition: " + refBook.getEditionNumber() + " | Enter new Edition Number (Leave empty to keep):");
        String newEdition = scanner.nextLine().trim();
        logger.logAction("Prompt", "Is Lendable (true/false) (Current: " + refBook.isLendable() + ") | Enter new value (Leave empty to keep):");
        String newLendable = scanner.nextLine().trim();

        if (!newISBN.isEmpty())
            refBook.setISBN(newISBN);

        if (!newEdition.isEmpty()) {
            try {
                refBook.setEditionNumber(Integer.parseInt(newEdition));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid edition number.");
            }
        }

        if (!newLendable.isEmpty()) {
            if (!newLendable.equalsIgnoreCase("true") && !newLendable.equalsIgnoreCase("false"))
                throw new IllegalArgumentException("Invalid lendable value (expected true/false).");

            refBook.setLendable(Boolean.parseBoolean(newLendable));
        }
    }
}
