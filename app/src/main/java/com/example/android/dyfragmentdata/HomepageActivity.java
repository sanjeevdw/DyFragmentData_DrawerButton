package com.example.android.dyfragmentdata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.onesignal.OneSignal;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ArrayList<Guide> temples;
    private GuideAdapter adapter;
    private ListView listView;
    private ListView listViewTwo;
    private ListView listViewThree;
    private GridAdapter gridAdapter;
    private ArrayList<GridCategory> gridCategories;
    private int[] tabIcons = {R.drawable.shoes, R.drawable.mensjeans, R.drawable.casecase, R.drawable.accessories, R.drawable.wallet};
    private TabLayout tabLayout;
    CustomViewPager imageViewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    int currentPage = 0;
    private Session session;
    private String sessionToken;
    private NavigationView navigationView;
    private String usernameGoogle;
    private String sessionGoogleEmil;
    private int childIndex;
    private String android_id;
    private SliderData sliderData;
    private ArrayList<SliderData> sliderDataItems;
    private String sessionUserName;
    private String sessionUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        sendSliderRequest();
        dealsProductsNetworkRequest();
        recommendedProductsNetworkRequest();
        trendingProductsNetworkRequest();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        listView = (ListView) findViewById(R.id.list);
        listViewTwo = (ListView) findViewById(R.id.list_two);
        listViewThree = (ListView) findViewById(R.id.list_three);
        temples = new ArrayList<Guide>();
        sliderDataItems = new ArrayList<SliderData>();
        // listView.setNestedScrollingEnabled(true);
        setNavigationViewListener();
        session = new Session(this);
        sessionToken = session.getusertoken();
        sessionUserName = session.getusename();
        sessionUserEmail = session.getUserEmail();
        sendGridTopCategoryRequest();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            usernameGoogle = account.getDisplayName();
            sessionToken = usernameGoogle;
            sessionGoogleEmil = account.getEmail();
            if (sessionToken.isEmpty()) {
                navigationView = findViewById(R.id.nav_view);
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.drawer_view_without_login);
                View header = navigationView.getHeaderView(0);
                TextView loggedInUserName = header.findViewById(R.id.header_username_tv);
                TextView loggedInUserEmail = header.findViewById(R.id.email_address_tv);
                loggedInUserName.setText(R.string.header_name);
                loggedInUserEmail.setVisibility(View.GONE);
                }

            if (!sessionToken.isEmpty()) {
                showFullNavItem();
                }
        }

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

             if (sessionToken.isEmpty()) {
                 navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view_without_login);
                 View header = navigationView.getHeaderView(0);
                 TextView loggedInUserName = header.findViewById(R.id.header_username_tv);
                 TextView loggedInUserEmail = header.findViewById(R.id.email_address_tv);
                 loggedInUserName.setText(R.string.header_name);
                 loggedInUserEmail.setVisibility(View.GONE);
        }

        if (!sessionToken.isEmpty()) {
            showFullNavItem();
            }

        // Create an ArrayList of badgujar objects
        gridCategories = new ArrayList<GridCategory>();

     /*   {
            gridCategories.add(new GridCategory(R.drawable.grid_image_one, "Voice control your world"));
            gridCategories.add(new GridCategory(R.drawable.grid_image_two, "Stream November 16"));
            gridCategories.add(new GridCategory(R.drawable.grid_image_three, "Thinner, Lighter"));
            gridCategories.add(new GridCategory(R.drawable.grid_image_four, "With Voice Remote"));
            } */

     //  gridAdapter = new GridAdapter(HomepageActivity.this, gridCategories);

        // Get a reference to the GridView, and attach the adapter to the gridView.
      /*  GridView gridView = (GridView) findViewById(R.id.gridView);
        GridView gridViewTwo = (GridView) findViewById(R.id.gridView_two);
        GridView gridViewThree = (GridView) findViewById(R.id.gridView_three);
        gridView.setAdapter(gridAdapter);
        gridViewTwo.setAdapter(gridAdapter);
        gridViewThree.setAdapter(gridAdapter); */

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
       // ViewPagerApdater viewPagerAdapter = new ViewPagerApdater(this);
       // imageViewPager.setAdapter(viewPagerAdapter);

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

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

        private void showFullNavItem() {
            navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view);
            View header = navigationView.getHeaderView(0);
            TextView loggedInUserName = header.findViewById(R.id.header_username_tv);
            TextView loggedInUserEmail = header.findViewById(R.id.email_address_tv);
            sessionUserName = session.getusename();
            sessionUserEmail = session.getUserEmail();
            loggedInUserName.setText(sessionUserName);
            loggedInUserEmail.setText(sessionUserEmail);
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
        navigationView = findViewById(R.id.nav_view);
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
                if (!sessionToken.isEmpty()) {
                    Intent intentUpdateProfile = new Intent(this, ProfileActivity.class);
                    startActivity(intentUpdateProfile);

                } else {
                    Intent intent = new Intent(this, SignupActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.action_drawer_cart:
                Intent intentCart = new Intent(this, CartActivity.class);
                startActivity(intentCart);
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                if (sessionToken.isEmpty()) {
                    navigationView = findViewById(R.id.nav_view);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.drawer_view_without_login);
                } else {
                    showFullNavItem();
                }
              //  setNavigationViewListener();
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
            case R.id.nav_master_category:
                Intent intentMasterCategory = new Intent(this, MasterCategoryActivity.class);
                startActivity(intentMasterCategory);
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
                Intent intentOrderHistory = new Intent(this, OrderHistoryListingActivity.class);
                startActivity(intentOrderHistory);
                break;
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                sessionToken = "";
                session.setusertoken("");
                session.setUserEmail("");
                session.setusename("");
                if (sessionToken.isEmpty()) {
                    navigationView = findViewById(R.id.nav_view);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.drawer_view_without_login);
                    View header = navigationView.getHeaderView(0);
                    TextView loggedInUserName = header.findViewById(R.id.header_username_tv);
                    TextView loggedInUserEmail = header.findViewById(R.id.email_address_tv);
                    loggedInUserName.setText(R.string.header_name);
                    loggedInUserEmail.setVisibility(View.GONE);
                }
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intentCart = new Intent(this, LoginActivity.class);
                startActivity(intentCart);
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
                                        String productPriceDollar = getResources().getString(R.string.price_dollar_detail) + productPrice;
                                        String imageUrl = e.getString("feature_image");
                                        String productRating = e.getString("rating");
                                        String productWishlist = e.getString("is_whishlit");

                                        Guide currentGuide = new Guide(productId, productName, productPriceDollar, imageUrl, productRating, productWishlist);
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

                                                getViewByPosition(position,listView);

                                                if (viewId == R.id.button_details_two) {
                                                    String productId = listView.getItemAtPosition(position).toString().trim();

                                                    TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                    String productID = PPid.getText().toString().trim();

                                                    Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                    intent.putExtra("ProductId", productID);
                                                    startActivity(intent);
                                                } else if(viewId == R.id.image_favorite) {
                                                    String productId = listView.getItemAtPosition(position).toString().trim();
                                                    TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                    String productID = PPid.getText().toString().trim();
                                                    sendWishlistRequest(productID);
                                                }
                                            }
                                        });

                                        listViewTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                long viewId = view.getId();

                                                getViewByPosition(position,listView);

                                                if (viewId == R.id.button_details_two) {
                                                    String productId = listView.getItemAtPosition(position).toString().trim();
                                                    TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                    String productID = PPid.getText().toString().trim();

                                                    Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                    intent.putExtra("ProductId", productID);
                                                    startActivity(intent);
                                                } else if(viewId == R.id.image_favorite) {
                                                    String productId = listView.getItemAtPosition(position).toString().trim();
                                                    TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                    String productID = PPid.getText().toString().trim();
                                                    sendWishlistRequest(productID);
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

    private void recommendedProductsNetworkRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/home_product.php?deal_of_day=yes";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String trimResponse = response.substring(3);
                            String trimmedResponse = trimResponse.trim();
                            JSONObject jsonObject = new JSONObject(trimmedResponse);
                            String status = jsonObject.getString("status");
                            int statusInt = Integer.parseInt(status);
                            String message = jsonObject.getString("message");
                            if (statusInt == 200) {
                              //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    //Loop the Array
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject currentObject = data.getJSONObject(i);
                                        JSONArray currentProductDetail = currentObject.getJSONArray("product_detail");
                                        for (int j = 0; j < currentProductDetail.length(); j++) {
                                            JSONObject e = currentProductDetail.getJSONObject(j);
                                            String productId = e.getString("product_id");
                                            String productName = e.getString("productsname");
                                            String productPrice = e.getString("price");
                                            String imageUrl = e.getString("feature_image");
                                            String productRating = e.getString("rating");
                                            String productWishlist = e.getString("is_whishlit");

                                            Guide currentGuide = new Guide(productId, productName, productPrice, imageUrl, productRating, productWishlist);
                                            temples.add(currentGuide);
                                            adapter = new GuideAdapter(HomepageActivity.this, temples, R.color.temples_category);
                                            listViewTwo.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                            listViewTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    long viewId = view.getId();

                                                    getViewByPosition(position,listView);

                                                    if (viewId == R.id.button_details_two) {
                                                        String productId = listView.getItemAtPosition(position).toString().trim();
                                                        //     TextView Pid = (TextView) parent.findViewById(R.id.product_id);
                                                        //    TextView PPid = (TextView) listView.getChildAt(position).findViewById(R.id.product_id);
                                                        TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                        String productID = PPid.getText().toString().trim();

                                                        Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                        intent.putExtra("ProductId", productID);
                                                        startActivity(intent);
                                                    } else if(viewId == R.id.image_favorite) {
                                                        String productId = listView.getItemAtPosition(position).toString().trim();
                                                        TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                        String productID = PPid.getText().toString().trim();
                                                        sendWishlistRequest(productID);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            } else if (statusInt == 201) {
                               //
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    private void trendingProductsNetworkRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/home_product.php?trending_product=yes";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String trimResponse = response.substring(3);
                            String trimmedResponse = trimResponse.trim();
                            JSONObject jsonObject = new JSONObject(trimmedResponse);
                            String status = jsonObject.getString("status");
                            int statusInt = Integer.parseInt(status);
                            String message = jsonObject.getString("message");
                            if (statusInt == 200) {
                              //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    //Loop the Array
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject currentObject = data.getJSONObject(i);
                                        JSONArray currentProductDetail = currentObject.getJSONArray("product_detail");
                                        for (int j = 0; j < currentProductDetail.length(); j++) {
                                            JSONObject e = currentProductDetail.getJSONObject(j);
                                            String productId = e.getString("product_id");
                                            String productName = e.getString("productsname");
                                            String productPrice = e.getString("price");
                                            String imageUrl = e.getString("feature_image");
                                            String productRating = e.getString("rating");
                                            String productWishlist = e.getString("is_whishlit");
                                            Guide currentGuide = new Guide(productId, productName, productPrice, imageUrl, productRating, productWishlist);
                                            temples.add(currentGuide);
                                            adapter = new GuideAdapter(HomepageActivity.this, temples, R.color.temples_category);
                                            listViewThree.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                            listViewThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    long viewId = view.getId();

                                                    getViewByPosition(position,listView);

                                                    if (viewId == R.id.button_details_two) {
                                                        String productId = listView.getItemAtPosition(position).toString().trim();
                                                        TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                        String productID = PPid.getText().toString().trim();

                                                        Intent intent = new Intent(HomepageActivity.this, DetailsActivity.class);
                                                        intent.putExtra("ProductId", productID);
                                                        startActivity(intent);
                                                    } else if(viewId == R.id.image_favorite) {
                                                        String productId = listView.getItemAtPosition(position).toString().trim();
                                                        TextView PPid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.product_id);
                                                        String productID = PPid.getText().toString().trim();
                                                        sendWishlistRequest(productID);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            } else if (statusInt == 201) {
                             //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    private void sendWishlistRequest(String pid) {

        // final String userId = String.valueOf(uid);
        final String productId = String.valueOf(pid);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/whishlist_add.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HomepageActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomepageActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        }) { @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", sessionToken);
            params.put("pid", productId);
            return params;
        }
        };
        queue.add(stringRequest);
    }

    private void sendSliderRequest() {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://www.godprice.com/api/slider.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Toast.makeText(HomepageActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            try {
                                String trimResponse = response.substring(3);
                                String trimmedResponse = trimResponse.trim();
                                JSONObject jsonObject = new JSONObject(trimmedResponse);
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    //Loop the Array
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject currentObject = data.getJSONObject(i);
                                        String sliderId = currentObject.getString("id");
                                        String sliderTitle = currentObject.getString("title");
                                        String sliderDescription = currentObject.getString("description");
                                        String sliderImage = currentObject.getString("image");
                                        SliderData sliderData = new SliderData(sliderId, sliderTitle, sliderDescription, sliderImage);
                                        sliderDataItems.add(sliderData);
                                        imageViewPager = (CustomViewPager) findViewById(R.id.viewPager);
                                        ViewPagerApdater viewPagerAdapter = new ViewPagerApdater(HomepageActivity.this, sliderDataItems);
                                        imageViewPager.setAdapter(viewPagerAdapter);
                                        viewPagerAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                //     Log.e("Volley", "Problem parsing the category JSON results", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(HomepageActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

                }

            }) { @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
            };
            queue.add(stringRequest);
    }

    private void sendGridTopCategoryRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/home_category.php?top_category=yes";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
                                    String categoryId = currentObject.getString("m_cid");
                                    String categoryName = currentObject.getString("categoryname");
                                    String categoryImage = currentObject.getString("categoryimage");
                                    GridCategory gridCategory = new GridCategory(categoryId, categoryName, categoryImage);
                                    gridCategories.add(gridCategory);
                                    gridAdapter = new GridAdapter(HomepageActivity.this, gridCategories);
                                    // Get a reference to the GridView, and attach the adapter to the gridView.
                                    GridView gridView = (GridView) findViewById(R.id.gridView);
                                    gridView.setAdapter(gridAdapter);
                                    GridView gridViewTwo = (GridView) findViewById(R.id.gridView_two);
                                    gridViewTwo.setAdapter(gridAdapter);
                                    GridView gridViewThree = (GridView) findViewById(R.id.gridView_three);
                                    gridViewThree.setAdapter(gridAdapter);
                                    gridAdapter.notifyDataSetChanged();
                                    }
                            }
                        } catch (JSONException e) {
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomepageActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        }) { @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            return params;
        }
        };
        queue.add(stringRequest);
    }
}

