package com.pickle.pickleproject;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
>>>>>>> 7376ff8a92381ce0f04ad47dbca3136ddb32544f
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Thrower_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrower_info);

<<<<<<< HEAD
        Button backButton = (Button) findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                changeUnused();
            }
        });

    }

    private void changeUnused(){
        Intent intent = new Intent(this, Unused_goods.class);
=======
        Button back_button = (Button) findViewById(R.id.back_button);
        Button call_button = (Button) findViewById(R.id.call_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBack();
            }
        });
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCall();
            }
        });
    }

    private void changeBack() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void changeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:08127648183"));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
>>>>>>> 7376ff8a92381ce0f04ad47dbca3136ddb32544f
        startActivity(intent);
    }

}
