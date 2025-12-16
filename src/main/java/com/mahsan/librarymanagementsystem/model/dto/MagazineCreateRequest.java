package com.mahsan.librarymanagementsystem.model.dto;

import com.mahsan.librarymanagementsystem.model.Magazine;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public class MagazineCreateRequest extends LibraryItemCreateRequest {
    private final String issn;
    private final int volumeNumber;
    private final int issueNumber;

    public MagazineCreateRequest(String title, String author, int yearOfPublication, int totalCopies,
                                 String issn, int volumeNumber, int issueNumber) {
        super(LibraryItemType.MAGAZINE, title, author, yearOfPublication, totalCopies);
        this.issn = issn;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
    }

    public String getIssn() {
        return issn;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public LibraryItem createItem(String uuid) {
        return new Magazine(uuid,
                this.getTitle(),
                this.getAuthor(),
                this.getYearOfPublication(),
                this.getTotalCopies(),
                this.getIssn(),
                this.getVolumeNumber(),
                this.getIssueNumber());
    }
}
