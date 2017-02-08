package com.example.pasca.planefasttickets.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FligthsContract {

    public static final String CONTENT_AUTHORITY = "com.example.pasca.planefasttickets";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_OUTBOUND = "outbound";
    public static final String PATH_INBOUND = "inbound";

    public static final class OutboundEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OUTBOUND).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTBOUND;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTBOUND;

        public static final String TABLE_NAME = "outbound";

        public static final String COLUMN_NUMS_OF_FLIGHTS = "flights_number";

        public static final String COLUMN_DEPARTURE = "departure";

        public static final String COLUMN_ARRIVAL = "arrival";

        public static final String COLUMN_PRICE = "price";

        public static final String COLUMN_AIRPORTS = "airports";

        public static final String COLUMN_COMPANIES = "companies";

        public static Uri buildOutboundFlight(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class InboundEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OUTBOUND).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INBOUND;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INBOUND;

        public static final String TABLE_NAME = "inbound";

        public static final String COLUMN_NUMS_OF_FLIGHTS = "flights_number";

        public static final String COLUMN_DEPARTURE = "departure";

        public static final String COLUMN_ARRIVAL = "arrival";

        public static final String COLUMN_PRICE = "price";

        public static final String COLUMN_AIRPORTS = "airports";

        public static final String COLUMN_COMPANIES = "companies";

        public static Uri buildOutboundFlight(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
