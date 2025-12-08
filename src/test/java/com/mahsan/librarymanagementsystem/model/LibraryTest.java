package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library library;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        library = new Library();

        book1 = new Book("Data structure", "Someone", 2020, BookState.EXIST);
        book2 = new Book("Java persistence", "Author", 2022, BookState.BORROWED);
        book3 = new Book("Java persistence", "OtherAuthor", 2023, BookState.EXIST);

        library.addBook(book1);
        library.addBook(book2);
    }

    @Test
    void testAddAndCountBooks() {
        assertEquals(2, library.countBooks());
    }

    @Test
    void testRemoveByTitleSuccess() {
        assertDoesNotThrow(() -> library.removeBookByTitle("Java persistence"));

        assertEquals(1, library.countBooks());
    }

    @Test
    void testRemoveByTitleNotFound() {
        assertThrows(BookNotFoundException.class, () -> library.removeBookByTitle("Non-existent book"));
    }

    @Test
    void testRemoveByTitleFromEmpty() {
        library = new Library();

        assertThrows(BookNotFoundException.class, () -> library.removeBookByTitle("Anything"));
    }
}