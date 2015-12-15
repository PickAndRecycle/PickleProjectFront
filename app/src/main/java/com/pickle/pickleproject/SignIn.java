package com.pickle.pickleproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleprojectmodel.Account;
import com.pickle.pickleprojectmodel.Notification;
import com.pickle.pickleprojectmodel.Trash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private RequestQueue mQueue;
    EditText usernameForm;
    EditText passwordForm;
    public static final String PREFS_NAME = "PicklePrefs";
    boolean validator = false;
    //GOOGLE
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private boolean alreadySignIn;

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
        usernameForm = (EditText) findViewById(R.id.usernameinput);
        passwordForm = (EditText) findViewById(R.id.passwordinput);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [END build_client]

        MontserratButton signInButton = (MontserratButton) findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GCP
                String url = "http://104.155.237.238:8080/account/";
                final String url2 = "http://104.155.237.238:8080/notification/";
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
                                    String getEmail = accountObj.getEmail();

                                    String x = "secure id= " + getSecure_id + ", " + "username= " + getUsername + ", " +  ", " + "email= " + getEmail;

                                    //put secure_id into SharedPreferences
                                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("secure_id", getSecure_id);
                                    editor.putString("username", getUsername);
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
                                    if(validator) {
                                        SharedPreferences sharedPreferences =
                                                PreferenceManager.getDefaultSharedPreferences(SignIn.this);
                                        String token = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");
                                        Notification notification = new Notification(usernameForm.getText().toString(), token);
                                        GsonBuilder gsonBuilder2 = new GsonBuilder();
                                        Gson gson2 = gsonBuilder.create();
                                        String json2 = gson2.toJson(notification);
                                        Log.d("notification", json2);

                                        try {
                                            CustomJSONObjectRequest jsonRequest2 = new CustomJSONObjectRequest(Request.Method.POST, url2, new JSONObject(json2), new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String tokenId = response.getString("result");
                                                        Log.d("notif_id", tokenId);
                                                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                                        SharedPreferences.Editor editor = settings.edit();
                                                        editor.putString("notifId", tokenId);
                                                        editor.commit();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("Error_Request2", error.toString());
                                                }
                                            });
                                            mQueue.add(jsonRequest2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (validator) {
                                        signIn();
                                    } else {
                                        //TOAST
                                        Toast boom = new Toast(getApplicationContext());
                                        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                                        boom.makeText(SignIn.this, "Invalid token", boom.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                            }

                        } catch (JSONException e) {
                            //TOAST
                            Toast boom = new Toast(getApplicationContext());
                            boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                            boom.makeText(SignIn.this, e.toString(), boom.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("kenapa",e.toString());
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

        TextView noAccount = (TextView) findViewById(R.id.noAccount);
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        Button googleSignInButton = (Button) findViewById(R.id.signingoogleplusbutton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGoogleSignIn();
            }
        });





    }

    private void changeGoogleSignIn(){
        /*
        Intent googleIntent = new Intent(this,SignInActivity.class);
        startActivity(googleIntent);*/
        //GOOGLE SIGN IN
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signIn(){
        Intent intent = new Intent(this, Home.class);
        usernameForm = (EditText) findViewById(R.id.usernameinput);
        passwordForm = (EditText) findViewById(R.id.passwordinput);
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

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]
    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            acct.getEmail();
            acct.getPhotoUrl();
            acct.getId();
            acct.getIdToken();
            final String email = acct.getEmail();
            final String nameOnly = email.substring(0,email.indexOf('@'));
            Log.d("Account lists", "username: " + nameOnly + " " + "email: " + acct.getEmail() + " " + "photo url: " + acct.getPhotoUrl() + " " + "ID: " + acct.getId() + " " + "ID Token: " + acct.getIdToken());
            final String url = "http://104.155.237.238:8080/account/";
            final String url2 = "http://104.155.237.238:8080/notification/";

            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        Toast boom = new Toast(getApplicationContext());
                        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                        boom.makeText(SignIn.this, "Checking if email available in database...", boom.LENGTH_SHORT).show();

                        Log.d("Account lists", "username: "+nameOnly+" "+"email: " + acct.getEmail() + " " + "photo url: " + acct.getPhotoUrl() + " " + "ID: " + acct.getId() + " " + "ID Token: " + acct.getIdToken());
                        JSONObject parentObject = response;
                        Log.d("json:", response.getString("result"));
                        JSONArray parentArray = parentObject.getJSONArray("result");
                        List<Account> accountList = new ArrayList<Account>();
                        for (int i = 0; i < parentArray.length(); i++) {
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            Account accountObj;
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            final Gson gson = gsonBuilder.create();
                            accountObj = gson.fromJson(String.valueOf(finalObject), Account.class);
                            Log.d("email", accountObj.getEmail());
                            Log.d("password", accountObj.getPassword());
                            if ((accountObj.getEmail().equals(email))) {
                                String getSecure_id = accountObj.getId();
                                String getUsername = accountObj.getUsername();
                                //Points is now deprecated
                                //Integer getPoint = accountObj.getPoint();
                                String getEmail = accountObj.getEmail();
                                //put secure_id into SharedPreferences
                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("secure_id", getSecure_id);
                                editor.putString("username", getUsername);
                                editor.putString("email", getEmail);
                                editor.putString("valid", "1");
                                editor.commit();

                                String x = "secure id= " + getSecure_id + ", " + "username= " + getUsername + ", " + "email= " + getEmail;

                                alreadySignIn = true;
                                if(alreadySignIn) {
                                    /*
                                    boom = new Toast(getApplicationContext());
                                    boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                                    boom.makeText(SignInActivity.this, "AVAILABLE", boom.LENGTH_SHORT).show();
                                    */
                                    //TOKEN FOR NOTIFICATION
                                    SharedPreferences sharedPreferences =
                                            PreferenceManager.getDefaultSharedPreferences(SignIn.this);
                                    String token = sharedPreferences.getString(QuickstartPreferences.GCM_TOKEN, "");
                                    Notification notification = new Notification(nameOnly, token);
                                    GsonBuilder gsonBuilder2 = new GsonBuilder();
                                    Gson gson2 = gsonBuilder.create();
                                    String json2 = gson2.toJson(notification);
                                    Log.d("notification", json2);
                                    Log.d("PHONY: ", accountObj.getPhone_number());

                                    try {
                                        CustomJSONObjectRequest jsonRequest2 = new CustomJSONObjectRequest(Request.Method.POST, url2, new JSONObject(json2), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    String tokenId = response.getString("result");
                                                    Log.d("notif_id", tokenId);
                                                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                                    SharedPreferences.Editor editor = settings.edit();
                                                    editor.putString("notifId", tokenId);
                                                    editor.commit();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("Error_Request2", error.toString());
                                            }
                                        });
                                        mQueue.add(jsonRequest2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    signOut();
                                    //signIn();
                                    if(accountObj.getPhone_number().isEmpty() || accountObj.getPhone_number()=="NULL"){
                                        changeEditProfile(accountObj);
                                    }else{
                                        toHome();
                                    }

                                }
                            }
                        }
                        if(!alreadySignIn) {
                            boom = new Toast(getApplicationContext());
                            boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                            boom.makeText(SignIn.this, "Email not available, registering email.", boom.LENGTH_SHORT).show();
                            Account account = new Account();
                            account.setUsername(nameOnly);
                            account.setEmail(acct.getEmail());
                            account.setPassword(acct.getIdToken());
                            account.setPhone_number("");

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(Account.class, new AccountSerializer());
                            Gson gson = gsonBuilder.create();
                            String json = gson.toJson(account);
                            Log.d("json", json);

                            try {
                                final CustomJSONObjectRequest jsonRequest2 = new CustomJSONObjectRequest(Request.Method.POST, url, new JSONObject(json), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Log.d("result", response.getString("result"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("Error:", error.getMessage());
                                    }
                                });
                                jsonRequest2.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                mQueue.add(jsonRequest2);
                                changeGoogleSignIn();
                                /*
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignIn.this);

                                // Setting Dialog Title
                                alertDialog.setTitle("Welcome to Pickle");
                                // Setting Dialog Message
                                alertDialog.setMessage("You can now sign in.");
                                // On pressing Settings button
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        dialog.dismiss();
                                    }

                                });

                                // Showing Alert Message
                                alertDialog.show();
                                */
                                boom = new Toast(getApplicationContext());
                                boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                                boom.makeText(SignIn.this, "Registered, you can sign in now.", boom.LENGTH_SHORT).show();
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                }
            });
            mQueue.add(jsonRequest);
        }
    }
    // [END handleSignInResult]
    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]
    private void toHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void changeEditProfile(Account account){
        Intent intent = new Intent(this, EditProfile.class);
        intent.putExtra("object", account);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private class AccountSerializer implements JsonSerializer<Account> {
        @Override
        public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id",src.getId());
            object.addProperty("username",src.getUsername());
            object.addProperty("email",src.getEmail());
            object.addProperty("password",src.getPassword());
            object.addProperty("phone_number",src.getPhone_number());

            return object;
        }
    }


}
