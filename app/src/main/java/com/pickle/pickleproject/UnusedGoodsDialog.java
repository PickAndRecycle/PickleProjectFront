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
import android.widget.Spinner;
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
import com.pickle.pickleprojectmodel.UnusedCondition;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UnusedGoodsDialog extends AppCompatActivity {
    private EditText titleForm;
    private Spinner conditionForm;
    private RequestQueue mQueue;
    private ProgressBar loadingIcon;
    private MontserratButton submitOtherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods_dialog);

        String path = "sdcard/Pickle/cam_image.jpg";
        RelativeLayout background = (RelativeLayout) findViewById(R.id.backgroundLayout);
        background.setBackground(Drawable.createFromPath(path));

        loadingIcon = (ProgressBar) findViewById(R.id.progressBar);
        loadingIcon.setVisibility(View.GONE);


        submitOtherButton = (MontserratButton) findViewById(R.id.submitButtonOtherWaste);

        submitOtherButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeSubmitButton();
        }
    });

    }

    private void changeSubmitButton() {
        submitOtherButton.setVisibility(View.GONE);
        loadingIcon.setVisibility(View.VISIBLE);
        titleForm = (EditText) findViewById(R.id.editText4);
        conditionForm = (Spinner) findViewById(R.id.conditionSpinner);
        String condition = conditionForm.getSelectedItem().toString();
        //get current date and time and convert it into milliseconds
        Date date = new Date();
        long currentTime = date.getTime();
        String pathPhoto = "sdcard/Pickle/cam_image.jpg";
        File picture = new File(pathPhoto);

        final Intent intent = new Intent(this, TrashNotification.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("title", titleForm.getText().toString());
        intent.putExtra("condition", condition);
        intent.putExtra("timestamp", currentTime);

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();

        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(UnusedGoodsDialog.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final String url = "http://104.155.237.238:8080/trash/";

        Trash trash = new Trash();
        trash.setLatitude(intent.getStringExtra("latitude") );
        trash.setLongitude(intent.getStringExtra("longitude"));
        trash.setUsername(intent.getStringExtra("username"));
        trash.setDesc(intent.getStringExtra("description"));
        trash.setReport(intent.getBooleanExtra("report",false));
        trash.setCategories(TrashCategories.UNUSED);
        trash.setTitle(intent.getStringExtra("title"));
        String conditionTrash = intent.getStringExtra("condition").toUpperCase();
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

        Map<String, String> map = new HashMap<String, String>();
        map.put("voInput", json);


        Log.d("json", json);

        final MultipartRequest multipartRequest = new MultipartRequest(url,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error:", error.getMessage());
                Toast boom = new Toast(getApplicationContext());
                boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                boom.makeText(UnusedGoodsDialog.this, "Trash posting success", boom.LENGTH_SHORT).show();
            }
        },new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("result", response);
                loadingIcon.setVisibility(View.GONE);
                finish();
                startActivity(intent);
            }
        },picture,map);

        mQueue.add(multipartRequest);

    }


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
            object.addProperty("trash_condition", src.getCondition().toString());
            object.addProperty("size", 0);
            return object;
        }
    }

}
