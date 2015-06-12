package com.udacity.android.spotifystreamer.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by yogesh.shrivastava on 6/12/15.
 */
public class TrackParcelable implements Parcelable {
    public String name;
    public String id;
    public String albumName;
    public String albumImageUrl;

    public TrackParcelable (Parcel source) {
        id = source.readString();
        name = source.readString();
        albumName = source.readString();
        albumImageUrl = source.readString();
    }

    public TrackParcelable() {

    }

    public TrackParcelable(Track track) {
        this.id = track.id;
        this.name = track.name;
        this.albumName = track.album.name;

        if(track.album.images.size() > 0) {
            Image selectedImage = null;

            // Check to see if image with 200 width is available
            for(Image image: track.album.images) {
                if(image.width == 200 || image.height == 200) {
                    selectedImage = image;
                    break;
                }
            }

            // fall back to last image in the list
            if(selectedImage == null) {
                selectedImage = track.album.images.get(track.album.images.size() - 1);
            }
            this.albumImageUrl = selectedImage.url;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(albumName);
        dest.writeString(albumImageUrl);
    }


    public static final Parcelable.Creator<TrackParcelable> CREATOR =
        new Parcelable.Creator<TrackParcelable>() {
            @Override
            public TrackParcelable createFromParcel(Parcel source) {
                return new TrackParcelable(source);
            }

            @Override
            public TrackParcelable[] newArray(int size) {
                return new TrackParcelable[0];
            }
    };
}
