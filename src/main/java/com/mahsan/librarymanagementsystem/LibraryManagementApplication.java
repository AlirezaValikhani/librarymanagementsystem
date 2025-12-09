package com.mahsan.librarymanagementsystem;

import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.Book;
import com.mahsan.librarymanagementsystem.model.Library;

import java.util.Scanner;

public class LibraryManagementApplication {
    public static void main(String[] args) {
        Library library = new Library();
        FileHandler fileHandler = new FileHandler();
        Scanner scanner = new Scanner(System.in);
        boolean runningFlag = true;

        fileHandler.loadBooksFromFile(library);

        while (runningFlag) {
            displayMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    addBook(library, scanner, fileHandler);
                    break;
                case "2":
                    removeBook(library, scanner, fileHandler);
                    break;
                case "3":
                    updateBook(library, scanner, fileHandler);
                    break;
                case "4":
                    displayBooks(library, fileHandler);
                    break;
                case "5":
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

    private static void updateBook(Library library, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Prompt", "Enter search key for update (title or author):");
        String searchKey = scanner.nextLine();

        Book[] matchingBooks = library.searchBooksByPartialMatch(searchKey);

        if (matchingBooks.length == 0) {
            fileHandler.logAction("Update Failed", "No books found matching " + searchKey + ".");
            return;
        }

        fileHandler.logAction("Selection Required", "Found " + matchingBooks.length + " matches. Select a number to update:");

        for (int i = 0; i < matchingBooks.length; i++) {
            Book book = matchingBooks[i];
            fileHandler.logAction("Match Found",
                    "[" + (i + 1) + "] Title: " + book.getTitle() +
                            ", Author: " + book.getAuthor() +
                            ", Year: " + book.getYearOfPublication());
        }

        fileHandler.logAction("Prompt", "Enter the number of the book to update (or 0 to cancel):");
        int selection = -1;
        try {
            selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid selection format. Update canceled.");
            return;
        }

        if (selection == 0) {
            fileHandler.logAction("Operation Canceled", "Update operation canceled by user.");
            return;
        }

        if (selection < 1 || selection > matchingBooks.length) {
            fileHandler.logAction("Error", "Selection number out of range. Update canceled.");
            return;
        }

        Book bookToUpdate = matchingBooks[selection - 1];
        fileHandler.logAction("Update Target", "Selected book: " + bookToUpdate.getTitle());

        fileHandler.logAction("Prompt", "Enter book title:");
        String newTitle = scanner.nextLine();
        fileHandler.logAction("Prompt", "Enter book author:");
        String newAuthor = scanner.nextLine();
        fileHandler.logAction("Prompt", "Enter book year of publication:");
        String yearOfPublication = scanner.nextLine();

        try {
            int newYear = Integer.parseInt(yearOfPublication);
            library.updateBook(bookToUpdate.getTitle(), newTitle, newAuthor, newYear, fileHandler);
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid year of publication: " + yearOfPublication + ". Update failed.");
        }
    }

    private static void displayBooks(Library library, FileHandler fileHandler) {
        library.displayBooks(fileHandler);
    }

    private static void removeBook(Library library, Scanner scanner, FileHandler fileHandler) {
        fileHandler.logAction("Prompt", "Enter search key for removal (title or author):");
        String searchKey = scanner.nextLine();

        Book[] matchingBooks = library.searchBooksByPartialMatch(searchKey);

        if (matchingBooks.length == 0) {
            fileHandler.logAction("Remove Failed", "No books found matching '" + searchKey + "'.");
            return;
        }

        fileHandler.logAction("Selection Required", "Found " + matchingBooks.length + " matches. Select a number to remove:");

        for (int i = 0; i < matchingBooks.length; i++) {
            Book book = matchingBooks[i];
            fileHandler.logAction("Match Found",
                    "[" + (i + 1) + "] Title: " + book.getTitle() +
                            ", Author: " + book.getAuthor() +
                            ", Year: " + book.getYearOfPublication());
        }

        fileHandler.logAction("Prompt", "Enter the number of the book to remove (or 0 to cancel):");
        int selection = -1;
        try {
            selection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid selection format. Removal canceled.");
            return;
        }

        if (selection == 0) {
            fileHandler.logAction("Operation Canceled", "Removal operation canceled by user.");
            return;
        }

        if (selection < 1 || selection > matchingBooks.length) {
            fileHandler.logAction("Error", "Selection number out of range. Removal canceled.");
            return;
        }

        Book bookToRemove = matchingBooks[selection - 1];

        library.removeBookByTitle(bookToRemove.getTitle(), fileHandler);
        fileHandler.logAction("Success", "Book with title " +  bookToRemove.getTitle() + " has been removed successfully.");
    }

    private static void addBook(Library library, Scanner scanner, FileHandler fileHandler) {
        String yearOfPublicationString = null;

        try {
            fileHandler.logAction("Prompt", "Please enter the book name: ");
            String title = scanner.nextLine();
            fileHandler.logAction("Prompt","Please enter the author name: ");
            String authorName = scanner.nextLine();
            fileHandler.logAction("Prompt","Please enter the year of publication name: ");
            yearOfPublicationString = scanner.nextLine();

            Book book = new Book(title, authorName, Integer.parseInt(yearOfPublicationString), BookState.EXIST);
            library.addBook(book);

            fileHandler.logAction("Add book", "Book with title " + title + " added to library successfully.");
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid year of publication : " + yearOfPublicationString);
        }
    }

    private static void displayMenu() {
        System.out.println("\n===============================");
        System.out.println(" Library Management System CLI ");
        System.out.println("===============================");
        System.out.println("1.Add new book");
        System.out.println("2.Delete book with title");
        System.out.println("3.Update book");
        System.out.println("4.Show all books");
        System.out.println("5.Exit");
        System.out.print("Choose a number between 1 and 5 : ");
    }
}