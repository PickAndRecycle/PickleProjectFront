package com.pickle.pickleproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Unused_goods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods);

        Button GeneralButton = (Button) findViewById(R.id.Generalbtn);
        Button RecycledButton = (Button) findViewById(R.id.Recycledbtn);
        Button GreenButton = (Button) findViewById(R.id.Greenbtn);

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
    }

    private void changeGeneral(){
        Intent intent = new Intent(this, pickList.class);

        startActivity(intent);
    }

    private void changeRecycled(){
        Intent intent = new Intent(this, pickRecycled.class);

        startActivity(intent);
    }

    private void changeGreen(){
        Intent intent = new Intent(this, pickGreen.class);

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
}
