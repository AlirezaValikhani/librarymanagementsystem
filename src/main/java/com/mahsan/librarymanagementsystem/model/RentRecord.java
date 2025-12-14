package com.mahsan.librarymanagementsystem.model;

import java.time.LocalDate;

public class RentRecord {

    private static int nextRecordId = 1;
    private int recordId;
    private String itemUUID;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public RentRecord(String itemUUID, LocalDate borrowDate) {
        this.recordId = nextRecordId++;
        this.itemUUID = itemUUID;
        this.borrowDate = borrowDate;
        this.returnDate = null;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "RentRecord{" +
                "recordId=" + recordId +
                ", itemUUID='" + itemUUID + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
