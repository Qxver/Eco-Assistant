package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Deposit implements Parcelable {
    private int id;
    private String packagingType;
    private int depositValue;
    private String barcode;
    private String addedDate;

    public Deposit(int id, String packagingType, int depositValue, String barcode, String addedDate) {
        this.id = id;
        this.packagingType = packagingType;
        this.depositValue = depositValue;
        this.barcode = barcode;
        this.addedDate = addedDate;
    }

    protected Deposit(Parcel in) {
        id = in.readInt();
        packagingType = in.readString();
        depositValue = in.readInt();
        barcode = in.readString();
        addedDate = in.readString();
    }

    public static final Creator<Deposit> CREATOR = new Creator<Deposit>() {
        @Override
        public Deposit createFromParcel(Parcel in) {
            return new Deposit(in);
        }

        @Override
        public Deposit[] newArray(int size) {
            return new Deposit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(packagingType);
        dest.writeInt(depositValue);
        dest.writeString(barcode);
        dest.writeString(addedDate);
    }

    public int getId() {
        return id;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public int getDepositValue() {
        return depositValue;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getAddedDate() {
        return addedDate;
    }
}