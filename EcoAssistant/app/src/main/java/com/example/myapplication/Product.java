package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
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

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readInt();
        expirationDate = in.readString();
        category = in.readString();
        shopName = in.readString();
        purchaseDate = in.readString();
        description = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(expirationDate);
        dest.writeString(category);
        dest.writeString(shopName);
        dest.writeString(purchaseDate);
        dest.writeString(description);
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