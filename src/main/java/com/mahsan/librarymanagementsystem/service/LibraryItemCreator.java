package com.mahsan.librarymanagementsystem.service;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.io.CsvDataLoader;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.dto.*;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

import java.util.Scanner;
import java.util.UUID;

public class LibraryItemCreator {

    private final LibraryManager manager;
    private final Scanner scanner;
    private final SystemFileLogger logger;

    public LibraryItemCreator(LibraryManager manager, Scanner scanner, SystemFileLogger logger) {
        this.manager = manager;
        this.scanner = scanner;
        this.logger = logger;
    }

    public void addItem() {
        logger.logAction("Prompt", "--- Add New Item ---");
        logger.logAction("Prompt", "Select item type: (1) Book, (2) Magazine, (3) Thesis, (4) Reference Book");
        System.out.print("Enter choice (1-4): ");
        String type = scanner.nextLine().trim();

        logger.logAction("Prompt", "--- Enter Common Details ---");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Year of Publication: ");
        String yearString = scanner.nextLine();
        System.out.print("Total copies: ");
        String totalCopies = scanner.nextLine();

        int year;
        int total;
        try {
            year = Integer.parseInt(yearString.trim());
            total = Integer.parseInt(totalCopies.trim());
        } catch (NumberFormatException e) {
            logger.logAction("Error", "Invalid number format for year or total copies. Operation canceled.");
            return;
        }

        LibraryItemCreateRequest request;
        LibraryItemType itemType;

        try {
            itemType = LibraryItemType.fromValue(Integer.parseInt(type));
        } catch (NumberFormatException e) {
            logger.logAction("Error", "Invalid item type format. Operation canceled.");
            return;
        } catch (IllegalArgumentException e) {
            logger.logAction("Error", "Unsupported item type selected. Operation canceled.");
            return;
        }

        switch (itemType) {
            case LibraryItemType.BOOK:
                request = createBookRequest(title, author, year, total);
                break;
            case LibraryItemType.MAGAZINE:
                request = createMagazineRequest(title, author, year, total);
                break;
            case LibraryItemType.THESIS:
                request = createThesisRequest(title, author, year, total);
                break;
            case LibraryItemType.REFERENCE:
                request = createReferenceRequest(title, author, year, total);
                break;
            default:
                logger.logAction("Error", "Invalid item type selected. Operation canceled.");
                return;
        }

        if (request == null) {
            logger.logAction("Error", "Invalid item details. Operation canceled.");
            return;
        }

        try {
            String uuid = UUID.randomUUID().toString();
            LibraryItem item = request.createItem(uuid);

            manager.addItem(uuid, item);
            logger.logAction("Add item",
                    "Item " + item.getTitle() + " (" + type + ") added successfully.");

        } catch (MissingParametersException | IllegalArgumentException e) {
            logger.logAction("Error", "Data creation failed for " + type + ": " + e.getMessage());
        }
    }

    private BookCreateRequest createBookRequest(String title, String author, int year, int total) {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("State (1=EXIST, 2=LOANED, 3=BANNED): ");
        String state = scanner.nextLine();

        try {
            BookState bookState = BookState.fromValue(Integer.parseInt(state.trim()));
            return new BookCreateRequest(title, author, year, total, bookState, isbn, publisher);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid book details: " + e.getMessage());
            return null;
        }
    }

    private MagazineCreateRequest createMagazineRequest(String title, String author, int year, int total) {
        System.out.print("ISSN: ");
        String issn = scanner.nextLine();
        System.out.print("Volume Number: ");
        String volume = scanner.nextLine();
        System.out.print("Issue Number: ");
        String issue = scanner.nextLine();

        try {
            int volumeNumber = Integer.parseInt(volume.trim());
            int issueNumber = Integer.parseInt(issue.trim());
            return new MagazineCreateRequest(title, author, year, total, issn, volumeNumber, issueNumber);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid magazine details: " + e.getMessage());
            return null;
        }
    }

    private ThesisCreateRequest createThesisRequest(String title, String author, int year, int total) {
        System.out.print("University Name: ");
        String university = scanner.nextLine();
        System.out.print("Degree Level: ");
        String degree = scanner.nextLine();
        System.out.print("Advisor Name: ");
        String advisor = scanner.nextLine();

        try {
            return new ThesisCreateRequest(title, author, year, total, university, degree, advisor);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid thesis details: " + e.getMessage());
            return null;
        }
    }

    private ReferenceBookCreateRequest createReferenceRequest(String title, String author, int year, int total) {
        System.out.print("ISBN: ");
        String refIsbn = scanner.nextLine();
        System.out.print("Edition Number: ");
        String edition = scanner.nextLine();
        System.out.print("Is Lendable (true/false): ");
        String lendable = scanner.nextLine();

        try {
            int editionNumber = Integer.parseInt(edition.trim());

            if (!lendable.trim().equals("true") && !lendable.trim().equals("false")) {
                logger.logAction("Error", "Invalid lendable details: " + lendable);
                return null;
            }

            boolean isLendable = Boolean.parseBoolean(lendable.trim());
            return new ReferenceBookCreateRequest(title, author, year, total, refIsbn, editionNumber, isLendable);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid reference details: " + e.getMessage());
            return null;
        }
    }
}
