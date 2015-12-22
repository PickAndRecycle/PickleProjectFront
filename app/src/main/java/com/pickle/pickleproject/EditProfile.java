package com.pickle.pickleproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pickle.pickleprojectmodel.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    private Context context;
    private RequestQueue mQueue;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText phone;
    private ProgressBar loadingBar;
    private Boolean googleEditProfile;
    private RelativeLayout emailLayout;
    private RelativeLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profiles);
        final Account account = (Account) getIntent().getSerializableExtra("object");

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        phone = (EditText) findViewById(R.id.phoneEditText);

        emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);
        passwordLayout = (RelativeLayout) findViewById(R.id.passwordLayout);

        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.GONE);

        googleEditProfile = account.getGoogle();

        if(googleEditProfile){
            emailLayout.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.GONE);
        }

        username.setText(account.getUsername());
        password.setText(account.getPassword());
        email.setText(account.getEmail());
        phone.setText(account.getPhone_number());

        final MontserratButton saveButton = (MontserratButton) findViewById(R.id.saveButton);
        ImageButton backIcon = (ImageButton) findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        //final String url = "http://192.168.56.1:8080/account/" + account.getId();
        final String url = "http://104.155.237.238:8080/account/" + account.getId();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
                    Toast boom = new Toast(getApplicationContext());
                    boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                    boom.makeText(EditProfile.this, "please don't empty the box", boom.LENGTH_SHORT).show();
                } else {
                    loadingBar.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
                    account.setUsername(username.getText().toString());
                    account.setPassword(password.getText().toString());
                    account.setEmail(email.getText().toString());
                    account.setPhone_number(phone.getText().toString());

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String json = gson.toJson(account);
                    Log.d("json", json);
                    final CustomJSONObjectRequest jsonRequest;
                    try {
                        jsonRequest = new CustomJSONObjectRequest(Request.Method.PUT, url, new JSONObject(json), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("result", response.getString("result"));
                                    loadingBar.setVisibility(View.GONE);
                                    changeProfile();
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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void changeProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

}
