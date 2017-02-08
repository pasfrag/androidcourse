package com.example.pasca.planefasttickets.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FlightDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "flights.db";

    public FlightDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_OUTBOUND_TABLE = "CREATE TABLE " + FligthsContract.InboundEntry.TABLE_NAME + " (" +
                FligthsContract.InboundEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                FligthsContract.OutboundEntry.COLUMN_DEPARTURE + " TEXT NOT NULL, " +
                FligthsContract.OutboundEntry.COLUMN_ARRIVAL + " TEXT NOT NULL, " +
                FligthsContract.OutboundEntry.COLUMN_NUMS_OF_FLIGHTS + " INTEGER NOT NULL," +

                FligthsContract.OutboundEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                FligthsContract.OutboundEntry.COLUMN_AIRPORTS + " TEXT NOT NULL, " +

                FligthsContract.OutboundEntry.COLUMN_COMPANIES + " TEXT NOT NULL);";


        final String SQL_CREATE_INBOUND_TABLE = "CREATE TABLE " + FligthsContract.InboundEntry.TABLE_NAME + " (" +
                FligthsContract.InboundEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                FligthsContract.InboundEntry.COLUMN_DEPARTURE + " TEXT NOT NULL, " +
                FligthsContract.InboundEntry.COLUMN_ARRIVAL + " TEXT NOT NULL, " +
                FligthsContract.InboundEntry.COLUMN_NUMS_OF_FLIGHTS + " INTEGER NOT NULL," +

                FligthsContract.InboundEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                FligthsContract.InboundEntry.COLUMN_AIRPORTS + " TEXT NOT NULL, " +

                FligthsContract.InboundEntry.COLUMN_COMPANIES + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_OUTBOUND_TABLE);
        db.execSQL(SQL_CREATE_INBOUND_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FligthsContract.OutboundEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FligthsContract.InboundEntry.TABLE_NAME);
        onCreate(db);
    }
}
