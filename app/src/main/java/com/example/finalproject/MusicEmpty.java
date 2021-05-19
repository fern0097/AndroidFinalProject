package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
import com.google.android.material.navigation.NavigationView;

public class MusicEmpty extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_empty);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        getSupportActionBar().setTitle("");

        /* For NavigationDrawer elements to be the display.
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

        Bundle dataToPass = getIntent().getExtras();

        MusicFragment musicFragment = new MusicFragment(); //add a Fragment
        musicFragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, musicFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MusicEmpty.this);
                alertDialogBuilder.setTitle(R.string.adriano_instruction);
                alertDialogBuilder.setMessage(R.string.adriano_description);
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