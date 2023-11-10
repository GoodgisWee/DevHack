package my.edu.devhacktesting3.BusStop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import my.edu.devhacktesting3.NearbyFragment;
import my.edu.devhacktesting3.R;
import my.edu.devhacktesting3.UserLocation;


public class BusStopFragment extends Fragment implements ScheduleAdapter.onGoClickListener, ScheduleAdapter.onTrackBusClickListener {


    private EditText searchBar;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private UserLocation userLocation;
    private double distance;

    private String carplate;

    //firebase variable
    String firebaseURL ="https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseURL);

    private String uid, type;

    public void setUid(String uid, String type) {
        this.uid = uid;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_stop, container, false);

        searchBar=view.findViewById(R.id.searchBar);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //xxx here need implement search function
                Toast.makeText(getActivity(), "Search bar clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        showSchedule(view);

        return view;
    }

    //recycle view to show schedule
    private void showSchedule(View view){

        // Recycle view setup
        recyclerView = view.findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create a list of data items to display in the RecyclerView
        List<ScheduleItem> scheduleItemsList = new ArrayList<>();

        // Create and set the adapter
        scheduleAdapter = new ScheduleAdapter(scheduleItemsList, this, this);
        recyclerView.setAdapter(scheduleAdapter);

//----------------------------------------------------------------------------------------------
        DatabaseReference userLocationRef = firebaseDatabase.getReference("/UserLocation");
        userLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    double longitude = dataSnapshot.child(uid).child("longitude").getValue(double.class);
                    double latitude = dataSnapshot.child(uid).child("latitude").getValue(double.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), "Error when getting User Location from Database", Toast.LENGTH_SHORT).show();
            }
        });

        //get data from firebase
        DatabaseReference busStopRef = firebaseDatabase.getReference("/BusStops");
        busStopRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Clear the list before adding new data
                    scheduleItemsList.clear();

                    // Iterate through the dataSnapshot and add items to the list
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String busStop = snapshot.child("busStop").getValue(String.class);
                        String busStart = snapshot.child("busStart").getValue(String.class);
                        String busEnd = snapshot.child("busEnd").getValue(String.class);
                        carplate = snapshot.child("carplate").getValue(String.class);
                        Double longitude = snapshot.child("longitude").getValue(Double.class);
                        Double latitude = snapshot.child("latitude").getValue(Double.class);

                        //get the user location from firebase
                        DatabaseReference locationRef = firebaseDatabase.getReference("/UserLocation");
                        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userLocation = dataSnapshot.getValue(UserLocation.class);
                                if(userLocation!=null){
                                    //xxx calculate walking distance using API

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });

                        ScheduleItem scheduleItem = new ScheduleItem(busStop, busStart, busEnd, distance, carplate);
                        scheduleItemsList.add(scheduleItem);
                    }
                    // Notify the adapter that the data has changed
                    scheduleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onGoClick(ScheduleItem scheduleItem) {
        //xxx apply route API in here to get walking distance to it
        Toast.makeText(requireActivity(), "Google Maps Tracking APi launched", Toast.LENGTH_SHORT).show();
    }
    //navigate to the bus location
    @Override
    public void onTrackBusClick(ScheduleItem scheduleItem) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("type", type);
        bundle.putString("carplate", carplate);
        bundle.putString("trackBus", "yes");

        // Create a new NearbyFragment instance and set the arguments
        NearbyFragment nearbyFragment = new NearbyFragment();
        nearbyFragment.setArguments(bundle);

        // Replace the current fragment with NearbyFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Use the correct layout ID for the fragment container in your activity
        fragmentTransaction.replace(R.id.frame_layout, nearbyFragment); // Replace with the correct ID
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}