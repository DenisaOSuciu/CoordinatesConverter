package com.example.coordinatesconverter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    String lat;
    String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent myIntent = getIntent(); // gets the previously created intent
        lat = myIntent.getStringExtra("lat");
        lng= myIntent.getStringExtra("lng");

        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
       map=googleMap;
        LatLng timisoara = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        map.moveCamera(CameraUpdateFactory.newLatLng(timisoara));
        Marker marker  = map.addMarker(new MarkerOptions()
                .position(timisoara)
                .title("Your location")
                .snippet(lat + " , "+ lng)); //set marker on map


        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15F); // zoom on marker
        googleMap.moveCamera(cu);



    }
}