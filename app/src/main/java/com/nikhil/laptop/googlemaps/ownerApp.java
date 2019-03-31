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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nikhil.laptop.googlemaps.R.string.navigation_drawer_open;

public class ownerApp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    SharedPreferences prf;
    String fword,lword;
    char[] arr,arr1,arr2;
    String s,s1,s3;
    private FirebaseFirestore mydb;
    long n,n1;
    int c=0;
    double d1,d2;
    private static final String TAG="Logs";
   public static String username;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_app);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);

       username=  prf.getString("username",null);
        mydb=FirebaseFirestore.getInstance();

        mydb.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                              String email =document.getId();
                              if (email.equals(username)){
                                  title= (String) document.getData().get("Addresss");

                              }
//                                namen=namen+document.getData().get("First Name");
//                                //obj=document.getData();
//                                //n=obj.toString();
//                                txt2.setText(namen);
                            }

                        } else {
                        }
                    }
                });
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(TAG, "Scanner 1");
                String ett=result.getContents();
                arr=ett.toCharArray();
                for(int i=0;i<ett.length();i++)
                {
                    if(arr[i]==',')
                    {
                        Log.d(TAG, "Scanner 2"+title);
                        fword=ett.substring(0,i);
                        lword=ett.substring(i+1,ett.length());
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
                s = sdf.format(new Date());
                s1 = sdf1.format(new Date());
                Log.d(TAG, "Scanner 2");
                mydb.collection(title+"_Stats")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        String s7=document.getId();
                                        if(fword.equals(s7))
                                        {
                                            Log.d(TAG, "Scanner 3");
                                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
                                            s3 = sdf2.format(new Date());
                                            c=1;
                                            DocumentReference washingtonRef = mydb.collection(title+"_Stats").document(fword);
                                            washingtonRef
                                                    .update("Out-Time", s3)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(ownerApp.this,"Done",Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ownerApp.this,"Fail",Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                            mydb.collection("locations")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            Log.d(TAG, "Scanner 4");
                                                            if (task.isSuccessful()) {
                                                                for (DocumentSnapshot document : task.getResult()) {
                                                                    String id=document.getId();
                                                                    d1=(Double)document.getData().get("Latitude");
                                                                    d2=(Double)document.getData().get("Longitude");
                                                                    if(id.equals(title))
                                                                    {
                                                                        n=document.getLong("Parkings");
                                                                        d1=(Double)document.getData().get("Latitude");
                                                                        d2=(Double)document.getData().get("Longitude");
                                                                        Log.d(TAG, "Scanner 6");
                                                                    }
                                                                }
                                                                n=n+1;
                                                             //   Toast.makeText(ownerApp.this,"Hello World"+n1,Toast.LENGTH_LONG).show();
                                                                onClickBtn5(n,d1,d2);

                                                            }

                                                        }
                                                    });





                                        }
                                    }
                                    if(c==0)
                                    {
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("Vehicle Number", fword);
                                        userMap.put("Email-ID", lword);
                                        userMap.put("Date", s);
                                        userMap.put("In-Time", s1);
                                        mydb.collection(title+"_Stats").document(fword).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Scanner 7");
                                                Toast.makeText(ownerApp.this, "Added successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        mydb.collection("locations")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (DocumentSnapshot document : task.getResult()) {
                                                                String id=document.getId();

                                                                if(id.equals(title))
                                                                {
                                                                    n1=document.getLong("Parkings");
                                                                    d1=(Double)document.getData().get("Latitude");
                                                                    d2=(Double)document.getData().get("Longitude");
                                                                 //   Toast.makeText(ownerApp.this,"Hello World"+n1,Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                            n1=n1-1;
                                                        //    Toast.makeText(ownerApp.this,"Hello World"+n1,Toast.LENGTH_LONG).show();
                                                            onClickBtn5(n1,d1,d2);

                                                        }
                                                    }
                                                });


                                    }


                                }
                            }
                        });






            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClickBtn5(long nt,double d1,double d2) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Latitude",d1);
        userMap.put("Longitude",d2);
        userMap.put("Parkings",nt);
        mydb.collection("locations").document(title).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ownerApp.this, "Done bro", Toast.LENGTH_LONG).show();

            }
        });
    }
}



