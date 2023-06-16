package com.example.mcs_lab_finalproject.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MedicinesHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BluejackPharmacy.db";
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
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "medicineID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "manufacturer TEXT," +
                "price INTEGER," +
                "image TEXT," +
                "description TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String manufacturer, int price, String image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }

    public Medicines getMedicineById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
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
}