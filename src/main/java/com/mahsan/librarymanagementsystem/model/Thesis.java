package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.Logger;
import com.mahsan.librarymanagementsystem.model.base.Lendable;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

public class Thesis extends LibraryItem implements Lendable {

    private String universityName;
    private String degreeLevel;
    private String advisorName;

    public Thesis(String UUID, String title, String author, int yearOfPublication, int totalCopies, String universityName, String degreeLevel, String advisorName) {
        super(UUID, title, author, yearOfPublication, totalCopies);
        this.universityName = universityName;
        this.degreeLevel = degreeLevel;
        this.advisorName = advisorName;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    @Override
    public void display(Logger logger) {
        logger.logAction("Display", "--- Thesis Details ---");
        logger.logAction("Display", "Title: " + getTitle());
        logger.logAction("Display", "Author: " + getAuthor());
        logger.logAction("Display", "Year of Publication: " + getYearOfPublication());
        logger.logAction("Display", "Total copies: " + getTotalCopies());
        logger.logAction("Display", "University: " + universityName);
        logger.logAction("Display", "Degree Level: " + degreeLevel);
        logger.logAction("Display", "Advisor: " + advisorName);
    }

    @Override
    public boolean matches(String query) {
        try {
            return getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    getYearOfPublication() == Integer.parseInt(query) ||
                    getAdvisorName().toLowerCase().contains(query.toLowerCase()) ||
                    getUniversityName().toLowerCase().contains(query.toLowerCase()) ||
                    getDegreeLevel().toLowerCase().contains(query.toLowerCase());
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
