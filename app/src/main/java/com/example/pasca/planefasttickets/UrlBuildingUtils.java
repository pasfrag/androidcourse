package com.example.pasca.planefasttickets;


import android.net.Uri;


public class UrlBuildingUtils {
    private final static String BASE_URL_AUTOCOMPLETE = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?";
    private final static String BASE_URL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
    private final static String API_KEY_PARAM = "apikey";
    private final static String TERM = "term";
    private final static String ORIGIN_PARAM = "origin";
    private final static String DESTINATION_PARAM = "destination";
    private final static String DEPARTURE_PARAM = "departure_date";
    private final static String RETURN_PARAM = "return_date";
    private final static String ADULTS_PARAM = "adults";
    private final static String CHILDREN_PARAM = "children";
    private final static String STOP_PARAM = "non_stop";
    private final static String PRICE_PARAM = "max_price";
    private final static String CURRENCY_PARAM = "currency";
    private final static String RESULTS = "number_of_results";
    private final static String API_KEY = "MKeJmVv3g1SyMW7PM0WWZ3GDyGCQtAR4";


    //Builds the uri
    public static Uri locationUri(String term){
        Uri builtUri = Uri.parse(BASE_URL_AUTOCOMPLETE).buildUpon()
            .appendQueryParameter(API_KEY_PARAM,API_KEY)
            .appendQueryParameter(TERM,term)
            .build();

        return builtUri;
    }

    public Uri flightSearchUri(String origin, String destination, String depDate, String retDate, String adults, String children, String stops, String price, String currency){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
            .appendQueryParameter(API_KEY_PARAM,API_KEY)
            .appendQueryParameter(ORIGIN_PARAM, origin)
            .appendQueryParameter(DESTINATION_PARAM, destination)
            .appendQueryParameter(DEPARTURE_PARAM, depDate)
            .appendQueryParameter(RETURN_PARAM, retDate)
            .appendQueryParameter(ADULTS_PARAM, adults)
            .appendQueryParameter(CHILDREN_PARAM, children)
            .appendQueryParameter(STOP_PARAM, stops)
            .appendQueryParameter(PRICE_PARAM, price)
            .appendQueryParameter(CURRENCY_PARAM, currency)
            .appendQueryParameter(RESULTS, "10")
            .build();

        return builtUri;
    }

}
