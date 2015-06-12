package com.udacity.android.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.android.spotifystreamer.adapter.RecyclerArtistAdapter;
import com.udacity.android.spotifystreamer.decoration.SimpleDividerItemDecoration;
import com.udacity.android.spotifystreamer.utils.Utils;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {

    private final static String TAG = HomeActivity.class.getSimpleName();

    private View view;
    private EditText search;
    private RecyclerView listView;
    private TextView noArtistFound;
    private ProgressBar progressBar;
    private RecyclerArtistAdapter adapter;
    private Activity activity;
    private List<Artist> artistList;
    private AsyncTask<String, Void, List<Artist>> artistTask;

    public HomeActivityFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        noArtistFound = (TextView) view.findViewById(R.id.no_artist_found);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        listView = (RecyclerView) view.findViewById(R.id.recycler_view_artist);
        listView.addItemDecoration(new SimpleDividerItemDecoration(activity));

        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        listView.setLayoutManager(layoutManager);

        if(artistList != null) {
            setArtistList(artistList);
        }

        search = (EditText) view.findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String artistName = v.getText().toString();
                    if (TextUtils.isEmpty(artistName)) {
                        Toast.makeText(activity, getString(R.string.enter_valid_artist_name), Toast.LENGTH_SHORT).show();
                    } else {
                        loadArtist(artistName);
                    }
                    handled = true;
                }

                Utils.hideSoftKeyboard(activity);
                return handled;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelAllTasks();

    }

    private void loadArtist(final String artist) {
        cancelAllTasks();

        artistTask = new AsyncTask<String, Void, List<Artist>>() {

            @Override
            protected List<Artist> doInBackground(String... params) {
                try {
                    if (params != null && params.length >= 1) {
                        SpotifyApi api = new SpotifyApi();
                        SpotifyService spotify = api.getService();
                        ArtistsPager results = spotify.searchArtists(params[0]);
                        return results.artists.items;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception while retrieving the Artist information", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(final List<Artist> artists) {
                super.onPostExecute(artists);
                if(isCancelled() || activity == null) {
                    return;
                }

                hideProgress();

                if(activity != null) {
                    if (artists == null) {
                        Toast.makeText(activity, activity.getString(R.string.error_while_getting_artist), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (artists.size() > 0) {
                        artistList = artists;
                        setArtistList(artists);
                        showArtistListFound();
                    } else {
                        showNoArtistFound();
                    }

                    artistTask = null;
                }
            }
        };

        showProgress();
        artistTask.execute(artist);
    }


    private void showNoArtistFound() {
        noArtistFound.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showArtistListFound() {
        noArtistFound.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress(){
        noArtistFound.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        noArtistFound.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }



    private void cancelAllTasks() {
        // Check if task is already instantiated then remove it
        if(artistTask != null) {
            artistTask.cancel(true);
            artistTask = null;
        }
    }


    private void setArtistList(final List<Artist> artists) {
        adapter = new RecyclerArtistAdapter(activity, artists, new RecyclerArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(activity, TopTenTracksActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, artists.get(position).id);
                intent.putExtra(Intent.EXTRA_TITLE, artists.get(position).name);
                activity.startActivity(intent);
            }
        });

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
 }
