package com.mahsan.librarymanagementsystem.model.dto;

import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public class ThesisCreateRequest extends LibraryItemCreateRequest {
    private final String universityName;
    private final String degreeLevel;
    private final String advisorName;

    public ThesisCreateRequest(String title, String author, int yearOfPublication, int totalCopies,
                               String universityName, String degreeLevel, String advisorName) {
        super(LibraryItemType.THESIS, title, author, yearOfPublication, totalCopies);
        this.universityName = universityName;
        this.degreeLevel = degreeLevel;
        this.advisorName = advisorName;
    }

    public String getUniversityName() {
        return universityName;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public String getAdvisorName() {
        return advisorName;
    }
}
