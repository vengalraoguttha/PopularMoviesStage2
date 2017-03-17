package com.vengalrao.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vengalrao.android.popularmovies.Adapters.ReviewAdapter;
import com.vengalrao.android.popularmovies.Adapters.TrailersAdapter;
import com.vengalrao.android.popularmovies.Data.MovieContract;
import com.vengalrao.android.popularmovies.Utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<DetailFragment.Coll>,TrailersAdapter.LinearInterface{

    private ImageView posterBack;
    private ImageView posterMain;
    private TextView originalName;
    private TextView rating;
    private TextView releaseDate;
    private TextView overview;
    private RecyclerView trailers;
    private TextView noTrailers;
    private RecyclerView reviews;
    private TextView noReviews;
    private static String INTENT_REC_ID="data";
    private TrailersAdapter mTrailersAdapter;
    private ReviewAdapter mReviewAdapter;
    private static String KEY_B="Movie_id";
    private static int LOADER_ID=222;
    private RatingBar ratingBar;
    private static String YOUTUBE_VIEW="https://www.youtube.com/watch?v=";
    private String[] trailerData;
    private Movie data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail, container, false);
        findViews(view);
        Intent intent=getActivity().getIntent();
        Movie movie=intent.getParcelableExtra(INTENT_REC_ID);
        data=movie;
        putData(movie);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        trailers.setLayoutManager(trailerLayoutManager);
        trailers.setHasFixedSize(true);
        mTrailersAdapter=new TrailersAdapter(this);
        trailers.setAdapter(mTrailersAdapter);

        mReviewAdapter=new ReviewAdapter();
        LinearLayoutManager reviewLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        reviews.setLayoutManager(reviewLayoutManager);
        reviews.setHasFixedSize(true);
        reviews.setAdapter(mReviewAdapter);
        trailers.setNestedScrollingEnabled(false);
        reviews.setNestedScrollingEnabled(false);
        load(movie);
        return view;
    }

    public void load(Movie movie){
        Bundle bundle=new Bundle();
        Log.v("aaa","id:"+movie.getId());
        bundle.putString(KEY_B,movie.getId());
        LoaderManager loaderManager=getLoaderManager();
        Loader<String> movieLoader=loaderManager.getLoader(LOADER_ID);
        if(movieLoader==null){
            loaderManager.initLoader(LOADER_ID,bundle,this);
        }else{
            loaderManager.restartLoader(LOADER_ID,bundle,this);
        }
    }

    public void parse(String data){
        try {
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("results");
            String[] paths=new String[array.length()];
            String[] keys=new String[array.length()];
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String key=jsonObject.getString("key");
                keys[i]=key;
                paths[i]=NetworkUtilities.buildMoviePath(key);
                Log.v("aaa",paths[i]+" "+NetworkUtilities.buildMoviePath(key)+" "+key);
            }
            trailerData=keys;
            mTrailersAdapter.getData(paths);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parse_review(String data){
        try {
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("results");
            String[] author=new String[array.length()];
            String[] review=new String[array.length()];
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                author[i]=jsonObject.getString("author");
                review[i]=jsonObject.getString("content");
            }

            mReviewAdapter.putData(author,review);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putData(Movie movie){
        Picasso.with(getContext()).load(movie.getBackDropPath()).into(posterBack);
        Picasso.with(getContext()).load(movie.getPosterPath()).into(posterMain);
        originalName.setText(movie.getOriginalName());
        rating.setText(movie.getRating().concat("/10"));
        releaseDate.setText(movie.getReleaseDate());
        overview.setText(movie.getOverview());
        ratingBar.setNumStars(5);
        ratingBar.setRating(Float.parseFloat(movie.getRating())/2f);
    }

    public void findViews(View view){
        posterBack=(ImageView)view.findViewById(R.id.detail_poster_back);
        posterMain=(ImageView)view.findViewById(R.id.detail_poster_main);
        originalName=(TextView)view.findViewById(R.id.detail_original_name);
        rating=(TextView)view.findViewById(R.id.detail_rating);
        releaseDate=(TextView)view.findViewById(R.id.detail_release_date);
        overview=(TextView)view.findViewById(R.id.detail_overview);
        trailers=(RecyclerView)view.findViewById(R.id.detail_trailers);
        noTrailers=(TextView)view.findViewById(R.id.noTrailers);
        reviews=(RecyclerView) view.findViewById(R.id.detail_reviews_recycler_view);
        noReviews=(TextView)view.findViewById(R.id.noReviews);
        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
    }

    @Override
    public Loader<Coll> onCreateLoader(int id,final Bundle args) {
        return new AsyncTaskLoader<Coll>(getContext()) {
            @Override
            public Coll loadInBackground() {
                URL url=NetworkUtilities.buildUrlMovie(args.getString(KEY_B));
                URL review_url=NetworkUtilities.buildUrlReview(args.getString(KEY_B));
                Log.v("aaa",url.toString());
                String data=NetworkUtilities.getResposeFromHttpUrl(url);
                String review=NetworkUtilities.getResposeFromHttpUrl(review_url);
                Coll coll=new Coll();
                coll.s1=data;
                coll.s2=review;
                return coll;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Coll> loader, Coll data) {
        Log.v("aaa","data:"+data);
        if(data!=null&&!data.equals("")){
            parse(data.s1);
            parse_review(data.s2);
            showTrailerData();
        }else {
            showErrorMessage();
        }
    }

    public void showTrailerData(){
        noTrailers.setVisibility(View.INVISIBLE);
        trailers.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(){
        noTrailers.setVisibility(View.VISIBLE);
        trailers.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Coll> loader) {

    }

    @Override
    public void onLinearInterface(int clickedPosition) {
        String url=YOUTUBE_VIEW.concat(trailerData[clickedPosition]);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    class Coll{
        String s1;
        String s2;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(checkData()){
            menu.getItem(0).setIcon(R.drawable.ic_favorite_pink_24px);
        }else{
            menu.getItem(0).setIcon(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    public boolean checkData(){
        Cursor cursor=getContext().getContentResolver().query(MovieContract.MovieDetail.CONTENT_URI_FAV_DET,
                null,
                null,
                null,
                null
        );
        Log.v("uri",cursor.toString());
        cursor.moveToFirst();
        Log.v("uri","count "+cursor.getCount());
        for(int i=0;i<cursor.getCount();i++){
           if(cursor.getString(cursor.getColumnIndex(MovieContract.MovieDetail.MOVIE_ID)).equals(data.getId())){
               return true;
           }
           cursor.moveToNext();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        Log.v("yyy","aa");
        if(id==R.id.favorite_action){
            Log.v("uri","entered1");
            Log.v("uri",""+id+" "+R.drawable.ic_favorite_border_white_24dp+" "+R.drawable.ic_favorite_pink_24px);
            if(item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp).getConstantState())){
                Log.v("uri","entered");
                Uri uri=insertIntoDataBase();
                if(uri!=null){
                    item.setIcon(R.drawable.ic_favorite_pink_24px);
                }
            }else if(item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_favorite_pink_24px).getConstantState())){
                int del=deleteFromDataBase();
                Log.v("uri del",""+del);
                if(del>0){
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int deleteFromDataBase(){
        Uri uri= MovieContract.MovieDetail.CONTENT_URI_FAV_DET.buildUpon().appendPath(data.getId()).build();
        int x=getContext().getContentResolver().delete(uri,null,null);
        return x;
    }

    public Uri insertIntoDataBase(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(MovieContract.MovieDetail.MOVIE_ID,data.getId());
        contentValues.put(MovieContract.MovieDetail.MOVIE_NAME,data.getOriginalName());
        contentValues.put(MovieContract.MovieDetail.POSTER_PATH,data.getPosterPath());
        contentValues.put(MovieContract.MovieDetail.RATING,data.getRating());
        contentValues.put(MovieContract.MovieDetail.RELEASE_DATE,data.getReleaseDate());
        contentValues.put(MovieContract.MovieDetail.BACKDROP_PATH,data.getBackDropPath());
        contentValues.put(MovieContract.MovieDetail.OVERVIEW,data.getOverview());
        Uri uri=getContext().getContentResolver().insert(MovieContract.MovieDetail.CONTENT_URI_FAV_DET,contentValues);
        Log.v("uriuuuuuuuuuuuu",data.getOriginalName());
        Log.v("uri",uri.toString());
        return uri;
    }
}
