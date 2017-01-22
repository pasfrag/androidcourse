package com.example.pasca.planefasttickets;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private AutoCompleteTextView originTextView, destinationTextView;
    private ArrayAdapter<String> airportOriginAdapter, airportDestinationAdapter;
    private String[] airportNames, airportValues;
    private int tvController;
    private AirportWatcher originWatcher, destinationWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        originTextView = (AutoCompleteTextView) findViewById(R.id.atv_origin_location);
        destinationTextView = (AutoCompleteTextView) findViewById(R.id.atv_destination_location);

        airportNames = null;
        airportValues = null;
        tvController = 1;

        originWatcher = new AirportWatcher();
        destinationWatcher = new AirportWatcher();

        airportOriginAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        originTextView.setAdapter(airportOriginAdapter);

        airportDestinationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        destinationTextView.setAdapter(airportDestinationAdapter);


        originTextView.addTextChangedListener(originWatcher);
        destinationTextView.addTextChangedListener(destinationWatcher);
    }


    //This class gets the airport names and values from amadeus api
    //and fills the adapters
    class FetchLocationTask extends AsyncTask<String, Void, String>{

        public FetchLocationTask(){

        }

        @Override
        protected String doInBackground(String... params) {

            if (params.length==0){
                return null;
            }
            String location = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String airportJSONStr = null;

            try {
                Uri builtUri = UrlBuildingUtils.locationUri(location);
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (stream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length()==0){
                    return null;
                }

                airportJSONStr = buffer.toString();
            } catch (ProtocolException e) {
                e.printStackTrace();
            }   catch (MalformedURLException e) {
              e.printStackTrace();
            }   catch (IOException e) {
              e.printStackTrace();
            }   catch (NullPointerException e) {
              e.printStackTrace();
            }


            return airportJSONStr;
        }

        @Override
        protected void onPostExecute(String s) {
            getJSONData(s);

            if (tvController == 1){
                adjustTheAdapters(airportOriginAdapter, originTextView);
            }else if (tvController == 2){
                adjustTheAdapters(airportDestinationAdapter, destinationTextView);
            }

        }


    }

    //This class watches for changes in the text views
    class AirportWatcher implements TextWatcher {



        public AirportWatcher(){

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {


            if (s.length()<3){
                return;
            }
            View view = getCurrentFocus();
            if (view == originTextView){
                tvController = 1;
            }
            else if(view == destinationTextView) {
                tvController = 2;
            }

            new FetchLocationTask().execute(s.toString());

        }
    }

    public void getJSONData(String JSONString) {

        try {
            JSONArray airportsArray = new JSONArray(JSONString);

            airportNames = new String[airportsArray.length()];
            airportValues = new String[airportsArray.length()];

            for (int i=0; i<airportsArray.length();i++){
                JSONObject object = airportsArray.getJSONObject(i);
                airportNames[i] = object.getString("label");
                airportValues[i] = object.getString("value");
            }

        } catch (JSONException e) {
          e.printStackTrace();
        }

    }

    public void adjustTheAdapters(ArrayAdapter<String> adapter, AutoCompleteTextView listView){
        if(adapter != null) {
            adapter.clear();
        }

        for (String airportName : airportNames) {
            adapter.add(airportName);
            adapter.notifyDataSetChanged();
        }
        listView.showDropDown();

    }
}
