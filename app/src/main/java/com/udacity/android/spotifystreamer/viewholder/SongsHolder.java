package com.udacity.android.spotifystreamer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.android.spotifystreamer.R;
import com.udacity.android.spotifystreamer.adapter.RecyclerTopTenAdapter;

/**
 * Created by yogesh.shrivastava on 6/7/15.
 */
public class SongsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView thumbnail;
    public TextView songTitle;
    public TextView albumTitle;

    private RecyclerTopTenAdapter.OnItemClickListener mItemClickListener;

    public SongsHolder(View itemView, RecyclerTopTenAdapter.OnItemClickListener mItemClickListener) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.songTitle = (TextView) itemView.findViewById(R.id.song_title);
        this.albumTitle = (TextView) itemView.findViewById(R.id.album_title);
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
