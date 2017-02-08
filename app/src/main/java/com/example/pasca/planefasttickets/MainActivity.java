package com.example.pasca.planefasttickets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, View.OnClickListener {

    //Defining data variables
    private String origin, destination, adults, children, depDate, retDate, direct, price;

    //Defining UI variables
    private AutoCompleteTextView originTextView, destinationTextView;
    private EditText depDateET, retDateET, priceET;
    private Switch retSwitch;
    private Spinner adultSpinner, childSpinner;
    private CheckBox directCB;
    private Button searchButton;

    //Defining data variables for UI
    private Calendar myCal;
    private int tvController;
    private AirportWatcher originWatcher, destinationWatcher;
    private ArrayAdapter<String> airportOriginAdapter, airportDestinationAdapter;
    private ArrayAdapter<CharSequence> adultsAdapter, childAdapter;
    private String[] airportNames, airportValues;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        originTextView = (AutoCompleteTextView) findViewById(R.id.atv_origin_location);
        destinationTextView = (AutoCompleteTextView) findViewById(R.id.atv_destination_location);

        airportNames = null;
        airportValues = null;

        adults = "1";
        children = "0";

        origin = null;
        destination = null;

        direct = "false";
        price = null;
        depDate = null;
        retDate = null;

        tvController = 1;

        depDateET = (EditText) findViewById(R.id.et_departure_date);
        retDateET = (EditText) findViewById(R.id.et_return_date);

        priceET = (EditText) findViewById(R.id.price_et);

        priceET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_ENTER:
                            price = priceET.getText().toString();
                            return true;
                    }

                }
                return false;
            }
        });

        retSwitch = (Switch) findViewById(R.id.switch_return);

        originWatcher = new AirportWatcher();
        destinationWatcher = new AirportWatcher();

        airportOriginAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        originTextView.setAdapter(airportOriginAdapter);

        airportDestinationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        destinationTextView.setAdapter(airportDestinationAdapter);


        originTextView.addTextChangedListener(originWatcher);
        destinationTextView.addTextChangedListener(destinationWatcher);

        originTextView.setOnItemClickListener(this);
        destinationTextView.setOnItemClickListener(this);

        adultSpinner = (Spinner) findViewById(R.id.spinner_adults);
        childSpinner = (Spinner) findViewById(R.id.spinner_child);

        adultsAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_adult_text, android.R.layout.simple_spinner_item);
        adultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        childAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_children_text, android.R.layout.simple_spinner_item);
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adultSpinner.setAdapter(adultsAdapter);
        childSpinner.setAdapter(childAdapter);

        adultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adults = adultsAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                children = childAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myCal = Calendar.getInstance();

        depDateET.setOnClickListener(this);
        retDateET.setOnClickListener(this);

        retSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    retDateET.setVisibility(View.VISIBLE);
                    retSwitch.setHint("With return");
                } else {
                    retDateET.setVisibility(View.INVISIBLE);
                    retSwitch.setHint("Without return");
                }
            }
        });

        directCB = (CheckBox) findViewById(R.id.checkBox);
        directCB.setOnClickListener(this);

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Just a watcher
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        myCal.set(Calendar.YEAR, year);
        myCal.set(Calendar.MONTH, month);
        myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        getDateData();
    }

    //The method that retrieves the dates from
    public void getDateData(){

        String myformat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat);

        if (tvController == 1){
            depDate = sdf.format(myCal.getTime());

            depDateET.setText(depDate);

        }else if (tvController == 2){
            retDate = sdf.format(myCal.getTime());

            retDateET.setText(retDate);
        }
    }

    //The listener for a an item to be clicked in the auutocomlete views
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (tvController == 1){
            origin = airportValues[position];
        }else if(tvController == 2){
            destination = airportValues[position];
        }
    }

    @Override
    public void onClick(View v) {
        if (v == retDateET){
            tvController = 2;
            new DatePickerDialog(MainActivity.this, MainActivity.this, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if (v == depDateET){
            tvController = 1;
            new DatePickerDialog(MainActivity.this, MainActivity.this, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal.get(Calendar.DAY_OF_MONTH)).show();
        }
        else if (v == directCB){
            if(directCB.isChecked()){
                direct = "true";
                directCB.setText("Without stops");
            }else{
                direct = "false";
                directCB.setText("With stops");
            }
        }
        else if (v == searchButton){
            if (isPressable()){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Toast toast = Toast.makeText(MainActivity.this, preferences.getString("curr_list", "EUR"), Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(MainActivity.this, "You can't press this button", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    //This class gets the airport names and values from amadeus api
    //and fills the adapters
    class FetchLocationTask extends AsyncTask<String, Void, String>{

        public FetchLocationTask(){

        }

        @Override
        protected String doInBackground(String... params) {

            if (params.length==0 || !isOnline()){
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
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return airportJSONStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!isOnline()){
                Toast toast = Toast.makeText(MainActivity.this, "There is no internet connection", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(s == null){
                Toast toast = Toast.makeText(MainActivity.this, "There is no data to show", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
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


            AutoCompleteTextView view = (AutoCompleteTextView) getCurrentFocus();
            if (s.length()<3 || view.isPerformingCompletion()){
                return;
            }

            if (view == originTextView){
                tvController = 1;
            }
            else if(view == destinationTextView) {
                tvController = 2;
            }

            new FetchLocationTask().execute(s.toString());

        }
    }

    //This is the method that converts the response to string data
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

    //The method that fills the adapters
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


    //A method to check network connection
    public boolean isOnline() {
        ConnectivityManager cm =
          (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isPressable(){
        if (origin == null || destination == null || depDate == null){
            return false;
        }
        return true;
    }

}
