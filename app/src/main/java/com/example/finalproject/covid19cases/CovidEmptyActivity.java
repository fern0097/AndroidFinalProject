package com.example.finalproject.covid19cases;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

/**
 * CovidEmptyActivity is a empty activity to pass data to the CovidCaseFragment
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
public class CovidEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid19_empty);

        //get the data that was passed from CovidCaseFragment
        Bundle dataToPass = getIntent().getExtras();

        //This is copied directly from CovidCaseFragment.java
        CovidCaseFragment dFragment = new CovidCaseFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, dFragment)
                .commit();
    }
}