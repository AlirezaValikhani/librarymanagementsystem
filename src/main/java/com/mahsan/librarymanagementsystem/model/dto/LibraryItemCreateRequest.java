package com.mahsan.librarymanagementsystem.model.dto;

import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public abstract class LibraryItemCreateRequest {
    private final LibraryItemType type;
    private final String title;
    private final String author;
    private final int yearOfPublication;
    private final int totalCopies;

    protected LibraryItemCreateRequest(LibraryItemType type, String title, String author, int yearOfPublication, int totalCopies) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.totalCopies = totalCopies;
    }

    public LibraryItemType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public int getTotalCopies() {
        return totalCopies;
    }
}
