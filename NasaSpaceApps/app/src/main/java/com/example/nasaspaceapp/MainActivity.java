package com.example.nasaspaceapp;

import com.google.android
        .gms.location
        .FusedLocationProviderClient;
import com.google.android
        .gms.location
        .LocationCallback;
import com.google.android
        .gms.location
        .LocationRequest;
import com.google.android
        .gms.location
        .LocationResult;
import com.google.android
        .gms.location
        .LocationServices;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat
        .app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android
        .gms.tasks
        .OnCompleteListener;
import com.google.android
        .gms.tasks.Task;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import android.os.Bundle;
import org.json.JSONObject;
import org.json.JSONArray;
import android.view.View;
import android.widget.Button;
import android.util.Log;


public class MainActivity
        extends AppCompatActivity {
    Button b1;
    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient
            mFusedLocationClient;

    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView;
    String temperature = "";
    String humidity = "";
    String windSpeed = "";
    String content;
    int PERMISSION_ID = 44;


    class Weather extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... address) {
            //String... means multiple address can be send. It acts as array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Establish connection with address
                connection.connect();

                //retrieve data from url
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //Retrieve data and return it as String
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(
            Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);

        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();

        String lat = latitudeTextView.getText().toString();
        String lon = longitTextView.getText().toString();

        //Double lati = Double.parseDouble(lat);
        //Double longi = Double.parseDouble(lon);


        Weather weather = new Weather();
        try {
            content = weather.execute("api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=764da74326715f9aeb1b40def87b509a").get();
            //First we will check data is retrieve successfully or not
            Log.i("contentData",content);

            JSONObject jsonObject = new JSONObject(content);
            String windData = jsonObject.getString("wind");
            String mainData = jsonObject.getString("main");



            JSONArray array = new JSONArray(mainData);
            for(int i=0; i<array.length(); i++){
                JSONObject mainPart = array.getJSONObject(i);
                 temperature = mainPart.getString("temp");
                 humidity = mainPart.getString("humidity");
            }


            JSONObject windPart = new JSONObject(windData);
            windSpeed = windPart.getString("speed");



        } catch (Exception e) {
            e.printStackTrace();
        }

        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
            }
        });

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation()
    {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient
                        .getLastLocation()
                        .addOnCompleteListener(
                                new OnCompleteListener<Location>() {

                                    @Override
                                    public void onComplete(
                                            @NonNull Task<Location> task)
                                    {
                                        Location location = task.getResult();
                                        if (location == null) {
                                            requestNewLocationData();
                                        }
                                        else {
                                            Double lati = location.getLatitude();
                                            Double longi = location.getLongitude();

                                            latitudeTextView
                                                    .setText(
                                                            location
                                                                    .getLatitude()
                                                                    + "");
                                            longitTextView
                                                    .setText(
                                                            location
                                                                    .getLongitude()
                                                                    + "");

                                        }
                                    }


                                });
            }

            else {
                Toast
                        .makeText(
                                this,
                                "Please turn on"
                                        + " your location...",
                                Toast.LENGTH_LONG)
                        .show();

                Intent intent
                        = new Intent(
                        Settings
                                .ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
        else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData()
    {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest
                = new LocationRequest();
        mLocationRequest.setPriority(
                LocationRequest
                        .PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);

        mFusedLocationClient
                .requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        Looper.myLooper());
    }

    private LocationCallback
            mLocationCallback
            = new LocationCallback() {

        @Override
        public void onLocationResult(
                LocationResult locationResult)
        {
            Location mLastLocation
                    = locationResult
                    .getLastLocation();
            latitudeTextView
                    .setText(
                            "Latitude: "
                                    + mLastLocation
                                    .getLatitude()
                                    + "");
            longitTextView
                    .setText(
                            "Longitude: "
                                    + mLastLocation
                                    .getLongitude()
                                    + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions()
    {
        return ActivityCompat
                .checkSelfPermission(
                        this,
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED

                && ActivityCompat
                .checkSelfPermission(
                        this,
                        Manifest.permission
                                .ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        /* ActivityCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission
                        .ACCESS_BACKGROUND_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        */
    }

    // method to requestfor permissions
    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION,
                        Manifest.permission
                                .ACCESS_FINE_LOCATION },
                PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled()
    {
        LocationManager
                locationManager
                = (LocationManager)getSystemService(
                Context.LOCATION_SERVICE);

        return locationManager
                .isProviderEnabled(
                        LocationManager.GPS_PROVIDER)
                || locationManager
                .isProviderEnabled(
                        LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(
                        requestCode,
                        permissions,
                        grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager
                    .PERMISSION_GRANTED) {

                getLastLocation();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}

