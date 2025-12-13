package com.mahsan.librarymanagementsystem;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.factory.LibraryItemFactory;
import com.mahsan.librarymanagementsystem.model.*;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.enums.BookState;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class LibraryManagementApplication {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
        FileHandler fileHandler = new FileHandler();
        Scanner scanner = new Scanner(System.in);
        boolean runningFlag = true;

        fileHandler.loadBooksFromFile(manager);

        while (runningFlag) {
            displayMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    addItem(manager, scanner, fileHandler);
                    break;
                case "2":
                    removeItem(manager, scanner, fileHandler);
                    break;
                case "3":
                    updateItem(manager, scanner, fileHandler);
                    break;
                case "4":
                    displayBooks(manager, fileHandler);
                    break;
                case "5":
                    search(manager, scanner, fileHandler);
                    break;
                case "6":
                    sortedList(manager, fileHandler);
                    break;
                case "7":
                    runningFlag = false;
                    fileHandler.logAction("Exit", "User exited.");
                    break;
                default:
                    fileHandler.logAction("CLI wrong input", "Invalid input. Choose a number between 1 and 5.");
                    break;
            }
        }
        scanner.close();
    }

    private static void sortedList(LibraryManager manager,  FileHandler fileHandler) {
        manager.getSortedItems().forEach(libraryItem -> libraryItem.display(fileHandler));
        fileHandler.logAction("Sorted list", "Sorted list successfully called.");
    }

    private static void search(LibraryManager manager, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Searching", "Enter anything to search in library: ");
        String query = scanner.nextLine();

        manager.search(query, fileHandler);
    }

    private static void updateItem(LibraryManager manager, Scanner scanner, FileHandler fileHandler) {
        LibraryItem itemToUpdate = selectItemBySearch(manager, scanner, fileHandler, "Update");

        if (itemToUpdate == null) {
            fileHandler.logAction("Update canceled", "No item selected or found.");
            return;
        }

        fileHandler.logAction("Prompt", "--- Updating Item: " + itemToUpdate.getTitle() + " (" + itemToUpdate.getClass().getSimpleName() + ") ---");

        System.out.println("Current Title: " + itemToUpdate.getTitle() + " | Enter new Title:");
        String newTitle = scanner.nextLine().trim();
        System.out.println("Current Author: " + itemToUpdate.getAuthor() + " | Enter new Author:");
        String newAuthor = scanner.nextLine().trim();
        System.out.println("Current Year: " + itemToUpdate.getYearOfPublication() + " | Enter new Year:");
        String yearOfPublication = scanner.nextLine().trim();

        try {
            if (itemToUpdate instanceof Book)
                updateBookSpecifics(manager, (Book) itemToUpdate, newTitle, newAuthor, yearOfPublication, scanner, fileHandler);
            else if (itemToUpdate instanceof Magazine)
                updateMagazineSpecifics(manager, (Magazine) itemToUpdate, newTitle, newAuthor, yearOfPublication, scanner, fileHandler);
            else if (itemToUpdate instanceof Thesis)
                updateThesisSpecifics(manager, (Thesis) itemToUpdate, newTitle, newAuthor, yearOfPublication, scanner, fileHandler);
            else if (itemToUpdate instanceof ReferenceBook)
                updateReferenceSpecifics(manager, (ReferenceBook) itemToUpdate, newTitle, newAuthor, yearOfPublication, scanner, fileHandler);
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", e.getMessage());
        }
    }

    private static void updateBookSpecifics(LibraryManager manager, Book book, String newTitle, String newAuthor,
                                            String yearOfPublicationString, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Update ISBN", "Current ISBN: " + book.getISBN() + " | Enter new ISBN (Leave empty to skip):");
        String newIsbn = scanner.nextLine().trim();
        fileHandler.logAction("Update number of copies", "Current Copies: " + book.getNumberOfCopies() + " | Enter new Copies Count (Leave empty to skip):");
        String newCopies = scanner.nextLine().trim();
        fileHandler.logAction("Update book state", "Current Book States: " + book.getState() + " | Enter number to chose Book State (1: Exists, 2: Borrowed, 3: Banned, Leave empty to skip):");
        String bookStateString = scanner.nextLine().trim();

        if (!newIsbn.isEmpty() && !newCopies.isEmpty() && !bookStateString.isEmpty()) {
            int yearOfPublication;
            int finalCopies;
            int bookState;

            try {
                yearOfPublication = Integer.parseInt(yearOfPublicationString);
                finalCopies = Integer.parseInt(newCopies);
                bookState = Integer.parseInt(bookStateString);
            } catch (NumberFormatException e) {
                fileHandler.logAction("Error", "Invalid parameter (ISBN, Number of copies, Book State)");
                return;
            }

            String oldTitle = book.getTitle();
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setYearOfPublication(yearOfPublication);
            book.setISBN(newIsbn);
            book.setNumberOfCopies(finalCopies);
            book.setState(BookState.fromValue(bookState));
            manager.addItem(book.getUUID(), book);
            fileHandler.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private static void updateMagazineSpecifics(LibraryManager manager, Magazine magazine, String newTitle, String newAuthor,
                                                String yearOfPublicationString, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Update ISSN", "Current ISSN: " + magazine.getISSN() + " | Enter new ISSN (Leave empty to skip):");
        String newISSN = scanner.nextLine().trim();
        fileHandler.logAction("Update volume number", "Current Volume: " + magazine.getVolumeNumber() + " | Enter new Volume Number (Leave empty to skip):");
        String newVolume = scanner.nextLine().trim();
        fileHandler.logAction("Update issue number", "Current Issue: " + magazine.getIssueNumber() + " | Enter new Issue Number (Leave empty to skip):");
        String newIssue = scanner.nextLine().trim();
        if (!newISSN.isEmpty())
            magazine.setISSN(newISSN);


        if (!newISSN.isEmpty() && !newVolume.isEmpty() && !newIssue.isEmpty()) {
            int yearOfPublication;
            int finalVolume;
            int finalIssue;

            try {
                yearOfPublication = Integer.parseInt(yearOfPublicationString);
                finalVolume = Integer.parseInt(newVolume);
                finalIssue = Integer.parseInt(newIssue);
            } catch (NumberFormatException e) {
                fileHandler.logAction("Error", "Invalid parameter (volume or issue number).");
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
            fileHandler.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private static void updateThesisSpecifics(LibraryManager manager, Thesis thesis, String newTitle, String newAuthor,
                                              String yearOfPublicationString, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Update university name", "Current University: " + thesis.getUniversityName() + " | Enter new University Name (Leave empty to skip):");
        String newUniversity = scanner.nextLine().trim();
        fileHandler.logAction("Update advisor name", "Current Advisor: " + thesis.getAdvisorName() + " | Enter new Advisor Name (Leave empty to skip):");
        String newAdvisor = scanner.nextLine().trim();
        fileHandler.logAction("Update degree level", "Current Degree Level: " + thesis.getDegreeLevel() + " | Enter new Degree Level (Leave empty to skip):");
        String newDegreeLevel = scanner.nextLine().trim();

        if (!newUniversity.isEmpty() && !newAdvisor.isEmpty() && !newDegreeLevel.isEmpty()) {
            int yearOfPublication;

            try {
                yearOfPublication = Integer.parseInt(yearOfPublicationString);
            } catch (NumberFormatException e) {
                fileHandler.logAction("Error", "Invalid parameter (Year of publication)!");
                return;
            }
            String oldTitle = thesis.getTitle();
            thesis.setTitle(newTitle);
            thesis.setAuthor(newAuthor);
            thesis.setYearOfPublication(yearOfPublication);
            thesis.setUniversityName(newUniversity);
            thesis.setAdvisorName(newAdvisor);
            thesis.setDegreeLevel(newDegreeLevel);
            manager.addItem(thesis.getUUID(), thesis);
            fileHandler.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private static void updateReferenceSpecifics(LibraryManager manager, ReferenceBook refBook, String newTitle, String newAuthor,
                                                 String yearOfPublicationString, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Update ISBN", "Current ISBN: " + refBook.getISBN() + " | Enter new ISBN (Leave empty to skip):");
        String newISBN = scanner.nextLine().trim();
        fileHandler.logAction("Update Edition number", "Current Edition: " + refBook.getEditionNumber() + " | Enter new Edition Number (Leave empty to skip):");
        String newEdition = scanner.nextLine().trim();
        fileHandler.logAction("Update is lendable", "Is Lendable (true/false) (Current: " + refBook.isLendable() + ") | Enter new value (Leave empty to skip):");
        String newLendable = scanner.nextLine().trim();

        if (!newISBN.isEmpty() && !newEdition.isEmpty() && !newLendable.isEmpty()) {
            int yearOfPublication;
            int finalEdition;
            boolean isLendable;

            try {
                yearOfPublication = Integer.parseInt(yearOfPublicationString);
                finalEdition = Integer.parseInt(newEdition);
                isLendable = Boolean.parseBoolean(newLendable);

            } catch (NumberFormatException e) {
                fileHandler.logAction("Error", "Invalid parameter (Year, Edition Number, Lendable).");
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
            fileHandler.logAction("Success", "Item " + oldTitle + " successfully updated.");
        }
    }

    private static void displayBooks(LibraryManager libraryManager, FileHandler fileHandler) {
        libraryManager.displayAll(fileHandler);
    }

    private static void removeItem(LibraryManager manager, Scanner scanner, FileHandler fileHandler) {
        LibraryItem itemToRemove = selectItemBySearch(manager, scanner, fileHandler, "Remove");

        if (itemToRemove == null)
            return;

        manager.removeItem(itemToRemove, fileHandler);
        fileHandler.logAction("Success", "Item " + itemToRemove.getTitle() + " has been removed successfully.");
    }

    private static void addItem(LibraryManager manager, Scanner scanner, FileHandler fileHandler) {

        fileHandler.logAction("Prompt", "--- Add New Item ---");
        fileHandler.logAction("Prompt", "Select item type: (1) Book, (2) Magazine, (3) Thesis, (4) Reference Book");
        System.out.print("Enter choice (1-4): ");
        String typeChoice = scanner.nextLine().trim();

        String type;
        switch (typeChoice) {
            case "1":
                type = "book";
                break;
            case "2":
                type = "magazine";
                break;
            case "3":
                type = "thesis";
                break;
            case "4":
                type = "reference";
                break;
            default:
                fileHandler.logAction("Error", "Invalid item type selected. Operation canceled.");
                return;
        }

        fileHandler.logAction("Prompt", "--- Enter Common Details ---");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Year of Publication: ");
        String yearString = scanner.nextLine();

        String[] details;

        switch (type) {
            case "book":
                details = getBookDetails(scanner);
                break;
            case "magazine":
                details = getMagazineDetails(scanner);
                break;
            case "thesis":
                details = getThesisDetails(scanner);
                break;
            case "reference":
                details = getReferenceDetails(scanner);
                break;
            default:
                return;
        }

        String[] factoryArgs = new String[3 + details.length];

        factoryArgs[0] = title;
        factoryArgs[1] = author;
        factoryArgs[2] = yearString;

        System.arraycopy(details, 0, factoryArgs, 3, details.length);

        try {
            String uuid = UUID.randomUUID().toString();
            LibraryItemFactory factory = new LibraryItemFactory();
            LibraryItem item = factory.createItem(type, factoryArgs, uuid);

            manager.addItem(uuid, item);
            fileHandler.logAction("Add item",
                    "Item " + item.getTitle() + " (" + type + ") added successfully.");

        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid number format detected (Year, Issue number, Volume, etc.). Operation failed.");
        } catch (MissingParametersException | IllegalArgumentException e) {
            fileHandler.logAction("Error", "Data creation failed for " + type + ": " + e.getMessage());
        }
    }

    private static String[] getBookDetails(Scanner scanner) {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Copies Count: ");
        String copies = scanner.nextLine();
        System.out.print("State (1=EXIST, 2=LOANED, 3=BANNED): ");
        String state = scanner.nextLine();

        return new String[]{state, isbn, copies};
    }

    private static String[] getMagazineDetails(Scanner scanner) {
        System.out.print("ISSN: ");
        String issn = scanner.nextLine();
        System.out.print("Volume Number: ");
        String volume = scanner.nextLine();
        System.out.print("Issue Number: ");
        String issue = scanner.nextLine();

        return new String[]{issn, volume, issue};
    }

    private static String[] getThesisDetails(Scanner scanner) {
        System.out.print("University Name: ");
        String university = scanner.nextLine();
        System.out.print("Degree Level: ");
        String degree = scanner.nextLine();
        System.out.print("Advisor Name: ");
        String advisor = scanner.nextLine();

        return new String[]{university, degree, advisor};
    }

    private static String[] getReferenceDetails(Scanner scanner) {
        System.out.print("ISBN: ");
        String refIsbn = scanner.nextLine();
        System.out.print("Edition Number: ");
        String edition = scanner.nextLine();
        System.out.print("Is Lendable (true/false): ");
        String lendable = scanner.nextLine();

        return new String[]{refIsbn, edition, lendable};
    }

    private static LibraryItem selectItemBySearch(LibraryManager manager, Scanner scanner,
                                                  FileHandler fileHandler, String operationName) {
        fileHandler.logAction("Prompt", "Enter search key for " + operationName + " (title or author):");
        String searchKey = scanner.nextLine();

        List<LibraryItem> matchingItems = manager.searchLibraryItemByPartialMatch(searchKey);

        if (matchingItems.isEmpty()) {
            fileHandler.logAction(operationName + " Failed", "No items found matching " + searchKey + ".");
            return null;
        }

        fileHandler.logAction("Selection Required", "Found " + matchingItems.size() + " matches. Select a number for " + operationName + ":");

        for (int i = 0; i < matchingItems.size(); i++) {
            LibraryItem item = matchingItems.get(i);
            fileHandler.logAction("Match Found", "[" + (i + 1) + "] Title: " + item.getTitle() +
                    ", Author: " + item.getAuthor() + ", Year: " + item.getYearOfPublication() + " (Type: " + item.getClass().getSimpleName() + ")");
        }

        fileHandler.logAction("Prompt", "Enter the number of the item to " + operationName.toLowerCase() + " (or 0 to cancel):");
        int selection = -1;
        try {
            selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid selection format. " + operationName + " canceled.");
            return null;
        }

        if (selection == 0) {
            fileHandler.logAction("Operation Canceled", operationName + " operation canceled by user.");
            return null;
        }

        if (selection < 1 || selection > matchingItems.size()) {
            fileHandler.logAction("Error", "Selection number out of range. " + operationName + " canceled.");
            return null;
        }

        LibraryItem itemToSelect = matchingItems.get(selection - 1);
        fileHandler.logAction("Target Selected", operationName + " target: " + itemToSelect.getTitle());
        return itemToSelect;
    }

    private static void displayMenu() {
        System.out.println("\n===============================");
        System.out.println(" Library Management System CLI ");
        System.out.println("===============================");
        System.out.println("1.Add new book");
        System.out.println("2.Delete book with title or author");
        System.out.println("3.Update book");
        System.out.println("4.Show all books");
        System.out.println("5.Search");
        System.out.println("6.Sorted list");
        System.out.println("7.Exit");
        System.out.print("Choose a number between 1 and 5 : ");
    }
}