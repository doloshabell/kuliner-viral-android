package com.example.newsapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.newsapplication.enums.Apis;
import com.example.newsapplication.enums.Params;
import com.example.newsapplication.interfaces.RetrofitAPILogin;
import com.example.newsapplication.utils.Network;
import com.example.newsapplication.utils.SharedPref;
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

public class LoginActivity extends AppCompatActivity {
    SharedPref sharedPref;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        IntentButtonDaftar();

        sharedPref = new SharedPref(this);
        boolean isAlreadyLogin = sharedPref.getSPAlreadyLogin();
        Log.d("SP", "sharedPreferences - Login Activity: " + isAlreadyLogin);

        if(isAlreadyLogin) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //  Set Edit Text Type to password
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnMasuk = findViewById(R.id.btnMasuk);

        btnMasuk.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String message = "";
            if (email.isEmpty() && password.isEmpty()) {
                message = "Silahkan isi email/password anda";
                showSnackBar(view, message);
            } else if (email.isEmpty()) {
                message = "Silahkan isi email anda";
                showSnackBar(view, message);
            } else if (password.isEmpty()) {
                message = "Silahkan isi password anda";
                showSnackBar(view, message);
            } else {
                Log.d(Params.API_EMAIL, "Email: " + email);
                Log.d(Params.API_PASSWORD, "Password: " + password);

                /*new SendLoginData().execute(email, password);*/
                retrofitPostDataLogin(email, password);
                /*fastAndroidNetworkingPostDataLogin(email, password);*/
                /*volleyPostDataLogin(email, password);*/
            }

            /*Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();*/

            /*Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {}
            }).show();*/
        });
    }

    public void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).show();
    }

    protected void IntentButtonDaftar() {
        TextView buttonDaftar = findViewById(R.id.buttonDaftar);

        buttonDaftar.setOnClickListener((view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }));
    }

    public class getData {
        String status = null;
        String message = null;

    }

    //    asynctask login
    private class SendLoginData extends AsyncTask<String, Void, /*getData*/ JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String email = strings[0];
            String password = strings[1];

            /*getData data = new getData();*/
            JSONObject jsonObject = null;

            /*String responseJSON = postJSON("http://192.3.168.178/flutter/login.php",
                    "email",
                    "password", email, password);
            Log.d("Raw Response", "responseJSON: " + responseJSON);*/
            String responseJSON = Network.getJSON(Apis.URL_LOGIN +
                    "?" + "email=" + email + "&" + "password=" + password);
            Log.d("Raw Response", "responseJSON: " + responseJSON);
            try {
                jsonObject = new JSONObject(responseJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*String status = null;
            String message = "";

            try {
                JSONObject jsonObject = new JSONObject(responseJSON);
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            /*cara variable inner class*/
            /*try {
                JSONObject jsonObject = new JSONObject(responseJSON);
                data.status = jsonObject.getString("status");
                data.message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Status", "status: " + data.status);
            Log.d("Message", "message: " + data.message);*/
            /*return status + ", " + message;*/
            /*return responseJSON;*/
            /*return data;*/
            return jsonObject;
        }

        @Override
        protected void onPostExecute(/*getData data*/ /*String s*/ JSONObject jsonObject) {
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

            /*try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Status", "status: " + status);
            Log.d("Message", "message: " + message);
            Log.d("status", "onPostExecute: " + s.split(", ")[0]);
            String message = s.split(", ")[1];
            if (s.equals("success")) {
                Log.d("STATUS_LOGIN", "Login success");
            } else {
                Log.d("STATUS_LOGIN", "Login failed");
            }

            cara split message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if (s.split(", ")[0].equals("success")) {
                Log.d("STATUS_LOGIN", "Login success");
            } else {
                Log.d("STATUS_LOGIN", "Login failed");
            }*/

            /*cara inner class*/
            /*Toast.makeText(getApplicationContext(), data.message, Toast.LENGTH_SHORT).show();
            if (data.status.equals("success")) {
                Log.d("STATUS_LOGIN", "Login success");
            } else {
                Log.d("STATUS_LOGIN", "Login failed");
            }*/

            /*cara return jsonObject*/
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if (status.equals("success")) {
                Log.d("STATUS_LOGIN", "Login success");
            } else {
                Log.d("STATUS_LOGIN", "Login failed");
            }
        }

    }

    /*Using retrofit to consume API*/
    private void retrofitPostDataLogin(String email, String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.3.168.178/flutter/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPILogin retrofitAPILogin = retrofit.create(RetrofitAPILogin.class);

        Call<String> call = retrofitAPILogin.STRING_CALL(email, password);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = null;
                String message = "";
                if (response.isSuccessful() && response.body() != null) {
                    progressDialog.dismiss();
                    try {
                        JSONObject allData = new JSONObject(response.body());
                        Log.d("data login", "onResponse: " + allData);
                        status = allData.getString("status");
                        message = allData.getString("message");
                        JSONObject data = allData.getJSONObject("data");
                        String nama = data.getString("nama");
                        String email = data.getString("email");
                        String foto = data.getString("foto");
                        Log.d("data login user", "onResponse: " + data);

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            Log.d("STATUS_LOGIN", "Login success");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            /*intent.putExtra("nama", nama);
                            intent.putExtra("email", email);
                            intent.putExtra("foto", foto);*/

                            /*Sharedpreferences*/
                            sharedPref.saveSPString(SharedPref.USER_NAME, nama);
                            sharedPref.saveSPString(SharedPref.USER_EMAIL, email);
                            sharedPref.saveSPString(SharedPref.USER_PHOTO, foto);
                            sharedPref.saveSPBoolean(SharedPref.SESSION_MODE, true);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Log.d("STATUS_LOGIN", "Login failed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
    /*End of using retrofit*/

    /*Using fast Android Networking to consume API*/
    private void fastAndroidNetworkingPostDataLogin(String email, String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post(Apis.URL_LOGIN)
                .addBodyParameter(Params.API_EMAIL, email)
                .addBodyParameter(Params.API_PASSWORD, password)
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
                            Log.d("STATUS_LOGIN", "Login success");
                        } else {
                            Log.d("STATUS_LOGIN", "Login failed");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), (CharSequence) anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /*End of using fast android networking*/

    /*Using volley*/
    private void volleyPostDataLogin(String email, String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        /*with GET Method*/
        /*String url = Apis.URL_LOGIN + "?" + "email=" + email + "&" + "password=" + password;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            Log.d("STATUS_LOGIN", "Login success");
                        } else {
                            Log.d("STATUS_LOGIN", "Login failed");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);*/

        /*with POST Method*/
        StringRequest postRequest = new StringRequest(Request.Method.POST, Apis.URL_LOGIN,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            Log.d("STATUS_LOGIN", "Login success");
                        } else {
                            Log.d("STATUS_LOGIN", "Login failed");
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
                params.put(Params.API_EMAIL, email);
                params.put(Params.API_PASSWORD, password);

                return params;
            }
        };
        queue.add(postRequest);
    }
    /*End of using volley*/
}