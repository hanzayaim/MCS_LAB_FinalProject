package com.example.mcs_lab_finalproject.model;

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
    private static final String TABLE_NAME = "Transactions";
    public static final String COLUMN_TRANSACTIONID = "transactionID";
    public static final String COLUMN_MEDICINEID = "medicineID";
    public static final String COLUMN_USERID = "userID";
    public static final String COLUMN_TRANSACTIONDATE = "transactionDate";
    public static final String COLUMN_QUANTITY = "quantity";

    public TransactionsHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "transactionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "medicineID INTEGER," +
                "userID INTEGER," +
                "transactionDate INTEGER," +
                "quantity INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Transaction> getAllDataByUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERID + "=?", new String[] { String.valueOf(userId) });
        List<Transaction> transactions = new ArrayList<>();

        while (res.moveToNext()) {
            int transactionID = res.getInt(0);
            int medicineID = res.getInt(1);
            int userID = res.getInt(2);
            long transactionDate = res.getLong(3);
            int quantity = res.getInt(4);

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

        long result = db.insert(TABLE_NAME, null, contentValues);

        db.close();

        if (result == -1) {
            Log.e("DB_INSERT_ERROR", "Error while inserting into Transactions table");
            return false;
        } else {
            Log.d("DB_INSERT", "Insert successful");
            return true;
        }
    }

    public boolean updateTransaction(int transactionId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", newQuantity);
        long result = db.update(TABLE_NAME, contentValues, "transactionID = ?", new String[]{String.valueOf(transactionId)});
        db.close();
        return result != -1;
    }

    public boolean deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "transactionID = ?", new String[]{String.valueOf(transactionId)});
        db.close();
        return result > 0;
    }
}