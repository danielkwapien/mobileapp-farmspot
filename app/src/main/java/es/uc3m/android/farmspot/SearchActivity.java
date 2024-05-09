package es.uc3m.android.farmspot;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Declare a Geocoder
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_search);

        // Initialize the Geocoder
        geocoder = new Geocoder(this, Locale.getDefault());

        //double[] coordinates = convertLocationToCoordinates(location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_search) {
                    return true;
                } else if (item.getItemId() == R.id.navigation_add) {
                    startActivity(new Intent(getApplicationContext(), AddActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.navigation_favorites){
                    startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_profile){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

    private GoogleMap googleMap;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        convertLocationToCoordinates("Madrid, Spain");
        // No need to add markers here because it's already done in the convertLocationToCoordinates method
    }

    // Method to convert the location into coordinates
    private void convertLocationToCoordinates(String location) {
        try {
            // Get the list of addresses matching the entered location
            List<Address> addresses = geocoder.getFromLocationName(location, 1);

            // Check if any addresses were found
            if (!addresses.isEmpty()) {
                // Get the first address
                Address address = addresses.get(0);

                // Get the coordinates of the address
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Use the coordinates as desired (e.g., add a marker on the map)
                LatLng locationLatLng = new LatLng(latitude, longitude);
                // Add a marker on the map
                googleMap.addMarker(new MarkerOptions().position(locationLatLng).title(location));
                // Move the camera to the marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 10));
            } else {
                // No addresses found for the entered location
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

