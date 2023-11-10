package my.edu.devhacktesting3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.devhacktesting3.R;

public class NearbyFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LatLng current;
    // Set the custom firebase URL
    String customDatabaseUrl = "https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);

    private String uid, type, carplateIntent, trackBus = "no";

    public void setUid(String uid, String type) {
        this.type = type;
        this.uid = uid;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if there are arguments
        Bundle args = getArguments();
        if (args != null) {
            // Retrieve values from arguments
            uid = args.getString("uid");
            type = args.getString("type");
            carplateIntent = args.getString("carplate");
            trackBus = args.getString("trackBus");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        // Load user location and bus stop locations from Firebase and display them on the map

        if(trackBus.equalsIgnoreCase("YES")){
            loadUserLocationFromFirebase();
            loadBusLocationFromFirebase();
        } else {
            loadBusStopsFromFirebase();
            loadUserLocationFromFirebase();
        }
    }

    //handle marker click for each icon
    @Override
    public boolean onMarkerClick(Marker marker) {
        String tag = (String) marker.getTag();
        if(tag!=null){
            if(tag.equalsIgnoreCase("bus")){
                showBusDetailsFrameLayout();
            } else if(tag.equalsIgnoreCase("busStop")){

            } else if(tag.equalsIgnoreCase("user")){

            }
        }
        return false;
    }

    private void updateMapWithLocation(LatLng currentLocation, Bitmap currentBitmap, String type) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLocation)
                .zoom(14)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Bitmap resizedCurrentBitmap = Bitmap.createScaledBitmap(currentBitmap, 100, 100, false);
        BitmapDescriptor resizedCurrentMarkerIcon = BitmapDescriptorFactory.fromBitmap(resizedCurrentBitmap);
        MarkerOptions currentMarkerOptions = new MarkerOptions()
                .position(currentLocation)  // Set the marker's position
                .icon(resizedCurrentMarkerIcon); // Set the custom marker image
        // Add the marker to the map
        Marker currentMarker = mMap.addMarker(currentMarkerOptions);
        if(type.equalsIgnoreCase("bus")){
            currentMarker.setTag("bus");
        } else if (type.equalsIgnoreCase("busStop")){
            currentMarker.setTag("busStop");
        } else if (type.equalsIgnoreCase("user")){
            currentMarker.setTag("user");
        }
    }
    private void loadUserLocationFromFirebase() {
        //if user is user, display as face icon
        if(type.equalsIgnoreCase("USER")){
            DatabaseReference userLocationRef = firebaseDatabase.getReference("/UserLocation/"+uid);

            userLocationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double userLatitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double userLongitude = dataSnapshot.child("longitude").getValue(Double.class);
                    if (userLatitude != 0 && userLongitude != 0) {
                        LatLng userLocation = new LatLng(userLatitude, userLongitude);
                        Bitmap currentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.limxuan);
                        updateMapWithLocation(userLocation, currentBitmap, "user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "Error retrieving user's location from Firebase.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //if user if driver, display as a bus icon
        else {
            DatabaseReference busLocationRef = firebaseDatabase.getReference("/Bus/"+uid);
            busLocationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double busLatitude = snapshot.child("latitude").getValue(Double.class);
                    double busLongitude = snapshot.child("longitude").getValue(Double.class);
                    if (busLatitude != 0 && busLongitude != 0) {
                        LatLng userLocation = new LatLng(busLatitude, busLongitude);
                        Bitmap currentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
                        updateMapWithLocation(userLocation, currentBitmap, "bus");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loadBusStopsFromFirebase() {
        // Replace "YourBusStopsReference" with the actual path to your bus stops data in Firebase
        DatabaseReference busStopsRef = firebaseDatabase.getReference("/BusStops");

        busStopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot busStopSnapshot : dataSnapshot.getChildren()) {
                    double busStopLatitude = busStopSnapshot.child("latitude").getValue(Double.class);
                    double busStopLongitude = busStopSnapshot.child("longitude").getValue(Double.class);

                    if (busStopLatitude != 0 && busStopLongitude != 0) {
                        LatLng busStopLocation = new LatLng (busStopLatitude,busStopLongitude);
                        Bitmap busStopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus_n);
                        updateMapWithLocation(busStopLocation, busStopBitmap, "busStop");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error retrieving bus stop locations from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBusLocationFromFirebase() {
        DatabaseReference busRef = firebaseDatabase.getReference("/Bus");
        final boolean[] busExist = {false};
        busRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot busStopSnapshot : dataSnapshot.getChildren()) {
                    String carplate = busStopSnapshot.child("carplate").getValue(String.class);

                    if(carplate.equals(carplateIntent)){
                        busExist[0] = true;
                        double busLatitude = busStopSnapshot.child("latitude").getValue(Double.class);
                        double busLongitude = busStopSnapshot.child("longitude").getValue(Double.class);

                        if (busLatitude != 0 && busLongitude != 0) {
                            LatLng busLocation = new LatLng (busLatitude,busLongitude);
                            Bitmap busStopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
                            updateMapWithLocation(busLocation, busStopBitmap, "bus");
                        }
                    }
                }
                if(busExist[0] = false){
                    Toast.makeText(requireActivity(), "Cannot retrieve the related bus details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error retrieving bus locations from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //show the bus details when user press on the bus
    private void showBusDetailsFrameLayout() {
        if (getView() != null) {
            // Set the visibility of busDetailsFrameLayout to VISIBLE
            View busDetailsFrameLayout = getView().findViewById(R.id.busDetailsFrameLayout);
            busDetailsFrameLayout.setVisibility(View.VISIBLE);

            // Set an OnClickListener for the closeButton
            ImageButton closeButton = getView().findViewById(R.id.closeButton);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hide the busDetailsFrameLayout when the closeButton is clicked
                    if (getView() != null) {
                        // Set the visibility of busDetailsFrameLayout to GONE
                        View busDetailsFrameLayout = getView().findViewById(R.id.busDetailsFrameLayout);
                        busDetailsFrameLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

}
