
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
                                                                            // generic parameter specifying what the loader will return
public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** Adapter for the list of earthquakes */
    private EarthQuakeAdapter mAdapter;

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        /* to hook up the TextView as the empty view of the ListView */


        mAdapter = new EarthQuakeAdapter(
                this, new ArrayList<EarthQuake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {


            //These below two line goes inside the onCreate() method of the EarthquakeActivity,
            // so that the loader can be initialized as soon as the app opens.

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = LoaderManager.getInstance(this);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null(to skip) for
            // the bundle of additional information. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

        /*
         The third argument is what object should receive the LoaderCallbacks
         (and therefore, the data when the load is complete!) - which will be this activity
        */
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else{
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        /*
        We need to declare an OnItemClickListener on the ListView.
        OnItemClickListener is an interface, which contains a single method onItemClick().
        We declare an anonymous class that implements this interface, and provides customized logic for what should happen in the onItemClick() method.
        Remember that the onItemClick() method is a callback triggered by the Android system when the user clicks on a list item.
        *
        */
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Finding the earthquake that was clicked on
                EarthQuake currentEarthquake = mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Intent.ACTION_VIEW is used to open the browser
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                Log.v("EarthQuakeActivity","I am here");
                startActivity(websiteIntent);
            }
        });
    }

    /*
     We need onCreateLoader(),
     for when the LoaderManager has determined that the loader with our specified ID isn't running, so we should create a new one.
     */
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new EarthQuakeLoader(this, USGS_REQUEST_URL);
    }

    /*
    We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(),
    and use the earthquake data to update our UI - by updating the dataset in the adapter.
     */
    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> earthquakes) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }


        // Hide loading indicator because the data has been loaded
        ProgressBar loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        /*
        To avoid the “No earthquakes found.” message blinking on the screen when the app first launches,
        we can leave the empty state TextView blank, until the first load completes
        so we set in LoadFinished
        */
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        /*
        It’s okay if this text is set every time the loader finishes because it’s not too expensive of an operation
        */
    }

    /*
    And we need onLoaderReset(), we're we're being informed that the data from our loader(previous load) is no longer valid.
     */
    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
