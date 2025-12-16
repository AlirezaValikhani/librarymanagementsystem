package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

class LibraryTest {

    private LibraryManager libraryManager;
    private SystemFileLogger logger;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        logger = new SystemFileLogger();
        libraryManager = new LibraryManager(logger);

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