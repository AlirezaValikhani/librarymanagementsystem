package com.mahsan.librarymanagementsystem.builder;

import com.mahsan.librarymanagementsystem.model.dto.LibraryItemCreateRequest;
import com.mahsan.librarymanagementsystem.model.dto.ReferenceBookCreateRequest;

public class ReferenceRequestBuilder implements LibraryItemRequestBuilder {
    @Override
    public LibraryItemCreateRequest build(String[] details) throws Exception {
        String title = details[0].trim();
        String author = details[1].trim();
        int year = Integer.parseInt(details[2].trim());
        int totalCopies = Integer.parseInt(details[3].trim());
        String refIsbn = details[4].trim();
        int edition = Integer.parseInt(details[5].trim());
        boolean lendable = Boolean.parseBoolean(details[6].trim());
        return new ReferenceBookCreateRequest(title, author, year,
                totalCopies, refIsbn, edition, lendable);
    }
}
