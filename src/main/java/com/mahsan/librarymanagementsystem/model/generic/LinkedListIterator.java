package com.mahsan.librarymanagementsystem.model.generic;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListIterator<T> implements Iterator<T> {

    private Node<T> current;

    public LinkedListIterator(Node<T> head) {
        this.current = head;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();

        T data = current.getData();
        current = current.getNext();

        return data;
    }
}
