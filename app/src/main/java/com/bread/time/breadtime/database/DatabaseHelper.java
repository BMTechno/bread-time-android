package com.bread.time.breadtime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Breadtime.db";
    public static final String TABLE_NAME = "Notification";

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, 1);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TOPIC TEXT,ACTIVATE BOOLEAN);");
        Cursor result = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        //when no rows then insert fresh rows..
        if (result.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("TOPIC", "Fornadas");
            contentValues.put("ACTIVATE", true);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

            contentValues.put("TOPIC", "Promocoes");
            contentValues.put("ACTIVATE", true);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

            contentValues.put("TOPIC", "Receitas");
            contentValues.put("ACTIVATE", true);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

            contentValues.put("TOPIC", "Produtos");
            contentValues.put("ACTIVATE", true);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        }
        if (!result.isClosed()) {
            result.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
