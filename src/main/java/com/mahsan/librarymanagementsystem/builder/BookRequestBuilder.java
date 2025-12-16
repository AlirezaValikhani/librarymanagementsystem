package com.mahsan.librarymanagementsystem.builder;

import com.mahsan.librarymanagementsystem.model.dto.BookCreateRequest;
import com.mahsan.librarymanagementsystem.model.dto.LibraryItemCreateRequest;
import com.mahsan.librarymanagementsystem.model.enums.BookState;

public class BookRequestBuilder implements LibraryItemRequestBuilder {
    @Override
    public LibraryItemCreateRequest build(String[] details) throws Exception {
        String title = details[0].trim();
        String author = details[1].trim();
        int year = Integer.parseInt(details[2].trim());
        int totalCopies = Integer.parseInt(details[3].trim());
        BookState state = BookState.fromValue(Integer.parseInt(details[4].trim()));
        String isbn = details[5].trim();
        String publisher = details[6].trim();
        return new BookCreateRequest(title, author, year, totalCopies, state, isbn, publisher);
    }
}
