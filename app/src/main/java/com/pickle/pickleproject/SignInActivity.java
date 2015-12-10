package com.pickle.pickleproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleproject.R;
import com.pickle.pickleprojectmodel.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    private boolean alreadySignIn;

    public static final String PREFS_NAME = "PicklePrefs";

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
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

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
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
            String nameOnly = email.substring(0,email.indexOf('@'));
            Log.d("Account lists", "email: " + acct.getEmail() + " " + "photo url: " + acct.getPhotoUrl() + " " + "ID: " + acct.getId() + " " + "ID Token: " + acct.getIdToken());

            //GCP
            final String url = "http://104.155.237.238:8080/account/";
            //CHECK if email is in database
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject parentObject = response;
                        Log.d("json:", response.getString("result"));
                        JSONArray parentArray = parentObject.getJSONArray("result");
                        List<Account> accountList = new ArrayList<Account>();
                        alreadySignIn = false;
                        final String username = email;
                        Log.d("username", username);
                        for (int i = 0; i < parentArray.length(); i++) {
                            JSONObject finalObject = parentArray.getJSONObject(i);
                            Account accountObj;
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            final Gson gson = gsonBuilder.create();
                            accountObj = gson.fromJson(String.valueOf(finalObject), Account.class);
                            Log.d("username", accountObj.getUsername());
                            Log.d("password", accountObj.getPassword());

                            if ((accountObj.getUsername().equals(username))) {
                                String getSecure_id = accountObj.getId();
                                String getUsername = accountObj.getUsername();
                                String getEmail = accountObj.getEmail();

                                String x = "secure id= " + getSecure_id + ", " + "username= " + getUsername + ", " + ", " + "email= " + getEmail;

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

                                alreadySignIn = true;
                                break;
                            }
                        }
                        if (alreadySignIn) {
                            signIn();
                        } else {
                            Account account = new Account();
                            account.setUsername(email);
                            account.setEmail(acct.getEmail());
                            account.setPassword(acct.getIdToken());
                            mQueue = CustomVolleyRequestQueue.getInstance(SignInActivity.this).getRequestQueue();
                            GsonBuilder gsonBuilder = new GsonBuilder();

                            gsonBuilder.registerTypeAdapter(Account.class, new AccountSerializer());
                            Gson gson = gsonBuilder.create();
                            String json = gson.toJson(account);
                            Log.d("json", json);
                            try {
                                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, new JSONObject(json), new Response.Listener<JSONObject>() {
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
                                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                mQueue.add(jsonRequest);
                                //TOAST
                                Toast boom = new Toast(getApplicationContext());
                                boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                                boom.makeText(SignInActivity.this, "Your e-mail finally registered.", boom.LENGTH_SHORT).show();


                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    } catch (JSONException e) {
                        //TOAST
                        Toast boom = new Toast(getApplicationContext());
                        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                        boom.makeText(SignInActivity.this, "Invalid username or password.", boom.LENGTH_SHORT).show();
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
/*
            if(alreadySignIn){
                // Session Storage ganti jadi account kita
            }
            else{
                // Masukin database
            }
*/



            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        //PUT EMAIL AND SECURE_ID

    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            //mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
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
