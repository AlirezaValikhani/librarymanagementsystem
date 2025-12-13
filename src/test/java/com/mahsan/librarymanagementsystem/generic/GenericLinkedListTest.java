package com.mahsan.librarymanagementsystem.generic;

import static org.junit.jupiter.api.Assertions.*;

import com.mahsan.librarymanagementsystem.io.FileHandler;
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
    private FileHandler fileHandler;

    @BeforeEach
    void setUp() {
        list = new GenericLinkedList<>();
        fileHandler = new FileHandler();
    }

    @Test
    void testAddElementsAndCheckOrder() {
        list.add(new ReferenceBook(UUID.randomUUID().toString(), "ReferenceBook", "F_Author", 1990,
                "1020304050123", 10));
        list.add(new Thesis(UUID.randomUUID().toString(), "Thesis", "F_Author", 1995,
                "MIT", "Bachelor", "Test"));

        assertNotNull(list.getHead());
        assertEquals("ReferenceBook", list.getHead().getData().getTitle());
        assertEquals("Thesis", list.getHead().getNext().getData().getTitle());
    }

    @Test
    void testRemove() {
        Book testBook = new Book(UUID.randomUUID().toString(), "First", "F_Author", 1990,
                BookState.EXIST, "1020304050123", 20);
        list.add(testBook);

        assertTrue(list.remove(testBook, fileHandler));
        assertNull(list.getHead());
        assertTrue(list.isEmpty());
    }

    @Test
    void testRemoveNotFound() {
        Magazine existBook = new Magazine(UUID.randomUUID().toString(), "Exist", "E_Author", 1990,
                "1234567891012", 10, 20);
        Magazine notFoundBook = new Magazine(UUID.randomUUID().toString(), "NotFound", "N_F_Author", 1990,
                "1234567891012", 10, 20);

        list.add(existBook);

        assertFalse(list.remove(notFoundBook, fileHandler));

        assertNotNull(list.getHead());
        assertEquals("Exist", list.getHead().getData().getTitle());
    }

    @Test
    void testRemoveFromEmpty() {
        Book randomBook = new Book(UUID.randomUUID().toString(), "Random", "R_Author", 1990,
                BookState.EXIST, "1234567891012", 4);

        assertFalse(list.remove(randomBook, fileHandler));
    }
}