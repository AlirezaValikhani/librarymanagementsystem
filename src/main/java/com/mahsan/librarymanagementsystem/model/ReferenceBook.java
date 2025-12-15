package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.Logger;
import com.mahsan.librarymanagementsystem.model.base.Lendable;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

public class ReferenceBook extends LibraryItem implements Lendable {

    private String ISBN;
    private int editionNumber;
    private boolean isLendable = false;

    public ReferenceBook(String UUID, String title, String author, int yearOfPublication, int totalCopies, String ISBN, int editionNumber) {
        super(UUID, title, author, yearOfPublication, totalCopies);
        this.ISBN = ISBN;
        this.editionNumber = editionNumber;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public boolean isLendable() {
        return isLendable;
    }

    public void setLendable(boolean lendable) {
        isLendable = lendable;
    }

    @Override
    public void display(Logger logger) {
        logger.logAction("Display", "--- Reference Book Details ---");
        logger.logAction("Display", "Title: " + getTitle());
        logger.logAction("Display", "Author: " + getAuthor());
        logger.logAction("Display", "Year of Publication: " + getYearOfPublication());
        logger.logAction("Display", "Total copies: " + getTotalCopies());
        logger.logAction("Display", "Edition: " + editionNumber);
        logger.logAction("Display", "Is Lendable: " + (isLendable ? "Yes" : "No"));
    }

    @Override
    public boolean matches(String query) {

        try {
        return getTitle().toLowerCase().contains(query.toLowerCase()) ||
                getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                getYearOfPublication() == Integer.parseInt(query) ||
                getISBN().equalsIgnoreCase(query) ||
                getEditionNumber() == Integer.parseInt(query) ||
                isLendable();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
