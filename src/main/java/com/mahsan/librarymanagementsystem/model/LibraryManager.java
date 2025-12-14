package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;
import com.mahsan.librarymanagementsystem.model.base.Searchable;

import java.time.LocalDate;
import java.util.*;

public class LibraryManager<T extends LibraryItem> {

    private HashMap<String, T> items;
    private final FileHandler fileHandler;

    public LibraryManager(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.items = new HashMap<>();
    }

    public void addItem(String key, T item) {
        items.put(key, item);
    }

    public void removeItem(LibraryItem item) {

        if (items.remove(item.getUUID()) != null)
            fileHandler.logAction("Remove item", "Item " + item.getTitle() + " removed successfully.");
        else
            fileHandler.logAction("Remove item failed", "Item " + item.getTitle() + " was not found.");
    }

    public void search(String query) {

        for (String key : items.keySet()) {
            LibraryItem libraryItem = items.get(key);
            if (libraryItem instanceof Searchable && ((Searchable) libraryItem).matches(query))
                libraryItem.display();
        }
    }

    public void displayAll() {
        for (String key : items.keySet()) {
            LibraryItem libraryItem = items.get(key);
            libraryItem.display();
            System.out.println("--------------------------------------------------------------------------------");
        }

        fileHandler.logAction("Display library items operation", "Items displayed.");
    }

    public void updateItemFields(LibraryItem itemToUpdate, String newTitle, String newAuthor, int newYear) {
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
        Comparator<LibraryItem> comparator =
                (o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle());

        itemList.sort(comparator);
        return itemList;
    }

    public boolean borrowItem(String itemUUID) {
        LibraryItem item = items.get(itemUUID);

        if (item == null) {
            fileHandler.logAction("Borrow failed", "Item not fount!");
            return false;
        }

        if (item.decreaseAvailableCopies()) {
            RentRecord record = new RentRecord(itemUUID, LocalDate.now());
            item.getRentRecords().add(record);
            fileHandler.logAction("Borrow item", "Item " + item.getTitle() + " borrowed successfully.");
            return true;
        } else {
            fileHandler.logAction("Borrow failed", "Item " + item.getTitle() + " has no available copies!");
            return false;
        }
    }

    public boolean returnItem(String itemUUID, int recordId) {
        LibraryItem item = items.get(itemUUID);

        if (item == null) {
            fileHandler.logAction("Error", "Item not found!");
            return false;
        }

        Optional<RentRecord> rentRecord = item.getRentRecords().stream()
                .filter(record -> record.getRecordId() == recordId && record.getReturnDate() == null)
                .findFirst();

        if (rentRecord.isEmpty()) {
            fileHandler.logAction("Error", "Item not found!");
            return false;
        }

        RentRecord recordToUpdate = rentRecord.get();
        recordToUpdate.setReturnDate(LocalDate.now());

        item.increaseAvailableCopies();

        fileHandler.logAction("Return", "Successfully returned: " + item.getTitle() +
                " | Record ID: " + recordId + " | New Available Copies: " + item.getAvailableCopies());
        return true;
    }

    public void displayAllBorrowedItems() {
        fileHandler.logAction("Report", "--- Currently Borrowed Items ---");
        boolean foundBorrowed = false;

        for (LibraryItem item : items.values()) {

            List<RentRecord> borrowedItems = item.getRentRecords().stream()
                    .filter(record -> record.getReturnDate() == null)
                    .toList();

            if (!borrowedItems.isEmpty()) {
                fileHandler.logAction("Borrowed items", "Title: " + item.getTitle());
                foundBorrowed = true;
            }

            for (RentRecord record : borrowedItems) {
                fileHandler.logAction("Record details",
                        String.format("Record id: %d | Borrow date: %s", record.getRecordId(), record.getBorrowDate()));
            }
        }

        if (!foundBorrowed)
            fileHandler.logAction("Report", "No items borrowed!");
    }
}
