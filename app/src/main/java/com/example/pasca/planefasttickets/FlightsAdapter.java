package com.example.pasca.planefasttickets;

import android.content.Context;
import android.database.Cursor;
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



    }
    public static class ViewHolder{
        public final TextView depTV;
        public final TextView arrTV;
        public final TextView depRetTV;
        public final TextView arrRetTV;
        public final TextView priceTV;

        public ViewHolder(View view, boolean flag){
            if (flag){
                depTV = (TextView) view.findViewById(R.id.departure_tv);
                arrTV = (TextView) view.findViewById(R.id.arrival_tv);
                depRetTV = (TextView) view.findViewById(R.id.departure_ret_tv);
                arrRetTV = (TextView) view.findViewById(R.id.arrival_ret_tv);
                priceTV = (TextView) view.findViewById(R.id.price_tv);
            }
            else{
                depTV = (TextView) view.findViewById(R.id.departure_tv2);
                arrTV = (TextView) view.findViewById(R.id.arrival_tv2);
                priceTV = (TextView) view.findViewById(R.id.price_tv2);
                depRetTV = null;
                arrRetTV = null;
            }
        }

    }
}
