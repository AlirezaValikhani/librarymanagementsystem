package com.mahsan.librarymanagementsystem.model.generic;

import com.mahsan.librarymanagementsystem.io.Logger;

import java.util.Iterator;

public class GenericLinkedList<T> implements Iterable<T> {

    private Node<T> head;

    public GenericLinkedList() {
        this.head = null;
    }

    public Node<T> getHead() {
        return head;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
            return;
        }

        Node<T> current = head;
        while (current.next != null)
            current = current.next;

        current.next = newNode;
    }

    public boolean remove(T data, Logger logger) {
        if (head == null)
            return false;

        if (head.data.equals(data)) {
            head = head.next;
            return true;
        }

        Node<T> current = head;
        Node<T> previous = null;

        while (current != null && !current.data.equals(data)) {
            previous = current;
            current = current.next;
        }

        if (current == null) {
            logger.logAction("Error", "Data not found!");
            return false;
        }

        previous.next = current.next;
        return true;
    }

    public void display(Logger logger) {
        Node<T> current = head;

        if (current == null) {
            logger.logAction("Display Content", "List is empty!");
            return;
        }

        while (current != null) {
            logger.logAction("Item Detail", current.data.toString());
            current = current.next;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<>(head);
    }
}
