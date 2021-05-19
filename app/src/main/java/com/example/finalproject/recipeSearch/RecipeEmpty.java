package com.example.finalproject.recipeSearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

public class RecipeEmpty extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private String recipe;
    private String ingredient;
    private String href;
    private String thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipesearchempty);

        textView1 = findViewById(R.id.emptyList);
        textView2 = findViewById(R.id.emptyList1);
        textView3 = findViewById(R.id.emptyList2);
        textView4 = findViewById(R.id.emptyList3);

        Intent i = getIntent();
        recipe = i.getStringExtra("recipes");
        ingredient = i.getStringExtra("ingredient");
        href = i.getStringExtra("href");
        thumbnail = i.getStringExtra("thumbnail");

        textView1.setText(recipe);
        textView2.setText(ingredient);
        textView3.setText(href);
        textView4.setText(thumbnail);

        //SharedPreferences sp = getApplicationContext().getSharedPreferences("recipes", Context.MODE_PRIVATE);
        //String recipes = sp.getString("recipes", "").toString();

        //t1.setText(recipes);
    }
}
