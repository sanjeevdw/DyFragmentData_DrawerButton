package com.example.android.dyfragmentdata;

public class GridCategory {

    private int mGridImageResourceId;
        private String mGridNames;

        public GridCategory(int gridImageResourceId, String gridNames) {

            mGridImageResourceId = gridImageResourceId;
            mGridNames = gridNames;
        }

        public int getGridImageResourceId() {
            return mGridImageResourceId;
        }

        public String getGridNames() {
            return mGridNames;
        }
    }

