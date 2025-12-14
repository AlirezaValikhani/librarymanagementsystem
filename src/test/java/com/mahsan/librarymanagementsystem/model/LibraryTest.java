package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private LibraryManager libraryManager;
    private FileHandler fileHandler;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        fileHandler = new FileHandler();
        libraryManager = new LibraryManager(fileHandler);

        book1 = new Book(UUID.randomUUID().toString(), "Data structure", "Someone", 2020,
                4, BookState.EXIST, "1234567891012", "test");
        book2 = new Book(UUID.randomUUID().toString(), "Java persistence", "Author", 2022,
                4, BookState.BORROWED, "1234567891012", "test");
        book3 = new Book(UUID.randomUUID().toString(), "Java persistence", "OtherAuthor", 2023,
                4, BookState.EXIST, "1234567891012", "test");

        libraryManager.addItem(UUID.randomUUID().toString(), book1);
        libraryManager.addItem(UUID.randomUUID().toString(), book2);
    }

//    @Test
//    void testAddAndCountBooks() {
//        assertEquals(2, libraryManager.());
//    }

//    @Test
//    void testRemoveByTitleSuccess() {
//        assertDoesNotThrow(() -> libraryManager.removeLibraryItemByTitle("Java persistence", fileHandler));
//
//        assertEquals(1, libraryManager.countBooks());
//    }

//    @Test
//    void testRemoveByTitleNotFound() {
//        assertThrows(BookNotFoundException.class, () -> libraryManager.removeLibraryItemByTitle("Non-existent book", fileHandler));
//    }
//
//    @Test
//    void testRemoveByTitleFromEmpty() {
//        libraryManager = new LibraryManager();
//
//        assertThrows(BookNotFoundException.class, () -> libraryManager.removeLibraryItemByTitle("Anything", fileHandler));
//    }
}