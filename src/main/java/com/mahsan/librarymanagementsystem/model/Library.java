package com.mahsan.librarymanagementsystem.model;

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
    }

    public void displayBooks(FileHandler fileHandler) {
        books.display(fileHandler);

        fileHandler.logAction("Display Books Operation", "Books displayed.");
    }

    public void updateBook(String title, String newTitle, String newAuthor, int newYear, FileHandler fileHandler) {
        Node<Book> current = books.getHead();

        while (current != null) {
            Book book = current.getData();

            if (book.getTitle().equals(title)) {
                book.setTitle(newTitle);
                book.setAuthor(newAuthor);
                book.setYearOfPublication(newYear);

                fileHandler.logAction("Update book", "Book with title " + title + " has been updated successfully.");
                return;
            }

            current = current.getNext();
        }

        fileHandler.logAction("Update book failed", "Book with title " + title + " doesn't exist!");
    }

    public void removeBookByTitle(String title, FileHandler fileHandler) {

        if (books.getHead() == null)
            fileHandler.logAction("Remove book fail", "There is no book in the library to remove!");

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
            if (books.remove(bookToRemove, fileHandler)) {
                count--;
                return;
            }
        }

        fileHandler.logAction("Remove book fail", "A book with title " + title + " was not found!");
    }

        public Book[] searchBooksByPartialMatch(String searchKey) {
        String keyword = searchKey.toLowerCase();

        int matchCount = 0;
        Node<Book> current = books.getHead();

        while (current != null) {
            Book book = current.getData();

            boolean titleMatch = book.getTitle().toLowerCase().contains(keyword);
            boolean authorMatch = book.getAuthor().toLowerCase().contains(keyword);

            if (titleMatch || authorMatch) {
                matchCount++;
            }
            current = current.getNext();
        }

        if (matchCount == 0)
            return new Book[0];

        Book[] matchingBooks = new Book[matchCount];
        int index = 0;
        current = books.getHead();

        while (current != null) {
            Book book = current.getData();

            boolean titleMatch = book.getTitle().toLowerCase().contains(keyword);
            boolean authorMatch = book.getAuthor().toLowerCase().contains(keyword);

            if (titleMatch || authorMatch) {
                matchingBooks[index] = book;
                index++;
            }
            current = current.getNext();
        }

        return matchingBooks;
    }

    public void remove(Book book) {
        books.remove(book, null);
        count--;
        System.out.println("The book with title " + book.getTitle() + " has been removed successfully.");
    }

    public int countBooks() {
        return count;
    }
}
