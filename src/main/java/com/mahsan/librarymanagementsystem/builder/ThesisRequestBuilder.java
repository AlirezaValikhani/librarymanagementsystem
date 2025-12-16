package com.mahsan.librarymanagementsystem.builder;

import com.mahsan.librarymanagementsystem.model.dto.LibraryItemCreateRequest;
import com.mahsan.librarymanagementsystem.model.dto.ThesisCreateRequest;

public class ThesisRequestBuilder implements LibraryItemRequestBuilder {
    @Override
    public LibraryItemCreateRequest build(String[] details) throws Exception {
        String title = details[0].trim();
        String author = details[1].trim();
        int year = Integer.parseInt(details[2].trim());
        int totalCopies = Integer.parseInt(details[3].trim());
        String university = details[4].trim();
        String degree = details[5].trim();
        String advisor = details[6].trim();
        return new ThesisCreateRequest(title, author,
                year, totalCopies, university, degree, advisor);
    }
}
