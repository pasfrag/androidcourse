package com.example.pasca.planefasttickets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pasca.planefasttickets.Data.FligthsContract;

public class SelectFlightsActivity extends AppCompatActivity {

    private static final int FLIGHTS_LOADER = 0;

    private static final String[] FLIGHTS_COLUMNS = {
        FligthsContract.InboundEntry.TABLE_NAME + "." + FligthsContract.InboundEntry._ID,
        FligthsContract.InboundEntry.COLUMN_DEPARTURE,
        FligthsContract.InboundEntry.COLUMN_ARRIVAL,
        FligthsContract.InboundEntry.COLUMN_NUMS_OF_FLIGHTS,
        FligthsContract.InboundEntry.COLUMN_PRICE,
        FligthsContract.InboundEntry.COLUMN_AIRPORTS,
        FligthsContract.InboundEntry.COLUMN_COMPANIES
    };

    static final int COL_FLIGHT_ID = 0;
    static final int COL_FLIGHT_DEP_DATE = 1;
    static final int COL_FLIGHT_ARR_DATE = 2;
    static final int COL_FLIGHT_NUMBER = 3;
    static final int COL_FLIGHT_PRICE = 4;
    static final int COL_FLIGHT_AIRPORTS = 5;
    static final int COL_FLIGHT_COMPANIES = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_flights);
    }
}
