package com.nikhil.laptop.googlemaps;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class getStats extends AppCompatActivity {
    private FirebaseFirestore mydb;
    String companies[] = {"Google","Windows","iPhone","Nokia","Samsung",

            "Google","Windows","iPhone","Nokia","Samsung",

            "Google","Windows","iPhone","Nokia","Samsung"};



    TableLayout tl;

    TableRow tr;

    TextView companyTV,valueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stats);
        TextView t =(TextView)findViewById(R.id.textViewTest);
        mydb = FirebaseFirestore.getInstance();
        mydb.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {


                                String title = document.getId();

//                                namen=namen+document.getData().get("First Name");
//                                //obj=document.getData();
//                                //n=obj.toString();
//                                txt2.setText(namen);
                            }

                        } else {
                        }
                    }
                });


    }
}
