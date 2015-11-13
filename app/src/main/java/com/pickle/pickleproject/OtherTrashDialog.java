package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OtherTrashDialog extends AppCompatActivity {
    private EditText sizeForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods_dialog);

    Button submitUnusedButton = (Button) findViewById(R.id.submitButtonUnusedGoods);
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
        sizeForm = (EditText) findViewById(R.id.editText3);
        Intent intent = new Intent(this, TrashNotification.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("size", Integer.parseInt(sizeForm.getText().toString()));

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(OtherTrashDialog.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

    private void changeBackHomeButton(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


}
