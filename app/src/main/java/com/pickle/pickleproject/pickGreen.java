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

public class pickGreen extends AppCompatActivity {
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_green);

        MyData myDataArray[]=new MyData[]{
                new MyData("Address1",10,5),
                new MyData("Address2",20,6),
                new MyData("Address3",30,7)
        };

        ListAdapter myAdapter=new ListAdapter( this, R.layout.rowlayout, myDataArray);
        ListView myList = (ListView)
                findViewById(R.id.listView2);
        myList.setAdapter(myAdapter);


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
