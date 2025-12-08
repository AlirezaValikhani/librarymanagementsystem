package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.exception.BookNotFoundException;
import com.mahsan.librarymanagementsystem.generic.GenericLinkedList;
import com.mahsan.librarymanagementsystem.generic.Node;

public class Library {

    private GenericLinkedList<Book> books;
    private int count;

    public Library(int capacity) {
        this.books = new GenericLinkedList<>();
        this.count = 0;
    }

    public void addBook(Book book) {
        books.add(book);
        count++;
        System.out.println(book.getTitle() + " added to library successfully");
    }

    public void displayBooks() {
        books.display();
    }

    public void removeBookByTitle(String title) {

        if (books.getHead() == null)
            throw new BookNotFoundException("There is no book in the library to remove!");

        Node<Book> current = books.getHead();
        Node<Book> previous = null;

        while (current != null) {
            Book currentBook = current.getData();

            if (currentBook.getTitle().equals(title)) {

                if (previous == null)
                    books.setHead(current.getNext());
                else
                    previous.setNext(current.getNext());

                count--;
                System.out.println("The book with title " + title + " has been removed successfully.");
                return;
            }

            previous = current;
            current = current.getNext();
        }

        throw new BookNotFoundException("The book with title " + title + " was not found!");
    }

    public void remove(Book book) {
        books.remove(book);
        count--;
        System.out.println("The book with title " + book.getTitle() + " has been removed successfully.");
    }
}
