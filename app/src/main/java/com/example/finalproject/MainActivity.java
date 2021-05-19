package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.covid19cases.Covid19CasesMainScreen;
import com.example.finalproject.recipeSearch.RecipesearchMainScreen;
import com.example.finalproject.ticketMaster.TicketMasterMainScreen;

public class MainActivity extends AppCompatActivity {

    /**
     * Buttons for redirecting to each member's work
     */
    private Button btnTicketMaster, btnRecipeSearch, btnCovid19Case, btnTheAudioDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTicketMaster = findViewById(R.id.btnTicketMaster);
        btnRecipeSearch = findViewById(R.id.btnRecipeSearch);
        btnCovid19Case = findViewById(R.id.btnCovid19Case);
        btnTheAudioDatabase = findViewById(R.id.btnTheAudioDatabase);

        btnTicketMaster.setOnClickListener(v -> {
            //Give directions to go from this page, to activity_profile Ticket Master
            Intent i = new Intent(MainActivity.this, TicketMasterMainScreen.class);

            // Now make the transition
            startActivity(i);
        });

        btnRecipeSearch.setOnClickListener(v -> {
            //Give directions to go from this page, to activity_profile Recipe Search
            Intent i = new Intent(MainActivity.this, RecipesearchMainScreen.class);
            startActivity(i);
        });

        btnCovid19Case.setOnClickListener(v -> {
            //Give directions to go from this page, to activity_profile Covid-19 Cases
            Intent i = new Intent(MainActivity.this, Covid19CasesMainScreen.class);

            // Now make the transition
            startActivity(i);
        });

        //Give directions to go from this page, to activity_profile Audio Database
        btnTheAudioDatabase.setOnClickListener(v -> {

            Intent goToAudio = new Intent(MainActivity.this, AudioDatabaseMainScreen.class);
            startActivity(goToAudio);
        });

    }

}