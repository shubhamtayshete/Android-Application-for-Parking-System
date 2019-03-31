package com.nikhil.laptop.googlemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class userRegister extends AppCompatActivity {
    private FirebaseFirestore mydb;
    EditText user,email,phone,address,name,psswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);
        user=(EditText)findViewById(R.id.edit1);
        email=(EditText)findViewById(R.id.edit4);
        phone=(EditText)findViewById(R.id.edit5);
        name=(EditText)findViewById(R.id.edit3);
        psswd=(EditText)findViewById(R.id.edit2);
        mydb=FirebaseFirestore.getInstance();
    }
    public void onUserSubmit(View view) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("Username", user.getText().toString());
        userMap.put("Password", psswd.getText().toString());
        userMap.put("Email-ID", email.getText().toString());
        userMap.put("Phone Number", phone.getText().toString());
        userMap.put("Role","User");
        mydb.collection("users").document(email.getText().toString()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(userRegister.this, "Added successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(userRegister.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

