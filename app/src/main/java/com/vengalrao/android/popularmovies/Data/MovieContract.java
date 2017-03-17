package com.vengalrao.android.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vengalrao on 15-03-2017.
 */

public class MovieContract {

    public static final String AUTHORITY="com.vengalrao.android.popularmovies";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

    public static final String PATH_FAV="favorite";
    public static final String PATH_FAV_DET="favoriteDetails";


    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI_FAV=BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();
        public static final String TABLE="favorite";
        public static final String MOVIE_ID="movieId";
    }

    public static final class MovieDetail implements BaseColumns{
        public static final Uri CONTENT_URI_FAV_DET=BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_DET).build();
        public static final String NAME_TABLE="favoriteDetails";
        public static final String MOVIE_ID="MovieId";
        public static final String MOVIE_NAME="movieName";
        public static final String RATING="rating";
        public static final String POSTER_PATH="posterPath";
        public static final String BACKDROP_PATH="backDropPath";
        public static final String RELEASE_DATE="releaseDate";
        public static final String OVERVIEW="overview";
    }
}
