package com.nikhil.laptop.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import static com.nikhil.laptop.googlemaps.R.string.navigation_drawer_open;

public class ownerApp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    SharedPreferences prf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_app);


        drawer = findViewById(R.id.drawer_layout1);
        NavigationView navigationView = findViewById(R.id.nav_view1);
       navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(this, "1 scanning", Toast.LENGTH_LONG).show();

        Toolbar toolbar = findViewById(R.id.toolBar1);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MessageFragment()).commit();
        }


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, item.getItemId(), Toast.LENGTH_LONG).show();

        switch (item.getItemId()) {
            case R.id.getStat:
                Intent intent =new Intent(ownerApp.this,getStats.class);
                startActivity(intent);
                break;
            case R.id.Scanner:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
                Activity activity = this;
                Toast.makeText(this, " scanning", Toast.LENGTH_LONG).show();

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                break;
            case R.id.nav_logout1:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                intent =new Intent(ownerApp.this,MainActivity.class);
                prf = getSharedPreferences("user_details",MODE_PRIVATE);

                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
