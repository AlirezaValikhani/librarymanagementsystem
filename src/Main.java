import librarymanagementsystem.enums.BookState;
import librarymanagementsystem.exception.BookNotFoundException;
import librarymanagementsystem.io.FileHandler;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Library;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library(100);
        FileHandler fileHandler = new FileHandler();
        Scanner scanner = new Scanner(System.in);
        boolean runningFlag = true;

        int loadedCount = fileHandler.loadBooksFromFile(library);
        fileHandler.logAction("Load book from file", loadedCount + " book added to library successfully.");

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
                    displayBooks(library, fileHandler);
                    break;
                case "4":
                    runningFlag = false;
                    fileHandler.logAction("Exit", "User exited.");
                    System.out.println("Logs saved in log_output.txt file.");
                    break;
                default:
                    System.err.println("Invalid input. Choose a number between 1 and 4.");
                    fileHandler.logAction("CLI wrong input", "Invalid input. User chose wrong number!");
                    break;
            }
        }
        scanner.close();
    }

    private static void displayBooks(Library library, FileHandler fileHandler) {
        library.displayBooks();
        fileHandler.logAction("Display", "Books displayed.");
    }

    private static void removeBook(Library library, Scanner scanner, FileHandler fileHandler) {
        System.out.println("Please enter the title of the book you would like to remove: ");
        String title = scanner.nextLine();

        try {
            library.removeBookByTitle(title);
            fileHandler.logAction("Remove book", "Book with title " + title + " removed from library successfully.");
        } catch (BookNotFoundException e) {
            fileHandler.logAction("Remove book fail", "Book with title " + title + " not found in library!");
            System.err.println("Book with title " + title + " not found in library!");
        }
    }

    private static void addBook(Library library, Scanner scanner, FileHandler fileHandler) {
        String yearOfPublicationString = null;

        try {
            System.out.println("Please enter the book name: ");
            String title = scanner.nextLine();
            System.out.println("Please enter the author name: ");
            String authorName = scanner.nextLine();
            System.out.println("Please enter the year of publication name: ");
            yearOfPublicationString = scanner.nextLine();

            Book book = new Book(title, authorName, Integer.parseInt(yearOfPublicationString), BookState.EXIST);
            library.addBook(book);

            fileHandler.logAction("Add book", "Book with title " + title + " added successfully.");
        } catch (NumberFormatException e) {
            fileHandler.logAction("Error", "Invalid year of publication : " + yearOfPublicationString);
            System.err.println("Invalid year of publication : " + yearOfPublicationString);
        }
    }

    private static void displayMenu() {
        System.out.println("\n===============================");
        System.out.println(" Library Management System CLI ");
        System.out.println("===============================");
        System.out.println("1.Add new book");
        System.out.println("2.Delete book with title");
        System.out.println("3.Show all books");
        System.out.println("4.Exit");
        System.out.print("Choose a number between 1 and 4 : ");
    }
}