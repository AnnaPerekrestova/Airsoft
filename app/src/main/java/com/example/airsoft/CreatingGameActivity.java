package com.example.airsoft;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.airsoft.Classes.PolygonClass;
import com.example.data.FirebaseData;
import java.util.Calendar;
import java.util.List;

public class CreatingGameActivity extends AppCompatActivity {

    TextView currentDateTime;
    Calendar dateAndTime = Calendar.getInstance();
    Spinner spinnerPolygons;
    String selectedPolygon;
    FirebaseData fbData = FirebaseData.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_game);
        createPolygonSpinner();
        currentDateTime = findViewById(R.id.currentDateTime);
        setInitialDateTime();
        addListenerOnButton();
    }

    //----------отображаем диалоговое окно для выбора даты---------------------------------------------------------
    public void setDate(View v) {
        new DatePickerDialog(CreatingGameActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    //---------отображаем диалоговое окно для выбора времени-----------------------------------------------------
    public void setTime(View v) {
        new TimePickerDialog(CreatingGameActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    //------------установка начальных даты и времени----------------------------------------------------------------
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }
    //-----------установка обработчика выбора времени----------------------------------------------------------
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };
    //-----------установка обработчика выбора даты---------------------------------------------------------------
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void createPolygonSpinner(){
        spinnerPolygons = findViewById(R.id.new_game_polygons_spinner);
        fbData.getOrgcomPolygonsList(new FirebaseData.orgcomPolygonListCallback() {
            @Override
            public void onOrgcomPolygonListChanged(String polygonKey, String polygonName, String polygonAddress, String polygonOrgcomID, boolean polygonActuality, String polygonDescription, Double polygonLatitude, Double polygonLongitude) {            }

            @Override
            public void onOrgcomPolygonListChanged() {            }

            @Override
            public void onPolygonNamesListChanged(List<String> polygonNamesList) {
                ArrayAdapter<String> adapterPolygons = new ArrayAdapter<String>(CreatingGameActivity.this, android.R.layout.simple_spinner_item, polygonNamesList);
                adapterPolygons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPolygons.setAdapter(adapterPolygons);
            }
        });




        spinnerPolygons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                selectedPolygon = (String) parent.getItemAtPosition(position);
                fbData.getPolygonInfoByName(new FirebaseData.polygonInfoByNameCallback() {
                    @Override
                    public void onPolygonIDByNameChanged(String polygonID) {         }

                    @SuppressLint("WrongViewCast")
                    @Override
                    public void onPolygonAddressByNameChanged(String polygonAddress) {
                        ((TextView)findViewById(R.id.new_game_polygon_address)).setText(polygonAddress);
                    }
                },selectedPolygon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addListenerOnButton(){
        Button buttonCreateNewGame = findViewById(R.id.button_creating_new_game);

        buttonCreateNewGame.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fbData.getPolygonInfoByName(new FirebaseData.polygonInfoByNameCallback() {
                            @Override
                            public void onPolygonIDByNameChanged(String polygonID) {
                                String gameName = ((EditText)findViewById(R.id.new_game_name)).getText().toString();
                                String gameDescription = ((EditText)findViewById(R.id.new_game_description)).getText().toString();
                                String gameDate = currentDateTime.getText().toString();;
                                fbData.creatingNewGame(gameName,gameDate,polygonID,gameDescription);
                            }

                            @Override
                            public void onPolygonAddressByNameChanged(String polygonAddress) {      }
                        }, selectedPolygon);
                        finish();
                    }
                }

        );}
}