package com.udacity.android.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.android.spotifystreamer.adapter.RecyclerTopTenAdapter;
import com.udacity.android.spotifystreamer.decoration.SimpleDividerItemDecoration;
import com.udacity.android.spotifystreamer.parcelable.TrackParcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTracksFragment extends Fragment {

    private final static String TAG = TopTenTracksFragment.class.getSimpleName();
    private final static String TOP_TEN_LIST = "topTenList";
    private final static String COUNTRY_CODE = "US";

    private View view;
    private RecyclerView listView;
    private RecyclerTopTenAdapter adapter;
    private TextView noTracksFound;
    private ProgressBar progressBar;
    private AsyncTask<String, Void, List<Track>> topTenTracksTask;
    private TopTenTracksActivity activity;
    private ArrayList<TrackParcelable> trackList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_ten_tracks, container, false);
        noTracksFound = (TextView) view.findViewById(R.id.no_tracks_found);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        listView = (RecyclerView) view.findViewById(R.id.recycler_view_top_ten);
        listView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);

        if(savedInstanceState != null){
            trackList = savedInstanceState.getParcelableArrayList(TOP_TEN_LIST);
            showTracksList(trackList);
        } else {
            // if there is no saved instance that means this is a new load
            Bundle bundle = activity.getIntent().getExtras();
            if(bundle != null) {
                String artistId = bundle.getString(Intent.EXTRA_TEXT);
                loadTopTenSongs(artistId);
            }
        }

        return view;
    }

    private void updateToolbar() {
        // if there is no saved instance that means this is a new load
        Bundle bundle = activity.getIntent().getExtras();
        if(bundle != null) {
            String artistName = bundle.getString(Intent.EXTRA_TITLE);
            activity.updateToolbarTitle(artistName);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (TopTenTracksActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateToolbar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TOP_TEN_LIST, trackList);
    }

    private void loadTopTenSongs(final String artist) {
        cancelAllTasks();
        topTenTracksTask = new AsyncTask<String, Void, List<Track>>() {
            @Override
            protected List<Track> doInBackground(String... params) {
                try {
                    if (params != null && params.length >= 1) {
                        SpotifyApi api = new SpotifyApi();
                        SpotifyService spotify = api.getService();
                        Map<String, Object> options = new HashMap<String, Object>();
                        options.put(SpotifyService.COUNTRY, COUNTRY_CODE);
                        Tracks tracksResponse = spotify.getArtistTopTrack(params[0], options);
                        return tracksResponse.tracks;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception while retrieving the Top Tracks information", e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(final List<Track> tracks) {
                super.onPostExecute(tracks);
                if(isCancelled() || activity == null) {
                    return;
                }

                hideProgress();

                if(tracks == null) {
                    Toast.makeText(activity, activity.getString(R.string.tracks_error_message), Toast.LENGTH_LONG).show();
                    return;
                }
                
                if (tracks.size() > 0) {
                    trackList = new ArrayList<>();
                    for(Track track: tracks){
                        trackList.add(new TrackParcelable(track));
                    }
                    showTracksList(trackList);
                    showTracksListFound();
                } else {
                    showNoTracksFound();
                }

                topTenTracksTask = null;
            }
        };

        showProgress();
        topTenTracksTask.execute(artist);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelAllTasks();
    }

    private void cancelAllTasks(){
        // Check if task is already instantiated then remove it
        if(topTenTracksTask != null) {
            topTenTracksTask.cancel(true);
            topTenTracksTask = null;
        }

    }


    private void showNoTracksFound() {
        noTracksFound.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showTracksListFound() {
        noTracksFound.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress(){
        noTracksFound.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        noTracksFound.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showTracksList(final ArrayList<TrackParcelable> tracks) {
        adapter = new RecyclerTopTenAdapter(getActivity(), tracks, new RecyclerTopTenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), tracks.get(position).id, Toast.LENGTH_LONG).show();
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
