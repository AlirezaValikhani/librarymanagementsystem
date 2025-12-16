package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.Logger;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.BookState;

public class Book extends LibraryItem {

    private BookState state;
    private String ISBN;
    private String publisher;

    public Book(String UUID, String title, String author, int yearOfPublication, int totalCopies, BookState state, String ISBN, String publisher) {
        super(UUID, title, author, yearOfPublication, totalCopies);
        this.state = state;
        this.ISBN = ISBN;
        this.publisher = publisher;
    }

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public void display(Logger logger) {
        logger.logAction("Display", "--- Book Details ---");
        logger.logAction("Display", "Title: " + getTitle());
        logger.logAction("Display", "Author: " + getAuthor());
        logger.logAction("Display", "Year of Publication: " + getYearOfPublication());
        logger.logAction("Display", "Total copies: " + getTotalCopies());
        logger.logAction("Display", "State: " + state);
        logger.logAction("Display", "ISBN: " + ISBN);
        logger.logAction("Display", "Publisher: " + publisher);
    }

    @Override
    public boolean matches(String query) {
        try {
            return getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    getYearOfPublication() == Integer.parseInt(query) ||
                    getISBN().equalsIgnoreCase(query) ||
                    getState().toString().equalsIgnoreCase(query) ||
                    getPublisher().equals(query.toLowerCase());
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
