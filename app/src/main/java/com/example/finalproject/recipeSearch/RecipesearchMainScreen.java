package com.example.finalproject.recipeSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.AudioDatabaseMainScreen;
import com.example.finalproject.R;
import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;
import com.google.android.material.navigation.NavigationView;


public class RecipesearchMainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView typeSearch;
    private Button searchBtn;
    private Button saveBtn;
    private EditText editText;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipesearchmainscreen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editText = findViewById(R.id.editText);
        searchBtn = findViewById(R.id.recipeSearchbtn);
        saveBtn = findViewById(R.id.recipeSavebtn);
        typeSearch = findViewById(R.id.typeSearch);

        searchBtn.setOnClickListener(view -> {
            loadData();
            Intent intent = new Intent(RecipesearchMainScreen.this, RecipeSearchActivity.class);
            intent.putExtra("title", editText.getText().toString());
            startActivity(intent);
        });

        saveBtn.setOnClickListener(view -> {
            saveData();
        });

        loadData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, editText.getText().toString());
        editor.commit();


        Toast.makeText(this, R.string.dataSaved_ariane, Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
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
            //what to do when the menu item is selected:
            case R.id.cookbook_Recipe:
                Intent cookbook = new Intent(RecipesearchMainScreen.this, RecipesearchMainScreen.class);
                startActivity(cookbook);
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
                Intent ticket = new Intent(RecipesearchMainScreen.this, TicketMasterMainScreen.class);
                startActivity(ticket);
                finish();
                break;


            case R.id.goToAudioDB:
                Intent audio = new Intent(RecipesearchMainScreen.this, AudioDatabaseMainScreen.class);
                startActivity(audio);
                finish();
                break;

            case R.id.goToCovidCases:
                Intent covid = new Intent(RecipesearchMainScreen.this, Covid19CasesMainScreen.class);
                startActivity(covid);
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cookbook_Recipe:
                Intent intent = new Intent(RecipesearchMainScreen.this, RecipesearchMainScreen.class);
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
                Intent ticket = new Intent(RecipesearchMainScreen.this, TicketMasterMainScreen.class);
                startActivity(ticket);
                finish();
                break;


            case R.id.goToAudioDB:
                Intent audio = new Intent(RecipesearchMainScreen.this, AudioDatabaseMainScreen.class);
                startActivity(audio);
                finish();
                break;

            case R.id.goToCovidCases:
                Intent covid = new Intent(RecipesearchMainScreen.this, Covid19CasesMainScreen.class);
                startActivity(covid);
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }
}
