package com.pickle.pickleproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private RequestQueue mQueue,aQueue;
    public static final String PREFS_NAME = "PicklePrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);

        //GCP
        //String id = "46d83e4c-93b9-4d88-a500-85a0809dc275";
        String id = preferences.getString("secure_id", "0");
        String notifId = preferences.getString("notifId","0");
        String url = "http://104.155.237.238:8080/account/" + id;
        final String url2 = "http://104.155.237.238:8080/notification/" + notifId;


        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject parentObject = response.getJSONObject("result");
                    Log.d("json:", response.getString("result"));

                    final Account account;

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    account = gson.fromJson(String.valueOf(parentObject), Account.class);

                    MontserratTextView username = (MontserratTextView) findViewById(R.id.username);
                    MontserratTextView email = (MontserratTextView) findViewById(R.id.email);

                    username.setText(account.getUsername());
                    email.setText(account.getEmail());

                    MontserratButton editProfile = (MontserratButton) findViewById(R.id.editProfileButton);
                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: GOOGLE.
                            if(account.getGoogle()){
                                changeEditGoogleProfile(account);
                            }else{
                                changeEditProfile(account);
                            }
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error:",error.toString());
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonRequest);

        MontserratButton signOut = (MontserratButton) findViewById(R.id.signOutButton);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomJSONObjectRequest jsonRequestNotif = new CustomJSONObjectRequest(Request.Method.DELETE, url2, new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Notif Deleted", String.valueOf(response.getBoolean("result")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Notif:", error.toString());
                    }
                });
                mQueue.add(jsonRequestNotif);
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //TOAST
                Toast boom = new Toast(getApplicationContext());
                boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                boom.makeText(ProfileActivity.this, "Logged Out", boom.LENGTH_SHORT).show();
                signOut();
            }

        });
        
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onUpSwipe();
            }
        });

        ImageButton configurationButton = (ImageButton) findViewById(R.id.configurationButton);
        configurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeConfiguration();
            }
        });
    }

    private void signOut(){
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);

    }

    private void changeConfiguration(){
        Intent intent = new Intent(this,ConfigurationActivity.class);
        startActivity(intent);
    }

    private void changeEditProfile(Account account){
        Intent intent = new Intent(this, EditProfile.class);
        intent.putExtra("object", account);
        startActivity(intent);
    }
    //TODO: GOOGLE
    private void changeEditGoogleProfile(Account account){
        Intent intent = new Intent(this, EditGoogleProfile.class);
        intent.putExtra("object", account);
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }
    private void onUpSwipe(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbsLeftRight = Math.abs(e1.getY() - e2.getY());
                float diffLeftRight = e1.getX() - e2.getX();

                float diffAbsUpDown = Math.abs(e1.getX() - e2.getX());
                float diffUpDown = e1.getY() - e2.getY();


                if (diffAbsLeftRight > SWIPE_MAX_OFF_PATH)
                    return false;



                // Right swipe


                if (diffAbsUpDown > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diffUpDown > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    ProfileActivity.this.onUpSwipe();

                    // Right swipe
                }

                 /*else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    YourActivity.this.onRightSwipe();
                }*/
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}
