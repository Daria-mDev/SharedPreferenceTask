package com.example.sharedpreferencetask;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyContentProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.sharedpreferencetask";
    private static final String PREFERENCES = "preferences";
    static final String PREFERENCES_NAME = "preferences";
    private static final int MATCH = 1;
    static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PREFERENCES);

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, PREFERENCES, MATCH);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int uriType = sUriMatcher.match(uri);
        MatrixCursor cursor;
        if (uriType == MATCH) {
            String key = uri.getPathSegments().get(0);
            cursor = new MatrixCursor(new String[]{key});
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if (!sharedPreferences.contains(key)) {
                return cursor;
            }
            MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
            rowBuilder.add(sharedPreferences.getString(key, "0"));
        } else {
            throw new IllegalArgumentException("Unexpected URI");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + ".item";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        if (uriType == MATCH) {
            String text = values.getAsString(PREFERENCES);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE);
            sharedPreferences.edit()
                    .putString(PREFERENCES_NAME, text)
                    .apply();
            getContext().getContentResolver().notifyChange(uri, null);
            return 0;
        }
        else {
            throw new UnsupportedOperationException();
        }

    }
}
