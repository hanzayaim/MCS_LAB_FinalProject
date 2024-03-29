package com.example.mcs_lab_finalproject.model;

import java.util.Date;

public class Transaction {

    private int transactionID;
    private int medicineID;
    private int userID;
    private long transactionDate;
    private int quantity;

    private String medicineName;
    private int medicinePrice;

    public Transaction(int transactionID, int medicineID, int userID, long transactionDate, int quantity, String medicineName, int medicinePrice) {
        this.transactionID = transactionID;
        this.medicineID = medicineID;
        this.userID = userID;
        this.transactionDate = transactionDate;
        this.quantity = quantity;
        this.medicineName = medicineName;
        this.medicinePrice = medicinePrice;
    }


    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(int medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}