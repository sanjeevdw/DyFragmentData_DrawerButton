package com.example.android.dyfragmentdata;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Guide {

    // Variable declaration

    private String mTempleName;
    private String mTempleLocation;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = 1;
    // created constructor with two parameters templeName and templeLocation


    public Guide(String templeName, String templeLocation) {

        // Variable initial values

        mTempleName = templeName;
        mTempleLocation = templeLocation;
    }

  //  public Guide(String templeName, String templeLocation) {

        // Variable initial values

      //  mTempleName = templeName;
      //  mTempleLocation = templeLocation;
       // mImageResourceId = imageResourceId;
  //  }

    // Get method to return temple name

    public String getTempleName() {
        return mTempleName;
    }

    // Get method to return temple location

    public String getTempleLocation() {
        return mTempleLocation;
    }

    // Get method to return temple location

  //  public int getImageResourceId() {

   //    return mImageResourceId;
  //  }

    /* Returns whether or not there is an image for this word
     *
     * @return
     */

  //  public boolean hasImage() {

 //       return mImageResourceId != NO_IMAGE_PROVIDED;
  //  }
}

