package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

public class Magazine extends LibraryItem {

    private String ISSN;
    private int volumeNumber;
    private int issueNumber;

    public Magazine(String title, String author, int yearOfPublication, String ISSN, int volumeNumber, int issueNumber) {
        super(title, author, yearOfPublication);
        this.ISSN = ISSN;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
    }

    public Magazine() {
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
    public void display(FileHandler fileHandler) {
        fileHandler.logAction("Display", "--- Magazine Details ---");
        fileHandler.logAction("Display", "Title: " + getTitle());
        fileHandler.logAction("Display", "Author: " + getAuthor());
        fileHandler.logAction("Display", "Year of Publication: " + getYearOfPublication());
        fileHandler.logAction("Display", "Volume Number: " + volumeNumber);
        fileHandler.logAction("Display", "Issue Number: " + issueNumber);
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
