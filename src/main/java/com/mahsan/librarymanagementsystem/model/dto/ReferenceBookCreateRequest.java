package com.mahsan.librarymanagementsystem.model.dto;

import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public class ReferenceBookCreateRequest extends LibraryItemCreateRequest {
    private final String isbn;
    private final int editionNumber;
    private final boolean lendable;

    public ReferenceBookCreateRequest(String title, String author, int yearOfPublication, int totalCopies,
                                      String isbn, int editionNumber, boolean lendable) {
        super(LibraryItemType.REFERENCE, title, author, yearOfPublication, totalCopies);
        this.isbn = isbn;
        this.editionNumber = editionNumber;
        this.lendable = lendable;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public boolean isLendable() {
        return lendable;
    }
}
