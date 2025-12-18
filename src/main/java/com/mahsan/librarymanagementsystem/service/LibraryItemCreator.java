package com.mahsan.librarymanagementsystem.service;

import com.mahsan.librarymanagementsystem.cli.ConsoleInput;
import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.CommonDetails;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.dto.*;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

import java.util.Scanner;
import java.util.UUID;

public class LibraryItemCreator {

    private final LibraryManager manager;
    private final SystemFileLogger logger;
    private final ConsoleInput input;

    public LibraryItemCreator(LibraryManager manager, Scanner scanner, SystemFileLogger logger) {
        this.manager = manager;
        this.logger = logger;
        this.input = new ConsoleInput(scanner, logger);
    }

    public void addItem() {
        logFirstPrompts();
        LibraryItemType itemType = promptItemType();
        CommonDetails commonDetails = promptCommonDetails();

        if (itemType == null)
            return;


        if (commonDetails == null)
            return;

        LibraryItemCreateRequest request = routeTypeToMakeRequest(itemType, commonDetails);

        if (request == null) {
            logger.logAction("Error", "Invalid item details. Operation canceled.");
            return;
        }

        try {
            String uuid = UUID.randomUUID().toString();
            LibraryItem item = request.createItem(uuid);

            manager.addItem(uuid, item);
            logger.logAction("Add item",
                    "Item " + item.getTitle() + " (" + itemType + ") added successfully.");

        } catch (MissingParametersException | IllegalArgumentException e) {
            logger.logAction("Error", "Data creation failed for " + itemType + ": " + e.getMessage());
        }
    }

    private void logFirstPrompts() {
        logger.logAction("Prompt", "--- Add New Item ---");
        logger.logAction("Prompt", "Select item type: (1) Book, (2) Magazine, (3) Thesis, (4) Reference Book");
    }

    private LibraryItemCreateRequest routeTypeToMakeRequest(LibraryItemType itemType,
                                                            CommonDetails commonDetails) {
        return switch (itemType) {
            case BOOK -> createBookRequest(commonDetails);
            case MAGAZINE -> createMagazineRequest(commonDetails);
            case THESIS -> createThesisRequest(commonDetails);
            case REFERENCE -> createReferenceRequest(commonDetails);
            default -> {
                logger.logAction("Error", "Invalid item type selected. Operation canceled.");
                throw new IllegalArgumentException("Unsupported item type: " + itemType);
            }
        };
    }

    private LibraryItemType promptItemType() {
        Integer value = input.readInt("Enter choice (1-4): ", "Invalid item type format. Operation canceled.");

        if (value == null)
            return null;

        try {
            return LibraryItemType.fromValue(value);
        } catch (IllegalArgumentException e) {
            logger.logAction("Error", "Unsupported item type selected. Operation canceled.");
            return null;
        }
    }

    private CommonDetails promptCommonDetails() {
        logger.logAction("Prompt", "--- Enter Common Details ---");
        String title = input.readLine("Title: ");
        String author = input.readLine("Author: ");

        Integer year = input.readInt("Year of Publication: ", "Invalid number format for year. Operation canceled.");
        if (year == null)
            return null;

        Integer totalCopies = input.readInt("Total copies: ", "Invalid number format for total copies. Operation canceled.");
        if (totalCopies == null)
            return null;

        return new CommonDetails(title, author, year, totalCopies);
    }

    private BookCreateRequest createBookRequest(CommonDetails common) {
        String isbn = input.readLine("ISBN: ");
        String publisher = input.readLine("Publisher: ");
        Integer state = input.readInt("State (1=EXIST, 2=LOANED, 3=BANNED): ", "Invalid book state. Operation canceled.");

        if (state == null)
            return null;

        try {
            BookState bookState = BookState.fromValue(state);
            return new BookCreateRequest(common.getTitle(), common.getAuthor(), common.getYear(), common.getTotalCopies(), bookState, isbn, publisher);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid book details: " + e.getMessage());
            return null;
        }
    }

    private MagazineCreateRequest createMagazineRequest(CommonDetails common) {
        String issn = input.readLine("ISSN: ");
        Integer volumeNumber = input.readInt("Volume Number: ", "Invalid magazine volume number. Operation canceled.");
        if (volumeNumber == null)
            return null;
        Integer issueNumber = input.readInt("Issue Number: ", "Invalid magazine issue number. Operation canceled.");
        if (issueNumber == null)
            return null;

        try {
            return new MagazineCreateRequest(common.getTitle(), common.getAuthor(), common.getYear(), common.getTotalCopies(), issn, volumeNumber, issueNumber);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid magazine details: " + e.getMessage());
            return null;
        }
    }

    private ThesisCreateRequest createThesisRequest(CommonDetails common) {
        String university = input.readLine("University Name: ");
        String degree = input.readLine("Degree Level: ");
        String advisor = input.readLine("Advisor Name: ");

        try {
            return new ThesisCreateRequest(common.getTitle(), common.getAuthor(), common.getYear(), common.getTotalCopies(), university, degree, advisor);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid thesis details: " + e.getMessage());
            return null;
        }
    }

    private ReferenceBookCreateRequest createReferenceRequest(CommonDetails common) {
        String refIsbn = input.readLine("ISBN: ");
        Integer editionNumber = input.readInt("Edition Number: ", "Invalid edition number. Operation canceled.");
        if (editionNumber == null)
            return null;

        Boolean isLendable = input.readBooleanStrict("Is Lendable (true/false): ", "Invalid lendable value (expected true/false). Operation canceled.");
        if (isLendable == null)
            return null;

        try {
            return new ReferenceBookCreateRequest(common.getTitle(), common.getAuthor(), common.getYear(),
                    common.getTotalCopies(), refIsbn, editionNumber, isLendable);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid reference details: " + e.getMessage());
            return null;
        }
    }
}
