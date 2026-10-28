
package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "Expenses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, category TEXT, " +
                "date TEXT, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }

    public boolean addExpense(double amount, String category,
                              String date, String description) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("amount", amount);
        values.put("category", category);
        values.put("date", date);
        values.put("description", description);

        return db.insert("expenses", null, values) != -1;
    }

    public Cursor getExpenses() {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM expenses ORDER BY id DESC", null);
    }

    public double getTotal() {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT SUM(amount) FROM expenses", null);

        double total = 0;

        if (c.moveToFirst() && !c.isNull(0)) {
            total = c.getDouble(0);
        }

        c.close();
        return total;
    }

    public void deleteExpense(int id) {
        getWritableDatabase().delete(
                "expenses", "id=?",
                new String[]{String.valueOf(id)});
    }
}
