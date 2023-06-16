package com.example.mcs_lab_finalproject.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MedicinesHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "finpromcs";
    public static final String TABLE_NAME = "Medicines";
    public static final String COLUMN_MEDICINEID = "medicineID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MANUFACTURER = "manufacturer";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";

    public MedicinesHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "medicineID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "manufacturer TEXT," +
                "price INTEGER," +
                "image TEXT," +
                "description TEXT" +
                ")");
    }

    public boolean insertData(String name, String manufacturer, int price, String image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!isTableExists(db, TABLE_NAME)) {
            createTable(db);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_MANUFACTURER, manufacturer);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_IMAGE, image);
        contentValues.put(COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, TABLE_NAME)) {
            return null;
        }

        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }

    public Medicines getMedicineById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, TABLE_NAME)) {
            return null;
        }

        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MEDICINEID + "=?", new String[] { String.valueOf(id) });

        if (result != null && result.moveToFirst()) {
            @SuppressLint("Range") String name = result.getString(result.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String manufacturer = result.getString(result.getColumnIndex(COLUMN_MANUFACTURER));
            @SuppressLint("Range") int price = result.getInt(result.getColumnIndex(COLUMN_PRICE));
            @SuppressLint("Range") String image = result.getString(result.getColumnIndex(COLUMN_IMAGE));
            @SuppressLint("Range") String description = result.getString(result.getColumnIndex(COLUMN_DESCRIPTION));

            result.close();
            return new Medicines(id, name, manufacturer, price, image, description);
        }

        return null;
    }

    public int getLastInsertId() {
        SQLiteDatabase db = this.getReadableDatabase();

        if (!isTableExists(db, TABLE_NAME)) {
            return -1;
        }

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        int id = -1;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = ?", new String[]{tableName});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}