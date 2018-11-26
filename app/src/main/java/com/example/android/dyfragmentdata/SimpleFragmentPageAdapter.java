package com.example.android.dyfragmentdata;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {

    /**
     * {@link SimpleFragmentPageAdapter} is a {@link FragmentPagerAdapter} that can provide layout for each list item based on a data source which is a list of (@link temples) objects.
     */
    // context of the app

        private Context mContext;

        /* Create a new {@link SimpleFragmentPageAdapter} object.
         * @param context is the context of the app
         * @param fm is the fragment manager that will keep each fragment's
         * state in the adapter across swipes.
         */

        public SimpleFragmentPageAdapter(Context context, FragmentManager fm) {
            // Required empty public constructor
            super(fm);
            mContext = context;
        }

        /* Return the {@link Fragment} that should be displayed for the given page number.

         */
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new TemplesFragment();
            }

            else if(position == 1) {
                return new TemplesFragment();
            }

            else if(position == 2) {
                return new TemplesFragment();
            }

            else if(position == 3) {
                return new TemplesFragment();
            }

            else  {
                return new TemplesFragment();
            }

        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return mContext.getString(R.string.temples_category);
            }

            else if(position == 1) {
                return mContext.getString(R.string.places_category);
            }

            else if (position == 2) {
                return mContext.getString(R.string.restaurants_category);
            }

            else if(position == 3) {
                return mContext.getString(R.string.monuments_category);
            }

            else  {
                return mContext.getString(R.string.outing_category);
            }
        }
    }

