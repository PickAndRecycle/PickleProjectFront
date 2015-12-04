package com.pickle.pickleproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Account;
import com.pickle.pickleprojectmodel.Trash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity {
    private RequestQueue mQueue;
    EditText usernameForm;
    EditText passwordForm;
    public static final String PREFS_NAME = "PicklePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        //EDIT
        /*
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "http://104.155.237.238:8080/account/";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        mQueue.add(jsonRequest);
        */
        usernameForm = (EditText) findViewById(R.id.editText8);
        passwordForm = (EditText) findViewById(R.id.editText9);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GCP
                String url = "http://104.155.237.238:8080/account/";

                //Localhost
                //String id  = "b86174cb-93c6-4b73-844c-be3f2070ea31";
                //String url = "http://192.168.56.1:8080/account/" + id;
                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject parentObject = response;
                            Log.d("json:", response.getString("result"));
                            JSONArray parentArray = parentObject.getJSONArray("result");
                            List<Account> accountList = new ArrayList<Account>();
                            boolean validator = false;
                            final String username = usernameForm.getText().toString();
                            final String password = passwordForm.getText().toString();
                            Log.d("username", username);
                            Log.d("password", password);
                            for (int i = 0; i < parentArray.length(); i++) {
                                JSONObject finalObject = parentArray.getJSONObject(i);
                                Account accountObj;
                                GsonBuilder gsonBuilder = new GsonBuilder();

                                final Gson gson = gsonBuilder.create();
                                accountObj = gson.fromJson(String.valueOf(finalObject), Account.class);
                                Log.d("username", accountObj.getUsername());
                                Log.d("password", accountObj.getPassword());

                                if ((accountObj.getUsername().equals(username)) && (accountObj.getPassword().equals(password))) {
                                    String getSecure_id = accountObj.getId();
                                    String getUsername = accountObj.getUsername();
                                    Integer getPoint = accountObj.getPoint();
                                    String getEmail = accountObj.getEmail();

                                    String x = "secure id= " + getSecure_id + ", " + "username= " + getUsername + ", " + "point= " + getPoint + ", " + "email= " + getEmail;

                                    //put secure_id into SharedPreferences
                                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("secure_id", getSecure_id);
                                    editor.putString("username", getUsername);
                                    editor.putInt("point", getPoint);
                                    editor.putString("email", getEmail);
                                    editor.putString("valid", "1");
                                    editor.commit();


                                    //TOAST
                                    /*
                                    Toast boom = new Toast(getApplicationContext());
                                    boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                                    boom.makeText(SignIn.this, x, boom.LENGTH_SHORT).show();
                                    */

                                    validator = true;
                                    break;
                                }
                            }
                            if (validator) {
                                signIn();
                            } else {
                                //TOAST
                                Toast boom = new Toast(getApplicationContext());
                                boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                                boom.makeText(SignIn.this, "Invalid username or password", boom.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            //TOAST
                            Toast boom = new Toast(getApplicationContext());
                            boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                            boom.makeText(SignIn.this, "Invalid username or password.", boom.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                    }
                });
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mQueue.add(jsonRequest);

            }
        });

        TextView noAccount = (TextView) findViewById(R.id.textView20);
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        Button googleSignInButton = (Button) findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGoogleSignIn();
            }
        });



    }

    private void changeGoogleSignIn(){
        Intent googleIntent = new Intent(this,SignInActivity.class);
        startActivity(googleIntent);
    }

    private void signIn(){
        Intent intent = new Intent(this, Home.class);
        usernameForm = (EditText) findViewById(R.id.editText8);
        passwordForm = (EditText) findViewById(R.id.editText9);
        //to pass usernameForm and passwordForm
        intent.putExtra("username", usernameForm.getText().toString());
        intent.putExtra("password", passwordForm.getText().toString());
        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(SignIn.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        super.onBackPressed();
    }


    private void register(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


}
