package com.mahsan.librarymanagementsystem.builder;

import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilderRegistry {
    private static final Map<LibraryItemType, LibraryItemRequestBuilder> builders = new HashMap<>();

    static {
        builders.put(LibraryItemType.BOOK, new BookRequestBuilder());
        builders.put(LibraryItemType.MAGAZINE, new MagazineRequestBuilder());
        builders.put(LibraryItemType.THESIS, new ThesisRequestBuilder());
        builders.put(LibraryItemType.REFERENCE, new ReferenceRequestBuilder());
    }

    public static LibraryItemRequestBuilder getBuilder(LibraryItemType type) {
        LibraryItemRequestBuilder builder = builders.get(type);
        if (builder == null) {
            throw new IllegalArgumentException("No builder registered for type: " + type);
        }
        return builder;
    }
}
