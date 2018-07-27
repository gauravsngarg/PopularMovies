/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package gauravsngarg.com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class FavContentProvider extends ContentProvider {


    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_TASKS, TASKS);
        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_TASKS + "/#", TASK_WITH_ID);

        return uriMatcher;
    }


    private FavDbHelper mFavDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavDbHelper = new FavDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASKS:

                long id = db.insert(FavContract.FavEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavContract.FavEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mFavDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case TASKS:
                Log.d("Gaurav31", "Tasks");
                retCursor = db.query(FavContract.FavEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TASK_WITH_ID:
                Log.d("Gaurav31", "Tasks with Id");
                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(FavContract.FavEntry.TABLE_NAME);
                queryBuilder.appendWhere(FavContract.FavEntry.COLUMN_MOVIE_ID + "=" +
                        uri.getLastPathSegment());

                retCursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int count = 0;
        final SQLiteDatabase db = mFavDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case TASKS:
                count = db.delete(FavContract.FavEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TASK_WITH_ID:

//                String id = uri.getLastPathSegment();
//                count = db.delete(FavContract.FavEntry.TABLE_NAME, FavContract.FavEntry.COLUMN_MOVIE_ID + "=" + id, null);
                break;

            default:
                throw new UnsupportedOperationException("URI is not Matched");
        }

        return count;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
