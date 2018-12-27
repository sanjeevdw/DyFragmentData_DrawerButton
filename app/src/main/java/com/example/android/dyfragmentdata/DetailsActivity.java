package com.example.android.dyfragmentdata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class DetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ArrayList<Guide> temples;
    private GuideAdapter adapter;
    private ListView listView;
    private ImageView mainImageView;
    private ArrayList<ProductDetails> products;
    private ProductAdapter productAdapter;
    private String pid;
    private Session session;
    private String sessionToken;
    private NavigationView navigationView;
    private String usernameGoogle;
    private String sessionGoogleEmil;
    private String currentAttributeColor;
    private String attributeColorValue;
    private String currentAttributeSize;
    private String attributeSizeValue;
    private String galleryThumbnail;
    private String colorButtonText;
    private String sizeButtonText;
    private int k;
    private int m;
    private ArrayList<String> ColorButtonText = new ArrayList<String>();
    private String attributeIdColor;
    private String isSelectedColor;
    private String isAttAvailableColor;
    private String attributeIdSize;
    private String isSelectedSize;
    private String isAttAvailableSize;
    private String colorClickedAttribute;
    private String sizeClickedAttribute;
    private String android_id;
    private EditText editTextQuantity;
    private String proQuantityIdCart;
    private String priceCart;
    private String currentQuantityCart;
    private int quantityProInt;
    private int cartQuantity;
    private String finalquantityCart;
    private String quantityCart;
    private int colorSelectedId;
    private ArrayList<ProductSpecificationData> specificationData;
    private SpecAdapter specAdapter;
    private ListView specListView;
    private LinearLayout sizeLayout;
    private LinearLayout colorLayout;
    private String galleryThumbnailNew;
    private LinearLayout imageLayout;
    private int childIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_category.xml layout file
        setContentView(R.layout.details_activity);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);
        Intent productIdIntent = getIntent();
        Bundle bundle = productIdIntent.getExtras();

        if (bundle != null) {
            pid = (String) bundle.get("ProductId");
        }
        session = new Session(this);
        sessionToken = session.getusertoken();

        relatedProductsNetworkRequest();
        listView = (ListView) findViewById(R.id.list);
        temples = new ArrayList<Guide>();
        products = new ArrayList<ProductDetails>();
        specificationData = new ArrayList<ProductSpecificationData>();
        specListView = (ListView) findViewById(R.id.spec_list);
        listView.setNestedScrollingEnabled(true);
        specListView.setNestedScrollingEnabled(true);
        productsDetailsRequest();
        setNavigationViewListener();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#e53935"));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ActionBar actionbar = getSupportActionBar();

        if (actionbar !=null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.back_arrow);
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

        editTextQuantity = (EditText) findViewById(R.id.quantity_et);

        Button buttonCart = (Button) findViewById(R.id.add_to_cart_button);

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityCart = editTextQuantity.getText().toString();
                if (TextUtils.isEmpty(quantityCart)) {
                    Toast.makeText(DetailsActivity.this, "Please enter the quantity", Toast.LENGTH_LONG).show();
                }
                else if (!TextUtils.isEmpty(quantityCart)) {
                    cartQuantity = Integer.parseInt(quantityCart);
                    if (cartQuantity > quantityProInt) {

                        if (quantityProInt == 0) {
                            Toast.makeText(DetailsActivity.this, "Out of Stock", Toast.LENGTH_LONG).show();
                        }
                    Toast.makeText(DetailsActivity.this, "Please enter quantity less than "+ quantityProInt, LENGTH_SHORT).show();
                    }
                    addToCartRequest();
                }
                }
        });

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
                Intent templesIntent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(templesIntent);
                //mDrawerLayout.openDrawer(GravityCompat.START);
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
                setNavigationViewListener();
                break;
            case R.id.nav_category:
                Intent intentCategory = new Intent(this, MainActivity.class);
                startActivity(intentCategory);
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
                Toast.makeText(this, "NavigationClick", LENGTH_SHORT).show();
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
                Toast.makeText(this, "Signed out", LENGTH_SHORT).show();
                sessionToken = "";
                session.setusertoken("");
                if (sessionToken.isEmpty()) {
                    navigationView = findViewById(R.id.nav_view);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.drawer_view_without_login);
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
                                        JSONObject e = currentProductDetail.getJSONObject(j);
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
        final Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.godprice.com")
                .appendPath("api")
                .appendPath("products.php")
                .appendQueryParameter("product_id", pid);
        String myUrl = builder.build().toString();
        String url = myUrl;
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
                                    JSONObject currentProductDetail = currentObject.getJSONObject("product_detail");
                                    String ProductDetailsId = currentProductDetail.getString("product_id");
                                    String productDetailsSku = currentProductDetail.getString("skucode");
                                    String productDetailsName = currentProductDetail.getString("productsname");
                                    String productDetailsDescription = currentProductDetail.getString("description");
                                    String productDetailsPrice = currentProductDetail.getString("discount_percent");
                                    String imageUrlDetails = currentProductDetail.getString("featured_image");

                                    JSONArray galleryThumbnailArray = currentObject.getJSONArray("gallery");
                                    if (galleryThumbnailArray.length() > 0) {
                                        //Loop the Array
                                        for (int b = 0; b < galleryThumbnailArray.length(); b++) {
                                            JSONObject galleryObject = galleryThumbnailArray.getJSONObject(b);
                                            galleryThumbnail = galleryObject.getString("gallery_image");
                                            imageLayout = (LinearLayout) findViewById(R.id.thumbnail_image_container);
                                            for(int a=0;a<galleryThumbnailArray.length();a++)
                                            {
                                                ImageView image = new ImageView(DetailsActivity.this);
                                                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
                                                image.setMaxHeight(60);
                                                image.setMaxWidth(60);
                                                Glide.with(image.getContext())
                                                        .load(galleryThumbnail)
                                                        .into(image);

                                                // Adds the view to the layout
                                                imageLayout.addView(image);
                                            }
                                            }
                                    }

                                    JSONArray specObject = currentObject.getJSONArray("specification");

                                    for (int d = 0; d < specObject.length(); d++) {
                                        JSONObject currentObjectSpec = specObject.getJSONObject(d);
                                        String currentObjectSpecHeading = currentObjectSpec.getString("specification_heading");
                                        String currentObjectSpecValue = currentObjectSpec.getString("specification_value");
                                        ProductSpecificationData currentProductSpec = new ProductSpecificationData(currentObjectSpecHeading, currentObjectSpecValue);
                                        specificationData.add(currentProductSpec);
                                        specAdapter = new SpecAdapter(DetailsActivity.this, specificationData);
                                        specListView.setAdapter(specAdapter);
                                        specAdapter.notifyDataSetChanged();
                                        }

                                    JSONArray attributeObject = currentObject.getJSONArray("attribute");
                                    for (int c = 0; c < attributeObject.length(); c++) {
                                        JSONObject currentObjectValue = attributeObject.getJSONObject(c);
                                        JSONObject currentObjectOne = currentObjectValue.getJSONObject("1");
                                        currentAttributeColor = currentObjectOne.getString("attribute_name");

                                        TextView attributeColorLabelView = (TextView) findViewById(R.id.attribute_label_text_view);
                                        attributeColorLabelView.setText(currentAttributeColor);
                                        JSONArray attributeValueArrayColor = currentObjectOne.getJSONArray("attribute_value");

                                        if (attributeValueArrayColor.length() > 0) {
                                            //Loop the Array
                                            for (k = 0; k < attributeValueArrayColor.length(); k++) {
                                                JSONObject currentObjectValueOne = attributeValueArrayColor.getJSONObject(k);
                                                attributeColorValue = currentObjectValueOne.getString("attribute_val");
                                                attributeIdColor = currentObjectValueOne.getString("attribute_id");
                                                isSelectedColor = currentObjectValueOne.getString("is_selected");
                                                isAttAvailableColor = currentObjectValueOne.getString("is_att_availble");
                                                final String attributeOne  = currentObjectValueOne.getString("att1");
                                                int colorNonSelectedId = Integer.parseInt(isAttAvailableColor);
                                               // stringBuilder.append(attributeColorValue);
                                             //   stringBuilder.append(" ");
                                                colorLayout = (LinearLayout) findViewById(R.id.text_color_container_two);
                                                final Button button = new Button(DetailsActivity.this);
                                                button.setId(k+1);
                                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63));
                                                LinearLayout.LayoutParams buttonMargin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63);
                                                buttonMargin.setMargins(15, 0, 0, 0);
                                                button.setText(attributeColorValue);
                                                button.setLayoutParams(buttonMargin);
                                                button.setTextSize(13);
                                                button.setTextColor(Color.RED);

                                                colorSelectedId = Integer.parseInt(isSelectedColor);
                                                if (colorSelectedId == 1) {
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button));
                                                 //   colorButtonText = button.getText().toString();
                                                    colorClickedAttribute = attributeOne;
                                                } else if (colorSelectedId == 0) {
                                                  //  button.setBackgroundColor(Color.GRAY);
                                                    button.setTextColor(Color.parseColor("#424242"));
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button_gray));

                                                }
                                                else if (colorNonSelectedId == 1) {
                                                  //  button.setBackgroundColor(Color.GRAY);
                                                    button.setTextColor(Color.parseColor("#424242"));
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button_gray));
                                                }  else if (colorNonSelectedId == 0) {
                                                //    button.setBackgroundColor(Color.WHITE);
                                                   // button.setTextColor(Color.parseColor("#ffffffff"));
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button_white));
                                                }
                                                final int index = k;
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        colorButtonText = button.getText().toString();
                                                        colorClickedAttribute = attributeOne;
                                                        colorLayout.removeAllViews();
                                                      //  imageLayout.removeAllViews();
                                                         Toast.makeText(DetailsActivity.this, "Index " + index, LENGTH_SHORT).show();
                                                       productsAttributesRequest();
                                                    }
                                                });
                                                colorLayout.addView(button);
                                            }
                                        }

                                        JSONObject currentObjectSize = currentObjectValue.getJSONObject("2");
                                        currentAttributeSize = currentObjectSize.getString("attribute_name");

                                        TextView attributeSizeLabelView = (TextView) findViewById(R.id.attribute_size_label_text_view);
                                        attributeSizeLabelView.setText(currentAttributeSize);
                                        JSONArray attributeValueArraySize = currentObjectSize.getJSONArray("attribute_value");
                                        if (attributeValueArraySize.length() > 0) {
                                            //Loop the Array
                                            for (m = 0; m < attributeValueArraySize.length(); m++) {
                                                JSONObject currentObjectValueSize = attributeValueArraySize.getJSONObject(m);
                                                attributeSizeValue = currentObjectValueSize.getString("attribute_val");
                                                attributeIdSize = currentObjectValueSize.getString("attribute_id");
                                                isSelectedSize = currentObjectValueSize.getString("is_selected");
                                                isAttAvailableSize = currentObjectValueSize.getString("is_att_availble");
                                                final String attributeTwo  = currentObjectValueSize.getString("att2");
                                                //   TextView attributeSizeValueView = (TextView) findViewById(R.id.attribute_size_value_text_view);
                                                sizeLayout = (LinearLayout) findViewById(R.id.text_size_container);
                                                final Button buttonSize = new Button(DetailsActivity.this);
                                                buttonSize.setId(m+1);
                                                buttonSize.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63));
                                                LinearLayout.LayoutParams buttonMargin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63);
                                                buttonMargin.setMargins(15, 0, 0, 0);
                                                buttonSize.setText(attributeSizeValue);
                                                buttonSize.setLayoutParams(buttonMargin);
                                                buttonSize.setTextSize(13);
                                                buttonSize.setTextColor(Color.RED);
                                                int sizeNonSelectedId = Integer.parseInt(isAttAvailableSize);
                                                int sizeSelectedId = Integer.parseInt(isSelectedSize);
                                                if (sizeSelectedId == 1) {
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button));
                                                    sizeClickedAttribute = attributeTwo;
                                                    //   buttonSize.setBackgroundColor(Color.YELLOW);
                                                } else if (sizeSelectedId == 0) {
                                                    buttonSize.setTextColor(Color.parseColor("#424242"));
                                                    sizeButtonText = buttonSize.getText().toString();
                                                 //   buttonSize.setBackgroundColor(Color.GRAY);
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button_gray));
                                                }
                                                else if (sizeNonSelectedId == 1) {
                                                    buttonSize.setTextColor(Color.parseColor("#424242"));
                                                 //   buttonSize.setBackgroundColor(Color.GRAY);
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button_gray));
                                                }  else if (sizeNonSelectedId == 0) {
                                                 //   buttonSize.setBackgroundColor(Color.WHITE);
                                                   // buttonSize.setTextColor(Color.parseColor("#ffffffff"));
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button_white));
                                                }
                                                final int indexTwo = m;
                                                buttonSize.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        sizeButtonText = buttonSize.getText().toString();
                                                        sizeClickedAttribute = attributeTwo;
                                                        Toast.makeText(DetailsActivity.this, "Index " + indexTwo, LENGTH_SHORT).show();
                                                        productsAttributesRequest();
                                                        sizeLayout.removeAllViews();
                                                       // imageLayout.removeAllViews();
                                                    }
                                                });
                                                sizeLayout.addView(buttonSize);
                                            }
                                        }
                                        }

                                    StringBuilder stringBuilder = new StringBuilder();
                                    StringBuilder stringBuilderValue = new StringBuilder();

                                    JSONObject currentObjectCart = currentObject.getJSONObject("cart_parameter");
                                    String productIdCart = currentObjectCart.getString("pid");
                                    proQuantityIdCart = currentObjectCart.getString("pro_qty_id");
                                    priceCart = currentObjectCart.getString("price");
                                    currentQuantityCart = currentObjectCart.getString("current_qty");
                                    quantityProInt = Integer.parseInt(currentQuantityCart);

                                    TextView productSkuView = (TextView) findViewById(R.id.sku);
                                    productSkuView.setText(getResources().getString(R.string.product_sku_detail) + " " + productDetailsSku);

                                    TextView productNameView = (TextView) findViewById(R.id.product_name_view);
                                    productNameView.setText(productDetailsName);

                                    TextView PriceView = (TextView) findViewById(R.id.product_price_view);
                                    PriceView.setText(getResources().getString(R.string.price_dollar_detail) + priceCart);

                                    TextView descriptionView = (TextView) findViewById(R.id.description_tv);
                                    descriptionView.setText(productDetailsDescription);

                                    ImageView productImageView = (ImageView) findViewById(R.id.main_image);

                                    Glide.with(productImageView.getContext())
                                            .load(galleryThumbnail)
                                            .into(productImageView);

                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                                                Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
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

                        } catch (JSONException e) {
                            // If an error is thrown when executing any of the above statements in the "try" block,
                            // catch the exception here, so the app doesn't crash. Print a log message
                            // with the message from the exception.
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }

                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        });
        queue.add(stringRequest);
    }

    private void productsAttributesRequest() {

        colorLayout.removeAllViews();
        sizeLayout.removeAllViews();
        imageLayout.removeAllViews();
        RequestQueue queue = Volley.newRequestQueue(this);
        final Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.godprice.com")
                .appendPath("api")
                .appendPath("products.php")
                .appendQueryParameter("product_id", pid)
                .appendQueryParameter("att1", colorClickedAttribute)
                .appendQueryParameter("att2", sizeClickedAttribute);
        String myUrl = builder.build().toString();
        String url = myUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                         //   Toast.makeText(DetailsActivity.this, "Attribute response", LENGTH_SHORT).show();
                            String trimResponse = response.substring(3);
                            String trimmedResponse = trimResponse.trim();
                            JSONObject jsonObject = new JSONObject(trimmedResponse);
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                //Loop the Array
                                for (int l = 0; l < data.length(); l++) {
                                    JSONObject currentObject = data.getJSONObject(l);
                                    JSONObject currentProductDetail = currentObject.getJSONObject("product_detail");
                                    String ProductDetailsId = currentProductDetail.getString("product_id");
                                    String productDetailsSku = currentProductDetail.getString("skucode");
                                    String productDetailsName = currentProductDetail.getString("productsname");
                                    String productDetailsDescription = currentProductDetail.getString("description");
                                    String productDetailsPrice = currentProductDetail.getString("discount_percent");
                                    String imageUrlDetails = currentProductDetail.getString("featured_image");

                                    JSONArray galleryThumbnailArray = currentObject.getJSONArray("gallery");

                                    if (galleryThumbnailArray.length() > 0) {
                                        //Loop the Array
                                        for (int b = 0; b < galleryThumbnailArray.length(); b++) {
                                            JSONObject galleryObject = galleryThumbnailArray.getJSONObject(b);
                                            galleryThumbnailNew = galleryObject.getString("gallery_image");
                                            imageLayout = (LinearLayout) findViewById(R.id.thumbnail_image_container);
                                            imageLayout.setVisibility(LinearLayout.VISIBLE);
                                            for(int a=0;a<galleryThumbnailArray.length();a++)
                                            {
                                                ImageView image = new ImageView(DetailsActivity.this);
                                                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                                                image.setMaxHeight(100);
                                                image.setMaxWidth(100);
                                                Glide.with(image.getContext())
                                                        .load(galleryThumbnailNew)
                                                        .into(image);

                                                // Adds the view to the layout
                                                imageLayout.addView(image);
                                            }
                                        }
                                    }

                                    JSONArray attributeObject = currentObject.getJSONArray("attribute");
                                    for (int g = 0; g < attributeObject.length(); g++) {
                                        JSONObject currentObjectValueNew = attributeObject.getJSONObject(g);
                                        JSONObject currentObjectOneNew = currentObjectValueNew.getJSONObject("1");
                                        currentAttributeColor = currentObjectOneNew.getString("attribute_name");
                                        JSONArray attributeValueArrayColorNew = currentObjectOneNew.getJSONArray("attribute_value");
                                        if (attributeValueArrayColorNew.length() > 0) {
                                            //Loop the Array
                                            for (int x = 0; x < attributeValueArrayColorNew.length(); x++) {
                                                JSONObject currentObjectValueOneNew = attributeValueArrayColorNew.getJSONObject(x);
                                                attributeColorValue = currentObjectValueOneNew.getString("attribute_val");
                                                attributeIdColor = currentObjectValueOneNew.getString("attribute_id");
                                                isSelectedColor = currentObjectValueOneNew.getString("is_selected");
                                                isAttAvailableColor = currentObjectValueOneNew.getString("is_att_availble");
                                                final String attributeOne = currentObjectValueOneNew.getString("att1");
                                                int colorNonSelectedId = Integer.parseInt(isAttAvailableColor);
                                                // stringBuilder.append(attributeColorValue);
                                                //   stringBuilder.append(" ");
                                                LinearLayout colorLayout = (LinearLayout) findViewById(R.id.text_color_container_two);
                                                LinearLayout.LayoutParams buttonMargin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63);
                                                buttonMargin.setMargins(15, 0, 0, 0);
                                                final Button button = new Button(DetailsActivity.this);
                                                button.setId(x + 1);
                                                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63));
                                                button.setText(attributeColorValue);
                                                button.setTextSize(13);

                                                button.setLayoutParams(buttonMargin);
                                                button.setTextColor(Color.RED);
                                                colorLayout.setVisibility(LinearLayout.VISIBLE);
                                                colorSelectedId = Integer.parseInt(isSelectedColor);
                                                if (colorSelectedId == 1) {
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button));
                                                    colorButtonText = button.getText().toString();
                                                    colorClickedAttribute = attributeOne;
                                                } else if (colorSelectedId == 0) {
                                                    //  button.setBackgroundColor(Color.GRAY);
                                                    button.setTextColor(Color.parseColor("#424242"));
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button_gray));

                                                } else if (colorNonSelectedId == 1) {
                                                    //  button.setBackgroundColor(Color.GRAY);
                                                    button.setTextColor(Color.parseColor("#424242"));
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button_gray));
                                                } else if (colorNonSelectedId == 0) {
                                                    //    button.setBackgroundColor(Color.WHITE);
                                                    // button.setTextColor(Color.parseColor("#ffffffff"));
                                                    button.setBackground(getResources().getDrawable(R.drawable.details_button_white));
                                                }

                                                final int index = x;
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        colorButtonText = button.getText().toString();
                                                        colorClickedAttribute = attributeOne;
                                                        productsAttributesRequest();
                                                        Toast.makeText(DetailsActivity.this, "Index" + index, LENGTH_SHORT).show();
                                                    }
                                                });
                                                colorLayout.addView(button);
                                            }
                                        }

                                        JSONObject currentObjectSizeNew = currentObjectValueNew.getJSONObject("2");
                                        currentAttributeSize = currentObjectSizeNew.getString("attribute_name");

                                        JSONArray attributeValueArraySizeNew = currentObjectSizeNew.getJSONArray("attribute_value");
                                        if (attributeValueArraySizeNew.length() > 0) {
                                            //Loop the Array
                                            for (int y = 0; y < attributeValueArraySizeNew.length(); y++) {
                                                JSONObject currentObjectValueSizeNew = attributeValueArraySizeNew.getJSONObject(y);
                                                attributeSizeValue = currentObjectValueSizeNew.getString("attribute_val");
                                                attributeIdSize = currentObjectValueSizeNew.getString("attribute_id");
                                                isSelectedSize = currentObjectValueSizeNew.getString("is_selected");
                                                isAttAvailableSize = currentObjectValueSizeNew.getString("is_att_availble");
                                                final String attributeTwo = currentObjectValueSizeNew.getString("att2");

                                                //   TextView attributeSizeValueView = (TextView) findViewById(R.id.attribute_size_value_text_view);
                                                LinearLayout sizeLayout = (LinearLayout) findViewById(R.id.text_size_container);

                                                final Button buttonSize = new Button(DetailsActivity.this);
                                                buttonSize.setId(y + 1);
                                                buttonSize.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63));
                                                LinearLayout.LayoutParams buttonMargin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 63);
                                                buttonMargin.setMargins(15, 0, 0, 0);
                                                buttonSize.setText(attributeSizeValue);
                                                buttonSize.setLayoutParams(buttonMargin);
                                                buttonSize.setTextSize(13);
                                                buttonSize.setTextColor(Color.RED);
                                                sizeLayout.setVisibility(LinearLayout.VISIBLE);
                                                int sizeNonSelectedId = Integer.parseInt(isAttAvailableSize);
                                                int sizeSelectedId = Integer.parseInt(isSelectedSize);
                                                if (sizeSelectedId == 1) {
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button));
                                                    sizeClickedAttribute = attributeTwo;
                                                    //   buttonSize.setBackgroundColor(Color.YELLOW);
                                                } else if (sizeSelectedId == 0) {
                                                    buttonSize.setTextColor(Color.parseColor("#424242"));
                                                    //   buttonSize.setBackgroundColor(Color.GRAY);
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button_gray));
                                                } else if (sizeNonSelectedId == 1) {
                                                    buttonSize.setTextColor(Color.parseColor("#424242"));
                                                    //   buttonSize.setBackgroundColor(Color.GRAY);
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button_gray));
                                                } else if (sizeNonSelectedId == 0) {
                                                    //   buttonSize.setBackgroundColor(Color.WHITE);
                                                    // buttonSize.setTextColor(Color.parseColor("#ffffffff"));
                                                    buttonSize.setBackground(getResources().getDrawable(R.drawable.details_button_white));
                                                }
                                                final int index = y;
                                                buttonSize.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        sizeButtonText = buttonSize.getText().toString();
                                                        sizeClickedAttribute = attributeTwo;
                                                        Toast.makeText(DetailsActivity.this, "Index" + index, LENGTH_SHORT).show();
                                                        productsAttributesRequest();
                                                    }
                                                });
                                                sizeLayout.addView(buttonSize);
                                            }
                                        }
                                    }

                                    JSONObject currentObjectCart = currentObject.getJSONObject("cart_parameter");
                                    String productIdCart = currentObjectCart.getString("pid");
                                    proQuantityIdCart = currentObjectCart.getString("pro_qty_id");
                                    priceCart = currentObjectCart.getString("price");
                                    currentQuantityCart = currentObjectCart.getString("current_qty");
                                    quantityProInt = Integer.parseInt(currentQuantityCart);

                                    TextView productSkuView = (TextView) findViewById(R.id.sku);
                                    productSkuView.setText(getResources().getString(R.string.product_sku_detail) + " " + productDetailsSku);

                                    TextView productNameView = (TextView) findViewById(R.id.product_name_view);
                                    productNameView.setText(productDetailsName);

                                    TextView PriceView = (TextView) findViewById(R.id.product_price_view);
                                    PriceView.setText(getResources().getString(R.string.price_dollar_detail) + priceCart);

                                    TextView descriptionView = (TextView) findViewById(R.id.description_tv);
                                    descriptionView.setText(productDetailsDescription);

                                    ImageView productImageView = (ImageView) findViewById(R.id.main_image);

                                    Glide.with(productImageView.getContext())
                                            .load(galleryThumbnailNew)
                                            .into(productImageView);
                                    }
                                        }
                                        } catch (Exception e) {
                            // If an error is thrown when executing any of the above statements in the "try" block,
                            // catch the exception here, so the app doesn't crash. Print a log message
                            // with the message from the exception.
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }
                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }
            });
        queue.add(stringRequest);
    }

    private void addToCartRequest() {

        if (cartQuantity < quantityProInt) {
            finalquantityCart = editTextQuantity.getText().toString();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://www.godprice.com/api/add_cart.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Intent intentCart = new Intent(DetailsActivity.this, CartActivity.class);
                            startActivity(intentCart);
                            Toast.makeText(DetailsActivity.this, "Added to cart successfully", LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // If an error is thrown when executing any of the above statements in the "try" block,
                            // catch the exception here, so the app doesn't crash. Print a log message
                            // with the message from the exception.
                            //     Log.e("Volley", "Problem parsing the category JSON results", e);
                        }
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
            params.put("device_id", android_id);
            params.put("pid", pid);
            params.put("pro_qty_id", proQuantityIdCart);
            params.put("qty", finalquantityCart);
            params.put("price", priceCart);
            return params;
        }
        };
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
                        Toast.makeText(DetailsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

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
}




