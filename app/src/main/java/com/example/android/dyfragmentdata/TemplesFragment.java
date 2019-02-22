package com.example.android.dyfragmentdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TemplesFragment extends Fragment{

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

   // private String productID;

    /**
     * A simple {@link Fragment} subclass.
     */

    public TemplesFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.guide_list, container, false);
          temples = new ArrayList<Guide>();
          categoryNetworkRequest();


        return rootView;
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

        public void categoryNetworkRequest() {
    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    String url = "https://www.godprice.com/api/product_list.php";
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Create an empty ArrayList that we can start adding earthquakes to
                    // List<Guide> guideList = new ArrayList<>();
                //    temples = new ArrayList<Guide>();
                    // Try to parse the JSON response string. If there's a problem with the way the JSON
                    // is formatted, a JSONException exception object will be thrown.
                    // Catch the exception so the app doesn't crash, and print the error message to the logs.

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

                                    String prodID = e.getString("product_id");
                                    String productName = e.getString("productsname");
                                    String productPrice = e.getString("price");
                                    String imageUrl = e.getString("feature_image");
                                    String productRating = e.getString("rating");
                                    String productWishlist = e.getString("is_whishlit");

                                    Guide currentGuide = new Guide(prodID, productName, productPrice, imageUrl, productRating, productWishlist);
                                    temples.add(currentGuide);

                                    adapter = new GuideAdapter(getActivity(), temples, R.color.temples_category);
                                  //  adapter = new GuideAdapter(getContext(), temples, R.color.temples_category);
                                    listView = (ListView) rootView.findViewById(R.id.list);
                                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                                    listView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

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

                                               Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
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

    private void sendWishlistRequest(String pid) {

       // final String userId = String.valueOf(uid);
        final String productId = String.valueOf(pid);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://www.godprice.com/api/whishlist_add.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

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

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://www.godprice.com/api/slider.php";
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
                                    String sliderId = currentObject.getString("id");
                                    String sliderTitle = currentObject.getString("title");
                                    String sliderDescription = currentObject.getString("description");
                                    String sliderImage = currentObject.getString("image");
                                    SliderData sliderData = new SliderData(sliderId, sliderTitle, sliderDescription, sliderImage);
                                    sliderDataItems.add(sliderData);
                                    imageViewPager = (CustomViewPager) rootView.findViewById(R.id.viewPager);
                                    ViewPagerApdater viewPagerAdapter = new ViewPagerApdater(getActivity().getApplicationContext(), sliderDataItems);
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
                Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        }) { @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            return params;
        }
        };
        queue.add(stringRequest);
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
}


