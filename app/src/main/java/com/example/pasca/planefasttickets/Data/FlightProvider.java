package com.example.pasca.planefasttickets.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class FlightProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FlightDBHelper mOpenHelper;

    static final int OUTBOUND = 100;
    static final int OUTBOUND_ROW = 101;
    static final int INBOUND = 200;
    static final int INBOUND_ROW = 201;



    static {
        String authority = FligthsContract.CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, FligthsContract.PATH_OUTBOUND, OUTBOUND);
        sUriMatcher.addURI(authority, FligthsContract.PATH_OUTBOUND + "/#", OUTBOUND_ROW);
        sUriMatcher.addURI(authority, FligthsContract.PATH_INBOUND, INBOUND);
        sUriMatcher.addURI(authority, FligthsContract.PATH_INBOUND + "/#", INBOUND_ROW);

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
        String tableName;

        switch (sUriMatcher.match(uri)){
            case OUTBOUND:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID ASC";
                }
                tableName = FligthsContract.OutboundEntry.TABLE_NAME;
                break;
            case OUTBOUND_ROW:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                tableName = FligthsContract.OutboundEntry.TABLE_NAME;
                break;
            case INBOUND:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID ASC";
                }
                tableName = FligthsContract.InboundEntry.TABLE_NAME;
                break;
            case INBOUND_ROW:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                tableName = FligthsContract.InboundEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor = mOpenHelper.getReadableDatabase().query(
              tableName,
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
            case OUTBOUND:
                return FligthsContract.OutboundEntry.CONTENT_TYPE;
            case OUTBOUND_ROW:
                return FligthsContract.OutboundEntry.CONTENT_ITEM_TYPE;
            case INBOUND:
                return FligthsContract.InboundEntry.CONTENT_TYPE;
            case INBOUND_ROW:
                return FligthsContract.InboundEntry.CONTENT_ITEM_TYPE;
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
            case OUTBOUND:
                _id = db.insert(FligthsContract.OutboundEntry.TABLE_NAME, null, values);
                if (_id > 0) returnUri = FligthsContract.OutboundEntry.buildOutboundFlight(_id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case INBOUND:
                _id = db.insert(FligthsContract.InboundEntry.TABLE_NAME, null, values);
                if (_id > 0) returnUri = FligthsContract.InboundEntry.buildOutboundFlight(_id);
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
            case OUTBOUND:
                rowsDeleted = db.delete(FligthsContract.OutboundEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INBOUND:
                rowsDeleted = db.delete(FligthsContract.InboundEntry.TABLE_NAME, selection, selectionArgs);
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
            case OUTBOUND:
                rowsUpdated =
                    db.update(FligthsContract.OutboundEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case INBOUND:
                rowsUpdated =
                    db.update(FligthsContract.InboundEntry.TABLE_NAME, values, selection, selectionArgs);
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
            case OUTBOUND:
                db.beginTransaction();
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(FligthsContract.OutboundEntry.TABLE_NAME, null, value);
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
            case INBOUND:
                db.beginTransaction();
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(FligthsContract.InboundEntry.TABLE_NAME, null, value);
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
