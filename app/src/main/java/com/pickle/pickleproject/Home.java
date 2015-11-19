package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;



public class Home extends Activity   {
    int CAM_REQUEST =1;
    Button Camera;
    private GestureDetector gestureDetector;
    //Location mLastLocation;
    //private GoogleApiClient mGoogleApiClient;
    GPSTracker gps;
    public static final String PREFS_NAME = "PicklePrefs";
    boolean doubleBackToExitPressedOnce = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        /*
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
        */

        Button profileButton = (Button) findViewById(R.id.profileButton);
        Button jarNewsButton = (Button) findViewById(R.id.jarNewsButton);
        Button pickButton = (Button) findViewById(R.id.pickButton);

        Camera = (Button) findViewById(R.id.throwReportButton);
        Camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);


            }
        });

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeProfileActivity();
            }
        });

        pickButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changePick();
            }
        });

        jarNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyJar();
            }
        });

        gestureDetector = new GestureDetector(new SwipeGestureDetector());

    }

    private void historyJar() {
        Intent intent = new Intent(this, Picklejar.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void changePick(){
        Intent intent = new Intent(this, PickList.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    private void changeAddDescription(){

        gps = new GPSTracker(Home.this);
        if(gps.canGetLocation()){
            //If GPS can get location
            Intent intent = new Intent(this, AddDescription.class);
            //GET THE CURRENT LATITUDE AND LONGITUDE
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            //PUT LATITUDE AND LONGITUDE INTO THE NEXT PAGE
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);

            //TOAST FOR DEBUGGING
            Bundle parseInfo = intent.getExtras();
            /*
            Toast boom = new Toast(getApplicationContext());
            boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            boom.makeText(Home.this, parseInfo.toString(), boom.LENGTH_SHORT).show();
            */

            startActivity(intent);

            // \n is for new line
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            //If GPS can get location
            Intent intent = new Intent(this, AddDescription.class);
            //GET THE CURRENT LATITUDE AND LONGITUDE
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            //PUT LATITUDE AND LONGITUDE INTO THE NEXT PAGE
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            //TOAST FOR DEBUGGING
            Bundle parseInfo = intent.getExtras();
            Toast boom = new Toast(getApplicationContext());
            boom.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            boom.makeText(Home.this, parseInfo.toString(), boom.LENGTH_SHORT).show();

            startActivity(intent);
        }

    }

    private void changeProfileActivity(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String id = settings.getString("valid", "0");
        if(id.equals("0")){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
    }

    private File getFile(){
        File folder = new File("sdcard/Pickle");
        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,"cam_image.jpg");
        return image_file;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/Pickle/cam_image.jpg";

        //Double latitude = 0.0;
        //Double longitude = 0.0;


        //changeAddDescription(latitude,longitude);
        changeAddDescription();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);

    }

    private void onLeftSwipe() {
        changePick();
    }

    private void onRightSwipe(){
        historyJar();
    }

    private void onDownSwipe() {
            changeProfileActivity();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbsLeftRight = Math.abs(e1.getY() - e2.getY());
                float diffLeftRight = e1.getX() - e2.getX();

                float diffAbsUpDown = Math.abs(e1.getX() - e2.getX());
                float diffUpDown = e1.getY() - e2.getY();


                if (diffAbsLeftRight > SWIPE_MAX_OFF_PATH)
                    return false;


                // Left swipe
                if (diffLeftRight > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Home.this.onLeftSwipe();

                } if (-diffLeftRight > SWIPE_MIN_DISTANCE // right swipe
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    Home.this.onRightSwipe();
                }

                if (diffAbsUpDown > SWIPE_MAX_OFF_PATH)
                    return false;

                // Down swipe
                if (-diffUpDown > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Home.this.onDownSwipe();

                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }


}

