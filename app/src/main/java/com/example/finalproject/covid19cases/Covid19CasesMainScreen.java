package com.example.finalproject.covid19cases;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.AudioDatabaseMainScreen;
import com.example.finalproject.R;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;


/**
 * Covid19CasesMainScreen is a class that displays the EditText to let the country to be selected,
 * select todate and fromdate using a DatePicker and a button to load the save Data. It implements a NavigationItemSlectedListener
 * that contains a buttons to go back to the main page or directly take to the other projects.
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
public class Covid19CasesMainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Field that refers to the EditText object to an user input to enter country name or country code.
     */
    private EditText editTextCountry;

    /**
     * Field that refers to the TextView to show tile, selected start date and end date.
     */
    private TextView textViewStartDate, textViewEndDate, textViewTitle;

    /**
     * Field that refers to the button to go to the next screen with loaded data.
     */
    private Button buttonGetCovid19Cases;

    /**
     * Field that refers to the button that links to the saved Covid Cases.
     */
    private Button buttonGetSavedCovid19Cases;

    /**
     * Storing String that store fromDate and toDate.
     */
    private String fromDate, toDate;

    /**
     * Storing start date for validation.
     */
    private Date startDate;

    /**
     * Storing the sharedPreference values for country, start date and end date.
     */
    private SharedPreferences mySharedPreferences;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_cases_main_screen);

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

        // Gradient control background for animation speed
        View constraintLayout = findViewById(R.id.covid_case_gradiant_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        editTextCountry = findViewById(R.id.editTextCountry);
        textViewTitle = findViewById(R.id.covid19CaseTile);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        buttonGetCovid19Cases = findViewById(R.id.buttonGetCovid19Cases);
        buttonGetSavedCovid19Cases = findViewById(R.id.buttonGetSavedCovid19Cases);

        /**
         * The file is being created named "MyCovid19Cases" and the EditText Country is a String retrieving the value.
         */
        mySharedPreferences = getSharedPreferences("MyCovid19Cases", MODE_PRIVATE);
        editTextCountry.setText(mySharedPreferences.getString("KEY_COUNTRY", ""));

        /**
         * TextView that stores fromdate into "mySharedPreferences".
         */
        String startDateFromSHaredPref = mySharedPreferences.getString("KEY_START_DATE", "");
        if (!startDateFromSHaredPref.isEmpty()) {
            textViewStartDate.setText(startDateFromSHaredPref);
            fromDate = startDateFromSHaredPref;
        }
        /**
         * TextView that stores todate into "mySharedPreferences".
         */
        String endDateFromSHaredPref = mySharedPreferences.getString("KEY_END_DATE", "");
        if (!startDateFromSHaredPref.isEmpty()) {
            textViewEndDate.setText(endDateFromSHaredPref);
            toDate = endDateFromSHaredPref;
        }

        /**
         * Date picker variables and References: https://stackoverflow.com/a/39916826, https://stackoverflow.com/a/6452062
         */
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        /**
         * setOnClickerListener when user press the fromdate will invoke a date picker dialog method.
         */
        textViewStartDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Covid19CasesMainScreen.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    fromDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                    //Testing if the fromDate is been received with Log.d(Debug).
                    Log.d("Covid19CasesMainScreen", "fromDate: " + fromDate);
                    textViewStartDate.setText(getString(R.string.wilker_start_date) + " " + fromDate);

                    Calendar calendarStartDate = Calendar.getInstance();
                    calendarStartDate.set(year, month, dayOfMonth);
                    startDate = calendarStartDate.getTime();
                }
            }, mYear, mMonth, mDay);

            /**
             * Then show the DatePicker reference: https://stackoverflow.com/a/20971151
             */
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        textViewEndDate.setOnClickListener(v -> {

            /**
             * This Validated if the user has choose a fromdate and show a Toast asking to choose a fromDate.
             */
            if (fromDate == null) {
                Toast.makeText(Covid19CasesMainScreen.this, getString(R.string.wilker_please_select_start_date), Toast.LENGTH_SHORT).show();
                return;
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Covid19CasesMainScreen.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    toDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                    //Testing if the toDate is been received with Log.d(Debug).
                    Log.d("Covid19CasesMainScreen", "toDate: " + toDate);
                    textViewEndDate.setText(getString(R.string.wilker_end_date) + "  " + toDate);

                }
            }, mYear, mMonth, mDay);

            /**
             * Then show the DatePicker reference: https://stackoverflow.com/a/14590523
             */
            datePickerDialog.getDatePicker().setMinDate(startDate.getTime());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        buttonGetCovid19Cases.setOnClickListener(v -> {

            /**
             * This Validation user to select a country or country code, starting date and end date with a toast message.
             */
            if (editTextCountry.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.wilker_please_enter_country_name, Toast.LENGTH_SHORT).show();
                return;
            }
            if (fromDate == null) {
                Toast.makeText(this, R.string.wilker_please_enter_start_date, Toast.LENGTH_SHORT).show();
                return;
            }
            if (toDate == null) {
                Toast.makeText(this, R.string.wilker_please_enter_end_date, Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * Storing values(Country, start date and end date) to the database.
             */
            SharedPreferences.Editor edit = mySharedPreferences.edit(); // Editing the file
            edit.putString("KEY_COUNTRY", editTextCountry.getText().toString()); // Inserting the String in the file
            edit.putString("KEY_START_DATE", fromDate); // Inserting the String in the file
            edit.putString("KEY_END_DATE", toDate); // Inserting the String in the file
            edit.commit(); // Saving the String into the file

            Intent i = new Intent(Covid19CasesMainScreen.this, CovidCasesListActivity.class);
            i.putExtra("COUNTRY_NAME", editTextCountry.getText().toString());
            i.putExtra("TO_DATE", toDate);
            i.putExtra("FROM_DATE", fromDate);
            startActivity(i);
        });

        buttonGetSavedCovid19Cases.setOnClickListener(v -> {
            Intent i = new Intent(Covid19CasesMainScreen.this, CovidCasesListActivity.class);
            startActivity(i);
        });
    }

    /**
     * onNavigationItemSelected is a method implements items on a navigation.
     *
     * @param item is passsing the items into the navigation.
     * @return false
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Covid19CasesMainScreen.this);
                alertDialogBuilder.setTitle(R.string.wilker_instruction);
                alertDialogBuilder.setMessage(R.string.wilker_description);
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
