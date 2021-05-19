package com.example.finalproject.covid19cases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MyCovidOpener is a class that extends SQLiteOpenHelper, basically this
 * class creates, upgrades or downgrades the database and also creates the
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
public class MyCovidOpener extends SQLiteOpenHelper {

    /**
     * Field stores the database file name.
     */
    public static final String DATABASE_NAME = "MyDatabaseFile";

    /**
     * Field that stores the database version.
     */
    public static final int VERSION_NUM = 1;

    /**
     * Field that stores the table name in the database.
     */
    public static final String TABLE_NAME = "COUNTRIES";

    /**
     * Field that stores column name that's holds an ID.
     */
    public static final String COL_ID = "_id";

    /**
     * Field that stores column name that's hold's a country name.
     */
    public static final String COL_COUNTRY = "COUNTRY";

    /**
     * Field that stores column name that's hold's a country code.
     */
    public static final String COL_COUNTRYCODE = "COUNTRYCODE";

    /**
     * Field that stores column name that's hold's a province.
     */
    public static final String COL_PROVINCE = "PROVINCE";

    /**
     * Field that stores column name that's hold's Latitude.
     */
    public static final String COL_LAT = "LAT";

    /**
     * Field that stores column name that's hold's a longitude.
     */
    public static final String COL_LON = "LON";

    /**
     * Field that stores column name that's hold's number of cases.
     */
    public static final String COL_CASES = "CASES";

    /**
     * Field that stores column name that's hold's current status.
     */
    public static final String COL_STATUS = "STATUS";

    /**
     * Field that stores column name that's hold's date.
     */
    public static final String COL_DATE = "DATE";

    /**
     * Constructor for my MyCovidOpener.
     *
     * @param context The context that calls this constructor.
     */
    public MyCovidOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Creates the table in the database.
     *
     * @param db is the database where the table is to be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_COUNTRY + " text,"
                + COL_COUNTRYCODE + " text,"
                + COL_PROVINCE + " text,"
                + COL_LAT + " text,"
                + COL_LON + " text,"
                + COL_CASES + " text,"
                + COL_STATUS + " text,"
                + COL_DATE + " text);");  // add or remove columns
    }

    /**
     * Upgrades the database if the current version number is higher.
     *
     * @param db         is the database where the table is to be created.
     * @param oldVersion the old database version.
     * @param newVersion the new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        // Delete the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    /**
     * Downgrade the database if the current version number is lower.
     *
     * @param db         is the database where the table is to be created.
     * @param oldVersion the old database version.
     * @param newVersion the new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        // Delete the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

}
