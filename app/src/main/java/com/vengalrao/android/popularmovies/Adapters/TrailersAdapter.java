package com.vengalrao.android.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vengalrao.android.popularmovies.R;

/**
 * Created by vengalrao on 15-03-2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerAdapterViewHolder>{

    String[] path;

    final public LinearInterface mLinearInterface;

    public TrailersAdapter(LinearInterface linearInterface){
        mLinearInterface=linearInterface;
    }

    public interface LinearInterface{
        void onLinearInterface(int clickedPosition);
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(R.layout.trailer_item,parent,shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        if(path!=null){
            String p=path[position];
            if(p!=null){
                Picasso.with(holder.trailerImage.getContext()).load(p).into(holder.trailerImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(path==null)
            return 0;
        else
        return path.length;
    }

    public  class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView trailerImage;
        ImageView play;
        TextView noTrailers;
        public TrailerAdapterViewHolder(View view){
            super(view);
            trailerImage=(ImageView)view.findViewById(R.id.trailer_image);
            play=(ImageView)view.findViewById(R.id.play_outline);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            mLinearInterface.onLinearInterface(clickedPosition);
        }
    }

    public void getData(String[] data){
        path=data;
        notifyDataSetChanged();
    }
}
