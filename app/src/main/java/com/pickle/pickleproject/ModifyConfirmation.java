package com.pickle.pickleproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pickle.pickleprojectmodel.Trash;

import java.io.InputStream;
import java.net.URL;

public class ModifyConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Trash trash = (Trash) getIntent().getSerializableExtra("object");
        Button modifyButton = (Button) findViewById(R.id.modify);
        Button backButton = (Button) findViewById(R.id.backToModify);
        if(trash ==null){
            Toast.makeText(getApplicationContext(), "Null Object", Toast.LENGTH_SHORT).show();
        }
        modifyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                    try{
                        if (trash.getCategories().toString().equals("Unused Goods")){
                            ModifyUnused(trash);
                        }
                        else{
                            ModifyOther(trash);
                        }

                    }
                    catch (NullPointerException e){
                        Toast.makeText(getApplicationContext(), "null cuy", Toast.LENGTH_SHORT).show();
                    }
                }
            });




    }
    private void ModifyUnused(Trash trash){
        Intent intent = new Intent(this,ModifyRequestUnused.class);
        intent.putExtra("object",  trash);
        startActivity(intent);
    }
    private void ModifyOther(Trash trash){
        Intent intent = new Intent(this,ModifyRequestOther.class);
        intent.putExtra("object", trash);
        startActivity(intent);
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}


