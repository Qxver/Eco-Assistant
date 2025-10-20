package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String DB_NAME = "EcoAssistant";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Product";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String PRICE_COL = "price";
    private static final String EXPIRATION_DATE_COL = "expiration_date";
    private static final String CATEGORY_COL = "category";
    private static final String SHOP_NAME_COL = "shop_name";
    private static final String PURCHASE_DATE_COL = "purchase_date";
    private static final String DESCRIPTION_COL = "description";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                PRICE_COL + " INTEGER, " +
                EXPIRATION_DATE_COL + " TEXT, " +
                CATEGORY_COL + " TEXT, " +
                SHOP_NAME_COL + " TEXT, " +
                PURCHASE_DATE_COL + " TEXT, " +
                DESCRIPTION_COL + " TEXT)";

        db.execSQL(query);
    }

    public void addNewProduct(String name, int price, String expiration_date, String category, String shop_name, String purchase_date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(PRICE_COL, price);
        values.put(EXPIRATION_DATE_COL, expiration_date);
        values.put(CATEGORY_COL, category);
        values.put(SHOP_NAME_COL, shop_name);
        values.put(PURCHASE_DATE_COL, purchase_date);
        values.put(DESCRIPTION_COL, description);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
