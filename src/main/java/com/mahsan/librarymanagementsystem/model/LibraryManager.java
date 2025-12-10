package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.base.Searchable;
import com.mahsan.librarymanagementsystem.model.generic.GenericLinkedList;
import com.mahsan.librarymanagementsystem.model.generic.Node;

public class LibraryManager {
    private GenericLinkedList<LibraryItem> items;

    public LibraryManager() {
        this.items = new GenericLinkedList<>();
    }

    public void addItem(LibraryItem item) {
        items.add(item);
    }

    public void removeItem(LibraryItem item, FileHandler fileHandler) {
        items.remove(item, fileHandler);
    }

    public void search(String query, FileHandler fileHandler) {

        for (LibraryItem item : items) {
            if (item instanceof Searchable && ((Searchable) item).matches(query))
                item.display(fileHandler);
        }
    }

    public void displayAll(FileHandler fileHandler) {
        for (LibraryItem item : items) {
            item.display(fileHandler);
            System.out.println("--------------------------------------------------------------------------------");
        }

        fileHandler.logAction("Display library items operation", "Items displayed.");
    }

    public void updateItemFields(String title, String newTitle, String newAuthor, int newYear, FileHandler fileHandler) {
        Node<LibraryItem> current = items.getHead();

        while (current != null) {
            LibraryItem libraryItem = current.getData();

            if (libraryItem.getTitle().equals(title)) {
                libraryItem.setTitle(newTitle);
                libraryItem.setAuthor(newAuthor);
                libraryItem.setYearOfPublication(newYear);

                fileHandler.logAction("Update item", "Book with title " + title + " has been updated successfully.");
                return;
            }

            current = current.getNext();
        }

        fileHandler.logAction("Update item failed", "item with title " + title + " doesn't exist!");
    }

    public void removeLibraryItemByTitle(String title, FileHandler fileHandler) {

        if (items.getHead() == null)
            fileHandler.logAction("Remove book fail", "There is no book in the library to remove!");

        String searchKeyword = title.toLowerCase();

        Node<LibraryItem> current = items.getHead();
        LibraryItem itemToRemove = null;

        while (current != null) {
            LibraryItem currentBook = current.getData();

            if (currentBook.getTitle().toLowerCase().contains(searchKeyword)) {
                itemToRemove = currentBook;
                break;
            }
            current = current.getNext();
        }

        if (itemToRemove != null) {
            if (items.remove(itemToRemove, fileHandler)) {
                return;
            }
        }

        fileHandler.logAction("Remove book fail", "A book with title " + title + " was not found!");
    }

    public LibraryItem[] searchLibraryItemByPartialMatch(String searchKey) {
        String keyword = searchKey.toLowerCase();

        int matchCount = 0;
        Node<LibraryItem> current = items.getHead();

        while (current != null) {
            LibraryItem libraryItem = current.getData();

            boolean titleMatch = libraryItem.getTitle().toLowerCase().contains(keyword);
            boolean authorMatch = libraryItem.getAuthor().toLowerCase().contains(keyword);

            if (titleMatch || authorMatch) {
                matchCount++;
            }
            current = current.getNext();
        }

        if (matchCount == 0)
            return new LibraryItem[0];

        LibraryItem[] matchingItems = new LibraryItem[matchCount];
        int index = 0;
        current = items.getHead();

        while (current != null) {
            LibraryItem libraryItem = current.getData();

            boolean titleMatch = libraryItem.getTitle().toLowerCase().contains(keyword);
            boolean authorMatch = libraryItem.getAuthor().toLowerCase().contains(keyword);

            if (titleMatch || authorMatch) {
                matchingItems[index] = libraryItem;
                index++;
            }
            current = current.getNext();
        }

        return matchingItems;
    }

    public void remove(LibraryItem libraryItem) {
        items.remove(libraryItem, null);
        System.out.println("The book with title " + libraryItem.getTitle() + " has been removed successfully.");
    }
}
