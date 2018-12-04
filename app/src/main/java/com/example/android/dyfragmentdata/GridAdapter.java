package com.example.android.dyfragmentdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<GridCategory> {
    private ImageView gridImageView;
    private Context mContext;

        public GridAdapter(Context context, ArrayList<GridCategory> gridCategories) {

            super(context, 0, gridCategories);

        }

           // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View gridItemView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes

                gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_items, parent, false);

                } else {
                gridItemView = (View) convertView;
            }

            GridCategory currentBadgujar = getItem(position);
            gridImageView = (ImageView) gridItemView.findViewById(R.id.gridImageView);
            gridImageView.setImageResource(currentBadgujar.getGridImageResourceId());

            TextView gridTextView = (TextView) gridItemView.findViewById(R.id.gridTextView);
            gridTextView.setText(currentBadgujar.getGridNames());

            return gridItemView;
        }
}
