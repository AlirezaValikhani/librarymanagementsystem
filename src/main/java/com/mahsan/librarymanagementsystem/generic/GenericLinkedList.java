package com.mahsan.librarymanagementsystem.generic;

public class GenericLinkedList<T> {

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

    public boolean remove(T data) {
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
            System.out.println("Data not found!");
            return false;
        }

        previous.next = current.next;
        return true;
    }

    public void display() {
        Node<T> current = head;

        if (current == null) {
            System.out.println("List is empty!");
            return;
        }

        System.out.println("List content : ");
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }
}
