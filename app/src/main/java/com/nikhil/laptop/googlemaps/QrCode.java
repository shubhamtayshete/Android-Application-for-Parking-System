package com.nikhil.laptop.googlemaps;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCode extends AppCompatActivity {
    EditText text12;
    Button gen_btn1;
    ImageView image;
    String text2Qr;
    SharedPreferences prf;
    private static final String TAG ="QrCode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcodelayout);
        text12 = (EditText) findViewById(R.id.text678);
        gen_btn1 = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image123);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        final String username= prf.getString("username",null);
        Log.d(TAG, "Session 1"+username);

        gen_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2Qr = text12.getText().toString().trim();
                String ftext = text2Qr +","+username;
                String s=text2Qr.substring(0,2);
                if(s.matches("^[a-zA-Z]*$")) {
                    //Toast.makeText(MainActivity.this, "First part correct", Toast.LENGTH_LONG).show();
                    String y = text2Qr.substring(2, 4);
                    if (y.matches("^[0-9]*$")) {
                        //  Toast.makeText(MainActivity.this, "Second part correct", Toast.LENGTH_LONG).show();
                        String h = text2Qr.substring(4, 6);
                        if (h.matches("^[a-zA-Z]*$")) {
                            //  Toast.makeText(MainActivity.this, "Third part correct123", Toast.LENGTH_LONG).show();
                            String r = text2Qr.substring(6, text2Qr.length());
                            if (r.matches("^[0-9]*$") && r.length() < 5) ;
                            {
                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                try {
                                    BitMatrix bitMatrix = multiFormatWriter.encode(ftext, BarcodeFormat.QR_CODE, 200, 200);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                    image.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            String c = text2Qr.substring(4, 5);
                            if (c.matches("^[a-zA-Z]*$")) {
                                //  Toast.makeText(MainActivity.this, "Third part correct456", Toast.LENGTH_LONG).show();
                                String r = text2Qr.substring(5, text2Qr.length());
                                if (r.matches("^[0-9]*$") && r.length() < 5) ;
                                {
                                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                    try {
                                        BitMatrix bitMatrix = multiFormatWriter.encode(ftext, BarcodeFormat.QR_CODE, 200, 200);
                                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                        image.setImageBitmap(bitmap);
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }
                                }



                            }
                        }
                    }
                }
                else {

                    Toast.makeText(QrCode.this, "Wrong Entry", Toast.LENGTH_LONG).show();
                }
            }





        });
    }
}