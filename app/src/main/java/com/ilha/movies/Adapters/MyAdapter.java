package com.ilha.movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ilha.movies.AppController;
import com.ilha.movies.R;
import com.ilha.movies.interfaces.RecyclerOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final String ATR_TITLE = "title";
    private static final String ATR_IMAGE = "thumbnail";
    private JSONArray mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private NetworkImageView mNetworkImageView;
        private ImageLoader mImageLoader;
        public TextView title;
        private RecyclerOnClickListener mRecyclerOnClickListener;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);

            // Get the NetworkImageView that will display the image.
            mNetworkImageView = (NetworkImageView) v.findViewById(R.id.networkImageView);

            // Get the ImageLoader through your singleton class.
            mImageLoader = AppController.getInstance().getImageLoader();

            v.setTag(this);

        }


        @Override
        public void onClick(View v) {
            if (mRecyclerOnClickListener != null){
                mRecyclerOnClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(JSONArray myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create the new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (mDataset.length() > 0){
            JSONObject item = null;
            try {

                item = mDataset.getJSONObject(position);
                showMovie(holder, item);

            } catch (JSONException e) {
                Log.e(AppController.TAG, e.toString());
            }


        }else {
            Log.e(AppController.TAG, "No data in mDaset");
        }

    }

    /**
     * Print the titles and images on list
     * */
    private void showMovie(ViewHolder holder, JSONObject item) throws JSONException {

        holder.title.setText(item.getString(ATR_TITLE));
        // Load image from url
        holder.mNetworkImageView.setImageUrl(item.getString(ATR_IMAGE), holder.mImageLoader);
    }

    /**
     * Auxiliar to set mDataset from an activity
     *
     * @param dataset - Dataset to list
     * */
    public void setParams(JSONArray dataset) {
        mDataset = dataset;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}

