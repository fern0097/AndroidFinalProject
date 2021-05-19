package com.example.finalproject.ticketMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketMasterDetailsScreen extends AppCompatActivity {

    private String picUrlStr;
    private Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tm_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String idStr = bundle.get(TicketMasterMainScreen.KEY_UNIQUE_ID).toString();
        String nameStr = bundle.get(TicketMasterMainScreen.KEY_NAME).toString();
        String dateStr = bundle.get(TicketMasterMainScreen.KEY_DATE).toString();
        String currStr = bundle.get(TicketMasterMainScreen.KEY_CURRENCY).toString();
        String minStr = bundle.get(TicketMasterMainScreen.KEY_LO_PRICE).toString();
        String maxStr = bundle.get(TicketMasterMainScreen.KEY_HI_PRICE).toString();
        String urlStr = bundle.get(TicketMasterMainScreen.KEY_URL).toString();
        picUrlStr = bundle.get(TicketMasterMainScreen.KEY_IMAGE).toString();

        TextView nameView = findViewById(R.id.tm_details_event_name);
        nameView.setText(nameStr);

        TextView dateView = findViewById(R.id.tm_details_date);
        dateView.setText(dateStr);

        TextView namePriceRange = findViewById(R.id.tm_details_price_range);
        String priceRange = "(" + currStr + ") " +
                            minStr +
                            " - " +
                            maxStr;
        namePriceRange.setText(priceRange);

        TextView urlView = findViewById(R.id.tm_details_url);
        urlView.setText(urlStr);

        ImageQuery query = new ImageQuery();
        query.execute(picUrlStr);

        // Handles the favorites switch
        SwitchCompat favSwitch = findViewById(R.id.tm_fav_switch);

        // Check if the item is in favorites
        Cursor results = TicketMasterMainScreen.getDb().query(
                false,
                TicketMasterOpener.TABLE_NAME,
                new String[] {TicketMasterOpener.COL_UNIQUE_ID},
                TicketMasterOpener.COL_UNIQUE_ID + "= ?",
                new String[] { idStr },
                null,
                null,
                null,
                null);


        favSwitch.setChecked(results.getCount() != 0); // sets the switch accordingly

        results.close();

        favSwitch.setOnClickListener( click -> {
            String favMsg;

            ContentValues newRowValues = new ContentValues();

            newRowValues.put(TicketMasterOpener.COL_NAME, nameStr);
            newRowValues.put(TicketMasterOpener.COL_DATE, dateStr);
            newRowValues.put(TicketMasterOpener.COL_CURR, currStr);
            newRowValues.put(TicketMasterOpener.COL_MIN, Float.parseFloat(minStr));
            newRowValues.put(TicketMasterOpener.COL_MAX, Float.parseFloat(maxStr));
            newRowValues.put(TicketMasterOpener.COL_URL, urlStr);
            newRowValues.put(TicketMasterOpener.COL_IMG_URL, picUrlStr);
            newRowValues.put(TicketMasterOpener.COL_UNIQUE_ID, idStr);

            TicketMasterMainScreen.getDb().insert(TicketMasterOpener.TABLE_NAME, null, newRowValues);

            if (favSwitch.isChecked()) {

                favMsg = "Event saved in favorites";
            } else {

                TicketMasterMainScreen.getDb().delete(
                        TicketMasterOpener.TABLE_NAME,
                        TicketMasterOpener.COL_UNIQUE_ID + "= ?",
                        new String[] { idStr });
                favMsg = "Event removed from favorites";
            }

            Toast.makeText(
                    this,
                    favMsg,
                    Toast.LENGTH_SHORT).show(); //TODO translate
        });

    }

    class ImageQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            URL pictureURL;

            try {
                pictureURL = new URL(picUrlStr);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) pictureURL.openConnection();
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    picture = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }
            } catch (IOException ignored) {}

            return "Image Loaded";
        }

        @Override
        protected void onPostExecute(String s) {

            ImageView picImgView = findViewById(R.id.tm_details_promotional_image);

            if (picture != null) {
                picImgView.setImageBitmap(picture);
                picImgView.setVisibility(View.VISIBLE);
            } else {
                picImgView.setVisibility(View.INVISIBLE);
            }

        }
    }
}