package com.vengalrao.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by vengalrao on 16-03-2017.
 */

public class MovieProvider extends ContentProvider {

    public static final int FAV=100;
    public static final int FAV_ID=101;
    public static final int FAV_DET=200;
    public static final int FAV_DET_ID=201;

    private MovieDBHelper mMovieDBHelper;
    private static final UriMatcher sUriMatcher=buildUriMacher();

    public static UriMatcher buildUriMacher(){
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAV,FAV);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAV+"/#",FAV_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAV_DET,FAV_DET);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAV_DET+"/#",FAV_DET_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        mMovieDBHelper=new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db=mMovieDBHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case FAV:
                retCursor=db.query(
                        MovieContract.MovieEntry.TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAV_DET:
                retCursor=db.query(
                        MovieContract.MovieDetail.NAME_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Operation not suppored");
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db=mMovieDBHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAV:
                long id=db.insert(MovieContract.MovieEntry.TABLE,null,values);
                if(id>0){
                    returnUri= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_FAV,id);
                }else{
                    throw new android.database.SQLException("Failed to insert into FAV table"+uri);
                }
                break;
            case FAV_DET:
                long id2=db.insert(MovieContract.MovieDetail.NAME_TABLE,null,values);
                if(id2>0){
                    returnUri=ContentUris.withAppendedId(MovieContract.MovieDetail.CONTENT_URI_FAV_DET,id2);
                }else{
                    throw new android.database.SQLException("Failed to insert into FAV_DET table"+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Operation Not Supported");
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db=mMovieDBHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Log.v("uri match",""+match);
        int retDelRows;
        switch (match){
            case FAV_ID:
                String id=uri.getPathSegments().get(1);
                String mSelection="MovieId=?";
                String[] mSelectionArgs=new String[]{id};
                retDelRows=db.delete(MovieContract.MovieEntry.TABLE,
                        mSelection,
                        mSelectionArgs
                );
                break;
            case FAV_DET_ID:
                String id2=uri.getPathSegments().get(1);
                String mSelection2="MovieId=?";
                String[] mSelectionArgs2=new String[]{id2};
                retDelRows=db.delete(MovieContract.MovieDetail.NAME_TABLE,
                        mSelection2,
                        mSelectionArgs2
                );
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported");
        }
        if (retDelRows!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return retDelRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

}
