package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.airsoft.R;
import com.example.data.FirebaseData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreatingPolygon extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseData fbData = new FirebaseData().getInstance();
    Double latitude;
    Double longitude;
    boolean fmos = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_polygon);

        addListenerOnButton();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void addListenerOnButton(){
        findViewById(R.id.creating_polygon_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                проверям, поставил ли организатор точку на карте (место полигона)
                if ( fmos==true ){
                    Toast.makeText(CreatingPolygon.this, "Укажите полигон на карте удержанием точки",
                            Toast.LENGTH_SHORT).show();
                }
                else {
//                    проверка на ввод названия, адреса и описания полигона
                    if (((EditText) findViewById(R.id.creating_polygon_name)).getText().toString().equals("") | (((EditText) findViewById(R.id.creating_polygon_adress)).getText().toString().equals("")) | (((EditText) findViewById(R.id.creating_polygon_description)).getText().toString().equals(""))){
                        Toast.makeText(CreatingPolygon.this, "Введите информацию о полигоне",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        fbData.creatingNewPolygon(((EditText) findViewById(R.id.creating_polygon_name)).getText().toString(), ((EditText) findViewById(R.id.creating_polygon_adress)).getText().toString(),
                                ((EditText) findViewById(R.id.creating_polygon_description)).getText().toString(), latitude, longitude);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // задаем координаты Москвы
        LatLng moscow = new LatLng(55.7558, 37.6173);
        // ставим маркер на Москву и переводим камеру на точку
        final Marker[] marker = {mMap.addMarker(new MarkerOptions().position(moscow).title("Москва"))};
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 9));
        // при удержании на карте перемещаем маркер на точку
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                marker[0].remove();
                fmos=false;
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                marker[0] = mMap.addMarker(new MarkerOptions().position(latLng).title("Полигон"));
            }
        });
    }
}

