package my.edu.devhacktesting3.MyVoucher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.edu.devhacktesting3.R;
import my.edu.devhacktesting3.MyVoucher.MyVoucherAdapter;
import my.edu.devhacktesting3.MyVoucher.MyVoucherItem;
import my.edu.devhacktesting3.Voucher.VoucherAdapter;
import my.edu.devhacktesting3.Voucher.VoucherFragment;


public class MyVoucherFragment extends Fragment implements MyVoucherAdapter.onUseClickListener{

    private RecyclerView recyclerView;
    private MyVoucherAdapter  myVoucherAdapter;
    String customDatabaseUrl = "https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);
    private Button claimNewVoucherBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_voucher, container, false);

        //navigate to user voucher page
        claimNewVoucherBtn = view.findViewById(R.id.claimNewVoucherBtn);
        claimNewVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment voucherFragment = new VoucherFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, voucherFragment).commit();
            }
        });
        
        showVoucher(view);

        return view;
    }

    private void showVoucher(View view) {
        //setup recycler view
        recyclerView = view.findViewById(R.id.recyclerViewVoucher);

        //set layout for recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //create voucher list
        List<MyVoucherItem> myVoucherItemList = new ArrayList<>();

        //create adapter to display the voucher list
        myVoucherAdapter = new MyVoucherAdapter(myVoucherItemList, this);

        //set adapter to recycler view
        recyclerView.setAdapter(myVoucherAdapter);

//---------------------------------------------------------------------------------
        //get all voucher from the firebase and display
        String uid = "ryan123";
        DatabaseReference voucherRef = firebaseDatabase.getReference("/Voucher/User");
        voucherRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the data snapshot has a value
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        // Map the data to a myVoucherItem
                        MyVoucherItem myVoucherItem = snapshot.getValue(MyVoucherItem.class);

                        if (myVoucherItem.getVoucherName() != null) {
                            // Add the voucher item to the list
                            myVoucherItemList.add(myVoucherItem);
                            myVoucherAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onUseClick(MyVoucherItem myVoucherItem){
        // Implement the voucher claiming logic here
        // You can access the selected voucher item (myVoucherItem) and perform the necessary actions.
        Toast.makeText(getActivity(), "Voucher Claimed: " + myVoucherItem.getVoucherName(), Toast.LENGTH_SHORT).show();
    }
}