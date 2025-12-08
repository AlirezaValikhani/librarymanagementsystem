package com.mahsan.librarymanagementsystem.generic;

import static org.junit.jupiter.api.Assertions.*;

import com.mahsan.librarymanagementsystem.enums.BookState;
import com.mahsan.librarymanagementsystem.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenericLinkedListTest {

    private GenericLinkedList<Book> list;

    @BeforeEach
    void setUp() {
        list = new GenericLinkedList<>();
    }

    @Test
    void testAddElementsAndCheckOrder() {
        list.add(new Book("First", "F_Author", 1990, BookState.EXIST));
        list.add(new Book("Second", "F_Author", 1995, BookState.EXIST));

        assertNotNull(list.getHead());
        assertEquals("First", list.getHead().getData().getTitle());
        assertEquals("Second", list.getHead().getNext().getData().getTitle());
    }

    @Test
    void testRemove() {
        Book testBook = new Book("First", "F_Author", 1990, BookState.EXIST);
        list.add(testBook);

        assertTrue(list.remove(testBook));
        assertNull(list.getHead());
        assertTrue(list.isEmpty());
    }

    @Test
    void testRemoveNotFound() {
        Book existBook = new Book("Exist", "E_Author", 1990, BookState.EXIST);
        Book notFoundBook = new Book("NotFound", "N_F_Author", 1990, BookState.EXIST);

        list.add(existBook);

        assertFalse(list.remove(notFoundBook));

        assertNotNull(list.getHead());
        assertEquals("Exist", list.getHead().getData().getTitle());
    }

    @Test
    void testRemoveFromEmpty() {
        Book randomBook = new Book("Random", "R_Author", 1990, BookState.EXIST);

        assertFalse(list.remove(randomBook));
    }
}