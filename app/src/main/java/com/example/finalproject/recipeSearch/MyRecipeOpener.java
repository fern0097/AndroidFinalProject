package com.example.finalproject.recipeSearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyRecipeOpener extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "RecipeDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "RECIPE_SEARCH";
    public static final String COL_ID = "_id";
    public static final String COL_RECIPE = "RECIPE";
    public static final String COL_INGREDIENT = "INGREDIENT";
    public static final String COL_HREF = "HREF";
    public static final String COL_THUMBNAIL = "THUMBNAIL";

    public MyRecipeOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ID + " text,"
                + COL_RECIPE + " text,"
                + COL_INGREDIENT + " text,"
                + COL_HREF + " text,"
                + COL_THUMBNAIL + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
