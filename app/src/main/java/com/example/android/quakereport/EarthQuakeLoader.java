package com.example.android.quakereport;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;
/**
 * We specify List as the generic parameter, which explains what type of data is expected to be loaded.
 * In this case, the loader is loading a list of Earthquake objects. Then we take a String URL in the constructor,
 * and in loadInBackground(), we'll do the exact same operations as in doInBackground back in EarthquakeAsyncTask.
 * */

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    /** Tag for log messages */
    private static final String LOG_TAG = EarthQuakeLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link EarthQuakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public EarthQuakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        /*
         Notice that we also override the onStartLoading() method to call forceLoad()
         which is a required step to actually trigger the loadInBackground() method to execute.
         */
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<EarthQuake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<EarthQuake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}