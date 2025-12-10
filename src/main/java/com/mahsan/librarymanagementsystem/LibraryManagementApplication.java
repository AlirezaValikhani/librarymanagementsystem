package com.mahsan.librarymanagementsystem;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.factory.LibraryItemFactory;
import com.mahsan.librarymanagementsystem.model.LibraryManager;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.io.FileHandler;

import java.util.Scanner;

public class LibraryManagementApplication {
    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();
        FileHandler fileHandler = new FileHandler();
        Scanner scanner = new Scanner(System.in);
        boolean runningFlag = true;

        fileHandler.loadBooksFromFile(libraryManager);

        while (runningFlag) {
            displayMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    addItem(libraryManager, scanner, fileHandler);
                    break;
                case "2":
                    removeItem(libraryManager, scanner, fileHandler);
                    break;
                case "3":
                    updateItem(libraryManager, scanner, fileHandler);
                    break;
                case "4":
                    displayBooks(libraryManager, fileHandler);
                    break;
                case "5":
                    search(libraryManager, scanner, fileHandler);
                    break;
                case "6":
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

    private static void search(LibraryManager libraryManager, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Searching", "Enter anything to search in library: ");
        String query = scanner.nextLine();

        libraryManager.search(query, fileHandler);
    }

    private static void updateItem(LibraryManager manager, Scanner scanner, FileHandler fileHandler) {
        LibraryItem itemToUpdate = selectItemBySearch(manager, scanner, fileHandler, "Update");

        if (itemToUpdate == null)
            return;

        fileHandler.logAction("Prompt", "Enter new Title (Leave empty to keep current: " + itemToUpdate.getTitle() + "):");
        String newTitle = scanner.nextLine();
        fileHandler.logAction("Prompt", "Enter new Author (Leave empty to keep current: " + itemToUpdate.getAuthor() + "):");
        String newAuthor = scanner.nextLine();
        fileHandler.logAction("Prompt", "Enter new Year of Publication (Current: " + itemToUpdate.getYearOfPublication() + "):");
        String yearOfPublication = scanner.nextLine();

        try {
            int currentYear = itemToUpdate.getYearOfPublication();
            int newYear = yearOfPublication.isEmpty() ? currentYear : Integer.parseInt(yearOfPublication);

            manager.updateItemFields(
                    itemToUpdate.getTitle(),
                    newTitle.isEmpty() ? itemToUpdate.getTitle() : newTitle,
                    newAuthor.isEmpty() ? itemToUpdate.getAuthor() : newAuthor,
                    newYear,
                    fileHandler);

            fileHandler.logAction("Success", "Item '" + itemToUpdate.getTitle() + "' successfully updated.");

        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid year of publication format. Update failed.");
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
        fileHandler.logAction("Success", "Item '" +  itemToRemove.getTitle() + "' has been removed successfully.");
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
            LibraryItemFactory factory = new LibraryItemFactory();
            LibraryItem item = factory.createItem(type, factoryArgs);

            manager.addItem(item);
            fileHandler.logAction("Add item",
                    "Item '" + item.getTitle() + "' (" + type + ") added successfully.");

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
        System.out.print("State (1=EXIST, 2=LOANED): ");
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

        LibraryItem[] matchingItems = manager.searchLibraryItemByPartialMatch(searchKey);

        if (matchingItems.length == 0) {
            fileHandler.logAction(operationName + " Failed", "No items found matching '" + searchKey + "'.");
            return null;
        }

        fileHandler.logAction("Selection Required", "Found " + matchingItems.length + " matches. Select a number for " + operationName + ":");

        for (int i = 0; i < matchingItems.length; i++) {
            LibraryItem item = matchingItems[i];
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

        if (selection < 1 || selection > matchingItems.length) {
            fileHandler.logAction("Error", "Selection number out of range. " + operationName + " canceled.");
            return null;
        }

        LibraryItem itemToSelect = matchingItems[selection - 1];
        fileHandler.logAction("Target Selected", operationName + " target: " + itemToSelect.getTitle());
        return itemToSelect;
    }

    private static void displayMenu() {
        System.out.println("\n===============================");
        System.out.println(" Library Management System CLI ");
        System.out.println("===============================");
        System.out.println("1.Add new book");
        System.out.println("2.Delete book with title");
        System.out.println("3.Update book");
        System.out.println("4.Show all books");
        System.out.println("5.Search");
        System.out.println("6.Exit");
        System.out.print("Choose a number between 1 and 5 : ");
    }
}