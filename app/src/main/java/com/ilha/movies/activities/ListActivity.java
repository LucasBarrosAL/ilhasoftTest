package com.ilha.movies.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ilha.movies.AppController;
import com.ilha.movies.R;
import com.ilha.movies.adapters.MyAdapter;
import com.ilha.movies.interfaces.RecyclerOnClickListener;
import com.ilha.movies.listeners.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity implements RecyclerOnClickListener{

    private static final String TAG = "Movies";
    private SearchView searchView;
    private String usrTyped;

    final private String BASE_URL = "http://imdb.wemakesites.net/api/";
    final private String SEARCH_PARAM = "search?q=";
    final private String API_KEY = "api_key=54efdaf7-ebe1-4284-a376-b9b335f8bfd0";

    private ProgressDialog mDialog;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONArray myDataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //Verify if mDataset is null
        if (myDataset == null){
            myDataset = new JSONArray();
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(AppController.getInstance(),
                mRecyclerView, this));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

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

    /**
     * Request data from server
     *
     * @param typed - What user wanna search
     * */
    private void getListOfMovies(String typed) {

        // Replace all spaces to plus
        typed = typed.replaceAll(" ", "+");

        String request = BASE_URL + SEARCH_PARAM + typed + "&" + API_KEY;

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

    /**
     * Start to build list of movies
     *
     * @param response -  Content data of request
     * */
    private void showMoviesList(JSONObject response) {

        try {
            myDataset = response.getJSONObject("data").getJSONObject("results")
                    .getJSONArray("titles");
            mAdapter.setParams(myDataset);
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();

            VolleyLog.d(TAG, "Error: title");
        }
    }

    private void hideProgressDialog() {
        mDialog.hide();
    }

    private void showProgressDialog() {
        mDialog.show();
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(AppController.TAG, "Position " + position);

        try {

            JSONObject item = myDataset.getJSONObject(position);

            requestDetail(item);

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void requestDetail(JSONObject item) throws JSONException {

        String id = item.getString("id");
        final String title = item.getString("title");

        Log.d(AppController.TAG, "ID: " + id);

        String request = BASE_URL + id + "?" + API_KEY;

        Log.e(TAG, request);

        showProgressDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(request, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //TODO: verificar codigo de sucesso
                        hideProgressDialog();

                        Log.d(TAG, response.toString());

                        showDetails(response, title);

//                        showMoviesList(response);

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

    void showDetails(JSONObject response, String title){

        JSONObject data = null;

        try {
            data = response.getJSONObject("data");

            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("data", data.toString());
            startActivity(intent);

        } catch (JSONException e) {
            Log.e(AppController.TAG, e.toString());
        }

    }
}
