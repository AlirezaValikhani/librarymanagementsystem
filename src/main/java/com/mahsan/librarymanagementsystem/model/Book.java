package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.BookState;

public class Book extends LibraryItem {

    private BookState state;
    private String ISBN;
    private int numberOfCopies;

    public Book(String UUID, String title, String author, int yearOfPublication, BookState state, String ISBN, int numberOfCopies) {
        super(UUID, title, author, yearOfPublication);
        this.state = state;
        this.ISBN = ISBN;
        this.numberOfCopies = numberOfCopies;
    }

    public Book() {
        super();
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

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    @Override
    public void display(FileHandler fileHandler) {
        fileHandler.logAction("Display", "--- Book Details ---");
        fileHandler.logAction("Display", "Title: " + getTitle());
        fileHandler.logAction("Display", "Author: " + getAuthor());
        fileHandler.logAction("Display", "Year of Publication: " + getYearOfPublication());
        fileHandler.logAction("Display", "State: " + state);
        fileHandler.logAction("Display", "ISBN: " + ISBN);
        fileHandler.logAction("Display", "Number of copies: " + numberOfCopies);
    }

    @Override
    public boolean matches(String query) {
        try {
            return getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    getYearOfPublication() == Integer.parseInt(query) ||
                    getISBN().equalsIgnoreCase(query) ||
                    getState().toString().equalsIgnoreCase(query) ||
                    getNumberOfCopies() == Integer.parseInt(query.toLowerCase());
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
