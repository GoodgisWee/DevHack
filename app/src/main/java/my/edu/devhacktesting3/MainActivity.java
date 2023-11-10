package my.edu.devhacktesting3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import my.edu.devhacktesting3.BusStop.BusStopFragment;
import my.edu.devhacktesting3.BusStop.ScheduleItem;
import my.edu.devhacktesting3.Profile.ProfileFragment;
import my.edu.devhacktesting3.Voucher.VoucherFragment;
import my.edu.devhacktesting3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    ActivityMainBinding binding;

    //initialize firebase
    String customDatabaseUrl = "https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);

    String uid, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get current user uid
        Intent intent = getIntent();
        if (intent.hasExtra("uid")) {
            // Retrieve the data from the Intent
            uid = intent.getStringExtra("uid");
            type = intent.getStringExtra("type");
        }

        if (firebaseDatabase == null) {
            firebaseDatabase.setPersistenceEnabled(true);
        }


//----------------------------------------------------------------------------
        //FRAGMENT BINDING
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setSelectedItemId(R.id.home);
        replaceFragment(new HomeFragment());
        binding.bottomNavigation.setOnItemSelectedListener(item ->
        {

            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.nearby:
                    replaceFragment(new NearbyFragment());
                    break;
                case R.id.busStop:
                    replaceFragment(new BusStopFragment());
                    break;
                case R.id.voucher:
                    replaceFragment(new VoucherFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    //switch to other fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Pass the uid to the fragment if it implements the setUid method
        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).setUid(uid, type);
        } else if (fragment instanceof NearbyFragment) {
            ((NearbyFragment) fragment).setUid(uid, type);
        } else if (fragment instanceof BusStopFragment) {
            ((BusStopFragment) fragment).setUid(uid, type);
        } else if (fragment instanceof VoucherFragment) {
            ((VoucherFragment) fragment).setUid(uid, type);
        } else if (fragment instanceof ProfileFragment) {
            ((ProfileFragment) fragment).setUid(uid, type);
        }

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}