package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
import com.google.android.material.navigation.NavigationView;

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


public class AudioDatabaseAlbumActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private ArrayList<Album> albums = new ArrayList<>();
    private ProgressBar progressBar;
    String artistName;
    private String musicIdSearch;
    private String musicAlbumNameSearch;
    ArrayAdapter<Album> listAdapter;
    private SharedPreferences myAlbumIdPreferences;

    public static final String ALBUM_ID = "ID";
    public static final String ALBUM_NAME = "NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_database_album);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

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
        artistName = fromMain.getStringExtra("Artist").replaceAll(" ", "%20");
        TextView emailEditText = (TextView) findViewById(R.id.artist_name);
        emailEditText.setText(getIntent().getStringExtra("Artist"));

        ListView albumList = findViewById(R.id.album_list);
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, albums);
        albumList.setAdapter(listAdapter);

        String searchURL = "https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + artistName;
        AlbumQuery search = new AlbumQuery();
        search.execute(searchURL);



        albumList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment

            MusicFragment musicFragment = new MusicFragment(); //add a Fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putLong(ALBUM_ID, albums.get(position).getIdAlbum());
            dataToPass.putString(ALBUM_NAME, albums.get(position).getStrAlbum());

            musicIdSearch = String.valueOf(albums.get(position).getIdAlbum());
            musicAlbumNameSearch = albums.get(position).getStrAlbum();
            myAlbumIdPreferences = getSharedPreferences("artistSearch", MODE_PRIVATE);

            Toast.makeText(getApplicationContext(),getString(R.string.Searching_for_album)+ musicAlbumNameSearch, Toast.LENGTH_LONG).show();


            /*Intent albumMusicList = new Intent(AudioDatabaseAlbumActivity.this, AudioDatabaseAlbumMusics.class);
            albumMusicList.putExtra("AlbumId", musicIdSearch );
            albumMusicList.putExtra("AlbumName", musicAlbumNameSearch);
            //albumMusicList.putExtras(dataToPass); //send data to next activity
            startActivity(albumMusicList);*/

            if(isTablet)
            {

                musicFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, musicFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in MusicFragment
            }
            else //isPhone
            {
                Intent albumMusicList = new Intent(AudioDatabaseAlbumActivity.this, MusicEmpty.class);
                //Intent albumMusicList = new Intent(AudioDatabaseAlbumActivity.this, AudioDatabaseAlbumMusics.class);
                albumMusicList.putExtras(dataToPass);
                //albumMusicList.putExtra("AlbumId", musicIdSearch );
                //albumMusicList.putExtra("AlbumName", musicAlbumNameSearch);
                //albumMusicList.putExtras(dataToPass); //send data to next activity
                startActivity(albumMusicList);


                /*Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition*/
            }
        });


    }


    public class Album {
        private long idAlbum;
        private String strAlbum;

        public Album(long idAlbum, String strAlbum) {
            this.idAlbum = idAlbum;
            this.strAlbum = strAlbum;
        }

        public String getStrAlbum() {return strAlbum;}

        public void setStrAlbum(String strAlbum) { this.strAlbum = strAlbum; }

        public long getIdAlbum() {return idAlbum;}

        public void setIdAlbum(long idAlbum) { this.idAlbum = idAlbum; }


        @Override
        public String toString() {
            return getString(R.string.Album_Id) + idAlbum + getString(R.string.Album_Name) + strAlbum;
        }
    }

    private class AlbumQuery extends AsyncTask<String, Integer, String> {

        String albumID, albumName;


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
                URL urlJson = new URL("https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + artistName);
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
                    JSONArray albumsArray = idReport.getJSONArray("album");

                    for (int i = 0; i < albumsArray.length(); i++) {

                        JSONObject a = albumsArray.getJSONObject(i);
                        albumID = a.getString("idAlbum");
                        albumName = a.getString("strAlbum");
                        Log.e("MainActivity", "The album ID is: " + albumID);
                        Log.e("MainActivity", "The album name is: " + albumName);

                        Album album = new Album(Long.parseLong(albumID), albumName);
                        albums.add(album);

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

            listAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

        }
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioDatabaseAlbumActivity.this);
                alertDialogBuilder.setTitle(R.string.wilker_instruction);
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

        return true;
    }

}
