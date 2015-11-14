package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pickle.pickleprojectmodel.TrashCategories;

public class ThrowCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throw_category);

    ImageButton unusedButton = (ImageButton) findViewById(R.id.UnusedButton);
    ImageButton generalButton = (ImageButton) findViewById(R.id.GeneralButton);
    ImageButton recycleButton = (ImageButton) findViewById(R.id.RecycleButton);
    ImageButton greenButton = (ImageButton) findViewById(R.id.GreenButton);

        Button backButton = (Button) findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    unusedButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            changeUnusedButton();
        }
    });
    generalButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeGeneralButton();
        }
    });
    recycleButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeRecycleButton();
        }
    });
    greenButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeGreenButton();
        }
    });

    }

    private void changeUnusedButton(){

        TrashCategories categories = TrashCategories.UNUSED;
        Intent intent = new Intent(this, UnusedGoodsDialog.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("categories", "Unused");

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowCategory.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

    private void changeGeneralButton(){
        TrashCategories categories = TrashCategories.GENERAL;
        Intent intent = new Intent(this, OtherTrashDialog.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("categories", "General");

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowCategory.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

    private void changeRecycleButton(){
        TrashCategories categories = TrashCategories.RECYCLED;
        Intent intent = new Intent(this, OtherTrashDialog.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("categories", "Recycled");

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowCategory.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

    private void changeGreenButton(){
        TrashCategories categories = TrashCategories.GREEN;
        Intent intent = new Intent(this, OtherTrashDialog.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("categories", "Green");

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(ThrowCategory.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

}
