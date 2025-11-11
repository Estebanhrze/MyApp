package com.example.myapp;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    private FeedReaderContract() { }

    public static final class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "personas";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_APELLIDO = "apellido";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_NOMBRE + " TEXT," +
                    FeedEntry.COLUMN_NAME_APELLIDO + " TEXT" +
                    ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

}
