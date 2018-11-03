package com.nsit.safaiapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsit.safaiapp.Calls.PickUpRequestCall;

public class PickupRequest extends AppCompatActivity implements LocationListener{

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1000;
    private Button getCoordinatesButton;
    private TextView coordinatesTextView;
    private EditText recyclableWeightEditText;
    private EditText nonRecyclableWeightEditText;
    private Button sendRequestButton;
    private double latitude;
    private double longitude;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_request);

        coordinatesTextView = findViewById(R.id.coordinatesTextView);
        getCoordinatesButton = findViewById(R.id.getCoordinatesButton);
        recyclableWeightEditText = findViewById(R.id.recyclableWeightEditText);
        nonRecyclableWeightEditText = findViewById(R.id.nonRecyclableWeightEditText);
        sendRequestButton = findViewById(R.id.sendRequestButton);

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recyclableWeigth = recyclableWeightEditText.getText().toString();
                String nonRecyclableWeigth = nonRecyclableWeightEditText.getText().toString();

                String username = "check2";

                PickUpRequestCall pickUpRequestCall = new PickUpRequestCall(PickupRequest.this,latitude,longitude,username);
                pickUpRequestCall.execute();
            }
        });

        getCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PickupRequest.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.shouldShowRequestPermissionRationale(PickupRequest.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        checkSelfPermission("You have to accept to enjoy the most hings in this app");
                    } else {


                        ActivityCompat.requestPermissions(PickupRequest.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

                    }
                } else {
                    System.out.println("Hey i m in");
                    getLocation();
                }
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5, this);
            Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String str = "Latitude : " + Double.toString(location.getLatitude()) + " : Longitude : " + Double.toString(location.getLongitude());
            coordinatesTextView.setText(str);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Location" + location.toString());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String str = "Latitude : " + Double.toString(location.getLatitude()) + " : Longitude : " + Double.toString(location.getLongitude());
        coordinatesTextView.setText(str);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast.makeText(PickupRequest.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getLocation();
                    Intent intent = new Intent(PickupRequest.this, PickupRequest.class);
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
