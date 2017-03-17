package com.vengalrao.android.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vengalrao.android.popularmovies.Movie;
import com.vengalrao.android.popularmovies.R;

/**
 * Created by vengalrao on 15-03-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    String[] authorName;
    String[] reviews;
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        int layoutId=R.layout.review_item;
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(layoutId,parent,shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        if(authorName!=null&&reviews!=null){
            String currentAuthor=authorName[position];
            String currentReview=reviews[position];
            holder.author_name.setText(currentAuthor);
            holder.review_item.setText(currentReview);
        }else{
            holder.review_item.setText(R.string.no_reviews_data);
        }
    }

    @Override
    public int getItemCount() {
        if(authorName==null)
        return 0;
        else
            return authorName.length;
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView author_name;
        TextView review_item;
        public ReviewAdapterViewHolder(View view){
            super(view);
            author_name=(TextView)view.findViewById(R.id.review_author_item);
            review_item=(TextView)view.findViewById(R.id.review_item);
        }
    }

    public  void putData(String[] authorName,String[] reviews){
        this.authorName=authorName;
        this.reviews=reviews;
        notifyDataSetChanged();
    }
}
