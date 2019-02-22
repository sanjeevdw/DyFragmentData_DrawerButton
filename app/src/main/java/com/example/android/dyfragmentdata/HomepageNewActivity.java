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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomepageNewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Guide> temples;
    private View rootView;
    private GuideAdapter adapter;
    private ListView listView;
    private String sessionToken;
    private int childIndex;
    private static final int GUIDE_LOADER_ID = 1;
    private DrawerLayout mDrawerLayout;
    private ArrayList<HomepageRecommendedCategoryData> gridCategoriesTwo;
    private HomepageRecommendedCategoryAdapter adapterRecommendedCategory;
    private ArrayList<HomepageFeaturedCategoryData> gridCategoriesThree;
    private HomepageFeaturedCategoryAdapter adapterFeaturedCategory;
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
    private NavigationView navigationView;
    private String usernameGoogle;
    private String sessionGoogleEmil;
    private String android_id;
    private SliderData sliderData;
    private ArrayList<SliderData> sliderDataItems;
    private String sessionUserName;
    private String sessionUserEmail;
    private GridView gridView;
    private GridView gridViewTwo;
    private GridView gridViewThree;
    private ArrayList<DealsOfTheDay> dealsOfTheDay;
    private DealsOfTheDayAdapter dealsOfTheDayAdapter;
    private ArrayList<HomepageTrendingProductData> homepageTrendingProductData;
    private HomepageTrendingProductAdapter homepageTrendingProductAdapter;
    private String sessionUserImage;
    private String sessionUserWalletAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new TemplesFragment()).commit();
        Session session = new Session(this);
        sessionToken = session.getusertoken();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
       // sendSliderRequest();
        setNavigationViewListener();
        session = new Session(this);
        sessionToken = session.getusertoken();
        sessionUserName = session.getusename();
        sessionUserEmail = session.getUserEmail();

        sliderDataItems = new ArrayList<SliderData>();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        // setNavigationViewListener();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            usernameGoogle = account.getDisplayName();
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
                sessionUserImage = session.getuserImage();
                if (!sessionUserImage.isEmpty()) {
                    navigationView = findViewById(R.id.nav_view);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.drawer_view_without_login);
                    View header = navigationView.getHeaderView(0);
                    ImageView loggedInUserImage = header.findViewById(R.id.user_image_header);
                    Glide.with(loggedInUserImage.getContext())
                            .load(sessionUserImage)
                            .into(loggedInUserImage);
                }
                showFullNavItem();
            }
        }

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
       // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
          setSupportActionBar(toolbar);
       // setHasOptionsMenu(true);
        toolbar.setBackgroundColor(Color.parseColor("#e53935"));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ActionBar actionbar = getSupportActionBar();

        if (actionbar !=null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        toolbar.findViewById(R.id.toolbar_title);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageNewActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

        mDrawerLayout = rootView.findViewById(R.id.drawer_layout);

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
            navigationView = findViewById(R.id.nav_view);
            navigationView.inflateMenu(R.menu.drawer_view);
            View header = navigationView.getHeaderView(0);
            sessionUserImage = session.getuserImage();
            if (!sessionUserImage.isEmpty()) {
                ImageView loggedInUserImage = header.findViewById(R.id.user_image_header);
                Glide.with(loggedInUserImage.getContext())
                        .load(sessionUserImage)
                        .into(loggedInUserImage);
            }
            showFullNavItem();
        }
        imageViewPager = (CustomViewPager) rootView.findViewById(R.id.viewPager);
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

        final EditText editText = (EditText) rootView.findViewById(R.id.search_box);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.search_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userSearchQuery = editText.getText().toString().trim();
                Intent intent = new Intent(HomepageNewActivity.this, CategoryChildActivity.class);
                intent.putExtra("userSearchQuery", userSearchQuery);
                startActivity(intent);
            }
        });
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
        navigationView = rootView.findViewById(R.id.nav_view);
        String WalletPriceDollar = getResources().getString(R.string.wallet_amount_label) + " " + getResources().getString(R.string.price_dollar_detail) + sessionUserWalletAmount;
        TextView loggedInUserWalletAmount = header.findViewById(R.id.wallet_amount_header);
        if (!sessionUserWalletAmount.isEmpty()) {
            loggedInUserWalletAmount.setText(WalletPriceDollar);
        }
        }

    // NavigationView click events
    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = ((AppCompatActivity)getActivity()).getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_drawer_signin:
                if (!sessionToken.isEmpty()) {
                    Intent intentUpdateProfile = new Intent(HomepageNewActivity.this, ProfileActivity.class);
                    startActivity(intentUpdateProfile);

                } else {
                    Intent intent = new Intent(HomepageNewActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
                return true;
            case R.id.action_drawer_cart:
                Intent intentCart = new Intent(HomepageNewActivity.this, CartActivity.class);
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
                Intent intent = new Intent(HomepageNewActivity.this, HomepageActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_master_category:
                Intent intentMasterCategory = new Intent(HomepageNewActivity.this, MasterCategoryActivity.class);
                startActivity(intentMasterCategory);
                break;
            case R.id.nav_login:
                Intent intentLogin = new Intent(HomepageNewActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.nav_register:
                Intent intentRegister = new Intent(HomepageNewActivity.this, SignupActivity.class);
                startActivity(intentRegister);
                break;

            case R.id.nav_profile:
                Intent intentProfile = new Intent(HomepageNewActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.nav_forgot_password:
                Intent intentForgotPassword = new Intent(HomepageNewActivity.this, ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
                break;
            case R.id.nav_change_password:
                Intent intentChangePassword = new Intent(HomepageNewActivity.this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.nav_wishlist:
                Intent intentWishlist = new Intent(HomepageNewActivity.this, WishlistActivity.class);
                startActivity(intentWishlist);
                break;

            case R.id.nav_checkout:
                Intent intentCheckout = new Intent(HomepageNewActivity.this, CheckoutActivity.class);
                startActivity(intentCheckout);
                break;
            case R.id.nav_order_history:
                Intent intentOrderHistory = new Intent(HomepageNewActivity.this, OrderHistoryListingActivity.class);
                startActivity(intentOrderHistory);
                break;
            case R.id.nav_transaction:
                Intent intentTransaction = new Intent(HomepageNewActivity.this, TransactionActivity.class);
                startActivity(intentTransaction);
                break;
            case R.id.nav_footer_merchant:
                Intent intentMechantLogin = new Intent(HomepageNewActivity.this, MerchantLoginActivity.class);
                startActivity(intentMechantLogin);
                break;
            case R.id.nav_footer_delivery:
                Intent intentDelivery = new Intent(HomepageNewActivity.this, DeliveryActivity.class);
                startActivity(intentDelivery);
                break;

            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(HomepageNewActivity.this);
                Toast.makeText(HomepageNewActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                sessionToken = "";
                session.setusertoken("");
                session.setUserEmail("");
                session.setusename("");
                session.setuserImage("");
                if (sessionToken.isEmpty()) {
                    navigationView = rootView.findViewById(R.id.nav_view);
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
                Intent intentCart = new Intent(HomepageNewActivity.this, LoginActivity.class);
                startActivity(intentCart);
                break;
        }
        return false;
    }
}

