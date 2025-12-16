package com.mahsan.librarymanagementsystem.model.dto;

import com.mahsan.librarymanagementsystem.model.Book;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public class BookCreateRequest extends LibraryItemCreateRequest {
    private final BookState state;
    private final String isbn;
    private final String publisher;

    public BookCreateRequest(String title, String author, int yearOfPublication, int totalCopies,
                             BookState state, String isbn, String publisher) {
        super(LibraryItemType.BOOK, title, author, yearOfPublication, totalCopies);
        this.state = state;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public BookState getState() {
        return state;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }


    @Override
    public LibraryItem createItem(String uuid) {
        return new Book(uuid,
                this.getTitle(),
                this.getAuthor(),
                this.getYearOfPublication(),
                this.getTotalCopies(),
                this.getState(),
                this.getIsbn(),
                this.getPublisher());
    }
}
