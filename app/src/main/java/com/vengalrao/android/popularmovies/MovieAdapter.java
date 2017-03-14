package com.vengalrao.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by vengalrao on 14-03-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    final private GridItemClickListener mGridItemClickListener;
    Movie[] movies;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutId=R.layout.grid_item_view;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(layoutId,parent,shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie currentMovie=movies[position];
        if (currentMovie!=null){
            Picasso.with(holder.poster.getContext()).load(currentMovie.getPosterPath()).into(holder.poster);
            holder.name.setText(currentMovie.getOriginalName());
            Log.v("Mov",currentMovie.getRating());
            holder.rating.setText(currentMovie.getRating());
        }
    }

    @Override
    public int getItemCount() {
        if(movies==null)
        return 0;
        else
        return movies.length;
    }

    public interface GridItemClickListener{
        void onGridItemClick(int clickedPosition);
    }

    public MovieAdapter(GridItemClickListener listener){
        mGridItemClickListener=listener;
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView poster;
        public final TextView name;
        public final TextView rating;
        public MovieAdapterViewHolder(View view){
            super(view);
            poster=(ImageView)view.findViewById(R.id.movie_image);
            name=(TextView)view.findViewById(R.id.movie_name);
            rating=(TextView)view.findViewById(R.id.movie_rating);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            Log.v("Mov",""+clickedPosition);
            mGridItemClickListener.onGridItemClick(clickedPosition);
        }
    }

    public void setData(Movie[] data){
        movies=data;
        notifyDataSetChanged();
    }
}
