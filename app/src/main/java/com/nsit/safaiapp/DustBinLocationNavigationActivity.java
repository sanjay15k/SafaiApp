package com.nsit.safaiapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.nsit.safaiapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

class Dustbin {
    double lat;
    double lon;

    Dustbin(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}

public class DustBinLocationNavigationActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1000;
    private GoogleMap mMap;
    ArrayList<Dustbin> dustbins;
    ArrayList<Marker> markers;
    LatLng myLocation;
    LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust_bin_locaiton_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dustbins = new ArrayList<>();
        dustbins.add(new Dustbin(26.8992512, 75.79811839999999));
        dustbins.add(new Dustbin(27.0992512, 76.09811839999999));
        dustbins.add(new Dustbin(26.9992512, 76.19811839999999));
        dustbins.add(new Dustbin(26.9992512, 76.04811839999999));
        dustbins.add(new Dustbin(26.9992512, 76.99811839999999));
        dustbins.add(new Dustbin(26.9992512, 75.89811839999999));
        markers = new ArrayList<>();
        myLocation = new LatLng(0,0);
//        TODO get all dustbins from api
        fetchDustbin();
    }

    private void fetchDustbin() {

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                if(CalculationByDistance(myLocation, new LatLng(arg0.getLatitude(),arg0.getLongitude())) > 2) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    myLocation = new LatLng(arg0.getLatitude(), arg0.getLatitude());
                }
            }
        });

        getNearbyDustbin(0);
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void getNearbyDustbin(int i) {

        int radius = getRadius(i);

        for (Dustbin dustbin : dustbins) {
            if(CalculationByDistance(myLocation,new LatLng(dustbin.lat,dustbin.lon)) < 100000) {
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(dustbin.lat, dustbin.lon))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .visible(true));
                markers.add(marker);
            }
        }
    }

    private int getRadius(int i) {
        switch(i) {
            case 0 :
                return 200;
            case 1:
                return 500;
            case 2:
                return 1000;
        }
        return -1;
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}