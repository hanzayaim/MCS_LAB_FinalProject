package com.example.mcs_lab_finalproject.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TransactionsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "finpromcs";
    public static final String TABLE_NAME = "Transactions";
    private static final String COLUMN_TRANSACTIONID = "transactionID";
    private static final String COLUMN_MEDICINEID = "medicineID";
    private static final String COLUMN_USERID = "userID";
    private static final String COLUMN_TRANSACTIONDATE = "transactionDate";
    private static final String COLUMN_QUANTITY = "quantity";

    private static final String COLUMN_MEDICINENAME = "medicineName";

    private static final String COLUMN_MEDICINEPRICE = "medicinePrice";


    public TransactionsHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "transactionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "medicineID INTEGER," +
                "userID INTEGER," +
                "transactionDate INTEGER," +
                "quantity INTEGER," +
                "medicineName TEXT," +
                "medicinePrice INTEGER," +
                "FOREIGN KEY(medicineID) REFERENCES Medicines(medicineID)," +
                "FOREIGN KEY(userID) REFERENCES Users(userID)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean buyTransaction(int medicineID, int userID, int quantity, String medicineName, int medicinePrice) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            if (!isTableExists(db, TABLE_NAME)) {
                onCreate(db);
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MEDICINEID, medicineID);
            contentValues.put(COLUMN_USERID, userID);
            contentValues.put(COLUMN_TRANSACTIONDATE, System.currentTimeMillis());
            contentValues.put(COLUMN_QUANTITY, quantity);
            contentValues.put(COLUMN_MEDICINENAME, medicineName);
            contentValues.put(COLUMN_MEDICINEPRICE, medicinePrice);

            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        } finally {
            if (db != null) {
                db.close();
            }
        }
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

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }

    public List<Transaction> getAllDataByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERID + "=?", new String[]{String.valueOf(userId)});
        List<Transaction> transactions = new ArrayList<>();

        try {
            while (res.moveToNext()) {
                @SuppressLint("Range") int transactionID = res.getInt(res.getColumnIndex(COLUMN_TRANSACTIONID));
                @SuppressLint("Range") int medicineID = res.getInt(res.getColumnIndex(COLUMN_MEDICINEID));
                @SuppressLint("Range") int userID = res.getInt(res.getColumnIndex(COLUMN_USERID));
                @SuppressLint("Range") long transactionDate = res.getLong(res.getColumnIndex(COLUMN_TRANSACTIONDATE));
                @SuppressLint("Range") int quantity = res.getInt(res.getColumnIndex(COLUMN_QUANTITY));
                @SuppressLint("Range") String medicineName = res.getString(res.getColumnIndex(COLUMN_MEDICINENAME));
                @SuppressLint("Range") int medicinePrice = res.getInt(res.getColumnIndex(COLUMN_MEDICINEPRICE));



                transactions.add(new Transaction(transactionID, medicineID, userID, transactionDate, quantity, medicineName,medicinePrice));
            }
        } finally {
            if (res != null) {
                res.close();
            }
            db.close();
        }

        return transactions;
    }

    public boolean updateTransaction(int transactionID, int newQuantity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_QUANTITY, newQuantity);
            long result = db.update(TABLE_NAME, contentValues, COLUMN_TRANSACTIONID + "=?", new String[]{String.valueOf(transactionID)});
            return result != -1;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean deleteTransaction(int transactionID) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int result = db.delete(TABLE_NAME, COLUMN_TRANSACTIONID + "=?", new String[]{String.valueOf(transactionID)});
            return result > 0;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}