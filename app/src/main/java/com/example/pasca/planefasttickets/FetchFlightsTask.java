package com.example.pasca.planefasttickets;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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


public class FetchFlightsTask extends AsyncTask<String, Void, Void> {


    private final Context mContext;
    public FetchFlightsTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if(params.length == 0){
            return null;
        }

        String originQuery = params[0];
        String destinationQuery = params[1];
        String departureDate = params[2];
        String returnDate = params[3];
        String adultsQuery = params[4];
        String childQuery = params[5];
        String directQuery = params[6];
        String priceQuery = params[7];
        String currencyQuery = params[8];

        HttpURLConnection urlConnection =null;
        BufferedReader reader = null;

        String flightsJSONStr = null;

        try {
            Uri uri = UrlBuildingUtils.flightSearchUri(originQuery,destinationQuery,departureDate,returnDate,adultsQuery,childQuery,directQuery,priceQuery,currencyQuery);
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream stream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (stream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            flightsJSONStr = buffer.toString();
            getFlightsDataFromJSON(flightsJSONStr);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("FetchFlightsTask", "Error closing stream", e);
                }
            }
        }
      return null;
    }

    private void getFlightsDataFromJSON(String flightsJSONStr)throws JSONException{
        JSONObject firstObject = new JSONObject(flightsJSONStr);
        JSONArray resultsArray = firstObject.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++){

        }
    }
}
