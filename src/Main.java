import librarymanagementsystem.enums.BookState;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Library;
import librarymanagementsystem.exception.BookNotFoundException;

public class Main {
    public static void main(String[] args) {

        System.out.println("--- Start testing the library management system ---");
        Library library = new Library(100);

        Book book1 = new Book("Data structure", "Saeed Shahrivari", 2010, BookState.EXIST);
        Book book2 = new Book("Java", "Amery", 2021, BookState.BORROWED);
        Book book3 = new Book("AI", "Josef", 2024, BookState.BANNED);
        Book book4 = new Book("Remove mothed", "Mohammad", 2020, BookState.EXIST);

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);

        System.out.println("\n--- Test remove method of GenericLinkedList ---");
        library.remove(book4);

        System.out.println("\n--- Show the list ---");
        library.displayBooks();

        System.out.println("\n--- Removing Java book ---");
        try {
            library.removeBookByTitle("Java");
        } catch (BookNotFoundException e) {
            System.err.println("Error : " + e.getMessage());
        }

        System.out.println("\n--- Show list after removing middle book ---");
        library.displayBooks();

        System.out.println("\n--- Removing First book (Head) ---");
        try {
            library.removeBookByTitle("Data structure");
        } catch (BookNotFoundException e) {
            System.err.println("Error : " + e.getMessage());
        }

        System.out.println("\n--- Show list after removing Head ---");
        library.displayBooks();

        System.out.println("\n--- Delete not exist book ---");
        try {
            library.removeBookByTitle("System design");
        } catch (BookNotFoundException e) {
            System.err.println("Error : " + e.getMessage());
        }
    }
}