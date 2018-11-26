package com.example.android.dyfragmentdata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

public class TemplesFragment extends Fragment {
    private ArrayList<Guide> temples;
    private View rootView;
    private GuideAdapter adapter;
    /**
     * A simple {@link Fragment} subclass.
     */

    public TemplesFragment() {
            // Required empty public constructor
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.guide_list, container, false);
          temples = new ArrayList<Guide>();
        //  adapter = new GuideAdapter(getActivity(), temples,  R.color.temples_category);
        //  ListView listView = (ListView) rootView.findViewById(R.id.list);
        //  listView.setAdapter(adapter);
        categoryNetworkRequest();
            //TODO: Insert all the code from the TemplesActivity onCreate method after the setContentView method.

            // Create an ArrayList of temples.
       // Bundle extras = getActivity().getIntent().getExtras();
       // if (extras!= null) {
       //     temples = extras.getParcelableArrayList("temples");
       // }

        // ArrayList<Guide> temples = (ArrayList<Guide>)getArguments().getSerializable("temples");

          //  temples.add(new Guide("Manua Bhan Ki Tekri", "Sun City, Lalghati"));
            //  temples.add(new Guide("Laxminarayan Temple", "Arera Hills"));
           //  temples.add(new Guide("Gayatri Mandir", "Zone-1, Maharana Pratap Nagar"));
            // temples.add(new Guide("Gufa Mandir", "Lalghati"));
           //  temples.add(new Guide("Balaji Mandir", "Bankhera, BHEL"));
            // temples.add(new Guide("Birla Mandir", "Arera Hills"));
            // temples.add(new Guide("Maa Kali Bijasen Temple", "Kolar Road, Chunna Bhatti"));
            // temples.add(new Guide("Mahalakshmi Temple", "Karunadham Ashram, Gomti Colony"));
           //  temples.add(new Guide("Bhojpur Shiva Temple", "Bhojpur"));
           //   temples.add(new Guide("ISKCON Bhopal Center", "Sector A, Indrapuri"));

     /* temples.add("Mata Mandir");



        temples.add("Manua Bhan Ki Tekri");
        temples.add("Laxminarayan Temple");
        temples.add("Shri Radha Krishna Mandir");
        temples.add("Bhojpur Shiva Temple");
        temples.add("Ganesh Mandir");
        temples.add("Sai Baba Temple");
        temples.add("Balaji Mandir");
        temples.add("Gayatri Mandir"); */


            /* Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
             * adapter knows how to create layouts for each item in the list, using the
             * simple_list_item_1.xml layout resource defined in the Android framework.
             * This list item layout contains a single {@link TextView}, which the adapter will set to
             * display a single temple.
             */

         //   GuideAdapter adapter = new GuideAdapter(getActivity().getApplicationContext(), temples,  R.color.temples_category);
          //  Log.d("tag", String.valueOf(adapter));

            /* Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
             * There should be a {@link ListView} with the view ID called list, which is declared in the
             * guide_list.xmlt file.
             */
          //  ListView listView = (ListView) rootView.findViewById(R.id.list);

            /* Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
             * {@link ListView} will display list items for each temple in the list of temples.
             * Do this by calling the setAdapter method on the {@link ListView} object and pass in
             * 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
             */


          //  listView.setAdapter(adapter);

            // Find the root view of the whole layout

            //   LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);

            // Create a variable to keep track of current index position


            // int index = 0;
            // while(index<temples.size()) {

            // update counter variable

       /* for (int index=0; index<temples.size(); index++)
        {
            TextView templesView = new TextView(this);
            templesView.setText(temples.get(index));
            rootView.addView(templesView);

         //   index++; // index = index +1
        } */

            // Create a new {@link TextView} to display the temple
            // and add the view as a child to the root view


            // Verify each elements in the ArrayList by printing out ArrayList elements in the log.

       /*  Log.v("TemplesActivity", "Temple at index 0: " + temples.get(0));
        Log.v("TemplesActivity", "Temple at index 0: " + temples.get(1));
        Log.v("TemplesActivity", "Temple at index 0: " + temples.get(2));
        Log.v("TemplesActivity", "Temple at index 0: " + temples.get(3));
        Log.v("TemplesActivity", "Temple at index 0: " + temples.get(4)); */

            return rootView;
        }

        private void categoryNetworkRequest() {
    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    String url = "http://carpediemsocial.com/onlineshop/api/category_master.php";
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

                              adapter = new GuideAdapter(getActivity(), temples,  R.color.temples_category);
                              ListView listView = (ListView) rootView.findViewById(R.id.list);
                              listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

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
                            Toast.makeText(getActivity().getApplicationContext(), "Post successful.", Toast.LENGTH_SHORT).show();
                            //  Log.d("tag", String.valueOf(map));
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
            Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

        }

    });
        queue.add(stringRequest);
    }
}


