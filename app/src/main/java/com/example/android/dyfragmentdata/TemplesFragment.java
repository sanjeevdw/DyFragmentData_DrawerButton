package com.example.android.dyfragmentdata;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemplesFragment extends Fragment {
    private ArrayList<Guide> temples;
    private View rootView;
    private GuideAdapter adapter;
    private ListView listView;
    private String sessionToken;
    private int childIndex;
   // private String productID;

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
        adapter = new GuideAdapter(getActivity(), temples, R.color.temples_category);
        //  adapter = new GuideAdapter(getActivity(), temples,  R.color.temples_category);
        //  ListView listView = (ListView) rootView.findViewById(R.id.list);
        //  listView.setAdapter(adapter);
        categoryNetworkRequest();

      //  TextView productIdtv = (TextView) listView.findViewById(R.id.product_id);
     //   productIdtv.setVisibility(View.GONE);

        Session session = new Session(getActivity().getApplicationContext());
        sessionToken = session.getusertoken();

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

        private void categoryNetworkRequest() {
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
                                    map.put("cid", "cid :" + e.getString("product_id"));
                                    map.put("Category name", "Category name : " + e.getString("productsname"));

                                    String prodID = e.getString("product_id");
                                    String productName = e.getString("productsname");
                                    String productPrice = e.getString("price");
                                    String imageUrl = e.getString("feature_image");
                                    String productRating = e.getString("rating");
                                    String productWishlist = e.getString("is_whishlit");

                                    Guide currentGuide = new Guide(prodID, productName, productPrice, imageUrl, productRating, productWishlist);
                                    temples.add(currentGuide);

                                    adapter = new GuideAdapter(getContext(), temples, R.color.temples_category);
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
                                            }
                                        }
                                    });


                                    /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            long viewId = view.getId();
                                            String productId = listView.getItemAtPosition(position).toString().trim();
                                       //     TextView Pid = (TextView) parent.findViewById(R.id.product_id);

                                            TextView PPid = (TextView) listView.getChildAt(position).findViewById(R.id.product_id);
                                          String productID = PPid.getText().toString().trim();
                                            if (viewId == R.id.image_favorite) {
                                                ImageView wishlistImage = (ImageView) view.findViewById(R.id.image_favorite);
                                                wishlistImage.setImageResource(R.drawable.red_wishlist);

                                                Drawable drawable = wishlistImage.getDrawable();
                                                Drawable drawable1 = getActivity().getApplicationContext().getDrawable(R.drawable.red_wishlist);
                                                if (drawable.equals(drawable1)) {
                                                    wishlistImage.setImageResource(R.drawable.favorite_icon);
                                                }
                                                /// String productId = listView.getItemAtPosition(position).toString().trim();
                                                    sendWishlistRequest(sessionToken, productID);
                                                    //     final String text = ((TextView) text.findViewById(R.id.product_id)).getText().toString();
                                             //   TextView Pid = (TextView) view.findViewById(R.id.product_id);
                                             //   String productId = Pid.getText().toString().trim();
                                                // String productId = ((TextView) view.findViewById(R.id.product_id)).getText().toString();

                                            }

                                        /*    if (viewId == R.id.image_favorite) {
                                                ImageView wishlistImage = (ImageView) view.findViewById(R.id.image_favorite);
                                                wishlistImage.setImageResource(R.drawable.red_wishlist);

                                                Drawable drawable = wishlistImage.getDrawable();
                                                Drawable drawable1 = getActivity().getApplicationContext().getDrawable(R.drawable.red_wishlist);
                                                if (drawable.equals(drawable1)) {
                                                    wishlistImage.setImageResource(R.drawable.favorite_icon);
                                                }

                                                /// String productId = listView.getItemAtPosition(position).toString().trim();
                                            } */


                                    //    }
                                //    }); */

                                  /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> c, View view, int position, long id) {

                                          //  if (id == R.id.product_id) {
                                            String productId = listView.getItemAtPosition(position).toString().trim();
                                            TextView Pid = (TextView) view.findViewById(R.id.product_id);
                                               productID = Pid.getText().toString().trim();
                                            // String productId = ((TextView) view.findViewById(R.id.product_id)).getText().toString();
                                          //  }
                                        //    long viewId = view.getId();
                                         //   if (viewId == R.id.image_favorite) {
                                        //        sendWishlistRequest(sessionToken, productID);
                                       //         }
                                        }
                                    }); */

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

    private void sendWishlistRequest(String uid, String pid) {

        final String userId = String.valueOf(uid);
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
            params.put("userid", userId);
            params.put("pid", productId);
            return params;
        }

        };

        queue.add(stringRequest);
    }
}

