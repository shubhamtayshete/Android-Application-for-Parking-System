package com.nikhil.laptop.googlemaps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikhil.laptop.googlemaps.MapActivity;


import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;
import static com.nikhil.laptop.googlemaps.MapActivity.lattitude;
import static com.nikhil.laptop.googlemaps.MapActivity.longitude;
public class MarkerDialog extends AppCompatDialogFragment {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    final MapActivity mapActivity =new MapActivity();
    Marker destination = mapActivity.returnDestination();
    private FirebaseFirestore mydb;
    public static Long parking;
    public static String parking1;

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.marker_dialog,null);
        TextView t1;
        t1 = (TextView) view.findViewById(R.id.editText);
        t1.setText(destination.getTitle());
        mydb=FirebaseFirestore.getInstance();
        mydb.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String location =document.getId();
                                if (location.equals(destination.getTitle())){
                                    parking= (Long) document.getLong("Parkings");
                                    parking1 =String.valueOf(parking);
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
        TextView t2;
        t2 =view.findViewById(R.id.editText2);
        t2.setText("Parkings available: "+parking1);
        builder.setView(view).setTitle("Info").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        getDirection = (Button) view.findViewById(R.id.button2);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mapActivity.findDirection();
                //new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

            }
        });





        return builder.create();
    }


}
