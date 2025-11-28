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
    private static final int DB_VERSION = 2;
    
    private static final String TABLE_NAME = "Product";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String PRICE_COL = "price";
    private static final String EXPIRATION_DATE_COL = "expiration_date";
    private static final String CATEGORY_COL = "category";
    private static final String SHOP_NAME_COL = "shop_name";
    private static final String PURCHASE_DATE_COL = "purchase_date";
    private static final String DESCRIPTION_COL = "description";
    
    // Deposit Table
    private static final String DEPOSIT_TABLE_NAME = "Deposit";
    private static final String DEPOSIT_ID_COL = "id";
    private static final String DEPOSIT_PACKAGING_TYPE_COL = "packaging_type";
    private static final String DEPOSIT_VALUE_COL = "deposit_value";
    private static final String DEPOSIT_BARCODE_COL = "barcode";
    private static final String DEPOSIT_ADDED_DATE_COL = "added_date";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String productQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                PRICE_COL + " INTEGER, " +
                EXPIRATION_DATE_COL + " TEXT, " +
                CATEGORY_COL + " TEXT, " +
                SHOP_NAME_COL + " TEXT, " +
                PURCHASE_DATE_COL + " TEXT, " +
                DESCRIPTION_COL + " TEXT)";

        db.execSQL(productQuery);

        String depositQuery = "CREATE TABLE " + DEPOSIT_TABLE_NAME + " (" +
                DEPOSIT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEPOSIT_PACKAGING_TYPE_COL + " TEXT, " +
                DEPOSIT_VALUE_COL + " INTEGER, " +
                DEPOSIT_BARCODE_COL + " TEXT, " +
                DEPOSIT_ADDED_DATE_COL + " TEXT)";
        
        db.execSQL(depositQuery);
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

    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, product.getName());
        values.put(PRICE_COL, product.getPrice());
        values.put(EXPIRATION_DATE_COL, product.getExpirationDate());
        values.put(CATEGORY_COL, product.getCategory());
        values.put(SHOP_NAME_COL, product.getShopName());
        values.put(PURCHASE_DATE_COL, product.getPurchaseDate());
        values.put(DESCRIPTION_COL, product.getDescription());
        db.update(TABLE_NAME, values, ID_COL + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEPOSIT_TABLE_NAME);
        onCreate(db);
    }

    public void addNewDeposit(String packaging_type, int deposit_value, String barcode, String added_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DEPOSIT_PACKAGING_TYPE_COL, packaging_type);
        values.put(DEPOSIT_VALUE_COL, deposit_value);
        values.put(DEPOSIT_BARCODE_COL, barcode);
        values.put(DEPOSIT_ADDED_DATE_COL, added_date);

        db.insert(DEPOSIT_TABLE_NAME, null, values);
        db.close();
    }

    public List<Deposit> getAllDeposits() {
        List<Deposit> deposits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DEPOSIT_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DEPOSIT_ID_COL));
                String packagingType = cursor.getString(cursor.getColumnIndexOrThrow(DEPOSIT_PACKAGING_TYPE_COL));
                int depositValue = cursor.getInt(cursor.getColumnIndexOrThrow(DEPOSIT_VALUE_COL));
                String barcode = cursor.getString(cursor.getColumnIndexOrThrow(DEPOSIT_BARCODE_COL));
                String addedDate = cursor.getString(cursor.getColumnIndexOrThrow(DEPOSIT_ADDED_DATE_COL));

                deposits.add(new Deposit(id, packagingType, depositValue, barcode, addedDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return deposits;
    }

    public void updateDeposit(Deposit deposit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEPOSIT_PACKAGING_TYPE_COL, deposit.getPackagingType());
        values.put(DEPOSIT_VALUE_COL, deposit.getDepositValue());
        values.put(DEPOSIT_BARCODE_COL, deposit.getBarcode());
        values.put(DEPOSIT_ADDED_DATE_COL, deposit.getAddedDate());
        db.update(DEPOSIT_TABLE_NAME, values, DEPOSIT_ID_COL + " = ?", new String[]{String.valueOf(deposit.getId())});
        db.close();
    }

    public void deleteDeposit(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DEPOSIT_TABLE_NAME, DEPOSIT_ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
