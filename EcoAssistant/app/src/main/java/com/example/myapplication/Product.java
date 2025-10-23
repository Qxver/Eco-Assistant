package com.example.myapplication;

public class Product {
    private int id;
    private String name;
    private int price;
    private String expirationDate;
    private String category;
    private String shopName;
    private String purchaseDate;
    private String description;

    public Product(int id, String name, int price, String expirationDate, String category, String shopName, String purchaseDate, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
        this.category = category;
        this.shopName = shopName;
        this.purchaseDate = purchaseDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public String getShopName() {
        return shopName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getDescription() {
        return description;
    }
}