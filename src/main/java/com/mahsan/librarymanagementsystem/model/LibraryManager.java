package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.base.Searchable;

import java.util.*;

public class LibraryManager {
    private HashMap<String, LibraryItem> items;

    public LibraryManager() {
        this.items = new HashMap<>();
    }

    public void addItem(String key, LibraryItem item) {
        items.put(key, item);
    }

    public void removeItem(LibraryItem item, FileHandler fileHandler) {

        if (items.remove(item.getUUID()) != null)
            fileHandler.logAction("Remove item", "Item " + item.getTitle() + " removed successfully.");
        else
            fileHandler.logAction("Remove item failed", "Item " + item.getTitle() + " was not found.");
    }

    public void search(String query, FileHandler fileHandler) {

        for (String key : items.keySet()) {
            LibraryItem libraryItem = items.get(key);
            if (libraryItem instanceof Searchable && ((Searchable) libraryItem).matches(query))
                libraryItem.display(fileHandler);
        }
    }

    public void displayAll(FileHandler fileHandler) {
        for (String key : items.keySet()) {
            LibraryItem libraryItem = items.get(key);
            libraryItem.display(fileHandler);
            System.out.println("--------------------------------------------------------------------------------");
        }

        fileHandler.logAction("Display library items operation", "Items displayed.");
    }

    public void updateItemFields(LibraryItem itemToUpdate, String newTitle, String newAuthor,
                                 int newYear, FileHandler fileHandler) {
        LibraryItem existingItem = items.get(itemToUpdate.getUUID());

        if (existingItem != null) {
            existingItem.setTitle(newTitle);
            existingItem.setAuthor(newAuthor);
            existingItem.setYearOfPublication(newYear);

            fileHandler.logAction("Update item success",
                    "Item " + newTitle + " (" + existingItem.getClass().getSimpleName() + ") updated successfully.");
        } else {
            fileHandler.logAction("Update item failed",
                    "Item with ID " + itemToUpdate.getUUID() + " doesn't exist in system!");
        }
    }

    public List<LibraryItem> searchLibraryItemByPartialMatch(String searchKey) {
        String keyword = searchKey.toLowerCase();

        List<LibraryItem> matchingItems = new ArrayList<>();

        for (LibraryItem libraryItem : items.values()) {

            boolean titleMatch = libraryItem.getTitle().toLowerCase().contains(keyword);
            boolean authorMatch = libraryItem.getAuthor().toLowerCase().contains(keyword);

            if (titleMatch || authorMatch)
                matchingItems.add(libraryItem);
        }

        return matchingItems;
    }

    public void remove(LibraryItem libraryItem) {
        items.remove(libraryItem, null);
        System.out.println("The book with title " + libraryItem.getTitle() + " has been removed successfully.");
    }

    public List<LibraryItem> getAllItemsAsList() {
        return new ArrayList<>(items.values());
    }

    public List<LibraryItem> getSortedItems() {
        List<LibraryItem> itemList = getAllItemsAsList();
        Comparator<LibraryItem> comparator = new Comparator<LibraryItem>() {
            @Override
            public int compare(LibraryItem o1, LibraryItem o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        };

        itemList.sort(comparator);
        return itemList;
    }
}
