import librarymanagementsystem.enums.BookState;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Library;

public class Main {
    public static void main(String[] args) {

        Library library = new Library(2);
        Book book = new Book("new", "Victor Hogo",
                1951, BookState.EXIST);

        Book newBook = new Book("otherBook", "Will Dorant",
                1943, BookState.BORROWED);

        Book book2 = new Book("something", "fdsa",
                1930, BookState.BANNED);

        System.out.println("-----------Add and sort book test------------\n");

        library.addBook(newBook);
        library.addBook(book2);
        library.addBook(book);

        library.sortByYearOfPublication();

        for (Book b : library.getBooks()) {
            System.out.println(b);
        }

        System.out.println();
        System.out.println("-----------Update book test------------\n");

        System.out.println("Old book data: " + book);
        library.updateBook("new", "Nima Youshij", 1960, BookState.BORROWED);

        for (Book b : library.getBooks()) {
            if (b != null && b.getTitle().equals("new"))
                System.out.println("Updated book data: " + b + "\n");
        }

        System.out.println("-----------Remove book test------------\n");

        library.removeBook(book);

        for (Book b : library.getBooks()) {
            System.out.println(b);
        }

        System.out.println();
        System.out.println("-----------Search book by title or author test------------\n");

        Book foundedBook = library.getBookByTitleOrAuthor("otherBook", "Will Dorant");

        System.out.println("Founded book is : " + foundedBook);
    }
}