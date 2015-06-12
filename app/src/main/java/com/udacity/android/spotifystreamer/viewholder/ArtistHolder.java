package com.udacity.android.spotifystreamer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.android.spotifystreamer.R;
import com.udacity.android.spotifystreamer.adapter.RecyclerArtistAdapter;

/**
 * Created by yogesh.shrivastava on 6/6/15.
 */
public class ArtistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView thumbnail;
    public TextView title;
    private RecyclerArtistAdapter.OnItemClickListener mItemClickListener;

    public ArtistHolder(View itemView, RecyclerArtistAdapter.OnItemClickListener mItemClickListener) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.title = (TextView) itemView.findViewById(R.id.artist_title);
        this.mItemClickListener = mItemClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mItemClickListener != null) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
