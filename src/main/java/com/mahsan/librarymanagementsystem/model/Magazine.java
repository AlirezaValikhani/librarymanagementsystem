package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.Logger;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

public class Magazine extends LibraryItem {

    private String ISSN;
    private int volumeNumber;
    private int issueNumber;

    public Magazine(String UUID, String title, String author, int yearOfPublication, int totalCopies, String ISSN, int volumeNumber, int issueNumber) {
        super(UUID, title, author, yearOfPublication, totalCopies);
        this.ISSN = ISSN;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    @Override
    public void display(Logger logger) {
        logger.logAction("Display", "--- Magazine Details ---");
        logger.logAction("Display", "Title: " + getTitle());
        logger.logAction("Display", "Author: " + getAuthor());
        logger.logAction("Display", "Year of Publication: " + getYearOfPublication());
        logger.logAction("Display", "Total copies: " + getTotalCopies());
        logger.logAction("Display", "Volume Number: " + volumeNumber);
        logger.logAction("Display", "Issue Number: " + issueNumber);
    }

    @Override
    public boolean matches(String query) {
        try {
            return getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    getYearOfPublication() == Integer.parseInt(query) ||
                    getISSN().equalsIgnoreCase(query) ||
                    getIssueNumber() == Integer.parseInt(query) ||
                    getVolumeNumber() == Integer.parseInt(query);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
