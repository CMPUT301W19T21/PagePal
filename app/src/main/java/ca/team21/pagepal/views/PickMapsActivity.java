package ca.team21.pagepal.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.team21.pagepal.R;

/**
 * An activity that is used to allow a user to pick a location from a map and pass the coordinates to another activity.
 */
public class PickMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private LatLng swapLatLng;
    LocationManager locationManager;
    LocationListener locationListener;

    /**
     * Callback method when permissions are requested.
     * If permissions are granted, gets current location updates from LocationManager.
     * @param requestCode code passed in during ActivityCompat.requestPermissions(), in this case will always be 1
     * @param permissions the current permissions
     * @param grantResults the grant results for the corresponding permissions, will be PERMISSION_GRANTED if permissions were granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (lastLocation != null) {
                            LatLng userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                            // Sets initial marker/map to be focused on user location
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("Book Swap Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            swapLatLng = userLocation;
                        } else {
                            Toast.makeText(getApplicationContext(), "Turn on Location Services", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Turn on Location Services", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    /**
     *  This is a callback  triggered when the map is ready to be used.
     * It sets up click and location listeners and requests location permissions if needed.
     * It then gets the user's current location and sets an initial marker to match that LatLng.
     * @param googleMap a map instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Sets up location listener
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

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

        //Requests location permission if sdk needs it
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {

                 if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                     // Gets current user location
                     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                     Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                     if (lastLocation != null) {
                         LatLng userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                         // Sets initial marker/map to be focused on user location
                         mMap.addMarker(new MarkerOptions().position(userLocation).title("Book Swap Location"));
                         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                         swapLatLng = userLocation;
                     } else {
                         Toast.makeText(getApplicationContext(), "Turn on Location Services", Toast.LENGTH_SHORT).show();

                     }
                 } else {
                     Toast.makeText(getApplicationContext(), "Turn on Location Services", Toast.LENGTH_SHORT).show();

                 }

            }
        }


    }

    /**
     * When the map is clicked, removes previous marker and adds a new one at click location
     * @param swapLocation is the LatLng of the long click on the map
     */
    @Override
    public void onMapLongClick(LatLng swapLocation) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(swapLocation).title("Book Swap Location"));
        swapLatLng = swapLocation;
    }

    /**
     * Gets the location of the current marker, adds it to intent
     * Passes information back to previous activity and finishes
     * @param view
     */
    public void getLocation (View view) {
        Double latitude = swapLatLng.latitude;
        Double longitude = swapLatLng.longitude;

        Intent data = new Intent();

        data.putExtra("latitude", latitude);
        data.putExtra("longitude", longitude);

        setResult(RESULT_OK, data);
        finish();


    }

}
