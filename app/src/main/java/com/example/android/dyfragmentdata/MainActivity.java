package com.example.android.dyfragmentdata;

import android.support.design.widget.TabLayout;
import android.support.v4.app.BundleCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
public Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_category.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments.

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page.

        SimpleFragmentPageAdapter adapter = new SimpleFragmentPageAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager.

        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view page. This will
        // 1. Update the tab layout when the view pager is swiped.
        // 2. Update the view pager when a tab is selected.
        // 3. Set the tab layout's tab names with the view pager's adapter's titles
        tabLayout.setupWithViewPager(viewPager);
        categoryNetworkRequest();
    }

    private void categoryNetworkRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://carpediemsocial.com/onlineshop/api/category_master.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Create an empty ArrayList that we can start adding earthquakes to
                       // List<Guide> guideList = new ArrayList<>();
                        ArrayList<Guide> temples = new ArrayList<Guide>();
                        // Try to parse the JSON response string. If there's a problem with the way the JSON
                        // is formatted, a JSONException exception object will be thrown.
                        // Catch the exception so the app doesn't crash, and print the error message to the logs.
                        try {

                            JSONArray internships = new JSONArray(response);

                            //Loop the Array
                            for (int i = 0; i < internships.length(); i++) {
                                Log.e("Message", "loop");
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject e = internships.getJSONObject(i);
                                map.put("cid", "cid :" + e.getString("m_cid"));
                                map.put("Category name", "Category name : " + e.getString("categoryname"));
                                String categoryId = e.getString("m_cid");
                               String categoryName = e.getString("categoryname");

Guide currentGuide = new Guide(categoryId, categoryName);
                                temples.add(currentGuide);

                                //  temples.add(new Guide("Manua Bhan Ki Tekri", "Sun City, Lalghati"));
                                //temples.add(new Guide("Laxminarayan Temple", "Arera Hills"));
                                //temples.add(new Guide("Gayatri Mandir", "Zone-1, Maharana Pratap Nagar"));
                               // temples.add(new Guide("Gufa Mandir", "Lalghati"));
                               // temples.add(new Guide("Balaji Mandir", "Bankhera, BHEL"));
                               // temples.add(new Guide("Birla Mandir", "Arera Hills"));
                               // temples.add(new Guide("Maa Kali Bijasen Temple", "Kolar Road, Chunna Bhatti"));
                               // temples.add(new Guide("Mahalakshmi Temple", "Karunadham Ashram, Gomti Colony"));
                               // temples.add(new Guide("Bhojpur Shiva Temple", "Bhojpur"));
                               // temples.add(new Guide("ISKCON Bhopal Center", "Sector A, Indrapuri"));

                                // addTab(categoryName);
                                Toast.makeText(getApplicationContext(), "Post successful.", Toast.LENGTH_SHORT).show();
                              //  Log.d("tag", String.valueOf(map));
                            }
                      //      bundle = new Bundle();
                    //        bundle.putParcelableArrayList("temples", temples);
                      //      Log.d("tag",String.valueOf(bundle));
                        //    TemplesFragment templesFragment = new TemplesFragment();
                          //  templesFragment.setArguments(bundle);

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
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }

        });
        queue.add(stringRequest);
    }

}
