package com.example.mcs_lab_finalproject.model;

public class Medicines {

    private int medicineID;
    private String name;
    private String manufacturer;
    private int price;
    private String image;
    private String description;

    public Medicines(int medicineID, String name, String manufacturer, int price, String image, String description) {
        this.medicineID = medicineID;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}