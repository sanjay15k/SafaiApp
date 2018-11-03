package com.nsit.safaiapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nsit.safaiapp.Calls.RequestBinCall;
import com.nsit.safaiapp.Calls.SendLocationOfUnmarkedBinCall;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationOfBinNotMarkedActivity extends AppCompatActivity {

    private Button sendMyLocation;

    private LocationManager locationManager;

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_of_bin_not_marked);

        sendMyLocation = findViewById(R.id.sendMyLocation);

        sendMyLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(LocationOfBinNotMarkedActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.shouldShowRequestPermissionRationale(LocationOfBinNotMarkedActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        checkSelfPermission("You have to accept to enjoy the most hings in this app");
                    } else {


                        ActivityCompat.requestPermissions(LocationOfBinNotMarkedActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

                    }
                } else {
                    getLocation();
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getLocation();
                } else {
                    Toast.makeText(this, "Current Location can't be detected", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            SendLocationOfUnmarkedBinCall sendLocationOfUnmarkedBinCall = new SendLocationOfUnmarkedBinCall(LocationOfBinNotMarkedActivity.this, location.getLatitude(),location.getLongitude());
            sendLocationOfUnmarkedBinCall.execute();


        }
        catch(SecurityException e) {
            e.printStackTrace();
        }

    }

}
