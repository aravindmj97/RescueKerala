package com.a.rescuekerala;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements LocationListener{

    CardView sos;
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference databaseReference;
    protected LocationManager locationManager;

    EditText name, phone, msg;
    TextView yourLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("help_data");

        name = (EditText) findViewById(R.id.Name);
        phone = (EditText) findViewById(R.id.PhoneNo);
        msg = (EditText) findViewById(R.id.Message);

        yourLocation = (TextView) findViewById(R.id.loc);

        sos = (CardView) findViewById(R.id.sosbtn);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new  String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                    return;
                }
            if (TextUtils.isEmpty(name.getText()) && TextUtils.isEmpty(phone.getText())){
                Toasty.warning(getApplicationContext(), "Enter Name and Phone Number", Toast.LENGTH_SHORT, true).show();
            }
            else {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                HelpData helpData = new HelpData(location.getLatitude(), location.getLongitude(), name.getText().toString(), phone.getText().toString(), msg.getText().toString());

                                databaseReference.push().setValue(helpData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toasty.success(getApplicationContext(), "Location Sent Successfully", Toast.LENGTH_SHORT, true).show();
                                                name.setText(null);
                                                phone.setText(null);
                                                msg.setText(null);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toasty.warning(getApplicationContext(), "Failed to Send Location. Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new  String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }

        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    @Override
    public void onLocationChanged(Location location) {
         yourLocation.setText("Current Location"+ String.valueOf(location.getLatitude())+"  "+ String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {


    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
