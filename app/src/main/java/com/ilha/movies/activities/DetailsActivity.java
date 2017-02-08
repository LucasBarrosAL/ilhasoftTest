package com.ilha.movies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ilha.movies.AppController;
import com.ilha.movies.R;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    private String title;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            String strData = extras.getString("data");
            try {
                data = new JSONObject(strData);
            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }
        }else{
            onBackPressed();
        }
        setTitle(title);
    }
}
