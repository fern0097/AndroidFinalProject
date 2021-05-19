package com.example.finalproject.covid19cases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;

/**
 * CovidCaseFragment is a fragment class that displays the details of the information of the Covid
 * case such as country, country code, province/state, latitude, longitude, number of cases status
 * and date with an option to save the data information.
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
public class CovidCaseFragment extends Fragment {


    private AppCompatActivity parentActivity;

    /**
     * Field that holds the id.
     */
    private long id;

    /**
     * Field that refers to the database object.
     */
    private SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View v = inflater.inflate(R.layout.activity_covid19_fragment, container, false);
        // Find the text view for country.
        TextView tvCountry = v.findViewById(R.id.tvCountry);
        // Find the text view for country code.
        TextView tvCountrycode = v.findViewById(R.id.tvCountrycode);
        // Find the text view for country province.
        TextView tvProvince = v.findViewById(R.id.tvProvince);
        // Find the text view for latitude.
        TextView tvLat = v.findViewById(R.id.tvLat);
        // Find the text view for longitude.
        TextView tvLon = v.findViewById(R.id.tvLon);
        // Find the text view for number of cases
        TextView tvCases = v.findViewById(R.id.tvCases);
        // Find the text view for status
        TextView tvStatus = v.findViewById(R.id.tvStatus);
        // Find the text view for date
        TextView tvDate = v.findViewById(R.id.tvDate);

        Button saveToDbButton = v.findViewById(R.id.saveToDbButton);

        Bundle bundle = getArguments();
        id = bundle.getLong("ID");
        String country = bundle.getString("COUNTRY");
        String countrycode = bundle.getString("COUNTRYCODE");
        String province = bundle.getString("PROVINCE");
        String lat = bundle.getString("LAT");
        String lon = bundle.getString("LON");
        int cases = bundle.getInt("CASES");
        String status = bundle.getString("STATUS");
        String date = bundle.getString("DATE");
        boolean received = bundle.getBoolean("RECEIVED");

        // show the save button when is press show the remove button.
        if (id == 0) {
            saveToDbButton.setText(R.string.wilker_save_to_db_button);
        } else {
            saveToDbButton.setText(R.string.wilker_remove_from_db_button);
        }

        tvCountry.setText("Country: " + country);
        tvCountrycode.setText("Countrycode: " + countrycode);
        tvProvince.setText("Province: " + province);
        tvLat.setText("Lat: " + lat);
        tvLon.setText("Lon: " + lon);
        tvCases.setText("Cases: " + cases);
        tvStatus.setText("Status: " + status);
        tvDate.setText("Date: " + date);

        //SQLiteDatabase db;
        MyCovidOpener dbOpener = new MyCovidOpener(parentActivity);

        //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db = dbOpener.getWritableDatabase();

        /**
         * Save button when is press to stores Covid cases data into the database
         */
        saveToDbButton.setOnClickListener(v1 -> {

            if (id == 0) {
                // Add covid case to database
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyCovidOpener.COL_COUNTRY, country);
                newRowValues.put(MyCovidOpener.COL_COUNTRYCODE, countrycode);
                newRowValues.put(MyCovidOpener.COL_PROVINCE, province);
                newRowValues.put(MyCovidOpener.COL_LAT, lat);
                newRowValues.put(MyCovidOpener.COL_LON, lon);
                newRowValues.put(MyCovidOpener.COL_CASES, cases);
                newRowValues.put(MyCovidOpener.COL_STATUS, status);
                newRowValues.put(MyCovidOpener.COL_DATE, date);

                long newId = db.insert(MyCovidOpener.TABLE_NAME, null, newRowValues);
                id = newId;

                //show a toast message saying has been saved.
                Toast.makeText(parentActivity, R.string.wilker_saved_to_db, Toast.LENGTH_SHORT).show();

                saveToDbButton.setText(R.string.wilker_remove_from_db_button);
            } else {
                // Remove message from database
                db.delete(MyCovidOpener.TABLE_NAME, MyCovidOpener.COL_ID + "= ?", new String[]{Long.toString(id)});
                id = 0;

                //show a toast message saying has been removed.
                Toast.makeText(parentActivity, R.string.wilker_removed_from_db, Toast.LENGTH_SHORT).show();
                saveToDbButton.setText(R.string.wilker_save_to_db_button);
            }

        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}