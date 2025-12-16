package com.mahsan.librarymanagementsystem.generic;

import static org.junit.jupiter.api.Assertions.*;

import com.mahsan.librarymanagementsystem.io.Logger;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.Magazine;
import com.mahsan.librarymanagementsystem.model.ReferenceBook;
import com.mahsan.librarymanagementsystem.model.Thesis;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.enums.BookState;
import com.mahsan.librarymanagementsystem.model.Book;
import com.mahsan.librarymanagementsystem.model.generic.GenericLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class GenericLinkedListTest {

    private GenericLinkedList<LibraryItem> list;
    private Logger logger;

    @BeforeEach
    void setUp() {
        list = new GenericLinkedList<>();
        logger = new SystemFileLogger();
    }

    @Test
    void testAddElementsAndCheckOrder() {
        list.add(new ReferenceBook(UUID.randomUUID().toString(), "ReferenceBook", "F_Author", 1990,
                2, "1020304050123", 10));
        list.add(new Thesis(UUID.randomUUID().toString(), "Thesis", "F_Author", 1995,
                5, "MIT", "Bachelor", "Test"));

        assertNotNull(list.getHead());
        assertEquals("ReferenceBook", list.getHead().getData().getTitle());
        assertEquals("Thesis", list.getHead().getNext().getData().getTitle());
    }

    @Test
    void testRemove() {
        Book testBook = new Book(UUID.randomUUID().toString(), "First", "F_Author", 1990,
                4, BookState.EXIST, "1020304050123", "test");
        list.add(testBook);

        assertTrue(list.remove(testBook, logger));
        assertNull(list.getHead());
        assertTrue(list.isEmpty());
    }

    @Test
    void testRemoveNotFound() {
        Magazine existBook = new Magazine(UUID.randomUUID().toString(), "Exist", "E_Author", 1990,
                5, "1234567891012", 10, 20);
        Magazine notFoundBook = new Magazine(UUID.randomUUID().toString(), "NotFound", "N_F_Author", 1990,
                5, "1234567891012", 10, 20);

        list.add(existBook);

        assertFalse(list.remove(notFoundBook, logger));

        assertNotNull(list.getHead());
        assertEquals("Exist", list.getHead().getData().getTitle());
    }

    @Test
    void testRemoveFromEmpty() {
        Book randomBook = new Book(UUID.randomUUID().toString(), "Random", "R_Author", 1990,
                3, BookState.EXIST, "1234567891012", "test");

        assertFalse(list.remove(randomBook, logger));
    }
}
