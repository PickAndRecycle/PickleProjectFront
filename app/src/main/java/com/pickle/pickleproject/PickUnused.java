package com.pickle.pickleproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PickUnused extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    private GestureDetector gestureDetector;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods);


        //new JSONTask().execute("http://10.0.0.2:8080/trash/");


        Button GeneralButton = (Button) findViewById(R.id.Generalbtn);
        Button RecycledButton = (Button) findViewById(R.id.Recycledbtn);
        Button GreenButton = (Button) findViewById(R.id.Greenbtn);

        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        GeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneral();
            }
        });

        RecycledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRecycled();
            }
        });

        GreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGreen();
            }
        });

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        //Use your machine IP (Mac/Linux ifconfig in terminal, Windows ipconfig in cmd) IP should be 192.x.x.x
        String url = "http://192.168.0.103:8080/trash/";
        //String url = "http://private-22976-pickleapi.apiary-mock.com/trash";


        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error:",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");

            List<Trash> Trashlist = new ArrayList<Trash>();

            for(int i=0 ; i<parentArray.length();i++){
                JSONObject finalObject = parentArray.getJSONObject(i);

                Trash trashObj;
                GsonBuilder gsonBuilder = new GsonBuilder();

                gsonBuilder.registerTypeAdapter(UnusedCondition.class, new UnusedConditionDeserialize());
                Gson gson = gsonBuilder.create();

                if(finalObject.getString("categories").equals("Unused Goods")){
                    trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);

                    trashObj.setCategories(TrashCategories.UNUSED);
                    Trashlist.add(trashObj);
                }

            }
            Trash[] trashArray = Trashlist.toArray(new Trash[0]);
            ListAdapter myAdapter=new ListAdapter(PickUnused.this ,R.layout.rowlayout, trashArray);
            ListView myList = (ListView)
                    findViewById(R.id.listView4);
            myList.setAdapter(myAdapter);

            //return Trashlist;
        }  catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        try {
            Log.d("json",response.getString("result"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


    private void changeGeneral(){
        Intent intent = new Intent(this, PickGeneral.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void changeRecycled(){
        Intent intent = new Intent(this, PickRecycled.class);
        startActivity(intent);
    }

    private void changeGreen(){
        Intent intent = new Intent(this, PickGreen.class);
        startActivity(intent);
    }
    private void changeHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void changeIndividual(){
        Intent intent = new Intent(this, IndividualTrashInfo.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_unused_goods, menu);
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

    /* Slide gestures */

    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        changeGeneral();
    }

    private void onRightSwipe() {
        changeHome();
    }


/*
    // To pull the JSON request
    public class JSONTask extends AsyncTask<String,String,List<Trash>>{

        @Override
        protected List<Trash> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();


                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("Result");

                List<Trash> Trashlist = new ArrayList<Trash>();

                for(int i=0 ; i<parentArray.length();i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Trash trashObj;
                    GsonBuilder gsonBuilder = new GsonBuilder();

                    gsonBuilder.registerTypeAdapter(UnusedCondition.class, new UnusedConditionDeserialize());
                    Gson gson = gsonBuilder.create();

                    if(finalObject.getString("categories").equals("Unused Goods")){
                        trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);

                        trashObj.setCategories(TrashCategories.UNUSED);
                        Trashlist.add(trashObj);
                    }


                }


                return Trashlist;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trash> Result) {
            super.onPostExecute(Result);

            //Log.d("IDOBJECT",Result.get(0).getDesc());

            Trash[] trashArray = Result.toArray(new Trash[0]);
            ListAdapter myAdapter=new ListAdapter(PickUnused.this ,R.layout.rowlayout, trashArray);
            ListView myList = (ListView)
                    findViewById(R.id.listView4);
            myList.setAdapter(myAdapter);

        }
    }

*/

    // Private class for gestures
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
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PickUnused.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    PickUnused.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }

    private class TrashCategoriesDeserialize implements JsonDeserializer<TrashCategories> {
        @Override
        public TrashCategories deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.getAsString().equals("Unused Goods")){
                return TrashCategories.UNUSED;
            } else if (json.getAsString().equals("General Waste")){
                return TrashCategories.GENERAL;
            } else if (json.getAsString().equals("Recycleable Waste")){
                return TrashCategories.RECYCLED;
            } else if (json.getAsString().equals("Green Waste")){
                return TrashCategories.GREEN;
            } else {
                return TrashCategories.UNSPECIFIED;
            }
        }
    }

    private class UnusedConditionDeserialize implements JsonDeserializer<UnusedCondition>{
        @Override
        public UnusedCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.getAsString().equals("Good")){
                return UnusedCondition.GOOD;
            } else if(json.getAsString().equals("Bad")){
                return UnusedCondition.BAD;
            } else if(json.getAsString().equals("New")){
                return UnusedCondition.NEW;
            } else{
                return UnusedCondition.UNSPECIFIED;
            }
        }
    }


}
