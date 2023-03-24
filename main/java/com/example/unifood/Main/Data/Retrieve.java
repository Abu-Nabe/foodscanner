package com.example.unifood.Main.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.unifood.Main.Model.FoodModel;

import java.util.ArrayList;

public class Retrieve
{
    public static void insertRow(Context context, String name, String purchase, String expiry){
        Database dbHelper = new Database(context);
        String[] myArray = new String[]{name, purchase, expiry};
        dbHelper.insertArray(myArray);
    }

    public static void deleteRow(Context context, String COLUMN_NAME_1, String TABLE_NAME, String value) {
        Database dbHelper = new Database(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = COLUMN_NAME_1 + "=?";
        String[] whereArgs = new String[]{value};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    @SuppressLint("Range")
    public static ArrayList<FoodModel> getAllData(Context context, String TABLE_NAME, String COLUMN_NAME_1, String COLUMN_NAME_2,
                                                  String COLUMN_NAME_3) {
        Database dbHelper = new Database(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<FoodModel> dataList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                FoodModel foodModel = new FoodModel();
                foodModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_1)));
                foodModel.setData(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_2)));
                foodModel.setWasted(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_3)));
                dataList.add(foodModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public static void updateData(Context context, String TABLE_NAME, String COLUMN_NAME_1,
                                  String COLUMN_NAME_2, String COLUMN_NAME_3, String name, String newData, String newData2) {
        Database dbHelper = new Database(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = COLUMN_NAME_1 + "=?";
        String[] whereArgs = new String[]{name};

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_2, newData);
        contentValues.put(COLUMN_NAME_3, newData2);

        db.update(TABLE_NAME, contentValues, whereClause, whereArgs);

        db.close();
    }

}
