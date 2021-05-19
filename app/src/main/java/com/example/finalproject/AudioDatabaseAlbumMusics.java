package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AudioDatabaseAlbumMusics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView albumId;
    String albumIdString, albumIdName;
    private ArrayList<Song> songs = new ArrayList<>();
    ArrayAdapter<Song> listAdapter2;
    private ProgressBar progressBar;
    private String googleSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_database_album_musics);

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



        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Intent fromMain = getIntent();
        albumIdString = fromMain.getStringExtra("AlbumId");
        albumIdName = fromMain.getStringExtra("AlbumName");
        albumId=findViewById(R.id.textView);
        albumId.setText(getString(R.string.Album_name) + albumIdName);

        ListView songsList = findViewById(R.id.musics_list);

        listAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, songs);
        //MyAdapter listAdapter2 = new MyAdapter(); //to be used with layout inflater
        songsList.setAdapter(listAdapter2);

        String searchURL = "https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumIdString;
        SongQuery search = new SongQuery();
        search.execute(searchURL);

        songsList.setOnItemClickListener((list, item, position, id) -> {
            googleSearch = ((songs.get(position).getStrArtist().replaceAll(" ", "+")) + "+" + (songs.get(position).getStrTrack().replaceAll(" ", "+")));
            //Toast.makeText(getApplicationContext(),getString(R.string.Launching_Google) + googleSearch, Toast.LENGTH_LONG).show();

            Snackbar snackbar = Snackbar.make(getCurrentFocus(), getString(R.string.Launching_Google) + googleSearch, Snackbar.LENGTH_LONG);
            snackbar.show();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+googleSearch));
            startActivity(browserIntent);

        });

    }


    public class Song {
        private Long idTrack;
        private String strTrack;
        private String strArtist;

        public Song(Long idTrack, String strTrack, String strArtist) {
            this.idTrack = idTrack;
            this.strTrack = strTrack;
            this.strArtist = strArtist;
        }

        public String getStrTrack() {return strTrack;}

        public void setStrTrack(String strTrack) { this.strTrack = strTrack; }

        public String getStrArtist() {return strArtist;}

        public void setStrArtist(String strArtist) { this.strArtist = strArtist; }

        public Long getIdTrack() {return idTrack;}


        @Override
        public String toString() {
            return getString(R.string.Song_Name) + strTrack + getString(R.string.Artist_Name) + strArtist + getString(R.string.Song_Id) + idTrack;
        }
    }


    private class SongQuery extends AsyncTask<String, Integer, String> {

        String strTrack, strArtist;
        Long idTrack;


        @Override
        protected String doInBackground(String... args) {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8"); //response is data from the server

                //JSON Part
                URL urlJson = new URL("https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumIdString);
                HttpURLConnection urlConnectionJson = (HttpURLConnection) urlJson.openConnection();
                InputStream responseJson = urlConnectionJson.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseJson, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                String result = sb.toString();


                if (result != null) {

                    JSONObject idReport = new JSONObject(result);

                    // Getting JSON Array node
                    JSONArray songsArray = idReport.getJSONArray("track");

                    for (int i = 0; i < songsArray.length(); i++) {

                        JSONObject a = songsArray.getJSONObject(i);
                        idTrack = a.getLong("idTrack");
                        strTrack = a.getString("strTrack");
                        strArtist = a.getString("strArtist");
                        Log.e("MainActivity", "The album ID is: " + strTrack);
                        Log.e("MainActivity", "The album name is: " + strArtist);

                        Song song = new Song(idTrack, strTrack, strArtist);
                        songs.add(song);
                        //listAdapter2.notifyDataSetChanged(); //to be used with layout inflater

                    }

                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                            "Can't find the artist, go back and try again.",
                            Toast.LENGTH_LONG).show());
                }

            } catch (Exception e) {

            }
            return null;

        }

        public void onProgressUpdate(Integer ... args) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }
        //Type3
        @Override
        public void onPostExecute(String fromDoInBackground) {

            listAdapter2.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

        }
    }


    // inner class extends BaseAdapter
    /*public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {return songs.size();}

        @Override
        public Song getItem(int position) {return songs.get(position);}

        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View newView;
            Song songPos = getItem(position);


            newView = getLayoutInflater().inflate(R.layout.layout_songs, parent, false);


            TextView text = newView.findViewById(R.id.display_song);
            text.setText(songPos.getStrTrack());

            return newView;
        }
    }*/

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
//            case R.id.recipeApp:
//                Intent rpScreen = new Intent(this, RecipesearchMainScreen.class);
//                startActivity(rpScreen);
//                finish();
//                break;

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioDatabaseAlbumMusics.this);
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
