package com.example.mcs_lab_finalproject.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TransactionsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BluejackPharmacy.db";
    public static final String TABLE_NAME = "Transactions";
    private static final String COLUMN_TRANSACTIONID = "transactionID";
    private static final String COLUMN_MEDICINEID = "medicineID";
    private static final String COLUMN_USERID = "userID";
    private static final String COLUMN_TRANSACTIONDATE = "transactionDate";
    private static final String COLUMN_QUANTITY = "quantity";

    public TransactionsHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "transactionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "medicineID INTEGER," +
                "userID INTEGER," +
                "transactionDate INTEGER," +
                "quantity INTEGER," +
                "FOREIGN KEY(medicineID) REFERENCES Medicines(medicineID)," +
                "FOREIGN KEY(userID) REFERENCES Users(userID)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Transactions");
        onCreate(db);
    }

    public List<Transaction> getAllDataByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERID + "=?", new String[]{String.valueOf(userId)});
        List<Transaction> transactions = new ArrayList<>();

        while (res.moveToNext()) {
            @SuppressLint("Range") int transactionID = res.getInt(res.getColumnIndex(COLUMN_TRANSACTIONID));
            @SuppressLint("Range") int medicineID = res.getInt(res.getColumnIndex(COLUMN_MEDICINEID));
            @SuppressLint("Range") int userID = res.getInt(res.getColumnIndex(COLUMN_USERID));
            @SuppressLint("Range") long transactionDate = res.getLong(res.getColumnIndex(COLUMN_TRANSACTIONDATE));
            @SuppressLint("Range") int quantity = res.getInt(res.getColumnIndex(COLUMN_QUANTITY));

            transactions.add(new Transaction(transactionID, medicineID, userID, transactionDate, quantity));
        }

        res.close();
        db.close();

        return transactions;
    }

    public boolean buyTransaction(int medicineID, int userID, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MEDICINEID, medicineID);
        contentValues.put(COLUMN_USERID, userID);
        contentValues.put(COLUMN_TRANSACTIONDATE, System.currentTimeMillis());
        contentValues.put(COLUMN_QUANTITY, quantity);

        try {
            long result = db.insert(TABLE_NAME, null, contentValues);
            db.close();

            if (result == -1) {
                Log.e("DB_INSERT_ERROR", "Error while inserting into Transactions table");
                return false;
            } else {
                Log.d("DB_INSERT", "Insert successful");
                return true;
            }
        } catch (Exception e) {
            Log.e("DB_INSERT_ERROR", "Error while inserting into Transactions table: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTransaction(int transactionId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_QUANTITY, newQuantity);
        long result = db.update(TABLE_NAME, contentValues, COLUMN_TRANSACTIONID + "=?", new String[]{String.valueOf(transactionId)});
        db.close();
        return result != -1;
    }

    public boolean deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_TRANSACTIONID + "=?", new String[]{String.valueOf(transactionId)});
        db.close();
        return result > 0;
    }
}
