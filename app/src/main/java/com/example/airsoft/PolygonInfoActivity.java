package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.data.FirebaseData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PolygonInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseData fbData = new FirebaseData().getInstance();
    Double latitude;
    Double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_info);

        getPolygonInfo();
    }

    public void getPolygonInfo(){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng polygonLL = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(polygonLL).title("Полигон"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polygonLL, 9));
    }
}