package com.ilha.movies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ilha.movies.AppController;
import com.ilha.movies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    private static final String SEPARETOR = ", ";
    private static final String ATR_TITLE = "title";
    private static final String ATR_DATA = "data";
    private static final String ATR_DESCRIPTION = "description";
    private static final String ATR_DURATION = "duration";
    private static final String ATR_RELEASED = "released";
    private static final String ATR_CAST = "cast";
    private static final String ATR_GENRES = "genres";
    private static final String ATR_IMAGE = "image";

    private String title;
    private JSONObject data;

    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;

    private TextView txvDescription;
    private TextView txvDuration;
    private TextView txvReleased;
    private TextView txvCast;
    private TextView txvGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // TextViews
        txvDescription = (TextView) findViewById(R.id.txv_description);
        txvDuration = (TextView) findViewById(R.id.txv_duration);
        txvReleased = (TextView) findViewById(R.id.txv_released);
        txvCast = (TextView) findViewById(R.id.txv_cast);
        txvGenres = (TextView) findViewById(R.id.txv_genres);

        // ImageView
        mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);

        // ImageLoader
        mImageLoader = AppController.getInstance().getImageLoader();

        // Get datas from others Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            title = extras.getString(ATR_TITLE);
            setTitle(title);

            String strData = extras.getString(ATR_DATA);

            try {

                data = new JSONObject(strData);

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

            setPoster();

            setDetails();

        }else{
            onBackPressed();
        }
    }

    /**
     * Get ditails from JSONObeject data and show in each textview
     * */
    private void setDetails() {
        try {

            txvDescription.setText(data.getString(ATR_DESCRIPTION));

        } catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }

        try {

            txvDuration.setText(data.getString(ATR_DURATION));

        } catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }

        try {

            txvReleased.setText(data.getString(ATR_RELEASED));

        } catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }

        try {
            JSONArray arr = data.getJSONArray(ATR_CAST);

            String cast = getStringTogetherFromArray(arr);

            txvCast.setText(cast);

        }catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }

        try {
            JSONArray arr = data.getJSONArray(ATR_GENRES);

            String genres = getStringTogetherFromArray(arr);

            txvGenres.setText(genres);

        }catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }
    }

    /**
     * Load Poster from url
     * */
    private void setPoster() {
        try{

            String urlImage = data.getString(ATR_IMAGE);

            mNetworkImageView.setImageUrl(urlImage, mImageLoader);

        } catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }
    }

    /**
     * Get array to put all values together in a String with the SEPARATOR between them.
     *
     * @param itens - Array to put all values together
     * */
    private String getStringTogetherFromArray(JSONArray itens) throws JSONException {

        StringBuilder strAux = new StringBuilder();
        for (int i = 0;i < itens.length(); i++){
            String aux = itens.get(i).toString();
            strAux.append(aux);
            if (i != itens.length()-1) {
                strAux.append(SEPARETOR);
            }
        }
        return strAux.toString();
    }
}
