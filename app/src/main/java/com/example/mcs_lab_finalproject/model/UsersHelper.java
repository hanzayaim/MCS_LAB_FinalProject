package com.example.mcs_lab_finalproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UsersHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BluejackPharmacy.db";
    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_USERID = "userID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_OTP = "otp";
    public static final String COLUMN_ISVERIFIED = "isVerified";

    public UsersHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT," +
                " email TEXT," +
                " password TEXT," +
                " phone TEXT," +
                "otp TEXT," +
                " isVerified TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String email, String password, String phone, String otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_OTP, otp);
        contentValues.put(COLUMN_ISVERIFIED, "false");
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getDataByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " +
                TABLE_NAME + " WHERE email=? AND password=?", new String[] {email, password});
        return result;
    }

    public Cursor getDataByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email=?", new String[] {email});
        return result;
    }

    public boolean updateData(String email, String isVerified) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ISVERIFIED, isVerified);
        db.update(TABLE_NAME, contentValues, "email = ?", new String[] {email});
        return true;
    }

    public boolean updateOTP(String email, String otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OTP, otp);
        long result = db.update(TABLE_NAME, contentValues, "email = ?", new String[] {email});
        return result != -1;
    }
}
