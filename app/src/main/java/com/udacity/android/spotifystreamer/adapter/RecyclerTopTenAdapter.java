package com.udacity.android.spotifystreamer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.udacity.android.spotifystreamer.R;
import com.udacity.android.spotifystreamer.parcelable.TrackParcelable;
import com.udacity.android.spotifystreamer.viewholder.SongsHolder;

import java.util.List;

/**
 * Created by yogesh.shrivastava on 6/7/15.
 */
public class RecyclerTopTenAdapter extends RecyclerView.Adapter<SongsHolder> {

    private List<TrackParcelable> songsList;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public RecyclerTopTenAdapter(Context context, List<TrackParcelable> songsList, OnItemClickListener mItemClickListener) {
        this.context = context;
        this.songsList = songsList;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public SongsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_item, parent, false);
        SongsHolder holder = new SongsHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(SongsHolder holder, int pos) {
        TrackParcelable track = songsList.get(pos);
        holder.songTitle.setText(track.name);
        holder.albumTitle.setText(track.albumName);
        if(!TextUtils.isEmpty(track.albumImageUrl)) {
            Picasso.with(context).load(track.albumImageUrl).into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return songsList != null ? songsList.size() : 0;
    }
}
