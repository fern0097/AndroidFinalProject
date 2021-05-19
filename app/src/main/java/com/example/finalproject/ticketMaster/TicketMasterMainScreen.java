package com.example.finalproject.ticketMaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.AudioDatabaseMainScreen;
import com.example.finalproject.R;
import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 *  Title Screen for the TicketMaster TicketMasterEvent Search App
 *  API Key: 9W0p2gXvprnP6XZpWSBcIKyorBPCyOH3
 *  Link: https://app.ticketmaster.com/discovery/v2/events.json?apikey=XXXXXX&city=YYYYY&radius=100
 *
 *  @author Rodrigo Tavares
 */
public class TicketMasterMainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     *  Holds the list of Events to be listed on the ListView
     */
    private ArrayList<TicketMasterEvent> events = new ArrayList<>();

    /**
     *  Holds the list of Favorite Events
     */
    private ArrayList<TicketMasterEvent> favEvents = new ArrayList<>();


    /**
     *  List of constants used as keys for each TicketMasterEvent
     */
    public static final String KEY_NAME     = "KEY_NAME";
    public static final String KEY_DATE     = "KEY_DATE";
    public static final String KEY_CURRENCY = "KEY_CURRENCY";
    public static final String KEY_LO_PRICE = "KEY_LO_PRICE";
    public static final String KEY_HI_PRICE = "KEY_HI_PRICE";
    public static final String KEY_URL      = "KEY_URL";
    public static final String KEY_IMAGE    = "KEY_IMAGE";
    public static final String KEY_UNIQUE_ID    = "KEY_UNIQUE_ID";


    /**
     *  Used to save the last searched city name
     */
    SharedPreferences prefs = null;

    /**
     *  Handles access to the SQLite DB
     */
    private static  SQLiteDatabase db;
    protected static SQLiteDatabase getDb() { return db; }

    private String city;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tm_main);

        /**
         * This gets the toolbar from the layout.
         */
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("");

        /**
         * For NavigationDrawer elements to be the display.
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.wilker_open, R.string.wilker_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TicketMasterOpener dbOpener = new TicketMasterOpener(this);
        db = dbOpener.getWritableDatabase();

//        boolean isPhone = (findViewById(R.id.frameLayout) == null) TODO configure

        // Configures the EditText field (checks sharedPrefs for saved city)
        EditText typedCity = findViewById(R.id.tm_enterCity_editText);
        prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        city = prefs.getString("City", "");
        typedCity.setText(city);

        // Populate the Radius Spinner (based on Android documentation example)
        Spinner spinner = (Spinner) findViewById(R.id.tm_radiusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tm_radius_values,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initializing the ListView
        ListView eventList = (ListView) findViewById(R.id.tm_listView);
        EventListAdapter eventListAdapter = new EventListAdapter();
        eventList.setAdapter(eventListAdapter);

        // Initializes the progressBar
        pb = findViewById(R.id.tm_progressBar);

        // Configures the Search button
        Button searchBtn = findViewById(R.id.tm_searchButton);
        searchBtn.setOnClickListener( click -> {
            // Gets the text from the input
            city = typedCity.getText().toString();
            saveSharedPrefs(city);
            if (city.isEmpty()) {
                Snackbar.make(eventList, "Please enter a city name.", Snackbar.LENGTH_LONG).show(); //TODO translate
            } else {

                EventQuery query = new EventQuery();
                String queryStr =
                        "https://app.ticketmaster.com/discovery/v2/events.json?apikey=9W0p2gXvprnP6XZpWSBcIKyorBPCyOH3&city=" +
                        city + "&radius=" + spinner.getSelectedItem().toString() + "&unit=km";
                try {
                    events = query.execute(queryStr).get();
                    eventListAdapter.notifyDataSetChanged();
                    if (eventListAdapter.getCount() != 0) {
                        Toast.makeText(this, "Found " + eventListAdapter.getCount() + " events.", Toast.LENGTH_SHORT).show(); //TODO translate
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e("TicketMasterMainScreen", "onCreate: ", e);
                }
            }
        });

        // Handle Favorites button
        Button favButton = findViewById(R.id.tm_savedButton);

        favButton.setOnClickListener( click -> {
            loadFavorites();

            if (favEvents.isEmpty()) {
                Snackbar.make(eventList, "Favorites is empty.", Snackbar.LENGTH_LONG).show(); //TODO translate
            } else {
                events.clear();
                events.addAll(favEvents);
                eventListAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Found " + eventListAdapter.getCount() + " events.", Toast.LENGTH_SHORT).show(); //TODO translate
            }

        });

        // Deals with clicking on the List
        // TODO rework for tablets
        eventList.setOnItemClickListener( (list, view, position, id) -> {
//            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();

            dataToPass.putString(KEY_NAME, ((TicketMasterEvent)eventListAdapter.getItem(position)).getName());

            Date date = ((TicketMasterEvent)eventListAdapter.getItem(position)).getStartingDate();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(date);
            dataToPass.putString(KEY_DATE, (dateStr));
            dataToPass.putString(KEY_CURRENCY, ((TicketMasterEvent)eventListAdapter.getItem(position)).getCurrency());
            dataToPass.putDouble(KEY_LO_PRICE, ((TicketMasterEvent)eventListAdapter.getItem(position)).getLowestPrice());
            dataToPass.putDouble(KEY_HI_PRICE, ((TicketMasterEvent)eventListAdapter.getItem(position)).getHighestPrice());
            dataToPass.putString(KEY_URL, ((TicketMasterEvent)eventListAdapter.getItem(position)).getUrl().toString());
            dataToPass.putString(KEY_IMAGE, ((TicketMasterEvent)eventListAdapter.getItem(position)).getImageUrl().toString());
            dataToPass.putString(KEY_UNIQUE_ID, ((TicketMasterEvent)eventListAdapter.getItem(position)).getId());

//            if (isPhone) {
                Intent nextActivity = new Intent(TicketMasterMainScreen.this, TicketMasterDetailsScreen.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
//            }
//            else { // isTablet
//                dFragment = new DetailsFragment(); //add a DetailFragment
//                dFragment.setArguments( dataToPass ); //pass it a bundle for information
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
//                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
//            }

        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // Take you back to the main screen of the application.
            case R.id.goToMainScreen:
                finish();
                break;

            // When TicketMaster event search icon is press it will bring to TicketMasterMainScreen main screen.
            case R.id.ticketApp:
                Intent ticketScreen = new Intent(this, TicketMasterMainScreen.class);
                startActivity(ticketScreen);
                finish();
                break;

            // When Recipe Search page icon is press it will bring to RecipesearchMainScreen main screen.
            case R.id.recipeApp:
                Intent rpScreen = new Intent(this, RecipesearchMainScreen.class);
                startActivity(rpScreen);
                finish();
                break;

            // When Covid-19 Cases icon is press it will bring to Covid19CasesMainScreen main screen.
            case R.id.covidApp:
                Intent cc19Screen = new Intent(this, Covid19CasesMainScreen.class);
                startActivity(cc19Screen);
                finish();
                break;

            // When The Audio Database icon is press it will bring to AudioDatabaseMainScreen main screen.
            case R.id.audioApp:
                Intent adScreen = new Intent(this, AudioDatabaseMainScreen.class);
                startActivity(adScreen);
                finish();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }


    /**
     *  Adapter for the List View used in the Ticket Master event search app
     */
    class EventListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) { return position; } // db id not used here

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.row_tm_event, parent, false);
            TextView eventText = newView.findViewById(R.id.tm_row);
            eventText.setText(((TicketMasterEvent)getItem(position)).getName());
            return newView;
        }
    }

    /**
     *  Used to save the last searched city name
     */
    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("City", stringToSave);
        editor.apply();
    }

    /**
     *  AsyncTask used to query the JSON information from TicketMaster API
     */
    class EventQuery extends AsyncTask<String, Integer, ArrayList<TicketMasterEvent>> {

        /**
         *  Temporary list of events to be returned later on
         */
        private ArrayList<TicketMasterEvent> events;

        @Override
        protected ArrayList<TicketMasterEvent> doInBackground(String... strings) {
            publishProgress(0);
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                events = new ArrayList<>();
                JSONObject eventReport = new JSONObject(result);
                JSONArray eventArray;
                try {
                    eventArray = eventReport.getJSONObject("_embedded").getJSONArray("events");
                } catch (JSONException e) {
                    Snackbar.make(findViewById(R.id.tm_listView), "No events found for that city.", Snackbar.LENGTH_LONG).show();
                    return events;
                }
                int numEvents = Integer.parseInt(eventReport.getJSONObject("page").getString("size")); // TODO: 1 page only. get multiple pages
                for (int i=0; i<eventArray.length(); i++) {
                    publishProgress((int)Math.round(100.0*i/numEvents));
                    try {
                        JSONObject event = eventArray.getJSONObject(i);
                        String name = event.getString("name");
                        String id = event.getString("id");
                        String dateStr = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                        Date date=new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(dateStr);

                        // considers only "standard" prices, keeps default values if not found
                        String priceCurrency = "?";
                        double priceMin = 0.0;
                        double priceMax = 0.0;

                        try {
                            JSONArray priceArray = event.getJSONArray("priceRanges");
                            for (int j=0; j<priceArray.length(); j++) {
                                JSONObject priceRange = priceArray.getJSONObject(j);
                                if (priceRange.getString("type").equals("standard")){
                                    priceCurrency = priceRange.getString("currency");
                                    priceMin = priceRange.getDouble("min");
                                    priceMax = priceRange.getDouble("max");
                                }
                            }
                        } catch (JSONException e) {
                            priceCurrency = "N/A";
                        }

                        URL eventUrl = new URL(event.getString("url"));

                        JSONArray imageArray = event.getJSONArray("images");

                        // uses the first image on the list
                        URL imageUrl = new URL(imageArray.getJSONObject(0).getString("url"));

                        events.add(new TicketMasterEvent(
                                id,
                                name,
                                date,
                                priceCurrency,
                                priceMin,
                                priceMax,
                                eventUrl,
                                imageUrl
                        ));

                    }
                    catch (JSONException e) {
                        Log.e("TicketMasterMainScreen", "doInBackground: ", e);
                        return events;
                    }
                }
            } catch (Exception e) {
                Log.e("TicketMasterMainScreen", "doInBackground: ", e);
                return events;
            }

            return events;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<TicketMasterEvent> events) {
            pb.setVisibility(View.INVISIBLE);
        }
    }

    private void loadFavorites() {
        favEvents.clear();
        String [] columns = {
                TicketMasterOpener.COL_ID,
                TicketMasterOpener.COL_NAME,
                TicketMasterOpener.COL_DATE,
                TicketMasterOpener.COL_CURR,
                TicketMasterOpener.COL_MIN,
                TicketMasterOpener.COL_MAX,
                TicketMasterOpener.COL_URL,
                TicketMasterOpener.COL_IMG_URL,
                TicketMasterOpener.COL_UNIQUE_ID
        };

        Cursor results = db.query(false, TicketMasterOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int uniqueIdColIndex = results.getColumnIndex(TicketMasterOpener.COL_UNIQUE_ID);
        int nameIndex = results.getColumnIndex(TicketMasterOpener.COL_NAME);
        int dateIndex = results.getColumnIndex(TicketMasterOpener.COL_DATE);
        int currIndex = results.getColumnIndex(TicketMasterOpener.COL_CURR);
        int minIndex = results.getColumnIndex(TicketMasterOpener.COL_MIN);
        int maxIndex = results.getColumnIndex(TicketMasterOpener.COL_MAX);
        int urlIndex = results.getColumnIndex(TicketMasterOpener.COL_URL);
        int imgUrlIndex = results.getColumnIndex(TicketMasterOpener.COL_IMG_URL);

        results.moveToPosition(-1);
        while(results.moveToNext())
        {
            String id = results.getString(uniqueIdColIndex);
            String name = results.getString(nameIndex);

            String dateStr = results.getString(dateIndex);
            Date date= null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(dateStr);
            } catch (ParseException ignored) {}
            String priceCurrency = results.getString(currIndex);

            long priceMin = results.getLong(minIndex);
            long priceMax = results.getLong(maxIndex);

            String imageUrlStr = results.getString(imgUrlIndex);
            String eventUrlStr = results.getString(urlIndex);

            URL eventUrl = null;
            URL imageUrl = null;
            try {
                eventUrl = new URL(eventUrlStr);
                imageUrl = new URL(imageUrlStr);
            } catch (MalformedURLException ignored) {}

            favEvents.add(new TicketMasterEvent(
                    id,
                    name,
                    date,
                    priceCurrency,
                    priceMin,
                    priceMax,
                    eventUrl,
                    imageUrl
            ));
        }
        results.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu items for use in the action bar.
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covid_toolbar_menu, menu);

        return true;
    }

    /**
     * onOptionsItemSelected is a method that implements items icons into the toolbar
     *
     * @param item is the items object that is going to be pass through the toolbar
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help:

                // This is the builder pattern, just call many functions on the same object:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TicketMasterMainScreen.this);
                alertDialogBuilder.setTitle(R.string.tm_instruction);
                alertDialogBuilder.setMessage(R.string.tm_description);
                alertDialogBuilder.setPositiveButton(android.R.string.ok, null);

                // then show the dialog.
                alertDialogBuilder.show();
                break;

            // when Ticketmaster event search icon is press it will bring to Covid19CasesMainScreen menu screen.
            case R.id.ticketApp:
                Intent ticketScreen = new Intent(this, TicketMasterMainScreen.class);
                startActivity(ticketScreen);
                finish();
                break;

            // When Recipe Search page icon is press it will bring to RecipesearchMainScreen main screen.
            case R.id.recipeApp:
                Intent rpScreen = new Intent(this, RecipesearchMainScreen.class);
                startActivity(rpScreen);
                finish();
                break;

            // When Covid-19 Cases icon is press it will bring to Covid19CasesMainScreen main screen.
            case R.id.covidApp:
                Intent cc19Screen = new Intent(this, Covid19CasesMainScreen.class);
                startActivity(cc19Screen);
                finish();
                break;

            // When The Audio Database icon is press it will bring to AudioDatabaseMainScreen main screen.
            case R.id.audioApp:
                Intent adScreen = new Intent(this, AudioDatabaseMainScreen.class);
                startActivity(adScreen);
                finish();
                break;
        }

        return true;
    }
}