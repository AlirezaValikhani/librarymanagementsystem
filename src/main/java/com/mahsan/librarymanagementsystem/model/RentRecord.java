package com.mahsan.librarymanagementsystem.model;

import java.time.LocalDateTime;

public class RentRecord {

    private static int nextRecordId = 1;
    private int recordId;
    private String itemUUID;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    public RentRecord(String itemUUID, LocalDateTime borrowDate) {
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

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
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
