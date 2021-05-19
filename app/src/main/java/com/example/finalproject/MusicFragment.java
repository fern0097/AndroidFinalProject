package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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


public class MusicFragment extends Fragment {

    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    TextView albumId;
    Long albumIdString;
    String albumIdName;
    private ArrayList<Song> songs = new ArrayList<>();
    ArrayAdapter<Song> listAdapter2;
    private ProgressBar progressBar;
    private String googleSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();

        View result = inflater.inflate(R.layout.fragment_music, container, false);




        progressBar=result.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        //Intent fromMain = getIntent();
        albumIdString = dataFromActivity.getLong(AudioDatabaseAlbumActivity.ALBUM_ID);
        albumIdName = dataFromActivity.getString(AudioDatabaseAlbumActivity.ALBUM_NAME);
        albumId=result.findViewById(R.id.textView);
        albumId.setText(getString(R.string.Album_name) + albumIdName);

        ListView songsList = (ListView) result.findViewById(R.id.musics_list);

        listAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);

        songsList.setAdapter(listAdapter2);


        String searchURL = "https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumIdString;
        MusicFragment.SongQuery search = new MusicFragment.SongQuery();
        search.execute(searchURL);

        songsList.setOnItemClickListener((list, item, position, id) -> {
            googleSearch = ((songs.get(position).getStrArtist().replaceAll(" ", "+")) + "+" + (songs.get(position).getStrTrack().replaceAll(" ", "+")));
            //Toast.makeText(getApplicationContext(),getString(R.string.Launching_Google) + googleSearch, Toast.LENGTH_LONG).show();

            //Snackbar snackbar = Snackbar.make(getCurrentFocus(), getString(R.string.Launching_Google) + googleSearch, Snackbar.LENGTH_LONG);
            //snackbar.show();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+googleSearch));
            startActivity(browserIntent);

        });

        listAdapter2.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return result;

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


                    }

                } else {
                    //runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Can't find the artist, go back and try again.", Toast.LENGTH_LONG).show());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}