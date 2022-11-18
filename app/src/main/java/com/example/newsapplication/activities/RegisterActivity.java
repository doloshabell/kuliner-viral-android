package com.example.newsapplication.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.newsapplication.R;
import com.example.newsapplication.utils.Network;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        IntentButtonLogin();

        EditText etNamaLengkapRegis = findViewById(R.id.etNamaLengkapRegis);
        EditText etEmailRegis = findViewById(R.id.etEmailRegis);
        EditText etPasswordRegis = findViewById(R.id.etPasswordRegis);
        EditText etConfirmPasswordRegis = findViewById(R.id.etConfirmPasswordRegis);
        Button btnUserRegis = findViewById(R.id.btnUserRegis);

        btnUserRegis.setOnClickListener(view -> {
            String namaLengkap = etNamaLengkapRegis.getText().toString();
            String email = etEmailRegis.getText().toString();
            String password = etPasswordRegis.getText().toString();
            String confirmPassword = etConfirmPasswordRegis.getText().toString();
            String message = "";

            if (namaLengkap.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                message = "Silahkan lengkapi isian yang ada";
            } else if (namaLengkap.isEmpty()) {
                message = "Silahkan mengisi nama lengkap anda";
            } else if (email.isEmpty()) {
                message = "Silahkan mengisi email anda";
            } else if (password.isEmpty()) {
                message = "Silahkan mengisi password anda";
            } else if (confirmPassword.isEmpty()) {
                message = "Silahkan mengisi confirm password anda";
            } else {
                if (!password.equals(confirmPassword)) {
                    message = "Passwordmu tidak sama";
                } else {
                    message = "Register Valid";
                    showAlertDialog(namaLengkap, email, password);
                    Log.d("nama lengkap", "Nama Lengkap: " + namaLengkap);
                    Log.d("email", "Email: " + email);
                    Log.d("password", "Password: " + password);
                    Log.d("confirm password", "Confirm Password: " + confirmPassword);
                }
            }

            showSnackbar(view, message);

//            Toast.makeText(getApplicationContext(), message,
//                    Toast.LENGTH_SHORT).show();
//            showSnackbar(view, message);

//            showSnackBar(view, message);
        });
    }

    /*post data register*/
    private class SendRegisterData extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String nama = strings[0];
            String email = strings[1];
            String password = strings[2];

            JSONObject jsonObject = null;
            String responseJSON = Network.getJSON("http://192.3.168.178/flutter/insert_user.php" +
                    "?" + "nama=" + nama + "&" + "email=" + email + "&" + "password=" + password);
            Log.d("Raw Response", "responseJSON: " + responseJSON);
            try {
                jsonObject = new JSONObject(responseJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            String status = null;
            String message = "";

            try {
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if (status.equals("success")) {
                Log.d("STATUS_REGISTER", "Register success");
            } else {
                Log.d("STATUS_REGISTER", "Register failed");
            }
        }

    }

    public void showAlertDialog(String nama, String email, String password) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Register");

        alertDialogBuilder
                .setMessage("Apakah datamu sudah benar?")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        /*new SendRegisterData().execute(nama, email, password);*/
                        /*fastAndroidNetworkingSendRegisterData(nama, email, password);*/
                        /*volleySendRegisterData(nama, email, password);*/
                        retrofitSendRegisterData(nama, email, password);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).show();
    }

    private void IntentButtonLogin() {
        TextView buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    /*use fast android networking*/
    private void fastAndroidNetworkingSendRegisterData(String nama, String email, String password) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("http://192.3.168.178/flutter/insert_user.php")
                .addBodyParameter("nama", nama)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String status = null;
                        String message = "";

                        try {
                            status = jsonObject.getString("status");
                            message = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            Log.d("STATUS_REGISTER", "Register success");
                        } else {
                            Log.d("STATUS_REGISTER", "Register failed");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), (CharSequence) anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /*end of using fast android networking*/

    /*using volley*/
    private void volleySendRegisterData(String nama, String email, String password) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = "http://192.3.168.178/flutter/insert_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            Log.d("STATUS_REGISTER", "Register success");
                        } else {
                            Log.d("STATUS_REGISTER", "Register failed");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        queue.add(stringRequest);
    }
    /*end of using volley*/

    /*using retrofit*/
    private interface RetrofitAPIRegister {
        @FormUrlEncoded
        @POST("insert_user.php")
        Call<String> STRING_CALL(
                @Field("nama") String nama,
                @Field("email") String email,
                @Field("password") String password
        );
    }

    private void retrofitSendRegisterData(String nama, String email, String password) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.3.168.178/flutter/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPIRegister retrofitAPIRegister = retrofit.create(RetrofitAPIRegister.class);

        Call<String> call = retrofitAPIRegister.STRING_CALL(nama, email, password);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = null;
                String message = "";
                if (response.isSuccessful() && response.body() != null) {
                    progressDialog.dismiss();
                    try {
                        JSONObject data = new JSONObject(response.body());
                        Log.d("data register", "onResponse: " + data);
                        status = data.getString("status");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (status.equals("success")) {
                        Log.d("STATUS_REGISTER", "Register success");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("STATUS_REGISTER", "Register failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", "onFailure: " + t.getMessage());
            }
        });
    }
    /*end of using retrofit*/
}