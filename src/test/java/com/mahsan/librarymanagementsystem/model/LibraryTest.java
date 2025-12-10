package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private LibraryManager libraryManager;
    private FileHandler fileHandler;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        libraryManager = new LibraryManager();
        fileHandler = new FileHandler();

        book1 = new Book("Data structure", "Someone", 2020,
                BookState.EXIST, "1234567891012", 2);
        book2 = new Book("Java persistence", "Author", 2022,
                BookState.BORROWED, "1234567891012", 2);
        book3 = new Book("Java persistence", "OtherAuthor", 2023,
                BookState.EXIST, "1234567891012", 2);

        libraryManager.addItem(book1);
        libraryManager.addItem(book2);
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

    @Test
    void testRemoveByTitleNotFound() {
        assertThrows(BookNotFoundException.class, () -> libraryManager.removeLibraryItemByTitle("Non-existent book", fileHandler));
    }

    @Test
    void testRemoveByTitleFromEmpty() {
        libraryManager = new LibraryManager();

        assertThrows(BookNotFoundException.class, () -> libraryManager.removeLibraryItemByTitle("Anything", fileHandler));
    }
}