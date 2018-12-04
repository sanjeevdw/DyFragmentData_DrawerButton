package com.example.android.dyfragmentdata;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GuideAdapter extends ArrayAdapter<Guide> {

// Resource ID for the Background color for this list of words.

    private int mColorResourceId;
    //private static final String LOG_TAG = GuideAdapter.class.getSimpleName();

    public GuideAdapter(Activity activity, ArrayList<Guide> temples, int colorResourceId) {
        super(activity, 0, temples);
        mColorResourceId = colorResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        View itemListView = convertView;

        if (itemListView == null) {

            itemListView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Guide currentGuide = getItem(position);

        TextView nameTempleView = (TextView) itemListView.findViewById(R.id.name_view);
        nameTempleView.setText(currentGuide.getTempleName());

        TextView locationTempleView = (TextView) itemListView.findViewById(R.id.location_view);
        locationTempleView.setText(currentGuide.getTempleLocation());

        ImageView imageTempleView = (ImageView) itemListView.findViewById(R.id.image);

        //   if (currentProduct.getImageUrl()) {
        if (currentGuide != null) {

            Glide.with(imageTempleView.getContext())
                    .load(currentGuide.getImageUrl())
                    .into(imageTempleView);
        }

        Button detailsButton = (Button) itemListView.findViewById(R.id.button_details_two);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);

             //   int position = getPosition();
              //  long id = getItemId(position);
              //  ((CatalogActivity) context).onButtonClick(id);
            }
        });
      //  if (currentGuide.hasImage()) {

         //   imageTempleView.setImageResource(currentGuide.getImageResourceId());
       //     imageTempleView.setVisibility(View.VISIBLE);
     //   }
    //   else {
     //       imageTempleView.setVisibility(View.GONE);
     //   }

        // Set the theme color for the list items.

        View textContainer = itemListView.findViewById(R.id.text_container);
        // FInd the color the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of the text container view.
        textContainer.setBackgroundColor(color);

        return itemListView;
        // return super.getView(position, convertView, parent);
    }
}

