package com.pickle.pickleproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.pickle.pickleprojectmodel.TrashCategories;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OtherTrashDialog extends AppCompatActivity {
    private EditText sizeForm;
    private RequestQueue mQueue;
    private ProgressBar loadingIcon;
    private MontserratButton submitUnusedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_trash_dialog);

        String path = "sdcard/Pickle/cam_image.jpg";
        RelativeLayout background = (RelativeLayout) findViewById(R.id.backgroundLayout);
        background.setBackground(Drawable.createFromPath(path));

        loadingIcon = (ProgressBar) findViewById(R.id.progressBar);
        loadingIcon.setVisibility(View.GONE);

        submitUnusedButton = (MontserratButton) findViewById(R.id.submitButtonUnusedGoods);
        //Button backHomeButton = (Button) findViewById(R.id.trash_notification_home);

        submitUnusedButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                changeSubmitButton();
            }
        });

        /*backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackHomeButton();
            }
        });*/

    }

    private void changeSubmitButton(){
        submitUnusedButton.setVisibility(View.GONE);
        loadingIcon.setVisibility(View.VISIBLE);
        sizeForm = (EditText) findViewById(R.id.editText3);
        //get current date and time and convert it into milliseconds
        Date date = new Date();
        long currentTime = date.getTime();
        String pathPhoto = "sdcard/Pickle/cam_image.jpg";
        File picture = new File(pathPhoto);

        final Intent intent = new Intent(this, TrashNotification.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("size", Integer.parseInt(sizeForm.getText().toString()));
        intent.putExtra("timestamp", currentTime);
        /*
        //TOAST FOR DEBUGGING

        Bundle parseInfo = intent.getExtras();

        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(OtherTrashDialog.this, parseInfo.toString(), boom.LENGTH_SHORT).show();
        */


        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/trash/";

        Trash trash = new Trash();
        trash.setLatitude(intent.getStringExtra("latitude") );
        trash.setLongitude(intent.getStringExtra("longitude"));
        trash.setUsername(intent.getStringExtra("username"));
        trash.setDesc(intent.getStringExtra("description"));
        Log.d("description", trash.getDesc());
        trash.setReport(intent.getBooleanExtra("report", false));
        Log.d("categories", intent.getStringExtra("categories").toUpperCase());
        trash.setCategories(TrashCategories.valueOf(intent.getStringExtra("categories").toUpperCase()));
        trash.setTitle("");
        trash.setCondition(null);
        trash.setsize(Integer.parseInt(sizeForm.getText().toString()));


        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Trash.class, new TrashSerializer());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(trash);
        Map<String, String> map = new HashMap<String, String>();
        map.put("voInput", json);


        Log.d("json", json);

        final MultipartRequest multipartRequest = new MultipartRequest(url,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error:", error.getMessage());
                Toast boom = new Toast(getApplicationContext());
                boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                boom.makeText(OtherTrashDialog.this, "Trash posting success", boom.LENGTH_SHORT).show();
            }
        },new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("result", response);
                loadingIcon.setVisibility(View.GONE);
                //finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        },picture,map);

        mQueue.add(multipartRequest);

        /*
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
            startActivity(intent);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        */

    }
    /*
    void changeBackHomeButton(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    */



    private class TrashSerializer implements JsonSerializer<Trash> {
        @Override
        public JsonElement serialize(Trash src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("id",src.getId());
            object.addProperty("categories",src.getCategories().toString());
            object.addProperty("username",src.getUsername());
            object.addProperty("status",src.getStatus());
            object.addProperty("description",src.getDesc());
            object.addProperty("distance", src.getDistance());
            object.addProperty("photo_url", src.getPhoto_url());
            object.addProperty("latitude", src.getLatitude());
            object.addProperty("longitude",src.getLongitude());
            object.addProperty("report",src.isReport());
            object.addProperty("title", src.getTitle());
            object.addProperty("trash_condition", "");
            object.addProperty("size", src.getsize());
            return object;
        }
    }
}
