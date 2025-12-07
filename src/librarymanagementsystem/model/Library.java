package librarymanagementsystem.model;

import librarymanagementsystem.enums.BookState;
import librarymanagementsystem.exception.BookNotFoundException;
import librarymanagementsystem.exception.MissingParametersException;

public class Library {

    private Book[] books;
    private int count;

    public Library(int capacity) {
        this.books = new Book[capacity];
        this.count = 0;
    }

    public Book[] getBooks() {
        return books;
    }

    public void addBook(Book book) {
        validate(book);

        int filledCount = getFilledCount();
        raiseLibraryCapacity(filledCount);

        books[count] = book;
        count++;
    }

    public void validate(Book book) {
        if (book != null && book.getTitle() == null || book.getAuthor() == null)
            throw new MissingParametersException("Title or Author can not be null!");
    }

    public void raiseLibraryCapacity(int filledCount) {

        if (filledCount * 2 > books.length) {
            int newSize = books.length * 2;
            Book[] temp = new Book[newSize];
            for (int i = 0; i < books.length; i++) {
                temp[i] = books[i];
            }
            books = temp;
        }
    }

    public int getFilledCount() {
        int filledCount = 0;
        for (Book book : books) {
            if (book != null)
                filledCount++;
        }

        return filledCount;
    }

    public void removeBook(Book book) {
        for (int i = 0; i < count; i++) {
            if (books[i].equals(book)) {
                for (int j = i; j < count - 1; j++) {
                    books[j] = books[j + 1];
                }
                books[count - 1] = null;
                count--;
                return;
            }
        }

        throw new BookNotFoundException(book.getTitle() + " book doesn't exist!");
    }

    public void updateBook(String currentTittle, String newAuthor,
                           Integer yearOfPublication, BookState bookState) {

        for (int i = 0; i < count; i++) {
            if (books[i].getTitle().equals(currentTittle)) {
                if (newAuthor != null)
                    books[i].setAuthor(newAuthor);
                if (yearOfPublication != null)
                    books[i].setYearOfPublication(yearOfPublication);
                if (bookState != null)
                    books[i].setState(bookState);
            }
        }
    }

    public Book getBookByTitleOrAuthor(String title, String author) {
        for (int i = 0; i < count; i++) {
            if (books[i].getTitle().equals(title) || books[i].getAuthor().equals(author)) {
                return books[i];
            }
        }

        throw new BookNotFoundException("There is no book with the given title or author!");
    }

    public void sortByYearOfPublication() {

        for (int i = 0; i < count - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < count; j++) {
                if (books[j] != null && books[minIndex] != null &&
                        books[j].getYearOfPublication() < books[minIndex].getYearOfPublication()) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                Book temp = books[i];
                books[i] = books[minIndex];
                books[minIndex] = temp;
            }
        }
    }
}
