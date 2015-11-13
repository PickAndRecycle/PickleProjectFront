package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UnusedGoodsDialog extends AppCompatActivity {
    private EditText titleForm;
    private Spinner conditionForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_trash_dialog);


    Button submitOtherButton = (Button) findViewById(R.id.submitButtonOtherWaste);

    submitOtherButton.setOnClickListener(new View.OnClickListener() {

        private String x;


        @Override
        public void onClick(View v) {
            changeSubmitButton();

        }
    });

    }

    private void changeSubmitButton(){
        titleForm = (EditText) findViewById(R.id.editText4);
        conditionForm = (Spinner)findViewById(R.id.conditionSpinner);

        Intent intent = new Intent(this, TrashNotification.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("title",titleForm.getText().toString());
        intent.putExtra("condition",conditionForm.getSelectedItem().toString());

        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(UnusedGoodsDialog.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);
    }

}
