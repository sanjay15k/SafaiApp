package com.nsit.safaiapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PickupRequest extends AppCompatActivity{

    private Button getCoordinatesButton;
    private TextView coordinatesTextView;
    private EditText recyclableWeightEditText;
    private EditText nonRecyclableWeightEditText;
    private Button sendRequestButton;

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

        getCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
