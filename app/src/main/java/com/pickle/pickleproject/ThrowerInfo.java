package com.pickle.pickleproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ThrowerInfo extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrower_info);

        Button back_button = (Button) findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBack();
            }
        });

        final Trash trash = (Trash) getIntent().getSerializableExtra("object");
        MontserratTextView titleText = (MontserratTextView) findViewById(R.id.titleText);
        MontserratTextView titleContent = (MontserratTextView) findViewById(R.id.titleContent);
        if (trash.getCategories().equals(TrashCategories.UNUSED))
        {
            titleText.setText("TITLE");
            titleContent.setText(trash.getTitle());
        }
        else
        {
            titleText.setText("CATEGORIES");
            titleContent.setText(trash.getCategories().toString());

        }

        MontserratTextView descriptionContent = (MontserratTextView) findViewById(R.id.descriptionContent);
        descriptionContent.setText(trash.getDesc());
        MontserratTextView throwerContent = (MontserratTextView) findViewById(R.id.throwerContent);
        throwerContent.setText(trash.getUsername());

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://104.155.237.238:8080/account/";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mImageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
        NetworkImageView photo = (NetworkImageView) findViewById(R.id.imageContent);
        photo.setImageUrl(trash.getPhoto_url(), mImageLoader);
        mQueue.add(jsonRequest);


    }

    private void changeBack() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void changeMap() {
        Intent intent = new Intent(this, ViewMap.class);
        final Trash trash = (Trash) getIntent().getSerializableExtra("object");
        intent.putExtra("object", trash);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void changeCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error:", error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        try{
            final Trash trash = (Trash) getIntent().getSerializableExtra("object");
            Log.d("trash", trash.toString());
            final String username = trash.getUsername();
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");

            for (int i=0; i<parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                if (finalObject.getString("username").equals(username)){
                    final String phone = finalObject.getString("phone_number");
                    PhoneCallListener phoneListener = new PhoneCallListener();
                    TelephonyManager telephonyManager = (TelephonyManager) this
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

                    ImageButton call_button = (ImageButton) findViewById(R.id.call_button);

                    call_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeCall(phone);
                        }
                    });

                    ImageButton getLocationButton = (ImageButton) findViewById(R.id.get_location_button);

                    getLocationButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            changeMap();
                        }
                    });
                    break;
                }
            }

            // add PhoneStateListener


        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }
}
