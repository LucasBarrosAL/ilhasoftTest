package com.ilha.movies.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ilha.movies.interfaces.RecyclerOnClickListener;

/**
 * Created by lucasmbarros on 07/02/17.
 */
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private RecyclerOnClickListener mRecyclerOnClickListener;

    public RecyclerTouchListener (final Context context, RecyclerView recyclerView,
                                  RecyclerOnClickListener recyclerOnClickListener){

        mRecyclerOnClickListener = recyclerOnClickListener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mRecyclerOnClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mRecyclerOnClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
