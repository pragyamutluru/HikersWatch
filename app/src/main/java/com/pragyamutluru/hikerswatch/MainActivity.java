package com.pragyamutluru.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geo;
    TextView lat;
    TextView lon;
    TextView add;
    TextView acc;
    TextView alt;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }


    public void updateLocation(Location location) {
        lat.setText("Lattitude : "+Double.toString(location.getLatitude()));
        lon.setText("Longitude : "+Double.toString(location.getLongitude()));
        acc.setText("Accuracy : "+Double.toString(location.getAccuracy()));
        alt.setText("Altitude : "+Double.toString(location.getAltitude()));
        geo= new Geocoder(getApplicationContext(),Locale.getDefault());
        try {
            List<Address> listAddress = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddress != null && listAddress.size() > 0) {
               // String str = listAddress.get(0).toString();
                StringBuilder str = new StringBuilder();
                Address loc = listAddress.get(0);
                for (int i = 0; i < loc.getMaxAddressLineIndex(); i++) {
                    str.append(loc.getAddressLine(i)).append("\n");
                }
                str.append(loc.getLocality()).append("\n");
                str.append(loc.getPostalCode()).append("\n");
                str.append(loc.getCountryName()).append("\n");

                add.setText("Address : \n"+str.toString());


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         lat= (TextView) findViewById(R.id.lat);
         lon= (TextView) findViewById(R.id.lon);
         add= (TextView) findViewById(R.id.add);
         acc= (TextView) findViewById(R.id.acc);
         alt= (TextView) findViewById(R.id.alt);

        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geo= new Geocoder(MainActivity.this, Locale.getDefault());

                updateLocation(location);

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
        };
        if(Build.VERSION.SDK_INT<23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                geo= new Geocoder(MainActivity.this, Locale.getDefault());

                Location user= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                updateLocation(user);
            }
        }


    }
}
