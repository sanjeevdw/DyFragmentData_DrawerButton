package com.example.android.dyfragmentdata;

public class ProductDetails {

    // Variable declaration

    private String mProductId;
    private String mProductName;
    private String mProductDescription;
    private String mDiscountPrice;
    private String mImageUrl;
    private String mRating;
    private String mWishlist;

    // created constructor with two parameters templeName and templeLocation

    public ProductDetails(String productId, String productName, String discountPrice, String mImageUrl) {

        // Variable initial values

        mProductId = productId;
        mProductName = productName;
     //   mProductDescription = productDescription;
        mDiscountPrice = discountPrice;
        this.mImageUrl = mImageUrl;

        }

    // Get method to return temple name

    public String getProductId() {
        return  mProductId;
    }

    public String getProductName() {
        return mProductName;
    }

    // Get method to return temple location

  //  public String getProductDescription() {
   //     return mProductDescription;
   // }

    public String getDiscountPrice() {
        return mDiscountPrice;
    }

    public String getImageUrl() {
        return  mImageUrl;
    }

}

