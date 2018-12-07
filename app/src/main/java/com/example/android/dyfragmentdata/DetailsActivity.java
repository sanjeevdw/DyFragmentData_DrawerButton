package com.example.android.dyfragmentdata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ArrayList<Guide> temples;
    private GuideAdapter adapter;
    private ListView listView;
    private ImageView mainImageView;
    private ArrayList<ProductDetails> products;
    private ProductAdapter productAdapter;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_category.xml layout file
        setContentView(R.layout.details_activity);

        Intent productIdIntent = getIntent();
        Bundle bundle = productIdIntent.getExtras();

        if (bundle != null) {
            pid = (String) bundle.get("ProductId");

        }

        relatedProductsNetworkRequest();
        listView = (ListView) findViewById(R.id.list);
        temples = new ArrayList<Guide>();
        products = new ArrayList<ProductDetails>();
        listView.setNestedScrollingEnabled(true);
        productsDetailsRequest();

        //  listView.setAdapter(adapter);

        setNavigationViewListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#e53935"));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ActionBar actionbar = getSupportActionBar();

        if (actionbar !=null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
             actionbar.setHomeAsUpIndicator(R.drawable.back_arrow);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

       mainImageView = (ImageView) findViewById(R.id.main_image);
       // mainImageView.setImageResource(R.drawable.product_image);

        ImageButton thumbOneImageView = (ImageButton) findViewById(R.id.thumbnail_image_one);
        thumbOneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                        mainImageView.setImageResource(R.drawable.product_image);
                        }
                });


       ImageButton thumbTwoImageView = (ImageButton ) findViewById(R.id.thumbnail_image_two);
        thumbTwoImageView.setOnClickListener(new View.OnClickListener() {

            @Override
                    public void onClick(View v) {
                        mainImageView.setImageResource(R.drawable.product_image_two);

                   }
                });

        ImageButton thumbThreeImageView = (ImageButton) findViewById(R.id.thumbnail_image_three);
        thumbThreeImageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mainImageView.setImageResource(R.drawable.product_image_three);
                        }
                });
        }

        // NavigationView click events
    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
    /*        case_tab R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true; */
            case R.id.action_drawer_signin:
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_drawer_cart:
                Intent intentCart = new Intent(this, CartActivity.class);
                startActivity(intentCart);
            case android.R.id.home:
                //mDrawerLayout.openDrawer(GravityCompat.START);
                Intent categoryIntent = new Intent(this, MainActivity.class);
                startActivity(categoryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        // set item as selected to persist highlight
        item.setChecked(true);

        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        switch(id) {

            case R.id.nav_home:
                Intent intent = new Intent(this, HomepageActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_category:
                Intent intentCategory = new Intent(this, MainActivity.class);
                startActivity(intentCategory);
                break;
            case R.id.nav_product:
                Intent intentProduct = new Intent(this, DetailsActivity.class);
                startActivity(intentProduct);
                break;
            case R.id.nav_login:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);

                break;
            case R.id.nav_register:
                Intent intentRegister = new Intent(this, SignupActivity.class);
                startActivity(intentRegister);

                break;
            case R.id.nav_profile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.nav_forgot_password:
                Intent intentForgotPassword = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
                break;
            case R.id.nav_change_password:
                Intent intentChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;

            case R.id.nav_wishlist:
                Intent intentWishlist = new Intent(this, WishlistActivity.class);
                startActivity(intentWishlist);
                break;
            case R.id.nav_about_industry:
                Toast.makeText(this, "NavigationClick", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_checkout:
                Intent intentCheckout = new Intent(this, CheckoutActivity.class);
                startActivity(intentCheckout);
                break;
            case R.id.nav_order_history:
                Intent intentOrderHistory = new Intent(this, OrderHistoryActivity.class);
                startActivity(intentOrderHistory);
                break;
            case R.id.sign_out_menu:
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private void relatedProductsNetworkRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/product_list.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            String trimResponse = response.substring(3);
                            String trimmedResponse = trimResponse.trim();
                            JSONObject jsonObject = new JSONObject(trimmedResponse);
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                //Loop the Array
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject currentObject = data.getJSONObject(i);
                                    JSONArray currentProductDetail = currentObject.getJSONArray("product_detail");
                                    for (int j = 0; j < currentProductDetail.length(); j++) {

                                        //   JSONArray productDetail = new JSONArray("product_detail");
                                        Log.e("Message", "loop");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        JSONObject e = currentProductDetail.getJSONObject(j);
                                        map.put("cid", "cid :" + e.getString("product_id"));
                                        map.put("Category name", "Category name : " + e.getString("productsname"));

                                        String productId = e.getString("product_id");
                                        String productName = e.getString("productsname");
                                        String productPrice = e.getString("price");
                                        String imageUrl = e.getString("feature_image");
                                        String productRating = e.getString("rating");
                                        String productWishlist = e.getString("is_whishlit");

                                        Guide currentGuide = new Guide(productId, productName, productPrice, imageUrl, productRating, productWishlist);
                                        temples.add(currentGuide);
                                        adapter = new GuideAdapter(DetailsActivity.this, temples, R.color.temples_category);

                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                long viewId = view.getId();

                                                if (viewId == R.id.button_details_two) {
                                                    // Toast.makeText(DetailsActivity.this, "View more clicked", Toast.LENGTH_SHORT);
                                                    Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                    }
                                }
                            }

                        } catch (JSONException e) {
                            // If an error is thrown when executing any of the above statements in the "try" block,
                            // catch the exception here, so the app doesn't crash. Print a log message
                            // with the message from the exception.
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }
                        // Return the list of earthquakes
                        // return categories;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        });
        queue.add(stringRequest);
    }

    private void productsDetailsRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/products.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            String trimResponse = response.substring(3);
                            String trimmedResponse = trimResponse.trim();
                            JSONObject jsonObject = new JSONObject(trimmedResponse);
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                //Loop the Array
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject currentObject = data.getJSONObject(i);
                                    JSONObject currentProductDetail = currentObject.getJSONObject("product_detail");
                                 //   for (int j = 0; j < currentProductDetail.length(); j++) {

                                        //   JSONArray productDetail = new JSONArray("product_detail");
                                        Log.e("Message", "loop");
                                        HashMap<String, String> map = new HashMap<String, String>();
                                      //  JSONObject e = currentProductDetail.getJSONObject(i);
                                        map.put("cid", "cid :" + currentProductDetail.getString("product_id"));
                                        map.put("Category name", "Category name : " + currentProductDetail.getString("productsname"));

                                        String productId = currentProductDetail.getString("product_id");
                                        String productName = currentProductDetail.getString("productsname");
                                       // String productDescription = e.getString("description");
                                        String productPrice = currentProductDetail.getString("discount_percent");
                                        String imageUrl = currentProductDetail.getString("image");

                                        TextView productIdView = (TextView) findViewById(R.id.sku);
                                        productIdView.setText(productId);

                                        TextView productNameView = (TextView) findViewById(R.id.product_name_view);
                                        productNameView.setText(productName);

                                        TextView discountPriceView = (TextView) findViewById(R.id.product_price_view);
                                        discountPriceView.setText(productPrice);

                                        ImageView productImageView = (ImageView) findViewById(R.id.main_image);

                                        Glide.with(productImageView.getContext())
                                                   .load(imageUrl)
                                                     .into(productImageView);

                                        //  ProductDetails currentProduct = new ProductDetails(productId, productName, productPrice, imageUrl);
                                     //   products.add(currentProduct);
                                     //   productAdapter = new ProductAdapter(DetailsActivity.this, products);

                                     //   listView.setAdapter(productAdapter);
                                    //    productAdapter.notifyDataSetChanged();

                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                long viewId = view.getId();

                                                if (viewId == R.id.button_details_two) {
                                                    // Toast.makeText(DetailsActivity.this, "View more clicked", Toast.LENGTH_SHORT);
                                                    Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                               //     }
                                }
                            }

                        } catch (JSONException e) {
                            // If an error is thrown when executing any of the above statements in the "try" block,
                            // catch the exception here, so the app doesn't crash. Print a log message
                            // with the message from the exception.
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }
                        // Return the list of earthquakes
                        // return categories;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        })
        { @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("product_id", pid);
            return params;
        }

        };

                ;
        queue.add(stringRequest);
    }
}


