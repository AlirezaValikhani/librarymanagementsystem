package com.mahsan.librarymanagementsystem.builder;

import com.mahsan.librarymanagementsystem.model.dto.LibraryItemCreateRequest;
import com.mahsan.librarymanagementsystem.model.dto.MagazineCreateRequest;

public class MagazineRequestBuilder implements LibraryItemRequestBuilder{
    @Override
    public LibraryItemCreateRequest build(String[] details) throws Exception {
        String title = details[0].trim();
        String author = details[1].trim();
        int year = Integer.parseInt(details[2].trim());
        int totalCopies = Integer.parseInt(details[3].trim());
        String isbn = details[5].trim();
        int volumeNumber = Integer.parseInt(details[6].trim());
        int issueNumber = Integer.parseInt(details[7].trim());
        return new MagazineCreateRequest(title, author, year,
                totalCopies, isbn, volumeNumber, issueNumber);
    }
}
