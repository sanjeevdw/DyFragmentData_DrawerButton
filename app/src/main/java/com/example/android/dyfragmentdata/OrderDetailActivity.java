package com.example.android.dyfragmentdata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class OrderDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private Session session;
    private String sessionToken;
    private NavigationView navigationView;
    private String usernameGoogle;
    private String sessionGoogleEmil;
    private ListView listView;
    private ArrayList<OrderDetailData> orderDetailData;
    private OrderDetailAdapter orderDetailAdapter;
    private String fullname;
    private String emailid;
    private String phoneno;
    private String address;
    private String country;
    private String city;
    private String zipcode;
    private String phoneno_alternative;
    private String productId;
    private String productName;
    private String productSize;
    private String productColor;
    private String productQuantity;
    private String productTotalPrice;
    private String productPrice;
    private String invoiceno;
    private int childIndex;
    private String invoicenoIntent;
    private String sessionUserName;
    private String sessionUserEmail;
    private String sessionUserImage;
    private String sessionUserWalletAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_category.xml layout file
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#e53935"));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ActionBar actionbar = getSupportActionBar();
        session = new Session(this);
        sessionToken = session.getusertoken();
        orderDetailData = new ArrayList<OrderDetailData>();

        Intent invoiceNoIntent = getIntent();
        Bundle bundle = invoiceNoIntent.getExtras();

        if (bundle != null) {
            invoicenoIntent = (String) bundle.get("invoiceNoClicked");
            orderHistoryNetworkRequest();
        }

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        toolbar.findViewById(R.id.toolbar_title);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            usernameGoogle = account.getDisplayName();
            sessionToken = usernameGoogle;
            sessionGoogleEmil = account.getEmail();
            if (sessionToken.isEmpty()) {
                navigationView = findViewById(R.id.nav_view);
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.drawer_view_without_login);
            }

            if (!sessionToken.isEmpty()) {
                showFullNavItem();
            }
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        if (sessionToken.isEmpty()) {
            navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view_without_login);
        }

        if (!sessionToken.isEmpty()) {
            showFullNavItem();
        }

        setNavigationViewListener();
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
            sessionUserWalletAmount = session.getuserWalletAmount();
            navigationView = findViewById(R.id.nav_view);
            String WalletPriceDollar = getResources().getString(R.string.wallet_amount_label) + " " + getResources().getString(R.string.price_dollar_detail) + sessionUserWalletAmount;
            TextView loggedInUserWalletAmount = header.findViewById(R.id.wallet_amount_header);
            if (!sessionUserWalletAmount.isEmpty()) {
                loggedInUserWalletAmount.setText(WalletPriceDollar);
            }
            ImageView loggedInUserImage = header.findViewById(R.id.user_image_header);
            sessionUserImage = session.getuserImage();
            if (!sessionUserImage.isEmpty()) {
                Glide.with(loggedInUserImage.getContext())
                        .load(sessionUserImage)
                        .into(loggedInUserImage);
            }

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
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                if (sessionToken.isEmpty()) {
                    navigationView = findViewById(R.id.nav_view);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.drawer_view_without_login);
                } else {
                    showFullNavItem();
                }
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

        switch (id) {

            case R.id.nav_home:
                Intent intent = new Intent(this, HomepageActivity.class);
                startActivity(intent);
                setNavigationViewListener();
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

            case R.id.nav_checkout:
                Intent intentCheckout = new Intent(this, CheckoutActivity.class);
                startActivity(intentCheckout);
                break;

            case R.id.nav_order_history:
                Intent intentOrderHistory = new Intent(this, OrderHistoryListingActivity.class);
                startActivity(intentOrderHistory);
                break;

            case R.id.nav_footer_merchant:
                Intent intentMechantLogin = new Intent(this, MerchantLoginActivity.class);
                startActivity(intentMechantLogin);
                break;
            case R.id.nav_footer_delivery:
                Intent intentDelivery = new Intent(this, DeliveryActivity.class);
                startActivity(intentDelivery);
                break;
            case R.id.nav_transaction:
                Intent intentTransaction = new Intent(this, TransactionActivity.class);
                startActivity(intentTransaction);
                break;

            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
                sessionToken = "";
                session.setusertoken("");
                session.setUserEmail("");
                session.setusename("");
                session.setuserImage("");
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

    private void orderHistoryNetworkRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://www.godprice.com/api/orderdetail.php?invoiceno="+invoicenoIntent+"&userid="+sessionToken;
      //  String url = "https://www.godprice.com/api/orderhistory.php?userid="+sessionToken;
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
                                for (int l = 0; l < data.length(); l++) {
                                    JSONObject currentObject = data.getJSONObject(l);
                                    JSONArray shippingDetail = currentObject.getJSONArray("shipping_detail");
                                    if (shippingDetail.length() > 0) {
                                        //Loop the Array
                                        for (int m = 0; m < shippingDetail.length(); m++) {
                                            JSONObject currentShippingDetail = shippingDetail.getJSONObject(m);
                                            invoiceno = currentShippingDetail.getString("invoiceno");
                                            fullname = currentShippingDetail.getString("fullname");
                                            emailid = currentShippingDetail.getString("emailid");
                                            phoneno = currentShippingDetail.getString("phoneno");
                                            address = currentShippingDetail.getString("address");
                                            country = currentShippingDetail.getString("country");
                                            city = currentShippingDetail.getString("city");
                                            zipcode = currentShippingDetail.getString("zipcode");
                                            phoneno_alternative = currentShippingDetail.getString("phoneno_alternative");

                                        }
                                    }
                                    JSONArray orderDetail = currentObject.getJSONArray("orderdetail");
                                    if (orderDetail.length() > 0) {
                                        //Loop the Array
                                        for (int n = 0; n < orderDetail.length(); n++) {
                                            JSONObject currentOrderDetail = orderDetail.getJSONObject(n);
                                            productId = currentOrderDetail.getString("id");
                                            productName = currentOrderDetail.getString("products");
                                            productQuantity = currentOrderDetail.getString("qty");
                                            productPrice = currentOrderDetail.getString("price");
                                            productTotalPrice = currentOrderDetail.getString("total_price");
                                           String productPriceString = getResources().getString(R.string.price_dollar_detail) + productPrice;
                                            String productTotalPriceString = getResources().getString(R.string.price_dollar_detail) + productTotalPrice;
                                            //  JSONArray productAttribute = currentOrderDetail.getJSONArray("pro_att");
                                            //Loop the Array
                                          //  for (int j = 0; j < productAttribute.length(); j++) {
                                           //     JSONObject currentProductAttribute = productAttribute.getJSONObject(j);
                                           //     productSize = currentProductAttribute.getString("Size");
                                              //   productColor = currentProductAttribute.getString("Color");
                                                OrderDetailData currentOrderDetailData = new OrderDetailData(productId, invoiceno, fullname, emailid, phoneno, address, country, city, zipcode, phoneno_alternative, productName, productSize, productColor, productQuantity, productPriceString, productTotalPriceString);
                                                orderDetailData.add(currentOrderDetailData);
                                                orderDetailAdapter = new OrderDetailAdapter(OrderDetailActivity.this, orderDetailData);
                                              //  Toast.makeText(OrderDetailActivity.this, "Order history response", Toast.LENGTH_SHORT).show();
                                                listView = (ListView) findViewById(R.id.order_detail_list);
                                                listView.setAdapter(orderDetailAdapter);
                                                orderDetailAdapter.notifyDataSetChanged();
                                                listView.setNestedScrollingEnabled(true);
                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        long viewId = view.getId();

                                                        if (viewId == R.id.back_label) {
                                                       //     Toast.makeText(OrderDetailActivity.this, "Back button clicked", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(OrderDetailActivity.this, OrderHistoryListingActivity.class);
                                                            startActivity(intent);
                                                        }

                                                        if (viewId == R.id.chat_button_label) {
                                                            getViewByPosition(position,listView);
                                                            String productId = listView.getItemAtPosition(position).toString().trim();
                                                            TextView Cartid = (TextView) listView.getChildAt(childIndex).findViewById(R.id.cart_id);
                                                            String cartID = Cartid.getText().toString().trim();

                                                            Intent intentChat = new Intent(OrderDetailActivity.this, MerchantHeaderActivity.class);
                                                            intentChat.putExtra("cartId", cartID);
                                                                    startActivity(intentChat);
                                                                    }
                                                    }
                                                });
                                           // }
                                        }
                                    }
                                }
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
