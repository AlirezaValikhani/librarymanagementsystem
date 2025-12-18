package com.mahsan.librarymanagementsystem.model;

public class CommonDetails {

    private String title;
    private String author;
    private int year;
    private int totalCopies;

    public CommonDetails(String title, String author, int year, int totalCopies) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.totalCopies = totalCopies;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
}
