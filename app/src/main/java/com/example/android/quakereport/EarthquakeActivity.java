
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ArrayList<EarthQuake> earthquakes =QueryUtils.extractEarthquakes();
        
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //we add final modifier on the EarthquakeAdapter local variable
        // so  that we could access the adapter variable within the OnItemClickListener.
        final EarthQuakeAdapter adapter = new EarthQuakeAdapter(
                this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

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
                EarthQuake currentEarthquake = adapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Intent.ACTION_VIEW is used to open the browser
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent);
            }
        });
    }
}
