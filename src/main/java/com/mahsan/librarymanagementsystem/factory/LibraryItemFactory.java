package com.mahsan.librarymanagementsystem.factory;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.model.*;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.dto.*;

public class LibraryItemFactory {

    public LibraryItem createItem(LibraryItemCreateRequest request, String uuid) {
        if (request == null) {
            throw new MissingParametersException("Request cannot be null");
        }

        return switch (request.getType()) {
            case BOOK -> createBook((BookCreateRequest) request, uuid);
            case MAGAZINE -> createMagazine((MagazineCreateRequest) request, uuid);
            case THESIS -> createThesis((ThesisCreateRequest) request, uuid);
            case REFERENCE -> createReference((ReferenceBookCreateRequest) request, uuid);
            default -> throw new MissingParametersException("Unknown item type: " + request.getType());
        };
    }

    private LibraryItem createBook(BookCreateRequest request, String uuid) {
        return new Book(uuid,
                request.getTitle(),
                request.getAuthor(),
                request.getYearOfPublication(),
                request.getTotalCopies(),
                request.getState(),
                request.getIsbn(),
                request.getPublisher());
    }

    private LibraryItem createMagazine(MagazineCreateRequest request, String uuid) {
        return new Magazine(uuid,
                request.getTitle(),
                request.getAuthor(),
                request.getYearOfPublication(),
                request.getTotalCopies(),
                request.getIssn(),
                request.getVolumeNumber(),
                request.getIssueNumber());
    }

    private LibraryItem createThesis(ThesisCreateRequest request, String uuid) {
        return new Thesis(uuid,
                request.getTitle(),
                request.getAuthor(),
                request.getYearOfPublication(),
                request.getTotalCopies(),
                request.getUniversityName(),
                request.getDegreeLevel(),
                request.getAdvisorName());
    }

    private LibraryItem createReference(ReferenceBookCreateRequest request, String uuid) {
        ReferenceBook refBook = new ReferenceBook(uuid,
                request.getTitle(),
                request.getAuthor(),
                request.getYearOfPublication(),
                request.getTotalCopies(),
                request.getIsbn(),
                request.getEditionNumber());
        refBook.setLendable(request.isLendable());
        return refBook;
    }
}
