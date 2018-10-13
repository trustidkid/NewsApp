package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsFeedLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsFeedLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsFeedLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsFeedLoader(Context context, String url)
    {super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST: onStartLoading called");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        Log.i(LOG_TAG,"TEST: LoadInBackground called");
        if (mUrl == null) {
            return null;
        }

        List<News> result = FeedQueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}