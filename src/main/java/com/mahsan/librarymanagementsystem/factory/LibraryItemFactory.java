package com.mahsan.librarymanagementsystem.factory;

import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.model.*;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public class LibraryItemFactory {

    public LibraryItem createItem(LibraryItemType type, String[] details, String uuid) {

        if (details.length < 4)
            throw new MissingParametersException("Insufficient details for item");

        String title = details[0].trim();
        String author = details[1].trim();
        int year;
        int totalCopies;

        try {
            year = Integer.parseInt(details[2].trim());
            totalCopies = Integer.parseInt(details[3].trim());
        } catch (NumberFormatException e) {
            throw new MissingParametersException("Invalid year: " + details[2]);
        }

        switch (type) {
            case LibraryItemType.BOOK:
                if (details.length < 7)
                    throw new MissingParametersException("Book requires state, ISBN, publisher");

                BookState state = BookState.fromValue(Integer.parseInt(details[4].trim()));
                String isbn = details[5].trim();
                String publisher = details[6].trim();
                return new Book(uuid, title, author, year, totalCopies, state, isbn, publisher);

            case LibraryItemType.MAGAZINE:
                if (details.length < 6)
                    throw new MissingParametersException("Magazine requires ISSN, volume, issue");

                String ISSN = details[4].trim();
                int volume = Integer.parseInt(details[5].trim());
                int issue = Integer.parseInt(details[6].trim());
                return new Magazine(uuid, title, author, year, totalCopies, ISSN, volume, issue);

            case LibraryItemType.THESIS:
                if (details.length < 7)
                    throw new MissingParametersException("Thesis requires university, degree, advisor");

                String university = details[4].trim();
                String degree = details[5].trim();
                String advisor = details[6].trim();
                return new Thesis(uuid, title, author, year, totalCopies, university, degree, advisor);

            case LibraryItemType.REFERENCE:
                if (details.length < 6)
                    throw new MissingParametersException("Reference requires ISBN, edition, lendable");

                String refIsbn = details[4].trim();
                int edition = Integer.parseInt(details[5].trim());
                boolean lendable = Boolean.parseBoolean(details[6].trim());
                ReferenceBook refBook = new ReferenceBook(uuid, title, author, year, totalCopies, refIsbn, edition);
                refBook.setLendable(lendable);
                return refBook;

            default:
                throw new MissingParametersException("Unknown item type: " + type);
        }
    }
}
