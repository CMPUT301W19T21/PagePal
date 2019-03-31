package ca.team21.pagepal.views;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ca.team21.pagepal.R;

/**
 *  Class used to display pickup location to the borrower
 */
public class DisplayMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     *  This is a callback  triggered when the map is ready to be used.
     * It  gets the latitude and longitude from the intent, and finds the corresponding map location.
     *  It then  sets an initial marker to match that LatLng.
     * @param googleMap a map instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get lat and long from the intent
        Double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        Double longitude = getIntent().getDoubleExtra("longitude", 0.0);

        //get location and place a marker on the map at that location
        LatLng swapLocation = new LatLng(new Double(latitude) , new Double(longitude) );
        mMap.addMarker(new MarkerOptions().position(swapLocation).title("Book Swap Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(swapLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(swapLocation, 15));


    }
}
