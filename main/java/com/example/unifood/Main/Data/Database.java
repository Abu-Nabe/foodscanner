package com.example.unifood.Main.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.unifood.R;

public class Database extends SQLiteOpenHelper {
        // Database name and version
        private static final String DATABASE_NAME = "my_database";
        private static final int DATABASE_VERSION = 1;

        // Table name and column names
        private static final String TABLE_NAME = "food_list";
        private static final String COLUMN_NAME_1 = "name";
        private static final String COLUMN_NAME_2 = "expiry";
        private static final String COLUMN_NAME_3 = "wasted";


        public Database(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create table
            String createTableQuery = "CREATE TABLE " + TABLE_NAME +
                    " (" + COLUMN_NAME_1 + " TEXT, " + COLUMN_NAME_2 + " TEXT, " +
                    COLUMN_NAME_3 + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Upgrade database
            String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(dropTableQuery);
            onCreate(db);
        }

        public void insertArray(String[] array) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            for (int i = 0; i < array.length; i++) {
                values.put(getColumnName(i), array[i]);
            }
            db.insert(TABLE_NAME, null, values);
            db.close();
        }

        private String getColumnName(int index) {
            switch (index) {
                case 0:
                    return COLUMN_NAME_1;
                case 1:
                    return COLUMN_NAME_2;
                case 2:
                    return COLUMN_NAME_3;
                default:
                    return "";
            }
        }

    public static boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        boolean exists = false;
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            exists = count > 0;
            cursor.close();
        }
        return exists;
    }
}
