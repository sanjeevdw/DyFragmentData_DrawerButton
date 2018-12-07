package com.example.android.dyfragmentdata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ArrayList<Guide> temples;
   private GuideAdapter adapter;
    private ListView listView;
    private ListView listViewTwo;
    private ListView listViewThree;
   private GridAdapter gridAdapter;
   private int[] tabIcons = {R.drawable.shoes, R.drawable.mensjeans, R.drawable.casecase, R.drawable.accessories, R.drawable.wallet};
    private TabLayout tabLayout;
    CustomViewPager imageViewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        dealsProductsNetworkRequest();
        listView = (ListView) findViewById(R.id.list);
        listViewTwo = (ListView) findViewById(R.id.list_two);
        listViewThree = (ListView) findViewById(R.id.list_three);
        temples = new ArrayList<Guide>();
        // listView.setNestedScrollingEnabled(true);
        setNavigationViewListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#e53935"));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ActionBar actionbar = getSupportActionBar();

        if (actionbar !=null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Create an ArrayList of badgujar objects
        ArrayList<GridCategory> gridCategories = new ArrayList<GridCategory>();

        {
            gridCategories.add(new GridCategory(R.drawable.grid_image_one, "Voice control your world"));
            gridCategories.add(new GridCategory(R.drawable.grid_image_two, "Stream November 16"));
            gridCategories.add(new GridCategory(R.drawable.grid_image_three, "Thinner, Lighter"));
            gridCategories.add(new GridCategory(R.drawable.grid_image_four, "With Voice Remote"));
            }

        gridAdapter = new GridAdapter(HomepageActivity.this, gridCategories);

        // Get a reference to the GridView, and attach the adapter to the gridView.
        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridView gridViewTwo = (GridView) findViewById(R.id.gridView_two);
        GridView gridViewThree = (GridView) findViewById(R.id.gridView_three);
        gridView.setAdapter(gridAdapter);
        gridViewTwo.setAdapter(gridAdapter);
        gridViewThree.setAdapter(gridAdapter);

        TextView seeMoreTv = findViewById(R.id.see_more_tv);
        TextView seeMoreTvTwo = findViewById(R.id.see_more_tv_two);
        TextView seeMoreTvThree = findViewById(R.id.see_more_tv_three);
        seeMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCategories = new Intent(HomepageActivity.this, MainActivity.class);
                startActivity(intentCategories);
            }
        });
        seeMoreTvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCategories = new Intent(HomepageActivity.this, MainActivity.class);
                startActivity(intentCategories);
            }
        });
        seeMoreTvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCategories = new Intent(HomepageActivity.this, MainActivity.class);
                startActivity(intentCategories);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page.

        SimpleFragmentPageAdapter adapter = new SimpleFragmentPageAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager.

        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tab
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        imageViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        ViewPagerApdater viewPagerAdapter = new ViewPagerApdater(this);
        imageViewPager.setAdapter(viewPagerAdapter);

        // Auto start of viewpager

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {

            public void run() {

                if (currentPage == 5 ) {
                    currentPage = 0;
                }
                imageViewPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2000, 2000);

        }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

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
                mDrawerLayout.openDrawer(GravityCompat.START);
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

    private void dealsProductsNetworkRequest() {
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
                                        //    for (int j = 0; j < productDetail.length(); j++) {
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
                                        adapter = new GuideAdapter(HomepageActivity.this, temples, R.color.temples_category);
                                        listView.setAdapter(adapter);
                                        listViewTwo.setAdapter(adapter);
                                        listViewThree.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                long viewId = view.getId();

                                                if (viewId == R.id.button_details_two) {
                                                    Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                        listViewTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                long viewId = view.getId();

                                                if (viewId == R.id.button_details_two) {
                                                    Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        listViewThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                long viewId = view.getId();

                                                if (viewId == R.id.button_details_two) {
                                                    Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        });
        queue.add(stringRequest);
    }
}
