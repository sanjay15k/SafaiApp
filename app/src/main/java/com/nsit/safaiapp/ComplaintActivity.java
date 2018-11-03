package com.nsit.safaiapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.Toast;

import com.nsit.safaiapp.Calls.ComplaintCall;

public class ComplaintActivity extends AppCompatActivity {

    private EditText complaintEditText;
    private Button submitButton;

    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        complaintEditText = findViewById(R.id.complaintEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String complaintDetails = complaintEditText.getText().toString();

                if (ContextCompat.checkSelfPermission(ComplaintActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.shouldShowRequestPermissionRationale(ComplaintActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        checkSelfPermission("You have to accept to enjoy the most hings in this app");
                    } else {


                        ActivityCompat.requestPermissions(ComplaintActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

                    }
                } else {
                    getLocation();
                }

                System.out.println("Lat is : "+latitude);
                System.out.println("Lon is : "+longitude);

                ComplaintCall complaintCall = new ComplaintCall(ComplaintActivity.this,latitude,longitude,"check2",complaintDetails);
                complaintCall.execute();
            }
        });


    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                Toast.makeText(ComplaintActivity.this,"GPS is not ON",Toast.LENGTH_LONG).show();
            }

//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5, this);
            Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude  = location.getLatitude();
            longitude = location.getLongitude();
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
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
                    Intent intent = new Intent(ComplaintActivity.this, PickupRequest.class);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Current Location can't be detected", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
