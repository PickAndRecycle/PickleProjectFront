package com.pickle.pickleproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pickle.pickleprojectmodel.Account;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class RegistrationActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText phoneNumber;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button registrationButton = (Button) findViewById(R.id.signUpButton);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignUpButton();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeSignUpButton() {
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        Intent intent = new Intent(this, ReportSuccess.class);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/account/";

        Account account = new Account();
        account.setUsername(username.toString());
        account.setEmail(email.toString());
        account.setPassword(password.toString());
        account.setPhone_number(phoneNumber.toString());


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


        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        startActivity(intent);
    }

    private class AccountSerializer implements JsonSerializer<Account> {
        @Override
        public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id",src.getId());
            object.addProperty("username",src.getUsername());
            object.addProperty("email",src.getEmail());
            object.addProperty("password",src.getPassword());
            object.addProperty("phoneNumber",src.getPhone_number());
            object.addProperty("photoUrl",src.getProfile_picture_url());

            return object;
        }
    }
}
