package com.example.pasca.planefasttickets;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.pasca.planefasttickets.Data.FligthsContract;

public class SelectFlightsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FLIGHTS_LOADER = 0;

    private static final String[] FLIGHTS_COLUMNS = {
        FligthsContract.FlightsEntry.TABLE_NAME + "." + FligthsContract.FlightsEntry._ID,
        FligthsContract.FlightsEntry.COLUMN_DEPARTURE,
        FligthsContract.FlightsEntry.COLUMN_ARRIVAL,
        FligthsContract.FlightsEntry.COLUMN_NUMS_OF_FLIGHTS,
        FligthsContract.FlightsEntry.COLUMN_PRICE,
        FligthsContract.FlightsEntry.COLUMN_AIRPORTS,
        FligthsContract.FlightsEntry.COLUMN_COMPANIES,
        FligthsContract.FlightsEntry.COLUMN_DEPARTURE_R,
        FligthsContract.FlightsEntry.COLUMN_ARRIVAL_R,
        FligthsContract.FlightsEntry.COLUMN_NUMS_OF_FLIGHTS_R,
        FligthsContract.FlightsEntry.COLUMN_AIRPORTS_R,
        FligthsContract.FlightsEntry.COLUMN_COMPANIES_R,
    };

    static final int COL_FLIGHT_ID = 0;
    static final int COL_FLIGHT_DEP_DATE = 1;
    static final int COL_FLIGHT_ARR_DATE = 2;
    static final int COL_FLIGHT_NUMBER = 3;
    static final int COL_FLIGHT_PRICE = 4;
    static final int COL_FLIGHT_AIRPORTS = 5;
    static final int COL_FLIGHT_COMPANIES = 6;
    static final int COL_FLIGHT_DEP_DATE_R = 7;
    static final int COL_FLIGHT_ARR_DATE_R = 8;
    static final int COL_FLIGHT_NUMBER_R = 9;
    static final int COL_FLIGHT_AIRPORTS_R = 10;
    static final int COL_FLIGHT_COMPANIES_R = 11;


    FlightsAdapter mFlightsAdapter;
    private ListView flightsLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FLIGHTS_LOADER, null, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_flights);



        Intent intent = getIntent();

        flightsLV = (ListView) findViewById(R.id.listview_flights);
        mFlightsAdapter = new FlightsAdapter(this, null, 0, intent.getStringExtra("").equals("true"));

        flightsLV.setAdapter(mFlightsAdapter);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri aUri = FligthsContract.FlightsEntry.buildFlight(id);
        return new CursorLoader(this, aUri, FLIGHTS_COLUMNS, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFlightsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFlightsAdapter.swapCursor(null);

    }
}
