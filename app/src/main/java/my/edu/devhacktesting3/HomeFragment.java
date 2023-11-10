package my.edu.devhacktesting3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.devhacktesting3.API.Weather;
import my.edu.devhacktesting3.BusStop.BusStopFragment;
import my.edu.devhacktesting3.Profile.ProfileFragment;
import my.edu.devhacktesting3.Voucher.VoucherFragment;
import my.edu.devhacktesting3.databinding.ActivityMainBinding;

public class HomeFragment extends Fragment implements Weather.WeatherCallback{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private double userLatitude , userLongitude;
    private CardView busStopCardView, profileCardView, nearbyCardView, voucherCardView;
    private BottomNavigationView bottomNavigationView;
    private Handler locationUpdateHandler;
    private TextView homeTitle;

    private TextView weatherMainTextView;
    private TextView weatherDescTextView;
    private ImageView weatherImageView;
    private String uid, type;

    // Set the custom firebase URL
    String customDatabaseUrl = "https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);

    public HomeFragment() {
        // Required empty public constructor
    }

    public void setUid(String uid, String type) {
        this.uid = uid;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//-------------------------------------------------------------------------------
        //WEATHER API
        // Initialize your UI elements after inflating the layout
        weatherMainTextView = view.findViewById(R.id.weatherMain);
        weatherDescTextView = view.findViewById(R.id.weatherDesc);
        weatherImageView = view.findViewById(R.id.weatherImage);
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Kampar&appid=d6cafd3faa892adf54b3bee23f64a95a";
        getWeatherData(apiUrl);

//------------------------------------------------------------------------------------------
        //NAVIGATION
        //reference to the bottom nav bar
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation);

        //navigation for each button
        busStopCardView = view.findViewById(R.id.busStopCardView);
        setupCardViewClick(busStopCardView, new BusStopFragment(), R.id.busStop);

        profileCardView = view.findViewById(R.id.profileCardView);
        setupCardViewClick(profileCardView, new ProfileFragment(), R.id.profile);

        nearbyCardView = view.findViewById(R.id.nearbyCardView);
        setupCardViewClick(nearbyCardView, new NearbyFragment(), R.id.nearby);

        voucherCardView = view.findViewById(R.id.voucherCardView);
        setupCardViewClick(voucherCardView, new VoucherFragment(), R.id.voucher);


//-----------------------------------------------------------------------------------------------
        //LOCATION
        //handle location update every 5 seconds
        locationUpdateHandler = new Handler();
        // Start the periodic updates
        locationUpdateHandler.post(locationUpdateRunnable);
        homeTitle = view.findViewById(R.id.homeTitle);
        // Check if location services are enabled
        if (!isLocationEnabled(requireContext())) {
            // Location services are not enabled; show a message and provide an option to enable them
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Location services are not enabled. Enable them to use this feature.");
            builder.setPositiveButton("Enable Location Services", (dialog, which) -> {
                openLocationSetting();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Handle the case where the user doesn't enable location services
                Toast.makeText(requireContext(), "Location services are required for this feature.", Toast.LENGTH_SHORT).show();
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            // Location services are enabled; proceed with location permission check
            checkLocationPermission();
        }

        return view;
    }

//----------------------------------------------------------------------------

    // Check location permission
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestLocationPermission();
        } else {
            // Permission granted, save user location
            saveUserLocation();
        }
    }

    //Check whether the location service is open or not
    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    //open the location setting
    private void openLocationSetting(){
        // Open device settings for location services
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
//----------------------------------------------------------------------------
    //LOCATION PERMISSION
    //request for location permission
    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Handle user choice for granting location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save user location
                saveUserLocation();
            } else {
                // Permission denied, show a message
                Toast.makeText(requireContext(), "Location permission is required for this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //save user location to db if permission granted
    private void saveUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationClient.getLastLocation();

        locationTask.addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //get user location
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();
                    UserLocation userLocation = new UserLocation(userLatitude, userLongitude);

                    //if user is user
                    if(type.equalsIgnoreCase("USER")){
                        DatabaseReference locationRef = firebaseDatabase.getReference("/UserLocation/" + uid);
                        locationRef.setValue(userLocation);
                    }
                    //if user is driver
                    else {
                        DatabaseReference busLocationRef = firebaseDatabase.getReference("/Bus/" + uid);
                        busLocationRef.child("longitude").setValue(userLongitude);
                        busLocationRef.child("latitude").setValue(userLatitude);
                    }
                }
                //if user location cannot be get
                else {
                    //default location: TTDI MRT
                    userLatitude = 3.1358;
                    userLongitude = 101.6314;
                }
            }
        });
    }

    //navigate to other fragment
    private void setupCardViewClick(CardView cardView, Fragment fragment, int selectedItemId) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();
                bottomNavigationView.setSelectedItemId(selectedItemId);
            }
        });
    }


    //update location to firebase every 5 seconds
    private Runnable locationUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            // Update the user's location here
            saveUserLocation(); // This saves the location to Firebase
            locationUpdateHandler.postDelayed(this, 5000); // Schedule the next update after 2 seconds
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Remove any pending callbacks to prevent memory leaks
        locationUpdateHandler.removeCallbacksAndMessages(null);
    }

//-------------------------------------------------------------------------------------
    //WEATHER API
    private void getWeatherData(String apiUrl) {
        Weather weather = new Weather(this);
        weather.getWeatherData(apiUrl);
    }

    @Override
    public void onWeatherDataReceived(@NonNull String weatherMain, String weatherDesc) {
        if (isAdded()) {

            String mainMessage;
            String descMessage;
            int imageResource;
            String imageurl;

            switch (weatherMain) {
                case "Thunderstorm":
                    mainMessage = "OH NO! It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", Please be prepared to get wet!!";
                    imageurl = "https://cdn.dribbble.com/users/2120934/screenshots/6193517/17_tstorm.gif";
                    break;
                case "Rain":
                    mainMessage = "NOOOO! It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", Remember to bring umbrella";
                    imageurl = "https://cdn.dribbble.com/users/2120934/screenshots/6193512/11_rain.gif";
                    break;
                case "Snow":
                    mainMessage = "Hurray! It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", FREEZEEEEE!!";
                    imageurl = "https://cdn.dribbble.com/users/2120934/screenshots/6193458/13_snow.gif";
                    break;
                case "Clear":
                    mainMessage = "WOAH! It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", What a great day for a picnic!";
                    imageurl = "https://i.gifer.com/1d.gif";
                    break;
                case "Clouds":
                    mainMessage = "Yeah! It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", Ohh, it's picnic time!!";
                    imageurl = "https://cdn-icons-gif.flaticon.com/6455/6455053.gif";
                    break;
                case "Fog":
                    mainMessage = "Hmmm... It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", You can't see me";
                    imageurl = "https://lordicon.com/icons/wired/outline/814-fog.gif";
                    break;
                case "Wind":
                    mainMessage = "Wheeee! It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", Windy day ahead!";
                    imageurl = "https://media.tenor.com/4uLdw5XYq1kAAAAi/animal-puppy.gif";
                    break;
                default:
                    mainMessage = "Hmm... It's " + weatherMain + " today";
                    descMessage = "It's " + weatherDesc + ", Weather can change anytime, be prepared!!";
                    imageurl = "https://cdn-icons-gif.flaticon.com/6455/6455053.gif";
                    break;
            }

            weatherMainTextView.setText(mainMessage);
            weatherDescTextView.setText(descMessage);


            // Use Glide to load and display the GIF
            Glide.with(this)
                    .asGif()
                    .load(imageurl)
                    .into(weatherImageView);
        }
    }


    @Override
    public void onWeatherDataFailed() {
        // Handle the case where the weather data request failed
        Toast.makeText(getContext(), "Weather data request failed", Toast.LENGTH_SHORT).show();
    }


}