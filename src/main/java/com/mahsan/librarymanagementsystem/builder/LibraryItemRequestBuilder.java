package com.mahsan.librarymanagementsystem.builder;

import com.mahsan.librarymanagementsystem.model.dto.LibraryItemCreateRequest;

public interface LibraryItemRequestBuilder {
    LibraryItemCreateRequest build(String[] details) throws Exception;
}
