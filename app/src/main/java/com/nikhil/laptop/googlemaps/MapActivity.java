package com.nikhil.laptop.googlemaps;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import static com.nikhil.laptop.googlemaps.R.string.navigation_drawer_open;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,TaskLoadedCallback,NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences prf;
    Intent intent;
    private static GoogleMap gMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        gMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gMap.setMyLocationEnabled(true);
            // UiSettings.setZoomControlsEnable(true);

            gMap.getUiSettings().setZoomControlsEnabled(true);
            gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker args) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker args) {

                    // Getting view from the layout file info_window_layout


                    // Getting the position from the marker



                    gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        public void onInfoWindowClick(Marker marker)
                        {
                            endMarkerOoption=marker;
                            Toast.makeText(MapActivity.this, "Clicked on window", Toast.LENGTH_SHORT).show();
                            MarkerDialog markerDialog =new MarkerDialog();
                            markerDialog.show(getSupportFragmentManager(),"marker dialog");

                        }
                    });

                    // Returning the view containing InfoWindow contents


                    return null;
                }
            });

        }
    }
    private DrawerLayout drawer;

    private static final String TAG ="MapActivity";
    private static final String FINE_LOCATION =Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION =Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private static  final float DefaultZoom=10f;
    private static Polyline currentPolyline;
    private static MarkerOptions markerOptions[]=new MarkerOptions[20];
    public static double lat[]=new double[20];
    public static double lng[]=new double[20];
   public static int i;
   public static Marker endMarkerOoption;
    static double lattitude,longitude,endLattitude,endLongitude;
    private   FirebaseFirestore mydb;
    //Widgets
//    Vars
    private Boolean mLocationPermissionGranted =false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);

        prf.getString("username",null);




        getLocationPermission();
        mydb = FirebaseFirestore.getInstance();
        FindNearByMe();
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MessageFragment()).commit();
            navigationView.setCheckedItem(R.id.QrCode);
        }

    }
@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.QrCode:
                Intent intent =new Intent(MapActivity.this,QrCode.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                 intent =new Intent(MapActivity.this,MainActivity.class);
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
                break;
           }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //find=(ImageView) findViewById(R.id.ic_find);
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!"+task.getResult());
                            Location currentLocation = (Location) task.getResult();
                            Log.d(TAG, "onComplete: found location!"+currentLocation);

                            lattitude=currentLocation.getLatitude();
                            longitude=currentLocation.getLongitude();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DefaultZoom,"My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title(title);
            gMap.addMarker(options);
        }


    }
    private void initMap()
    {
        Log.d(TAG, "initMap: Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
//        LatLng latLng1= new LatLng((22323),(3343));
//        MarkerOptions options = new MarkerOptions().position(latLng1);

//        gMap.addMarker(options);
//        find.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MapActivity.this,"Done",Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    private  void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Gettting location Permission");
        String[] permission ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted=true;
                initMap();
            }else
            {
                ActivityCompat.requestPermissions(this,permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }

        }else
        {
            ActivityCompat.requestPermissions(this,permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted=false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length>0){
                    for (int i =0;i <grantResults.length;i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;

                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionGranted=true;
                    //Init the MAp
                    initMap();
                }
            }
        }
    }
    public void getDeviceLocation(View view) {
        Log.d(TAG, "getDeviceLocation: Get device's current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocationPermissionGranted) {
                com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: Found location");
                            Location currentlocatiom = (Location)task.getResult();
                            moveCamera(new LatLng(currentlocatiom.getLatitude(),currentlocatiom.getLongitude()),DefaultZoom,"My Location");
                        }
                        else {
                            Log.d(TAG, "onComplete: Unable to get location");
                            Toast.makeText(MapActivity.this,"Unable to get location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e)
        {
            Log.d(TAG, "getDeviceLocation: "+e.getMessage());
        }

    }
    public void FindNearByMe() {
if(i<13) {
    mydb.collection("locations")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            lat[i] = (Double) document.getData().get("Latitude");
                            lng[i] = (Double) document.getData().get("Longitude");


                            String title = document.getId();
                            Log.d(TAG, "1)Error getting documents: " + i);
                            LatLng latLng = new LatLng((lat[i]), (lng[i]));
                            markerOptions[i] = new MarkerOptions();
                            markerOptions[i].position(latLng);
                            markerOptions[i].title(title);
                            gMap.addMarker(markerOptions[i]);
                            i = i + 1;
//                                namen=namen+document.getData().get("First Name");
//                                //obj=document.getData();
//                                //n=obj.toString();
//                                txt2.setText(namen);
                        }
                        i=0;
                    } else {
                    }
                }
            });

    mydb.collection("locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

            if (e !=null)
            {

            }

            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
            {

                Double lat = (Double) documentChange.getDocument().getData().get("Latitude");
                Double lng = (Double) documentChange.getDocument().getData().get("Longitude");
                String title = documentChange.getDocument().getId();
                Log.d(TAG, "1)Error getting documents: " + title);
                LatLng latLngNew = new LatLng((lat), (lng));
                MarkerOptions markerOptionsNew = new MarkerOptions();
                markerOptionsNew.position(latLngNew);
                markerOptionsNew.title(title);
                gMap.addMarker(markerOptionsNew);

            }
        }
    });


}
//        LatLng latLng1[] =new LatLng[5];
//        for(int i=0;i<5;i++){
//           latLng1[i]=new LatLng((28.7041),(77.1025));
//            Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng1[i].latitude + ", lng: " + latLng1[i].longitude );
//            // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DefaultZoom))
//            options[0] = new MarkerOptions().position(latLng1[i]).title("Delhi");
//            Log.d(TAG, "moveCamera: moving the camera to: lat: " + options[0].getTitle() );
//            gMap.addMarker(options[0]);
//        }
//        LatLng latLng=new LatLng((56.9663),(77.1025));
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
//        // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DefaultZoom));
//        options = new MarkerOptions();
//        options.position(latLng);
//        options.title("Delhi");
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + options.getTitle() );
//        gMap.addMarker(options);


//        LatLng latLng2=new LatLng((42.6942),(72.83023));
//        endLattitude=latLng2.latitude;
//        endLongitude=latLng2.longitude;
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng2.latitude + ", lng: " + latLng2.longitude );
//        // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DefaultZoom));
//        options2 = new MarkerOptions().position(latLng2).title("Surat");
//        gMap.addMarker(options2);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        this.endMarkerOoption =marker;
        MarkerDialog markerDialog =new MarkerDialog();
        markerDialog.show(getSupportFragmentManager(),"marker dialog");
        gMap.getUiSettings().setMapToolbarEnabled(true);

        return true;
    }
   public Marker returnDestination(){
        return endMarkerOoption;
   }


    public void findDirection(){
        LatLng currentLocation = new LatLng((lattitude), (longitude));
        MarkerOptions startMarkerOption =new MarkerOptions().position(currentLocation);
        //Log.d(TAG, "travel from  " +startMarkerOption.getPosition()+"  To  "+endMarkerOoption.getPosition());
        String url = getUrl(startMarkerOption.getPosition(),endMarkerOoption.getPosition(),"driving");
        new FetchURL(MapActivity.this).execute(url, "driving");


    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String api = "AIzaSyA4-dMnY_NUzgrpfEuT8e9XfF8fLwoy0YU";
        Log.d(TAG, "Dirction calculation starts" + api);
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key="+api;

        //Log.d(TAG, "Entered" +options2.getPosition());
        return url;

    }
    @Override
    public void onTaskDone(Object... values) {
        Log.d(TAG, "Value oc Current polyline" +currentPolyline);
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        currentPolyline =gMap.addPolyline((PolylineOptions) values[0]);
    }
    @Override
    protected void attachBaseContext(Context base) {
       MultiDex.install(this);
        super.attachBaseContext(base);
    }
}
