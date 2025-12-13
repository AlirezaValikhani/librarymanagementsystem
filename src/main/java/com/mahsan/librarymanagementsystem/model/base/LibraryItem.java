package com.mahsan.librarymanagementsystem.model.base;

import com.mahsan.librarymanagementsystem.io.FileHandler;

public abstract class LibraryItem implements Searchable {

    private String UUID;
    private String title;
    private String author;
    private int yearOfPublication;

    public LibraryItem(String UUID, String title, String author, int yearOfPublication) {
        this.UUID = UUID;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public LibraryItem() {
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

    public abstract void display(FileHandler  fileHandler);

    public abstract boolean matches(String query);
}
