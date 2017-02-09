package com.example.pasca.planefasttickets.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FligthsContract {

    public static final String CONTENT_AUTHORITY = "com.example.pasca.planefasttickets";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FLIGHTS = "flights";


    public static final class FlightsEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FLIGHTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLIGHTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLIGHTS;

        public static final String TABLE_NAME = "flights";

        public static final String COLUMN_NUMS_OF_FLIGHTS = "flights_number";

        public static final String COLUMN_DEPARTURE = "departure";

        public static final String COLUMN_ARRIVAL = "arrival";

        public static final String COLUMN_PRICE = "price";

        public static final String COLUMN_AIRPORTS = "airports";

        public static final String COLUMN_COMPANIES = "companies";

        public static final String COLUMN_NUMS_OF_FLIGHTS_R = "flights_number_r";

        public static final String COLUMN_DEPARTURE_R = "departure_r";

        public static final String COLUMN_ARRIVAL_R = "arrival_r";

        public static final String COLUMN_AIRPORTS_R = "airports_r";

        public static final String COLUMN_COMPANIES_R = "companies_r";

        public static Uri buildFlight(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
