package com.example.airsoft.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.airsoft.Adapters.PolygonsAdapter;
import com.example.airsoft.Classes.PolygonClass;
import com.example.airsoft.R;
import com.example.airsoft.RecyclerTouchListener;
import com.example.airsoft.RecyclerViewDecorator;
import com.example.data.FirebaseData;

import java.util.ArrayList;
import java.util.List;

public class Polygons extends AppCompatActivity {
    private List<PolygonClass> polygonsList = new ArrayList<>();
    private PolygonsAdapter polygonsAdapter;
    FirebaseData fbData = FirebaseData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygons);

        addListenerOnButton();

        RecyclerView recyclerView = findViewById(R.id.polygons_recycler);
//------Добавляем разделение между строками в RecyclerView ------------------------------------------------------
        recyclerView.addItemDecoration(new RecyclerViewDecorator(this,LinearLayoutManager.VERTICAL, 16));

        polygonsAdapter = new PolygonsAdapter(polygonsList);
        RecyclerView.LayoutManager polygonsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(polygonsLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(polygonsAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
        @Override
        public void onClick(View view, int position) {
            PolygonClass selectedPolygon = polygonsList.get(position);
            Intent intent = new Intent(".PolygonInfoActivity");

            String polygonID = (String) selectedPolygon.getPolygonKey();
            intent.putExtra("polygonID",  polygonID);

            startActivity(intent);
        }

        @Override
        public void onLongClick(View view, int position) {

        }

    }));
        addToPolygonsRecycler();
        updateRecycler();
    }
    public void updateRecycler(){

        fbData.getOrgcomPolygonsList(new FirebaseData.orgcomPolygonListCallback() {

            @Override
            public void onOrgcomPolygonListChanged(String polygonKey, String polygonName, String polygonAddress, String polygonOrgcomID,  String polygonDescription, Double polygonLatitude, Double polygonLongitude) { }

            @Override
            public void onOrgcomPolygonListChanged() {
                polygonsList.clear();
                addToPolygonsRecycler();
                polygonsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPolygonNamesListChanged(List<String> polygonNamesList) {

            }
        });
    }
    public void addToPolygonsRecycler(){
        fbData.getOrgcomPolygonsList(new FirebaseData.orgcomPolygonListCallback() {
            @Override
            public void onOrgcomPolygonListChanged(String polygonKey, String polygonName, String polygonAddress, String polygonOrgcomID,  String polygonDescription, Double polygonLatitude, Double polygonLongitude) {
                addRow(polygonKey, polygonName, polygonAddress, polygonOrgcomID, polygonDescription,  polygonLatitude,  polygonLongitude);
            }

            @Override
            public void onOrgcomPolygonListChanged() {}

            @Override
            public void onPolygonNamesListChanged(List<String> polygonNamesList) {

            }
        });
    }

    private void addRow(String polygonKey, String polygonName, String polygonAddress,
                        String polygonOrgcomID, String polygonDescroption, Double polygonLatitude, Double polygonLongitude) {
        PolygonClass polygon = new PolygonClass(polygonKey);
        polygon.setPolygonKey(polygonKey);
        polygon.setPolygonName(polygonName);
        polygon.setPolygonAddress(polygonAddress);
        polygon.setPolygonOrgcomID(polygonOrgcomID);
//        polygon.setPolygonActuality(polygonActuality);
        polygon.setPolygonDescription(polygonDescroption);
        polygon.setPolygonLatitude(polygonLatitude);
        polygon.setPolygonLongitude(polygonLongitude);
        polygonsList.add(polygon);

        polygonsAdapter.notifyDataSetChanged();
    }

    private void addListenerOnButton() {
        Button buttonNewPolygon = findViewById(R.id.new_polygon_button);

        buttonNewPolygon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(".CreatingPolygon");
                        startActivity(i);
                        finish();
                    }
                }
        );
    }
}
