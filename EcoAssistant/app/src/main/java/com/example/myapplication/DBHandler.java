package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

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

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COL));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(PRICE_COL));
                String expirationDate = cursor.getString(cursor.getColumnIndexOrThrow(EXPIRATION_DATE_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COL));
                String shopName = cursor.getString(cursor.getColumnIndexOrThrow(SHOP_NAME_COL));
                String purchaseDate = cursor.getString(cursor.getColumnIndexOrThrow(PURCHASE_DATE_COL));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION_COL));

                products.add(new Product(id, name, price, expirationDate, category, shopName, purchaseDate, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
