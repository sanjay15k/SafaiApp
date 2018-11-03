package com.nsit.safaiapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.nsit.safaiapp.CommonUtils.RetrofitInstance;
import com.nsit.safaiapp.DTO.DustbinsLocations;
import com.nsit.safaiapp.Interface.ApiInterface;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DustBinLocationNavigationActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1000;
    float Zoom = 15;
    private GoogleMap mMap;
    ArrayList<DustbinsLocations> dustbins;
    ArrayList<Marker> markers;
    LatLng myLocation;
    LatLngBounds.Builder builder;
    private CircleOptions circleOptions;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust_bin_locaiton_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        dustbins = new ArrayList<>();
        markers = new ArrayList<>();
        myLocation = new LatLng(0,0);

        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<Integer> distanceValues = new ArrayList<>();
        distanceValues.add(100);
        distanceValues.add(200);
        distanceValues.add(400);
        distanceValues.add(700);
        distanceValues.add(1000);
        distanceValues.add(1200);
        distanceValues.add(1500);

        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, distanceValues);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        fetchDustbin();
    }

    private void fetchDustbin() {
        GetNearByDustBinCoordinates getNearByDustBinCoordinates = new GetNearByDustBinCoordinates(DustBinLocationNavigationActivity.this);
        getNearByDustBinCoordinates.execute();
    }

    private void getNearbyDustbin(int i) {

        if (mMap != null){
            mMap.clear();
        }
        markers.clear();

        System.out.println("Hey radius is : "+ i);
        for (DustbinsLocations dustbin : dustbins) {

//            System.out.println("Lat1 : "+myLocation.latitude);
//            System.out.println("Long1 : "+myLocation.longitude);
//
//            System.out.println("Lat2 : "+dustbin.getLat());
//            System.out.println("Long2 : "+dustbin.getLon());

            Location loc1 = new Location("");
            loc1.setLatitude(myLocation.latitude);
            loc1.setLongitude(myLocation.longitude);

            Location loc2 = new Location("");
            loc2.setLatitude(dustbin.getLat());
            loc2.setLongitude(dustbin.getLon());

            float distanceInMeters = loc1.distanceTo(loc2);

            if( distanceInMeters < i) {
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(dustbin.getLat(), dustbin.getLon()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .visible(true));
                markers.add(marker);
                System.out.println("Hey i m in bro");
            }
        }
    }

     private void drawCircle(LatLng point){
        // Instantiating CircleOptions to draw a circle around the marker

         circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(400);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

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
                    mMap.setMyLocationEnabled(true);
                    Intent intent = new Intent(DustBinLocationNavigationActivity.this, DustBinLocationNavigationActivity.class);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Current Location can't be detected", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int distance = (int) spinner.getItemAtPosition(i);
        getNearbyDustbin(distance);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class GetNearByDustBinCoordinates extends AsyncTask<Void, Void, Void> {

        Context context;
        private ProgressDialog progressDialog;

        GetNearByDustBinCoordinates(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait while we get nearby dustbins");
            progressDialog.setCancelable(false);
        }


        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance();
            Call<ArrayList<DustbinsLocations>> getDustbinsLocations = apiInterface.getDustbinsLocatons();
            try {
                ArrayList<DustbinsLocations> dustbinsLocationsArrayList = getDustbinsLocations.execute().body();
                if (dustbinsLocationsArrayList != null) {
                    for (int i=0; i<dustbinsLocationsArrayList.size(); i++){
                        DustbinsLocations dustbinsLocations = new DustbinsLocations(dustbinsLocationsArrayList.get(i).getLat(),dustbinsLocationsArrayList.get(i).getLon());
                        dustbins.add(dustbinsLocations);
                    }
                }
                System.out.println(dustbinsLocationsArrayList);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            new ReadyMap(DustBinLocationNavigationActivity.this);
        }
    }

    public class ReadyMap implements OnMapReadyCallback{

        Context context;
        boolean found = false;

        ReadyMap(Context context){
            this.context = context;
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if (ContextCompat.checkSelfPermission(DustBinLocationNavigationActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(DustBinLocationNavigationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    checkSelfPermission("You have to accept to enjoy the most hings in this app");
                } else {


                    ActivityCompat.requestPermissions(DustBinLocationNavigationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

                }
            } else {
                mMap.setMyLocationEnabled(true);
            }

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    if(!found) {
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                        myLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                        drawCircle(myLocation);
                        System.out.println("Location :" + myLocation.latitude + " " + myLocation.longitude);
                        found = true;
                        getNearbyDustbin((Integer) spinner.getSelectedItem());
                    }
                }
            });
        }
    }



}