package com.example.android.dyfragmentdata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeliveryActivity extends AppCompatActivity {

    private WebView myWebView;
    private String android_id;
    private Session session;
    private String sessionToken;
    private static final String TAG = "MyActivity";
    private String ramdomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        session = new Session(this);
        sessionToken = session.getDeliveryToken();

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
          launchNetworkRequest();

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.godprice.com/pannel/delivery_login.php");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new DeliveryActivity.MyWebViewClient());
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

     /*   if (!sessionToken.isEmpty()) {
            launchNetworkRequest();
        } */
        }

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            ramdomId = toast;
            ButtonNetworkRequest(ramdomId);
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
        }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  if (Uri.parse(url).getHost().equals("http://hqsales.net/mobileappdev/makeadminlogin/")) {
            if (Uri.parse(url).getHost().equals("www.godprice.com")) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    private void launchNetworkRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/providenowlogin.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonResponse = response.toString().trim();
                               jsonResponse = jsonResponse.substring(3);
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String status = jsonObject.getString("status");
                            String tokenApi = jsonObject.getString("tokenApi");
                            String authToken = jsonObject.getString("auth_token");
                            session.setDeliveryToken("");
                            session.setDeliveryToken(tokenApi);
                            sessionToken = session.getDeliveryToken();

                            if (authToken.isEmpty()) {
                                myWebView.loadUrl("https://www.godprice.com/pannel/delivery_login.php");
                            } else if (!authToken.isEmpty()) {
                                myWebView.loadUrl("https://www.godprice.com/pannel/account-profile.php?auth="+authToken);
                            }

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }

        }) { @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", android_id);
            params.put("tokenApi", sessionToken);
            return params;
        }
        };
        queue.add(stringRequest);
    }

    private void ButtonNetworkRequest(String ramdomId) {
        final String RandomId = ramdomId;

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.godprice.com/api/thisishavelogin.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonResponse = response.toString().trim();
                               jsonResponse = jsonResponse.substring(3);
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String status = jsonObject.getString("status");
                            // String deviceId = jsonObject.getString("device_id");
                            String tokenApi = jsonObject.getString("tokenApi");
                            session.setDeliveryToken("");
                            //  authToken = jsonObject.getString("auth_token");
                            session.setDeliveryToken(tokenApi);
                            sessionToken = session.getDeliveryToken();
                          /*  if (tokenApi.isEmpty()) {
                                myWebView.loadUrl("https://www.godprice.com/pannel/delivery_login.php");
                            } else if (!tokenApi.isEmpty()) {
                                myWebView.loadUrl("https://www.godprice.com/pannel/account-profile.php?auth_token="+authToken);
                            } */
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

            }
        }) { @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", android_id);
            params.put("randomId", RandomId);
            return params;
        }
        };
        queue.add(stringRequest);
    }
}



