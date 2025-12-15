package com.mahsan.librarymanagementsystem.model;

import com.mahsan.librarymanagementsystem.io.Logger;
import com.mahsan.librarymanagementsystem.model.base.Lendable;
import com.mahsan.librarymanagementsystem.model.base.LibraryItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LibraryManager<T extends LibraryItem> {

    private HashMap<String, T> items;
    private final Logger logger;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LibraryManager(Logger logger) {
        this.logger = logger;
        this.items = new HashMap<>();
    }

    public void addItem(String key, T item) {
        items.put(key, item);
    }

    public void removeItem(LibraryItem item) {

        if (items.remove(item.getUUID()) != null)
            logger.logAction("Remove item", "Item " + item.getTitle() + " removed successfully.");
        else
            logger.logAction("Remove item failed", "Item " + item.getTitle() + " was not found.");
    }

    public void search(String query) {

        for (String key : items.keySet()) {
            LibraryItem libraryItem = items.get(key);
            if (libraryItem != null && (libraryItem).matches(query))
                libraryItem.display(logger);
        }
    }

    public void displayAll() {
        for (String key : items.keySet()) {
            LibraryItem libraryItem = items.get(key);
            libraryItem.display(logger);
            System.out.println("--------------------------------------------------------------------------------");
        }

        logger.logAction("Display library items operation", "Items displayed.");
    }

    public void updateItemFields(LibraryItem itemToUpdate, String newTitle, String newAuthor, int newYear) {
        LibraryItem existingItem = items.get(itemToUpdate.getUUID());

        if (existingItem != null) {
            existingItem.setTitle(newTitle);
            existingItem.setAuthor(newAuthor);
            existingItem.setYearOfPublication(newYear);

            logger.logAction("Update item success",
                    "Item " + newTitle + " (" + existingItem.getClass().getSimpleName() + ") updated successfully.");
        } else {
            logger.logAction("Update item failed",
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
            logger.logAction("Borrow failed", "Item not found!");
            return false;
        }

        if (!(item instanceof Lendable)) {
            logger.logAction("Borrow failed", "Item " + item.getTitle() + " is not lendable.");
            return false;
        }

        if (item instanceof ReferenceBook referenceBook && !referenceBook.isLendable()) {
            logger.logAction("Borrow failed", "Reference book " + item.getTitle() + " is marked as non-lendable.");
            return false;
        }

        if (item.decreaseAvailableCopies()) {
            LocalDateTime rentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            RentRecord record = new RentRecord(itemUUID, rentTime);
            item.getRentRecords().add(record);
            logger.logAction("Borrow item", "Item " + item.getTitle() + " borrowed successfully.");
            return true;
        } else {
            logger.logAction("Borrow failed", "Item " + item.getTitle() + " has no available copies!");
            return false;
        }
    }

    public boolean returnItem(String itemUUID, int recordId) {
        LibraryItem item = items.get(itemUUID);

        if (item == null) {
            logger.logAction("Error", "Item not found!");
            return false;
        }

        if (!(item instanceof Lendable)) {
            logger.logAction("Error", "Item " + item.getTitle() + " is not lendable.");
            return false;
        }

        if (item instanceof ReferenceBook referenceBook && !referenceBook.isLendable()) {
            logger.logAction("Error", "Reference book " + item.getTitle() + " is marked as non-lendable.");
            return false;
        }

        Optional<RentRecord> rentRecord = item.getRentRecords().stream()
                .filter(record -> record.getRecordId() == recordId && record.getReturnDate() == null)
                .findFirst();

        if (rentRecord.isEmpty()) {
            logger.logAction("Error", "Item not found!");
            return false;
        }

        RentRecord recordToUpdate = rentRecord.get();
        LocalDateTime returnDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        recordToUpdate.setReturnDate(returnDate);

        item.increaseAvailableCopies();

        logger.logAction("Return", "Successfully returned: " + item.getTitle() +
                " | Record ID: " + recordId + " | New Available Copies: " + item.getAvailableCopies());
        return true;
    }

    public void displayAllBorrowedItems() {
        logger.logAction("Report", "--- Currently Borrowed Items ---");
        boolean foundBorrowed = false;

        for (LibraryItem item : items.values()) {

            List<RentRecord> borrowedItems = item.getRentRecords().stream()
                    .filter(record -> record.getReturnDate() == null)
                    .toList();

            if (!borrowedItems.isEmpty()) {
                logger.logAction("Borrowed items", "Title: " + item.getTitle());
                foundBorrowed = true;
            }

            for (RentRecord record : borrowedItems) {
                logger.logAction("Record details",
                        String.format("Record id: %d | Borrow date: %s", record.getRecordId(), record.getBorrowDate()));
            }
        }

        if (!foundBorrowed)
            logger.logAction("Report", "No items borrowed!");
    }
}
