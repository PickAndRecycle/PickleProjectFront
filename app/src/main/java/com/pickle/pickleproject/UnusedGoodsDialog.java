package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class UnusedGoodsDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods_dialog);

    Button submitUnusedButton = (Button) findViewById(R.id.submitButtonUnusedGoods);
    //Button backHomeButton = (Button) findViewById(R.id.trash_notification_home);

    submitUnusedButton.setOnClickListener(new View.OnClickListener() {
        private EditText sizeForm;

        @Override
        public void onClick(View v) {
            changeSubmitButton();
            sizeForm = (EditText) findViewById(R.id.editText3);
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.makeText(UnusedGoodsDialog.this, sizeForm.getText(), toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, TrashNotification.class);
        startActivity(intent);
    }

    private void changeBackHomeButton(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


}
