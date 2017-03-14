package com.vengalrao.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vengalrao.android.popularmovies.Utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.GridItemClickListener,LoaderManager.LoaderCallbacks<String>,SharedPreferences.OnSharedPreferenceChangeListener{

    RecyclerView mRecyclerView;
    TextView errorMessage;
    MovieAdapter mMovieAdapter;
    public static final String KEY="sortby";
    private static int LOADER_ID=10;
    private static String STATE_SAVE_ID="Main.vengalrao";
    private static String INTENT_SENT_ID="data";
    private Movie[] movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=(RecyclerView) findViewById(R.id.recycler_view_main);
        errorMessage=(TextView)findViewById(R.id.error_meaasge_main);

        int columns;
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            columns=2;
        }else{
            columns=3;
        }

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,columns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter=new MovieAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadData();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.movie_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_setting){
            Intent intent=new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadData(){
        Bundle bundle=new Bundle();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Log.v("xxx",sharedPreferences.getString(getString(R.string.pref_movie_key),getString(R.string.popular_key)));
        bundle.putString(KEY,sharedPreferences.getString(getString(R.string.pref_movie_key),getString(R.string.popular_key)));
        LoaderManager loaderManager=getSupportLoaderManager();
        Loader<String> movieLoader=loaderManager.getLoader(LOADER_ID);
        if(movieLoader==null){
            loaderManager.initLoader(LOADER_ID,bundle,this);
        }else{
            loaderManager.restartLoader(LOADER_ID,bundle,this);
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(STATE_SAVE_ID,movieData);
    }

    @Override
    public void onGridItemClick(int clickedPosition) {
        Toast.makeText(this,"entered "+clickedPosition,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,DetailAcitvity.class);
        intent.putExtra(INTENT_SENT_ID,movieData[clickedPosition]);
        startActivity(intent);
    }

    public void showMovieData(){
        errorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(){
        errorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader(this) {
            @Override
            public String loadInBackground() {
                URL url= NetworkUtilities.buildUrl(args.getString(KEY));
                return NetworkUtilities.getResposeFromHttpUrl(url);
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, String data) {
        if(data!=null&&!data.equals("")){
            parseJson(data);
            showMovieData();
        }else {
            showErrorMessage();
        }
    }

    public void parseJson(String data){
        Movie[] movies=null;
        try {
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("results");
            movies=new Movie[array.length()];
            for(int i=0;i<array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String path=jsonObject.getString("poster_path");
                Log.v("xx",path);
                movies[i]=new Movie();
                if(movies[i]!=null){
                    movies[i].setPosterPath(NetworkUtilities.buidPosterPath(path));
                    movies[i].setRating(jsonObject.getString("vote_average"));
                    movies[i].setBackDropPath(jsonObject.getString("backdrop_path"));
                    movies[i].setId(jsonObject.getString("id"));
                    movies[i].setOriginalName(jsonObject.getString("original_title"));
                    movies[i].setOverview(jsonObject.getString("overview"));
                    movies[i].setReleaseDate(jsonObject.getString("release_date"));
                }
            }
            movieData=movies;
            mMovieAdapter.setData(movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadData();
    }
}
