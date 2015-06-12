package com.udacity.android.spotifystreamer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.udacity.android.spotifystreamer.R;
import com.udacity.android.spotifystreamer.viewholder.ArtistHolder;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by yogesh.shrivastava on 6/6/15.
 */
public class RecyclerArtistAdapter extends RecyclerView.Adapter<ArtistHolder> {

    private List<Artist> artistList;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public RecyclerArtistAdapter(Context context, List<Artist> artistList, OnItemClickListener mItemClickListene) {
        this.context = context;
        this.artistList = artistList;
        this.mItemClickListener = mItemClickListene;
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);
        ArtistHolder holder = new ArtistHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArtistHolder artistHolder, int i) {
        Artist artist = artistList.get(i);


        if(artist.images.size() > 0) {
            Image selectedImage = null;

            // Check to see if image with 200 width is available
            for(Image image: artist.images) {
                if(image.width == 200 || image.height == 200) {
                    selectedImage = image;
                    break;
                }
            }

            // fall back to last image in the list
            if(selectedImage == null) {
                selectedImage = artist.images.get(artist.images.size() - 1);
            }

            // Load selected image in the image view
            Picasso.with(artistHolder.thumbnail.getContext()).load(selectedImage.url).into(artistHolder.thumbnail);
        }

        artistHolder.title.setText(artist.name);
    }

    @Override
    public int getItemCount() {
        return artistList != null ? artistList.size() : 0;
    }
}
