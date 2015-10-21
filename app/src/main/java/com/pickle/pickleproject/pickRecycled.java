package com.pickle.pickleproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class pickRecycled extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recycled);

        Button UnusedButton = (Button) findViewById(R.id.Unusedbtn);
        Button GeneralButton = (Button) findViewById(R.id.Generalbtn);
        Button GreenButton = (Button) findViewById(R.id.Greenbtn);

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

        GreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGreen();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick_recycled, menu);
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
        Intent intent = new Intent(this, pickList.class);

        startActivity(intent);
    }

    private void changeGreen(){
        Intent intent = new Intent(this, pickGreen.class);

        startActivity(intent);
    }


}
