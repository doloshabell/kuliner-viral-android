package com.example.newsapplication.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.newsapplication.R;
import com.example.newsapplication.utils.SharedPref;
import com.example.newsapplication.utils.TabLayoutAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    SharedPref sharedPref;

    TabLayout tabLayout;
    TabLayoutAdapter tabLayoutAdapter;

    /*ViewPager2 viewPager2;
    String[] titles = new String[]{"Kuliner", "Review", "Gallery"};*/
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------------- NAV DRAWER ------------ //
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // color hamburger icon
        toggle.getDrawerArrowDrawable().setColor(
                getResources().getColor(R.color.white));

        NavigationView navigationView = findViewById(R.id.nav_view);

        // tablayout
        /*tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutAdapter = new TabLayoutAdapter(this);
        viewPager2.setAdapter(tabLayoutAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();*/

        RelativeLayout relativeLayoutAppBarMain = findViewById(R.id.layoutAppBarMain);
        tabLayout = relativeLayoutAppBarMain.findViewById(R.id.tabLayout);
        viewPager = relativeLayoutAppBarMain.findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Culinary"));
        tabLayout.addTab(tabLayout.newTab().setText("Review"));
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayoutAdapter = new TabLayoutAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabLayoutAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*Floating Action Button*/
        FloatingActionButton floatingActionButton = relativeLayoutAppBarMain.findViewById(R.id.fabToKulinerViral);
        floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FormKulinerViralActivity.class));
        });

        /*use intent to get data from login*/
        /*Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String email = intent.getStringExtra("email");
        String foto = intent.getStringExtra("foto");*/
        /*end of using intent to get data from login*/

        /*use SharedPreferences to get data from login*/
        sharedPref = new SharedPref(this);
        String nama = sharedPref.getSPName();
        String email = sharedPref.getSPEmail();
        String foto = sharedPref.getSPPhoto();
        boolean isAlreadyLogin = sharedPref.getSPAlreadyLogin();


        Log.d("main activity", "nama dari login: " + nama);
        Log.d("main activity", "email dari login: " + email);
        Log.d("main activity", "foto dari login: " + foto);
        Log.d("SP", "sharedPreferences - Main Activity: " + isAlreadyLogin);
        /*end of using SharedPreferences to get data from login*/

        navigationView.getMenu().findItem(R.id.nav_user_access).setOnMenuItemClickListener(menuItem -> {
            logout(isAlreadyLogin);
            return true;
        });

        View header = navigationView.getHeaderView(0);
        TextView textName = header.findViewById(R.id.text_username);
        TextView textEmail = header.findViewById(R.id.text_user_email);
        ImageView imageView = header.findViewById(R.id.imageView);

        if (!isAlreadyLogin) {
            imageView.setImageResource(R.drawable.androidstudioicon);
            navigationView.getMenu().findItem(R.id.nav_user_access).setTitle("Login");
            showAlertDialog();
        } else if (isAlreadyLogin) {
            textName.setText(nama);
            textEmail.setText(email);
            new GetImage(imageView).execute(foto);
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(boolean isAlreadyLogin) {
        if (isAlreadyLogin) {
            sharedPref.removeData();
        }

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            /*super.moveTaskToBack(true);*/
            super.onBackPressed();
        }
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetImage(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new java.net.URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Warning")
                .setMessage("Kamu belum login. Login dulu yuk :)")
                .setIcon(R.drawable.androidstudioicon)
                .setCancelable(false)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}