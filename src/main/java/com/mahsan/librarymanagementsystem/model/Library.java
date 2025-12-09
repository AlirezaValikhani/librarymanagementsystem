package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.exception.BookNotFoundException;
import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.generic.GenericLinkedList;
import com.mahsan.librarymanagementsystem.model.generic.Node;

public class Library {

    private GenericLinkedList<Book> books;
    private int count;

    public Library() {
        this.books = new GenericLinkedList<>();
        this.count = 0;
    }

    public void addBook(Book book) {
        books.add(book);
        count++;
        System.out.println(book.getTitle() + " book added to library successfully");
    }

    public void displayBooks(FileHandler fileHandler) {
        books.display(fileHandler);
    }

    public void updateBook(String searchKey, String newTitle, String newAuthor, int newYear, FileHandler fileHandler) {
        String keyword = searchKey.toLowerCase();
        Node<Book> current = books.getHead();

        while (current != null) {
            Book book = current.getData();

            boolean titleMatch = book.getTitle().toLowerCase().contains(keyword);
            boolean authorMatch = book.getAuthor().toLowerCase().contains(keyword);

            if (titleMatch || authorMatch) {
                book.setTitle(newTitle);
                book.setAuthor(newAuthor);
                book.setYearOfPublication(newYear);

                fileHandler.logAction("Update book", "Book with search key " + searchKey + " has been updated successfully.");
                System.out.println("Update book with search key " + searchKey + " has been updated successfully.");

                return;
            }

            current = current.getNext();
        }

        fileHandler.logAction("Update book failed", "Book with search key " + searchKey + " doesn't exist!");
        System.out.println("Update book with search key " + searchKey + " doesn't exist!");
    }

    public void removeBookByTitle(String title, FileHandler fileHandler) {

        if (books.getHead() == null)
            throw new BookNotFoundException("There is no book in the library to remove!");

        String searchKeyword = title.toLowerCase();

        Node<Book> current = books.getHead();
        Book bookToRemove = null;

        while (current != null) {
            Book currentBook = current.getData();

            if (currentBook.getTitle().toLowerCase().contains(searchKeyword)) {
                bookToRemove = currentBook;
                break;
            }
            current = current.getNext();
        }

        if (bookToRemove != null) {
            if (books.remove(bookToRemove)) {
                count--;
                System.out.println(bookToRemove.getTitle() + " removed from library successfully");
                fileHandler.logAction("Remove book", "Book with title " + title + " removed from library successfully.");
                return;
            }
        }

        fileHandler.logAction("Remove book fail", "A book with title " + title + " was not found!");
        throw new BookNotFoundException("A book with title containing '" + title + "' was not found!");
    }

    public void remove(Book book) {
        books.remove(book);
        count--;
        System.out.println("The book with title " + book.getTitle() + " has been removed successfully.");
    }

    public int countBooks() {
        return count;
    }
}
