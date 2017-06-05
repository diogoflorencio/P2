package com.example.diogo.discoverytrip.DataBase;

import android.provider.BaseColumns;

/**
 * Created by diogo on 04/02/17.
 */

public abstract class WlanTable {

    public static class Column implements BaseColumns {
        public static final String COLUMN_Id = "id";
        public static final String COLUMN_SSID = "ssid";
        public static final String COLUMN_PassWord = "password";
        public static final String COLUMN_Mercado = "mercado";
    }

    /* Script Lembretes.DML */
    public static final String TABLE_NAME = "Lembretes";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COLUMN_SEP = ",";
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +TABLE_NAME + " (" +
                    Column._ID + " INTEGER PRIMARY KEY," +
                    Column.COLUMN_Id + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_SSID + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_PassWord + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Mercado + TEXT_TYPE + " )";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
