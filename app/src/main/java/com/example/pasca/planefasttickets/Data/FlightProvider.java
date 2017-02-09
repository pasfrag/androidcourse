package com.example.pasca.planefasttickets.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class FlightProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FlightDBHelper mOpenHelper;

    static final int FLIGHTS = 100;
    static final int FLIGHTS_ROW = 101;



    static {
        String authority = FligthsContract.CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, FligthsContract.PATH_FLIGHTS, FLIGHTS);
        sUriMatcher.addURI(authority, FligthsContract.PATH_FLIGHTS + "/#", FLIGHTS_ROW);

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FlightDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case FLIGHTS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID ASC";
                }
                break;
            case FLIGHTS_ROW:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor = mOpenHelper.getReadableDatabase().query(
              FligthsContract.FlightsEntry.TABLE_NAME,
              projection,
              selection,
              selectionArgs,
              null,
              null,
              sortOrder
        );
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case FLIGHTS:
                return FligthsContract.FlightsEntry.CONTENT_TYPE;
            case FLIGHTS_ROW:
                return FligthsContract.FlightsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;

        switch (match){
            case FLIGHTS:
                _id = db.insert(FligthsContract.FlightsEntry.TABLE_NAME, null, values);
                if (_id > 0) returnUri = FligthsContract.FlightsEntry.buildFlight(_id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        if (selection == null) selection="1";
        switch(match){
            case FLIGHTS:
                rowsDeleted = db.delete(FligthsContract.FlightsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match){
            case FLIGHTS:
                rowsUpdated =
                    db.update(FligthsContract.FlightsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0)getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match){
            case FLIGHTS:
                db.beginTransaction();
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(FligthsContract.FlightsEntry.TABLE_NAME, null, value);
                        if (_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
