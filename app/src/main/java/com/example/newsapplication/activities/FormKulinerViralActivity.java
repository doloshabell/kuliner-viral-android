package com.example.newsapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.newsapplication.R;
import com.example.newsapplication.enums.Apis;
import com.example.newsapplication.utils.GpsTracker;

import org.json.JSONObject;

import java.util.Locale;

public class FormKulinerViralActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kuliner_viral);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*form kuliner viral*/
        EditText etNamaLokasi = findViewById(R.id.etNamaLokasi);
        EditText etKeterangan = findViewById(R.id.etKeterangan);
        EditText etKontributor = findViewById(R.id.etKontributor);
        EditText etLatitude = findViewById(R.id.etLatitude);
        EditText etLongitude = findViewById(R.id.etLongitude);
        Button btnSimpanFormKulinerViral = findViewById(R.id.btnSimpanFormKulinerViral);
        Button btnShowMap = findViewById(R.id.btnShowMap);

        GpsTracker gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            etLatitude.setText(String.valueOf(latitude));
            etLongitude.setText(String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }

        btnSimpanFormKulinerViral.setOnClickListener(view -> {
            String namaLokasi = etNamaLokasi.getText().toString();
            String keterangan = etKeterangan.getText().toString();
            String kontributor = etKontributor.getText().toString();
            String latitude = etLatitude.getText().toString();
            String longitude = etLongitude.getText().toString();

            Log.d("btn-simpan-form-kuliner", "onCreate: " + namaLokasi + " " + keterangan + " " + kontributor + " " + latitude + " " + longitude);
            postDataKuliner("simpan", namaLokasi, keterangan, kontributor, latitude, longitude, "1");
        });

        btnShowMap.setOnClickListener(view -> {
            /*String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);*/

            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(FormKulinerViralActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void postDataKuliner(String aksi, String nama, String keterangan, String kontributor,
                                String lat, String lon, String status) {
        showProgressDialog();
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post(Apis.DATA_CULINARY)
                .addBodyParameter("aksi", aksi)
                .addBodyParameter("nama", nama)
                .addBodyParameter("keterangan", keterangan)
                .addBodyParameter("kontributor", kontributor)
                .addBodyParameter("lat", lat)
                .addBodyParameter("lon", lon)
                .addBodyParameter("status", status)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();
                        String status = null;

                        try {
                            status = jsonObject.getString("status");
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                        Log.d("Status", "onResponse: " + status);
                        if (status.equals("success")) {
                            /*FormKulinerViralActivity.super.onBackPressed();*/
                            finish();
                            /*startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", "onError: " + anError.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }
}