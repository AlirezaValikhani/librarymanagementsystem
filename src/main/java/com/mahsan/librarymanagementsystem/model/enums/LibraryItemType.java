package com.mahsan.librarymanagementsystem.model.enums;

public enum LibraryItemType {
    BOOK(1),
    MAGAZINE(2),
    THESIS(3),
    REFERENCE(4);

    private final int value;

    LibraryItemType(int value) {
        this.value = value;
    }

    public static LibraryItemType fromValue(int value) {

        for (LibraryItemType itemType : LibraryItemType.values()) {
            if (itemType.value == value) {
                return itemType;
            }
        }

        throw new IllegalArgumentException("Wrong value for Library item type!");
    }
}
