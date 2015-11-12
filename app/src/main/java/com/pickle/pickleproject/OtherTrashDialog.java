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
import android.widget.Toast;

public class OtherTrashDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_trash_dialog);


    Button submitOtherButton = (Button) findViewById(R.id.submitButtonOtherWaste);

    submitOtherButton.setOnClickListener(new View.OnClickListener() {
        private EditText titleForm;
        private Spinner conditionForm;
        private String x;


        @Override
        public void onClick(View v) {
            changeSubmitButton();
            titleForm = (EditText) findViewById(R.id.editText4);
            conditionForm = (Spinner)findViewById(R.id.conditionSpinner);
            x = titleForm.getText() +" "+ conditionForm.getSelectedItem().toString();

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.makeText(OtherTrashDialog.this, x, toast.LENGTH_SHORT).show();

        }
    });

    }

    private void changeSubmitButton(){
        Intent intent = new Intent(this, TrashNotification.class);
        startActivity(intent);
    }

}
