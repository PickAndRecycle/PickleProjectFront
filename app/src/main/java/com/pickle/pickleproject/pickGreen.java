package com.pickle.pickleproject;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class pickGreen extends AppCompatActivity {
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_green);

        new JSONTask().execute("http://private-22976-pickleapi.apiary-mock.com/trash");


        Button UnusedButton = (Button) findViewById(R.id.Unusedbtn);
        Button GeneralButton = (Button) findViewById(R.id.Generalbtn);
        Button RecycledButton = (Button) findViewById(R.id.Recycledbtn);

        UnusedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUnused();
            }
        });

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
        gestureDetector = new GestureDetector(new SwipeGestureDetector());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_green, menu);
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

    private void changeUnused(){
        Intent intent = new Intent(this, Unused_goods.class);

        startActivity(intent);
    }

    private void changeGeneral(){
        Intent intent = new Intent(this, pickGeneral.class);

        startActivity(intent);
    }

    private void changeRecycled() {
        Intent intent = new Intent(this, pickRecycled.class);

        startActivity(intent);
        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    public class JSONTask extends AsyncTask<String,String,List<Trash>> {

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

                    Trash trashObj = new Trash();
                    trashObj.setDesc(finalObject.getString("description"));
                    trashObj.setDistance(finalObject.getInt("distance"));
                    trashObj.setTotal(finalObject.getInt("total"));

                    Trashlist.add(trashObj);

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
            ListAdapter myAdapter=new ListAdapter(pickGreen.this ,R.layout.rowlayout, trashArray);
            ListView myList = (ListView)
                    findViewById(R.id.listView2);
            myList.setAdapter(myAdapter);

        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onRightSwipe() {
        changeRecycled();
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
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    pickGreen.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }


}
