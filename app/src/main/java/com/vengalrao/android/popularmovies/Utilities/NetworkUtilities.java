package com.vengalrao.android.popularmovies.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by vengalrao on 14-03-2017.
 */

public class NetworkUtilities {
    private static String BASE_URL="https://api.themoviedb.org/3/movie";
    private static String API="api_key";
    private static String KEY="YOUR_API_KEY";
    private static String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w500";
    private static String YOUTUBE_BASE="http://img.youtube.com/vi/";
    private static String END="/hqdefault.jpg";
    private static String TAG="NetworkUtil";
    private static String VIDEO="videos";
    private static String REVIEW="reviews";

    public static String buidPosterPath(String s){
        if(s!=null||!s.equals("")){
            return (IMAGE_BASE_URL.concat(s));
        }else
            return null;
    }

    public static String buildMoviePath(String Key){
        if(Key!=null||!Key.equals("")){
            return (YOUTUBE_BASE.concat(Key).concat(END));
        }else
            return null;
    }

    public static URL buildUrl(String query){
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(API,KEY)
                .build();

        URL queryUrl=null;
        try {
            queryUrl=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    public static URL buildUrlMovie(String query){
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendPath(VIDEO)
                .appendQueryParameter(API,KEY)
                .build();

        URL queryUrl=null;
        try {
            queryUrl=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    public static URL buildUrlReview(String query){
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendPath(REVIEW)
                .appendQueryParameter(API,KEY)
                .build();

        URL queryUrl=null;
        try {
            queryUrl=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    public static String getResposeFromHttpUrl(URL url){
        HttpURLConnection urlConnection=null;
        try {
            urlConnection=(HttpURLConnection) url.openConnection();
            InputStream in=urlConnection.getInputStream();
            Scanner sc=new Scanner(in);
            sc.useDelimiter("\\A");

            if(sc.hasNext()){
                return sc.next();
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            urlConnection.disconnect();
        }
    }
}
