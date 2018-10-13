package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_FEED_LOADER_ID = 1;
    private static final String LOG_TAG = MainActivity.class.getName();
    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL = "https://content.guardianapis.com/search?page=1&q=technology&api-key=46b4f304-2d67-44e7-9525-3ea9d03ead9a";
    private TextView emptyView;

    /**
     * Adapter for the list of earthquakes
     */
    private NewsFeedAdapater mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();

        //get device internet connection hardware
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_FEED_LOADER_ID, null, this);
        } else {
            //remove progress bar when the earthquake finish loading
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);

            //display internet error on th empty view
            emptyView = (TextView) findViewById(R.id.empty_view);
            emptyView.setText(R.string.no_internet);
        }

    }

    private void updateUI() {

        //emptyView = (TextView)findViewById(R.id.empty_view);
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new NewsFeedAdapater(this, new ArrayList<News>());

        //set list view to empty view if there is no data to display
        earthquakeListView.setEmptyView(emptyView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //gets the position of news article click
                News itemClick = mAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(itemClick.getURL());

                //create a new intent to view the earthquake url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(newsUri);
                // Send the intent to launch a new activity
                startActivity(intent);
            }
        });
    }

    /**
     * We need onCreateLoader(), for when the LoaderManager has determined that
     * the loader with our specified ID isn't running, so we should create a new one
     *
     * @param i
     * @param bundle
     * @return Loader<List   <   Earthquake>>
     */
    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {


        return new NewsFeedLoader(this,USGS_REQUEST_URL);

    }

    /**
     * updating the dataset in the adapter.
     *
     * @param loader
     * @param newsFeed list of news article
     */
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsFeed) {
        // Clear the adapter of previous earthquake data
        Log.i(LOG_TAG, "TEST: onLoadFinished called");
        mAdapter.clear();

        //remove progress bar when the earthquake finish loading
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        emptyView.setText(R.string.no_earthquakes);
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsFeed != null && !newsFeed.isEmpty()) {
            mAdapter.addAll(newsFeed);
        }
    }

    /**
     * This isn't actually a case that's going to come up with our simple loader,
     * but the correct thing to do is to remove
     * all the earthquake data from our UI by clearing out the adapter’s data set.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.i(LOG_TAG, "TEST: onLoaderReset called");
        mAdapter.clear();
    }

}
