package com.mahsan.librarymanagementsystem.model.base;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.RentRecord;

import java.util.ArrayList;
import java.util.List;

public abstract class LibraryItem implements Searchable {

    private String UUID;
    private String title;
    private String author;
    private int yearOfPublication;
    private int totalCopies;
    private int availableCopies;
    private List<RentRecord> rentRecords;

    private static final FileHandler fileHandler = new FileHandler();

    public LibraryItem(String UUID, String title, String author, int yearOfPublication, int totalCopies) {
        this.UUID = UUID;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.rentRecords = new ArrayList<>();
    }

    public String getUUID() {
        return UUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public List<RentRecord> getRentRecords() {
        return rentRecords;
    }

    public void setRentRecords(List<RentRecord> rentRecords) {
        this.rentRecords = rentRecords;
    }

    public boolean increaseAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            return true;
        }

        return false;
    }

    public boolean decreaseAvailableCopies(){
        if (availableCopies > 0 ){
            availableCopies--;
            return true;
        }

        return false;
    }

    public void getBorrowingStatus() {
        int borrowedCopies = totalCopies - availableCopies;
        fileHandler.logAction("Borrowed copies report", "Borrowed copies: " + borrowedCopies);
    }

    public abstract void display();

    public abstract boolean matches(String query);
}
