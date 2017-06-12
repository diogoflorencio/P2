package com.example.diogo.discoverytrip.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by diogo on 04/02/17.
 *
 * Script DDL DataBase
 */

public class DataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DataBase";
    private String[] projection = {
            WlanTable.Column.COLUMN_Id,
            WlanTable.Column.COLUMN_SSID,
            WlanTable.Column.COLUMN_PassWord,
            WlanTable.Column.COLUMN_Mercado
    };

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WlanTable.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /* onUpgrade atualização e/ou sincronização de dados
        * onCreate(sqLiteDatabase);*/
    }

    public void insert(String id, String ssid, String passWord, String mercado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WlanTable.Column.COLUMN_Id, id);
        values.put(WlanTable.Column.COLUMN_SSID, ssid);
        values.put(WlanTable.Column.COLUMN_PassWord, passWord);
        values.put(WlanTable.Column.COLUMN_Mercado, mercado);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WlanTable.TABLE_NAME, null, values);
    }

    public String selectPassWord(String ssid){
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = WlanTable.Column.COLUMN_SSID + " = ?";
        String[] selectionArgs = {ssid};
        Cursor cursor = db.query(
                WlanTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor.getString(cursor.getColumnIndexOrThrow(WlanTable.Column.COLUMN_PassWord));
    }
}
