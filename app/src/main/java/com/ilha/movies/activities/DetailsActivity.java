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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            title = extras.getString("title");
            setTitle(title);

            String strData = extras.getString("data");

            try {

                data = new JSONObject(strData);
                Log.d(AppController.TAG, data.toString());

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

            try{

                String urlImage = data.getString("image");

                mNetworkImageView.setImageUrl(urlImage, mImageLoader);

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }


            try {

                txvDescription.setText(data.getString("description"));

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

            try {

                txvDuration.setText(data.getString("duration"));

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

            try {

                txvReleased.setText(data.getString("released"));

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

            try {
                JSONArray itens = data.getJSONArray("cast");

                StringBuilder cast = new StringBuilder();
                for (int i = 0;i < itens.length(); i++){
                    String aux = itens.get(i).toString();
                    cast.append(aux);
                    if (i != itens.length()-1) {
                        cast.append(SEPARETOR);
                    }
                }

                txvCast.setText(cast.toString());

            }catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

            try {
                JSONArray itens = data.getJSONArray("genres");

                StringBuilder genres = new StringBuilder();
                for (int i = 0;i < itens.length(); i++){
                    String aux = itens.get(i).toString();
                    genres.append(aux);
                    if (i != itens.length()-1) {
                        genres.append(SEPARETOR);
                    }
                }

                txvGenres.setText(genres.toString());

            }catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }

        }else{
            onBackPressed();
        }
    }
}
