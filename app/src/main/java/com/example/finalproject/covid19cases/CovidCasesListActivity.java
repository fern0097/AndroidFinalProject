package com.example.finalproject.covid19cases;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.AudioDatabaseMainScreen;
import com.example.finalproject.R;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * CovidCasesListActivity is a class that display the Listview of Covid Cases that
 * have been search.
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
public class CovidCasesListActivity extends AppCompatActivity {

    /**
     * List to stores and fetched COVID-19 cases.
     */
    private List<CovidCase> covidCasesList = new ArrayList<>();

    /**
     * Field that refers to Adapter that handle list view.
     */
    private MyListAdapter myAdapter;

    /**
     * Field that refers to the loading progressbar.
     */
    private ProgressBar loading;

    /**
     * Field that refers to List view to display covid-19 cases.
     */
    private ListView listView;

    /**
     * Field that refers to database.
     */
    private SQLiteDatabase db;

    /**
     * Field for whether is a tablet device.
     */
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_cases_list);

        // This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        loading = findViewById(R.id.loading);

        listView = findViewById(R.id.listView);
        myAdapter = new MyListAdapter(CovidCasesListActivity.this, covidCasesList);
        listView.setAdapter(myAdapter);

        MyCovidOpener dbOpener = new MyCovidOpener(this);

        //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db = dbOpener.getWritableDatabase();

        // Tablet view by Id
        isTablet = findViewById(R.id.fragmentContainer) != null;

        // Long on click listener when press and hold will bing alert box with Covid cases displays cases and detail information
        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            // CovidCase is an object of cc thats get's the item position from the covidCasesList
            CovidCase cc = covidCasesList.get(position);

            /**
             * Alert Box which will display the country name, country code, province, latitude, longitude, number of cases,
             * status and the date.
             */
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CovidCasesListActivity.this);
            alertDialogBuilder.setTitle(cc.getCountryCode() + " - " + cc.getProvince());

            alertDialogBuilder.setMessage(
                    getString(R.string.wilker_country) + "  " + cc.getCountry() +
                            "\n" +
                            getString(R.string.wilker_country_code) + " " + cc.getCountryCode() +
                            "\n" +
                            getString(R.string.wilker_province) + "  " + cc.getProvince() +
                            "\n" +
                            getString(R.string.wilker_lat) + "  " + cc.getLat() +
                            "\n" +
                            getString(R.string.wilker_lon) + "  " + cc.getLon() +
                            "\n" +
                            getString(R.string.wilker_cases) + "  " + cc.getCases() +
                            "\n" +
                            getString(R.string.wilker_status) + "  " + cc.getStatus() +
                            "\n" +
                            getString(R.string.wilker_date) + "  " + cc.getDate()
            );

            // An alert box with a button saying "Save to Database".
            if (cc.getId() == 0) {
                alertDialogBuilder.setPositiveButton(R.string.wilker_save_to_db_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // After you press the button the data is add covid case to database.
                        ContentValues newRowValues = new ContentValues();
                        newRowValues.put(MyCovidOpener.COL_COUNTRY, cc.getCountry());
                        newRowValues.put(MyCovidOpener.COL_COUNTRYCODE, cc.getCountryCode());
                        newRowValues.put(MyCovidOpener.COL_PROVINCE, cc.getProvince());
                        newRowValues.put(MyCovidOpener.COL_LAT, cc.getLat());
                        newRowValues.put(MyCovidOpener.COL_LON, cc.getLon());
                        newRowValues.put(MyCovidOpener.COL_CASES, cc.getCases());
                        newRowValues.put(MyCovidOpener.COL_STATUS, cc.getStatus());
                        newRowValues.put(MyCovidOpener.COL_DATE, cc.getDate());

                        long newId = db.insert(MyCovidOpener.TABLE_NAME, null, newRowValues);
                        cc.setId(newId);

                        // Toast message confirming the data have been saved.
                        Toast.makeText(CovidCasesListActivity.this, R.string.wilker_saved_to_db, Toast.LENGTH_SHORT).show();

                    }
                });
            } else { // Once the data has been stores in the database the alert button will change into "Remove from database".
                alertDialogBuilder.setPositiveButton(R.string.wilker_remove_from_db_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Remove the data from the database.
                        db.delete(MyCovidOpener.TABLE_NAME, MyCovidOpener.COL_ID + "= ?", new String[]{Long.toString(cc.getId())});
                        cc.setId(0);

                        // Toast message confirming the data have been removed.
                        Toast.makeText(CovidCasesListActivity.this, R.string.wilker_removed_from_db, Toast.LENGTH_SHORT).show();

                    }
                });
            }

            alertDialogBuilder.show();
            return true;
        });

        // This listener for items being click in the list view.
        listView.setOnItemClickListener((parent, view, position, id) -> {

            CovidCase covidCase = covidCasesList.get(position);
            //Create a bundle to pass data to the CovidCaseFragment
            Bundle dataToPass = new Bundle();
            dataToPass.putLong("ID", covidCase.getId());
            dataToPass.putString("COUNTRY", covidCase.getCountry());
            dataToPass.putString("COUNTRYCODE", covidCase.getCountryCode());
            dataToPass.putString("PROVINCE", covidCase.getProvince());
            dataToPass.putString("LAT", covidCase.getLat());
            dataToPass.putString("LON", covidCase.getLon());
            dataToPass.putInt("CASES", covidCase.getCases());
            dataToPass.putString("STATUS", covidCase.getStatus());
            dataToPass.putString("DATE", covidCase.getDate());

            // if the Covid-19 Cases Application is runing into a Table
            if (isTablet) {
                CovidCaseFragment ccFragment = new CovidCaseFragment(); //add a DetailFragment
                ccFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, ccFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            } else {// else in Phone

                Intent nextActivity = new Intent(CovidCasesListActivity.this, CovidEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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
        // It a menu item with an alert box button "help" when is pressed it will prompts an instructions how to use the application
        switch (item.getItemId()) {
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CovidCasesListActivity.this);
                alertDialogBuilder.setTitle(R.string.wilker_instruction);
                alertDialogBuilder.setMessage(R.string.wilker_description);
                alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
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

            // when Covid Virus icon is press it will bring to Covid19CasesMainScreen menu screen.
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

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().hasExtra("COUNTRY_NAME")) {
            try {
                String countryName = URLEncoder.encode(getIntent().getStringExtra("COUNTRY_NAME"), "UTF-8");
                String fromDate = getIntent().getStringExtra("FROM_DATE");
                String toDate = getIntent().getStringExtra("TO_DATE");

                //Covid19CasesQuery is extends AsyncTask and execute website link https.
                new Covid19CasesQuery().execute("https://api.covid19api.com/country/" + countryName + "/status/confirmed/live?from=" + fromDate + "T00:00:00Z&to=" + toDate + "T00:00:00Z");
            } catch (Exception e) {
            }
        } else {
            // load from database
            loadDataFromDatabase();
        }
    }

    public void loadDataFromDatabase() {

        // Get all of the columns from MyCovidOpener.java for the covid cases details.
        String[] columns = {MyCovidOpener.COL_ID, MyCovidOpener.COL_COUNTRY, MyCovidOpener.COL_COUNTRYCODE, MyCovidOpener.COL_PROVINCE, MyCovidOpener.COL_LAT, MyCovidOpener.COL_LON, MyCovidOpener.COL_CASES, MyCovidOpener.COL_STATUS, MyCovidOpener.COL_DATE};
        // Query all the results from the database.
        Cursor results = db.query(false, MyCovidOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        /**
         * Now the results object has rows of results that match the query.
         * Find the column indices:
         */
        int idColIndex = results.getColumnIndex(MyCovidOpener.COL_ID);
        int countryIndex = results.getColumnIndex(MyCovidOpener.COL_COUNTRY);
        int countrycodeIndex = results.getColumnIndex(MyCovidOpener.COL_COUNTRYCODE);
        int provinceIndex = results.getColumnIndex(MyCovidOpener.COL_PROVINCE);
        int latIndex = results.getColumnIndex(MyCovidOpener.COL_LAT);
        int lonIndex = results.getColumnIndex(MyCovidOpener.COL_LON);
        int casesIndex = results.getColumnIndex(MyCovidOpener.COL_CASES);
        int statusIndex = results.getColumnIndex(MyCovidOpener.COL_STATUS);
        int dateIndex = results.getColumnIndex(MyCovidOpener.COL_DATE);

        // Iterate over the results, return true if there is a next item:
        covidCasesList.clear();
        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            String country = results.getString(countryIndex);
            String countrycode = results.getString(countrycodeIndex);
            String province = results.getString(provinceIndex);
            String lat = results.getString(latIndex);
            String lon = results.getString(lonIndex);
            int cases = results.getInt(casesIndex);
            String status = results.getString(statusIndex);
            String date = results.getString(dateIndex);

            CovidCase m = new CovidCase(id, country, countrycode, province, lat, lon, cases, status, date);

            //add the new Covid Cases details to the array list:
            covidCasesList.add(m);
        }

        myAdapter.notifyDataSetChanged();
        loading.setVisibility(View.INVISIBLE);

        if (covidCasesList.isEmpty()) {
            Snackbar.make(listView, R.string.wilker_cant_find_covid_cases, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(listView, R.string.wilker_covid19_cases_loaded, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * The Class Covid19CasesQuery is query data from the internet takes three generic parameters String, Integer and String.
     */
    class Covid19CasesQuery extends AsyncTask<String, Integer, String> {

        /**
         * This method will fetch the data from network in background.
         *
         * @param strings is taking argurments of strings
         * @return a null.
         */
        @Override
        protected String doInBackground(String... strings) { // var-args

            try {

                //create a URL object of what server to contact
                URL url = new URL(strings[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data
                InputStream response = urlConnection.getInputStream();

                /**
                 * JSON reading
                 * Build the entire string response
                 */
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                //result is the whole string
                String result = sb.toString();

                //convert string to JSON
                JSONArray covidCasesJsonArray = new JSONArray(result);

                /**
                 * This for loop will loop covidCasesJsonArray arrray and place the value names from the website
                 * place them into there place.
                 */
                covidCasesList.clear();
                for (int i = 0; i < covidCasesJsonArray.length(); i++) {

                    JSONObject covidCaseJsonObject = covidCasesJsonArray.getJSONObject(i);

                    String Country = covidCaseJsonObject.getString("Country");
                    String CountryCode = covidCaseJsonObject.getString("CountryCode");
                    String Province = covidCaseJsonObject.getString("Province");
                    String Lat = covidCaseJsonObject.getString("Lat");
                    String Lon = covidCaseJsonObject.getString("Lon");
                    int Cases = covidCaseJsonObject.getInt("Cases");
                    String Status = covidCaseJsonObject.getString("Status");
                    String Date = covidCaseJsonObject.getString("Date");

                    CovidCase cc = new CovidCase(Country, CountryCode, Province, Lat, Lon, Cases, Status, Date);
                    covidCasesList.add(cc);

                }

            } catch (Exception e) {

            }

            return null;
        }

        /**
         * After fetching data in doInBackground method, we can update the UI in this method
         *
         * @param s pass String into onPostExecute
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            myAdapter.notifyDataSetChanged();
            loading.setVisibility(View.INVISIBLE);

            if (covidCasesList.isEmpty()) {
                Snackbar.make(listView, R.string.wilker_cant_find_covid_cases, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(listView, R.string.wilker_covid19_cases_loaded, Snackbar.LENGTH_LONG).show();
            }

        }

    }
}