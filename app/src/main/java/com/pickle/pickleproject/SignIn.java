package com.pickle.pickleproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn();
            }
        });
    }

    private void signIn(){
        EditText usernameForm;
        EditText passwordForm;
        Intent intent = new Intent(this, Home.class);
        usernameForm = (EditText) findViewById(R.id.editText8);
        passwordForm = (EditText) findViewById(R.id.editText9);
        //to pass usernameForm and passwordForm
        intent.putExtra("username", usernameForm.getText().toString());
        intent.putExtra("password", passwordForm.getText().toString());
        //TOAST FOR DEBUGGING
        Bundle parseInfo = intent.getExtras();
        Toast boom = new Toast(getApplicationContext());
        boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        boom.makeText(SignIn.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

        startActivity(intent);

    }
}
