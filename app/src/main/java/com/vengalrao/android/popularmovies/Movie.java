package com.vengalrao.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vengalrao on 14-03-2017.
 */

public class Movie implements Parcelable {
    private String originalName;
    private String rating;
    private String posterPath;
    private String backDropPath;
    private String releaseDate;
    private String overview;
    private String id;

    public Movie(){
        super();
    }

    public Movie(Parcel in){
        readFromParcel(in);
    }

    public String getOriginalName(){
        return originalName;
    }

    public  String getRating(){
        return rating;
    }

    public String getPosterPath(){
        return posterPath;
    }

    public String getBackDropPath(){
        return backDropPath;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getOverview(){
        return overview;
    }

    public String getId(){ return id;}

    public void setOriginalName(String originalName){
        this.originalName=originalName;
    }

    public  void setRating(String rating){
        this.rating=rating;
    }

    public void setPosterPath(String posterPath){
        this.posterPath=posterPath;
    }

    public void setBackDropPath(String backDropPath){
        this.backDropPath=backDropPath;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate=releaseDate;
    }

    public void setOverview(String overview){
        this.overview=overview;
    }

    public void setId(String id){ this.id=id;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalName);
        dest.writeString(rating);
        dest.writeString(posterPath);
        dest.writeString(backDropPath);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(id);
    }

    private void readFromParcel(Parcel in){
        originalName=in.readString();
        rating=in.readString();
        posterPath=in.readString();
        backDropPath=in.readString();
        releaseDate=in.readString();
        overview=in.readString();
        id=in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR=new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
