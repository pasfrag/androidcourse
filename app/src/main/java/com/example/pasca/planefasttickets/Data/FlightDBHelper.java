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

        final String SQL_CREATE_OUTBOUND_TABLE = "CREATE TABLE " + FligthsContract.FlightsEntry.TABLE_NAME + " (" +
                FligthsContract.FlightsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                FligthsContract.FlightsEntry.COLUMN_DEPARTURE + " TEXT NOT NULL, " +
                FligthsContract.FlightsEntry.COLUMN_ARRIVAL + " TEXT NOT NULL, " +
                FligthsContract.FlightsEntry.COLUMN_NUMS_OF_FLIGHTS + " INTEGER NOT NULL," +

                FligthsContract.FlightsEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                FligthsContract.FlightsEntry.COLUMN_AIRPORTS + " TEXT NOT NULL, " +

                FligthsContract.FlightsEntry.COLUMN_COMPANIES + " TEXT NOT NULL, " +

                FligthsContract.FlightsEntry.COLUMN_DEPARTURE_R + " TEXT, " +
                FligthsContract.FlightsEntry.COLUMN_ARRIVAL_R + " TEXT, " +
                FligthsContract.FlightsEntry.COLUMN_NUMS_OF_FLIGHTS_R + " INTEGER," +

                FligthsContract.FlightsEntry.COLUMN_AIRPORTS_R + " TEXT, " +

                FligthsContract.FlightsEntry.COLUMN_COMPANIES_R + " TEXT);";


        db.execSQL(SQL_CREATE_OUTBOUND_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FligthsContract.FlightsEntry.TABLE_NAME);
        onCreate(db);
    }
}
