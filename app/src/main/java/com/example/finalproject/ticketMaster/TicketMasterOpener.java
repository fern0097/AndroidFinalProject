package com.example.finalproject.ticketMaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TicketMasterOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "EventsDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "EVENTS";
    public final static String COL_NAME = "NAME";
    public final static String COL_DATE = "DATE";
    public final static String COL_CURR = "CURR";
    public final static String COL_MIN = "MIN";
    public final static String COL_MAX = "MAX";
    public final static String COL_URL = "URL";
    public final static String COL_IMG_URL = "IMG_URL";
    public final static String COL_UNIQUE_ID = "UNIQUE_ID";
    public final static String COL_ID = "_id";

    public TicketMasterOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT,"
                + COL_DATE + " TEXT,"
                + COL_CURR + " TEXT,"
                + COL_MIN + " REAL,"
                + COL_MAX + " REAL,"
                + COL_URL + " TEXT,"
                + COL_IMG_URL + " TEXT, "
                + COL_UNIQUE_ID + " TEXT);");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
