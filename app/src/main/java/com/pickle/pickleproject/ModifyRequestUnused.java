package com.pickle.pickleproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class ModifyRequestUnused extends AppCompatActivity {

    private Context context;
    private RequestQueue mQueue,aQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_request_unused);
        context = this.getApplicationContext();
        final Trash trash = (Trash) getIntent().getSerializableExtra("object");

        final EditText title = (EditText) findViewById(R.id.editText5);
        final EditText description = (EditText) findViewById(R.id.editText7);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        title.setText(trash.getTitle());
        description.setText(trash.getDesc());


        Button saveButton = (Button) findViewById(R.id.saveButton);
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/trash/" + trash.getId();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trash.setTitle(title.getText().toString());
                trash.setDesc(description.getText().toString());
                String conditionTrash = spinner.getSelectedItem().toString().toUpperCase();
                if(conditionTrash.equals("LAYAK")){
                    conditionTrash = "GOOD";
                } else if(conditionTrash.equals("TIDAK LAYAK")){
                    conditionTrash = "BAD";
                } else{
                    conditionTrash = "NEW";
                }
                trash.setCondition(UnusedCondition.valueOf(conditionTrash));

                GsonBuilder gsonBuilder = new GsonBuilder();

                gsonBuilder.registerTypeAdapter(Trash.class, new TrashSerializer());
                Gson gson = gsonBuilder.create();
                String json = gson.toJson(trash);
                Log.d("json", json);
                try {
                    final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.PUT, url, new JSONObject(json), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("result",response.getString("result"));
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
                    changeJar();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goBack();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(trash);
            }
        });


    }
    private void delete(Trash trash){
        aQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "http://104.155.237.238:8080/trash/" + trash.getId();
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
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
        aQueue.add(jsonRequest);
        changeJar();
    }

    private void goBack(){
        finish();
    }

    private void changeJar(){
        Intent intent = new Intent(this,Picklejar.class);
        finish();
        startActivity(intent);
    }




    private class TrashSerializer implements JsonSerializer<Trash>{
        @Override
        public JsonElement serialize(Trash src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id",src.getId());
            object.addProperty("categories",src.getCategories().toString());
            object.addProperty("username",src.getUsername());
            object.addProperty("status",src.getStatus());
            object.addProperty("description",src.getDesc());
            object.addProperty("distance",src.getDistance());
            object.addProperty("photo_url",src.getPhoto_url());
            object.addProperty("latitude",src.getLatitude());
            object.addProperty("longitude",src.getLongitude());
            object.addProperty("report",src.isReport());
            object.addProperty("title",src.getTitle());
            object.addProperty("trash_condition",src.getCondition().toString());
            object.addProperty("size",src.getsize());
            return object;
        }
    }


}
