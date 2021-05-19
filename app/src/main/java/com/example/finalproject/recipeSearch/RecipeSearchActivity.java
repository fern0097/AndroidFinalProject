package com.example.finalproject.recipeSearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.AudioDatabaseMainScreen;
import com.example.finalproject.R;
import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
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
import java.util.List;

public class RecipeSearchActivity extends AppCompatActivity {
    private List<RecipeSearch> recipeSearchArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView recipeListView;
    private ImageButton btnFavorite;
    private String recipeTitle;
    private String recipeContent;
    private String recipeURL;
    private String thumbnail;
    private ArrayAdapter<RecipeSearch> recipeAdapter;

    public SharedPreferences sp;
    private String recipesFavName;
    private String recipesFavIng;
    private String recipesFavURL;
    private String recipesFavThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipesearchactivity);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnFavorite = findViewById(R.id.btnFavorite);
        progressBar = findViewById(R.id.progressBar);
        recipeListView = findViewById(R.id.recipeListView);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        TextView recipeName = (TextView) findViewById(R.id.recipeTitle);
        recipeName.setText(getIntent().getStringExtra(recipeTitle));

        TextView recipeIngredient = (TextView) findViewById(R.id.recipeContent);
        recipeIngredient.setText(getIntent().getStringExtra(recipeContent));

        recipeAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeSearchArrayList);
        recipeListView.setAdapter(recipeAdapter);

        recipeTitle = getIntent().getStringExtra("title");

        recipeURL = "http://www.recipepuppy.com/api/?i=&q=" + recipeTitle;

        loadFromDatabase();

        //get shared prefs for listview
        sp = getSharedPreferences("recipes", Context.MODE_PRIVATE);

        recipeListView.setOnItemLongClickListener((p, b, pos, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.saveRecipe)
                    .setMessage(R.string.ifRecipe)
                    .setPositiveButton(R.string.yesRecipe, (click, arg) -> {
                        recipesFavName = recipeAdapter.getItem(pos).getRecipe();
                        recipesFavIng = recipeAdapter.getItem(pos).getIngredient();
                        recipesFavURL = recipeAdapter.getItem(pos).getHref();
                        recipesFavThumbnail = recipeAdapter.getItem(pos).getThumbnail();

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("recipes", recipesFavName);
                        editor.putString("ingredient", recipesFavIng);
                        editor.putString("href", recipesFavURL);
                        editor.putString("thumbnail", recipesFavThumbnail);


                        editor.commit();

                        Toast.makeText(this, R.string.yesRecipe, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.noRecipe, (click, arg) -> {
                        Toast.makeText(this, R.string.noRecipe, Toast.LENGTH_SHORT).show();
                        Intent no = new Intent(RecipeSearchActivity.this, RecipesearchMainScreen.class);
                        startActivity(no);
                    })
                    .setNeutralButton("", (click, arg) -> {
                    })
                    .setView(getLayoutInflater().inflate(R.layout.recipebuilder, null))
                    .create().show();

            return true;
        });

        btnFavorite.setOnClickListener((v) -> {
            Intent openEmpty = new Intent(RecipeSearchActivity.this, RecipeEmpty.class);
            openEmpty.putExtra("recipes", recipesFavName);
            openEmpty.putExtra("ingredient", recipesFavIng);
            openEmpty.putExtra("href", recipesFavURL);
            openEmpty.putExtra("thumbnail", recipesFavThumbnail);
            startActivity(openEmpty);

            //Snackbar.make(v, "I supposed to show your favourite recipes but I failed to do so.", Snackbar.LENGTH_LONG).show();
        });

        recipeListView.setOnItemClickListener((p, b, pos, id) -> {
            String url = recipeAdapter.getItem(pos).getHref();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }

    private void loadFromDatabase() {
        RecipeQuery recipeQuery = new RecipeQuery();
        recipeQuery.execute(recipeURL);
    }

    public class RecipeSearch {
        private String recipe;
        private String ingredient;
        private String href;
        private String thumbnail;
        private boolean value;
        private long id;

        public RecipeSearch(String recipe, String ingredient, String href, String thumbnail) {
            this.recipe = recipe;
            this.ingredient = ingredient;
            this.href = href;
            this.thumbnail = thumbnail;
        }

        public RecipeSearch(long id, String recipe, String ingredient, String href, String thumbnail) {
            this.id = id;
            this.recipe = recipe;
            this.ingredient = ingredient;
            this.href = href;
            this.thumbnail = thumbnail;
        }

        public String getRecipe() {
            return recipe;
        }

        public void setRecipe(String recipe) {
            this.recipe = recipe;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        @Override
        public String toString() {
            return "Title: " + recipe + "\nIngredients: " + ingredient + "\nURL: " + href + "\nThumbnail: " + thumbnail;
        }
    }

    private class RecipeQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {

            try {

                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");


                URL urlJson = new URL("http://www.recipepuppy.com/api/?i=&q=" + recipeTitle);
                HttpURLConnection urlConnectionJson = (HttpURLConnection) urlJson.openConnection();
                InputStream responseJson = urlConnectionJson.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseJson, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();


                if (result != null) {

                    JSONObject id = new JSONObject(result);

                    JSONArray recipeArray = id.getJSONArray("results");

                    for (int i = 0; i < recipeArray.length(); i++) {

                        JSONObject a = recipeArray.getJSONObject(i);
                        recipeTitle = a.getString("title");
                        recipeContent = a.getString("ingredients");
                        recipeURL = a.getString("href");
                        thumbnail = a.getString("thumbnail");

                        RecipeSearch recipeSearch = new RecipeSearch(recipeTitle, recipeContent, recipeURL, thumbnail);
                        recipeSearchArrayList.add(recipeSearch);

                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                            "Invalid",
                            Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
            }
            return null;
        }

        public void onProgressUpdate(Integer... args) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }

        @Override
        public void onPostExecute(String fromDoInBackground) {
            recipeAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

        }
    }
    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.cookbook_Recipe:
                Toast.makeText(this, R.string.youclickedMain, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecipeSearchActivity.this, RecipesearchMainScreen.class);
                startActivity(intent);
                finish();
                break;
            case R.id.helpRecipe:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.helpRecipe)
                        .setMessage(R.string.helpInfoMessage)
                        .setPositiveButton(R.string.okRecipe, (click, arg) -> {
                        })
                        .setNegativeButton("", (click, arg) -> {
                        })
                        .setNeutralButton("", (click, arg) -> {
                        })
                        .setView(getLayoutInflater().inflate(R.layout.recipebuilder, null))
                        .create().show();
                break;

            case R.id.goToTicketMaster:
                Toast.makeText(this, R.string.youclickedTicketMaster, Toast.LENGTH_SHORT).show();
                Intent ticket = new Intent(RecipeSearchActivity.this, TicketMasterMainScreen.class);
                startActivity(ticket);
                finish();
                break;


            case R.id.goToAudioDB:
                Toast.makeText(this, R.string.youclickedAudioDB, Toast.LENGTH_SHORT).show();
                Intent audio = new Intent(RecipeSearchActivity.this, AudioDatabaseMainScreen.class);
                startActivity(audio);
                finish();
                break;

            case R.id.goToCovidCases:
                Toast.makeText(this, R.string.youclickedCovidCases, Toast.LENGTH_SHORT).show();
                Intent covid = new Intent(RecipeSearchActivity.this, Covid19CasesMainScreen.class);
                startActivity(covid);
                finish();
                break;
        }
        return true;
    }
}
