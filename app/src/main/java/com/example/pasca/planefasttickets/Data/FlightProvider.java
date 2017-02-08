package com.example.pasca.planefasttickets.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class FlightProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FlightDBHelper mOpenHelper;

    static final int OUTBOUND = 100;
    static final int INBOUND = 200;



    static {
        String authority = FligthsContract.CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, FligthsContract.PATH_OUTBOUND, OUTBOUND);
        sUriMatcher.addURI(authority, FligthsContract.PATH_INBOUND, INBOUND);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
