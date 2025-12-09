package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.model.enums.BookState;

import java.util.Objects;

public class Book {

    private String title;
    private String author;
    private Integer yearOfPublication;
    private BookState state;

    public Book(String title, String author, Integer yearOfPublication, BookState state) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.state = state;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(yearOfPublication, book.yearOfPublication) && state == book.state;
    }

    @Override
    public String toString() {
        return "Book { " +
                "title = '" + title + '\'' +
                ", author = '" + author + '\'' +
                ", yearOfPublication = " + yearOfPublication +
                ", state = " + state +
                '}';
    }
}
