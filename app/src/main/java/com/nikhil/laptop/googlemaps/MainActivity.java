package com.nikhil.laptop.googlemaps;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    EditText uname, pwd;
    Button loginBtn;
    SharedPreferences pref;
    private FirebaseFirestore mydb;

    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb =FirebaseFirestore.getInstance();
        uname = (EditText)findViewById(R.id.txtName);
        pwd = (EditText)findViewById(R.id.txtPwd);
        loginBtn = (Button)findViewById(R.id.btnLogin);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = uname.getText().toString();
                final String pwd1 = pwd.getText().toString();

                mydb.collection("users")


                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                int c=0;
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        String password = (String) document.getData().get("Password");
                                        String role = (String) document.getData().get("Role");
                                        String email = document.getId();


                                        if(username.equals(email) && pwd1.equals(password)){
                                            c=1;
                                            SharedPreferences.Editor editor = pref.edit();                                            editor.putString("username",username);
                                            editor.putString("username",username);
                                            editor.commit();
                                            Log.d(TAG, "Session 1"+username);


                                            ;
                                            if(IsServicesOk()){
                                                Toast.makeText(getApplicationContext(), "Login Successful",Toast.LENGTH_SHORT).show();
                                                if (role.equals("Owner"))
                                                {
                                                    intent =new Intent(MainActivity.this,ownerApp.class);
                                                    startActivity(intent);

                                                }
                                                else {
                                                    intent= new Intent(MainActivity.this,MapActivity.class);
                                                    startActivity(intent);
                                                    }
                                            }

                                        }

                                    }
                                    if (c==0){


                                        Toast.makeText(getApplicationContext(),"Credentials are not valid",Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),"Login Unsuccesful",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }
        });

    }

    @Override
    public void onBackPressed() {
    }
    public boolean IsServicesOk(){

        Log.d(TAG, "IsServicesOk: Checking_Google_Services_Version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS){
            //Everything is OK
            Log.d(TAG, "IsServicesOk: Google_Play_Services_is_Working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //An error is resolvable
            Log.d(TAG, "IsServicesOk: Error _Can_fix");
            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else       {

            Toast.makeText(this,"You cant make map request",Toast.LENGTH_SHORT).show();
                    }return false;
    }
    public void registerAsUser(View view){
        intent=new Intent(MainActivity.this,userRegister.class);
        startActivity(intent);
    }
    public void registerAsOwner(View view){

        intent=new Intent(MainActivity.this,OwnerSignUp.class);
        startActivity(intent);

    }
}
