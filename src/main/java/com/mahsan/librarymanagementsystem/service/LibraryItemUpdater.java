package com.mahsan.librarymanagementsystem.service;

import com.mahsan.librarymanagementsystem.cli.LibraryItemSelector;
import com.mahsan.librarymanagementsystem.io.CsvDataLoader;
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

        logger.logAction("Prompt", "--- Updating Item: " + itemToUpdate.getTitle() + " (" + itemToUpdate.getClass().getSimpleName() + ") ---");

        logger.logAction("Prompt","Current Title: " + itemToUpdate.getTitle() + " | Enter new Title:");
        String newTitle = scanner.nextLine().trim();
        logger.logAction("Prompt","Current Author: " + itemToUpdate.getAuthor() + " | Enter new Author:");
        String newAuthor = scanner.nextLine().trim();
        int yearOfPublication = getYearOfPublication(itemToUpdate);

        if (yearOfPublication <= 0) {
            logger.logAction("Error", "Invalid year of publication!");
            return;
        }

        try {
            if (itemToUpdate instanceof Book)
                updateBookSpecifics((Book) itemToUpdate, newTitle, newAuthor, yearOfPublication);
            else if (itemToUpdate instanceof Magazine)
                updateMagazineSpecifics((Magazine) itemToUpdate, newTitle, newAuthor, yearOfPublication);
            else if (itemToUpdate instanceof Thesis)
                updateThesisSpecifics((Thesis) itemToUpdate, newTitle, newAuthor, yearOfPublication);
            else if (itemToUpdate instanceof ReferenceBook)
                updateReferenceSpecifics((ReferenceBook) itemToUpdate, newTitle, newAuthor, yearOfPublication);
        } catch (NumberFormatException e) {
            logger.logAction("Error", e.getMessage());
        }
    }

    private int getYearOfPublication(LibraryItem itemToUpdate) {
        logger.logAction("Input", "Current Year: " + itemToUpdate.getYearOfPublication() + " | Enter new Year:");
        String yearOfPublicationString = scanner.nextLine().trim();

        try {
            return Integer.parseInt(yearOfPublicationString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateBookSpecifics(Book book, String newTitle, String newAuthor,
                                     int yearOfPublication) {
        logger.logAction("Update ISBN", "Current ISBN: " + book.getISBN() + " | Enter new ISBN (Leave empty to skip):");
        String newIsbn = scanner.nextLine().trim();
        logger.logAction("Update publisher", "Current Publisher: " + book.getPublisher() + " | Enter new Publisher (Leave empty to skip):");
        String newPublisher = scanner.nextLine().trim();
        logger.logAction("Update book state", "Current Book States: " + book.getState() + " | Enter number to chose Book State (1: Exists, 2: Borrowed, 3: Banned, Leave empty to skip):");
        String bookStateString = scanner.nextLine().trim();

        if (!newIsbn.isEmpty() && !newPublisher.isEmpty() && !bookStateString.isEmpty()) {
            int bookState;

            try {
                bookState = Integer.parseInt(bookStateString);
            } catch (NumberFormatException e) {
                logger.logAction("Error", "Invalid parameter (ISBN, Book State)");
                return;
            }

            String oldTitle = book.getTitle();
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setYearOfPublication(yearOfPublication);
            book.setISBN(newIsbn);
            book.setPublisher(newPublisher);
            book.setState(BookState.fromValue(bookState));
            manager.addItem(book.getUUID(), book);
            logger.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private void updateMagazineSpecifics(Magazine magazine, String newTitle, String newAuthor,
                                         int yearOfPublication) {
        logger.logAction("Update ISSN", "Current ISSN: " + magazine.getISSN() + " | Enter new ISSN (Leave empty to skip):");
        String newISSN = scanner.nextLine().trim();
        logger.logAction("Update volume number", "Current Volume: " + magazine.getVolumeNumber() + " | Enter new Volume Number (Leave empty to skip):");
        String newVolume = scanner.nextLine().trim();
        logger.logAction("Update issue number", "Current Issue: " + magazine.getIssueNumber() + " | Enter new Issue Number (Leave empty to skip):");
        String newIssue = scanner.nextLine().trim();
        if (!newISSN.isEmpty())
            magazine.setISSN(newISSN);

        if (!newISSN.isEmpty() && !newVolume.isEmpty() && !newIssue.isEmpty()) {
            int finalVolume;
            int finalIssue;

            try {
                finalVolume = Integer.parseInt(newVolume);
                finalIssue = Integer.parseInt(newIssue);
            } catch (NumberFormatException e) {
                logger.logAction("Error", "Invalid parameter (volume or issue number).");
                return;
            }
            String oldTitle = magazine.getTitle();
            magazine.setTitle(newTitle);
            magazine.setAuthor(newAuthor);
            magazine.setYearOfPublication(yearOfPublication);
            magazine.setISSN(newISSN);
            magazine.setVolumeNumber(finalVolume);
            magazine.setIssueNumber(finalIssue);
            manager.addItem(magazine.getUUID(), magazine);
            logger.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private void updateThesisSpecifics(Thesis thesis, String newTitle, String newAuthor,
                                       int yearOfPublication) {
        logger.logAction("Update university name", "Current University: " + thesis.getUniversityName() + " | Enter new University Name (Leave empty to skip):");
        String newUniversity = scanner.nextLine().trim();
        logger.logAction("Update advisor name", "Current Advisor: " + thesis.getAdvisorName() + " | Enter new Advisor Name (Leave empty to skip):");
        String newAdvisor = scanner.nextLine().trim();
        logger.logAction("Update degree level", "Current Degree Level: " + thesis.getDegreeLevel() + " | Enter new Degree Level (Leave empty to skip):");
        String newDegreeLevel = scanner.nextLine().trim();

        if (!newUniversity.isEmpty() && !newAdvisor.isEmpty() && !newDegreeLevel.isEmpty()) {
            String oldTitle = thesis.getTitle();
            thesis.setTitle(newTitle);
            thesis.setAuthor(newAuthor);
            thesis.setYearOfPublication(yearOfPublication);
            thesis.setUniversityName(newUniversity);
            thesis.setAdvisorName(newAdvisor);
            thesis.setDegreeLevel(newDegreeLevel);
            manager.addItem(thesis.getUUID(), thesis);
            logger.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private void updateReferenceSpecifics(ReferenceBook refBook, String newTitle, String newAuthor,
                                          int yearOfPublication) {
        logger.logAction("Update ISBN", "Current ISBN: " + refBook.getISBN() + " | Enter new ISBN (Leave empty to skip):");
        String newISBN = scanner.nextLine().trim();
        logger.logAction("Update Edition number", "Current Edition: " + refBook.getEditionNumber() + " | Enter new Edition Number (Leave empty to skip):");
        String newEdition = scanner.nextLine().trim();
        logger.logAction("Update is lendable", "Is Lendable (true/false) (Current: " + refBook.isLendable() + ") | Enter new value (Leave empty to skip):");
        String newLendable = scanner.nextLine().trim();

        if (!newISBN.isEmpty() && !newEdition.isEmpty() && !newLendable.isEmpty()) {
            int finalEdition;
            boolean isLendable;

            try {
                finalEdition = Integer.parseInt(newEdition);
                isLendable = Boolean.parseBoolean(newLendable);
            } catch (NumberFormatException e) {
                logger.logAction("Error", "Invalid parameter (Year, Edition Number, Lendable).");
                return;
            }

            String oldTitle = refBook.getTitle();
            refBook.setTitle(newTitle);
            refBook.setAuthor(newAuthor);
            refBook.setYearOfPublication(yearOfPublication);
            refBook.setISBN(newISBN);
            refBook.setEditionNumber(finalEdition);
            refBook.setLendable(isLendable);
            manager.addItem(refBook.getUUID(), refBook);
            logger.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }
}
