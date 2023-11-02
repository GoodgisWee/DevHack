package my.edu.devhacktesting3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import my.edu.devhacktesting3.BusStop.BusStopFragment;
import my.edu.devhacktesting3.Voucher.VoucherFragment;
import my.edu.devhacktesting3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new NearbyFragment());
        binding.bottomNavigation.setOnItemSelectedListener(item ->
        {

            switch (item.getItemId()){
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
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}