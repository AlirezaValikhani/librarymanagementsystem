package com.mahsan.librarymanagementsystem.io;

import com.mahsan.librarymanagementsystem.builder.LibraryItemRequestBuilder;
import com.mahsan.librarymanagementsystem.builder.RequestBuilderRegistry;
import com.mahsan.librarymanagementsystem.model.dto.LibraryItemCreateRequest;
import com.mahsan.librarymanagementsystem.model.enums.LibraryItemType;

public class CsvRequestRegistry {

    private final Logger logger;

    public CsvRequestRegistry(Logger logger) {
        this.logger = logger;
    }

    public LibraryItemCreateRequest buildRequest(LibraryItemType type, String[] details) {
        try {
            validateDetailsLength(details);

            LibraryItemRequestBuilder builder = RequestBuilderRegistry.getBuilder(type);
            return builder.build(details);
        } catch (Exception e) {
            logger.logAction("Error", "Invalid parameters: " + e.getMessage());
            return null;
        }
    }

    private void validateDetailsLength(String[] details) {
        if (details.length < 7)
            throw new IllegalArgumentException("Insufficient details provided");
    }
}
