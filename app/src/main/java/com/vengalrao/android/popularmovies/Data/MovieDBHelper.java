package com.vengalrao.android.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vengalrao on 15-03-2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME="movie.db";
    private static final int VERSION=1;

    public MovieDBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_FAV="CREATE TABLE "+MovieContract.MovieEntry.TABLE+" ("+
                MovieContract.MovieEntry._ID+" INTEGER PRIMARY KEY, "+
                MovieContract.MovieEntry.MOVIE_ID+" TEXT NOT NULL );";
        db.execSQL(CREATE_TABLE_FAV);
        final String CREATE_TABLE_FAV_DET="CREATE TABLE "+ MovieContract.MovieDetail.NAME_TABLE+" ("+
                MovieContract.MovieDetail._ID+" INTEGER PRIMARY KEY, "+
                MovieContract.MovieDetail.MOVIE_NAME+" TEXT NOT NULL, "+
                MovieContract.MovieDetail.MOVIE_ID+" TEXT NOT NULL, "+
                MovieContract.MovieDetail.RATING+" TEXT NOT NULL, "+
                MovieContract.MovieDetail.POSTER_PATH+" TEXT NOT NULL, "+
                MovieContract.MovieDetail.BACKDROP_PATH+" TEXT NOT NULL, "+
                MovieContract.MovieDetail.RELEASE_DATE+" TEXT NOT NULL, "+
                MovieContract.MovieDetail.OVERVIEW+" TEXT NOT NULL );";
        db.execSQL(CREATE_TABLE_FAV_DET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DEL_FAV="DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE;
        final String DEL_FAV_DET="DROP TABLE IF EXISTS "+ MovieContract.MovieDetail.NAME_TABLE;
        db.execSQL(DEL_FAV);
        db.execSQL(DEL_FAV_DET);
        onCreate(db);
    }
}
