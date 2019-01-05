package com.example.android.dyfragmentdata;

public class GridCategory {

        private String mGridCategoryId;
        private String mGridCategoryName;
        private String mGridCategoryImage;

        public GridCategory(String GridCategoryId, String GridCategoryName, String GridCategoryImage) {

            mGridCategoryId = GridCategoryId;
            mGridCategoryName = GridCategoryName;
            mGridCategoryImage = GridCategoryImage;
        }

        public String getGridCategoryName() {
            return mGridCategoryName;
        }

        public String getGridCategoryImage() {
            return mGridCategoryImage;
        }
    }

