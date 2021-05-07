package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PolygonInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseData fbData = new FirebaseData().getInstance();
    String polygonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_info);

        Intent intent = getIntent();
        polygonID = intent.getStringExtra("polygonID");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getPolygonInfo();
    }

    public void getPolygonInfo(){
        fbData.getPolygonInfo(new FirebaseData.polygonInfoCallback() {
            @Override
            public void onPolygonInfoChanged(String polygonName, String polygonAddress, String polygonOrgcomID,  String polygonDescription, Double polygonLatitude, Double polygonLongitude) {
                LatLng polygonLL = new LatLng(polygonLatitude, polygonLongitude);
                mMap.addMarker(new MarkerOptions().position(polygonLL).title("Полигон"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polygonLL, 9));
                fillPolygonInfo(polygonName, polygonAddress, polygonDescription);
            }
        }, polygonID);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @SuppressLint("SetTextI18n")
    private void fillPolygonInfo(String name,String addr,String descr){
        TextView pname = (TextView) findViewById(R.id.polygon_info_name);
        TextView paddr = (TextView) findViewById(R.id.polygon_info_adress);
        TextView pdescr = (TextView) findViewById(R.id.polygon_info_description);

        pname.setText(name);
        paddr.setText(addr);
        pdescr.setText(descr);
    }
}