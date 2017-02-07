package com.ilha.movies;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "Movies";
    private SearchView searchView;
    private String usrTyped;

    final private String BASE_URL = "http://imdb.wemakesites.net/api/search?q=";
    final private String API_KEY = "&api_key=54efdaf7-ebe1-4284-a376-b9b335f8bfd0";

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.requestFocus();

        // Get what users type
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {

                  usrTyped = searchView.getQuery().toString();

                  getListOfMovies(usrTyped);

                  return false;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  return false;
              }
          });

            //Force keyboard to appear
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
        toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

        return true;

    }

    private void getListOfMovies(String typed) {

        typed = typed.replaceAll(" ", "+");

        String request = BASE_URL + typed + API_KEY;

        Log.e(TAG, request);

        showProgressDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(request, null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                hideProgressDialog();

                Log.d(TAG, response.toString());

                showMoviesList(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showMoviesList(JSONObject response) {

    }

    private void hideProgressDialog() {
        mDialog.hide();
    }

    private void showProgressDialog() {
        mDialog.show();
    }
}
