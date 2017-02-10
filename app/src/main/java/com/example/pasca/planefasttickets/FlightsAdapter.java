package com.example.pasca.planefasttickets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.util.Currency;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class FlightsAdapter extends CursorAdapter {

    private boolean withReturn;
    private final int VIEW_TYPE_WITH = 0;
    private final int VIEW_TYPE_WITHOUT = 1;

    public FlightsAdapter(Context context, Cursor c, int flags, boolean withReturn) {
        super(context, c, flags);
        this.withReturn = withReturn;
    }

    public int getItemViewType(){
        return (withReturn) ? VIEW_TYPE_WITH : VIEW_TYPE_WITHOUT;
    }

    public int getViewTypeCount(){return 2;}
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType();
        int layoutID = -1;
        ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_WITH) layoutID = R.layout.flights_with;
        else if (viewType == VIEW_TYPE_WITHOUT) layoutID = R.layout.flights_without;

        View view = LayoutInflater.from(context).inflate(layoutID, parent, false);
        if (viewType == VIEW_TYPE_WITH) viewHolder = new ViewHolder(view, true);
        else if (viewType == VIEW_TYPE_WITHOUT) viewHolder = new ViewHolder(view, false);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String dep = cursor.getString(SelectFlightsActivity.COL_FLIGHT_DEP_DATE);
        viewHolder.depTV.setText(getTime(dep));

        String arr = cursor.getString(SelectFlightsActivity.COL_FLIGHT_ARR_DATE);
        viewHolder.arrTV.setText(getTime(arr));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String price = cursor.getString(SelectFlightsActivity.COL_FLIGHT_PRICE);
        viewHolder.arrTV.setText(price + preferences.getString("curr_list", "EUR"));

        String numFlights = cursor.getString(SelectFlightsActivity.COL_FLIGHT_NUMBER);
        viewHolder.numFlightsTV.setText(numFlights);

        if (withReturn){
            String depr = cursor.getString(SelectFlightsActivity.COL_FLIGHT_DEP_DATE_R);
            viewHolder.depRetTV.setText(depr);

            String arr_r = cursor.getString(SelectFlightsActivity.COL_FLIGHT_ARR_DATE_R);
            viewHolder.depRetTV.setText(arr_r);

            String numFlightsR = cursor.getString(SelectFlightsActivity.COL_FLIGHT_NUMBER_R);
            viewHolder.numFlightsTV.setText(numFlightsR);
        }


    }

    private String getTime(String date){
        return date.substring(date.indexOf("T") + 1);
    }


    public static class ViewHolder{
        public final TextView depTV;
        public final TextView arrTV;
        public final TextView depRetTV;
        public final TextView arrRetTV;
        public final TextView priceTV;
        public final TextView numFlightsTV;
        public final TextView numFlightsRetTV;

        public ViewHolder(View view, boolean flag){
            if (flag){
                depTV = (TextView) view.findViewById(R.id.departure_tv);
                arrTV = (TextView) view.findViewById(R.id.arrival_tv);
                depRetTV = (TextView) view.findViewById(R.id.departure_ret_tv);
                arrRetTV = (TextView) view.findViewById(R.id.arrival_ret_tv);
                priceTV = (TextView) view.findViewById(R.id.price_tv);
                numFlightsTV = (TextView) view.findViewById(R.id.num_of_flights);
                numFlightsRetTV = (TextView) view.findViewById(R.id.num_of_flights_r);
            }
            else{
                depTV = (TextView) view.findViewById(R.id.departure_tv2);
                arrTV = (TextView) view.findViewById(R.id.arrival_tv2);
                priceTV = (TextView) view.findViewById(R.id.price_tv2);
                depRetTV = null;
                arrRetTV = null;
                numFlightsTV = (TextView) view.findViewById(R.id.num_of_flights2);
                numFlightsRetTV = null;
            }
        }

    }
}
