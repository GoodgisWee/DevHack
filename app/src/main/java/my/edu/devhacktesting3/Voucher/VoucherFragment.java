package my.edu.devhacktesting3.Voucher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import my.edu.devhacktesting3.MyVoucher.MyVoucherAdapter;
import my.edu.devhacktesting3.MyVoucher.MyVoucherFragment;
import my.edu.devhacktesting3.MyVoucher.MyVoucherItem;
import my.edu.devhacktesting3.UserLocation;
import my.edu.devhacktesting3.Voucher.VoucherAdapter;
import my.edu.devhacktesting3.Voucher.VoucherItem;

import my.edu.devhacktesting3.R;

public class VoucherFragment extends Fragment implements VoucherAdapter.onClaimClickListener {

    private Button claimBtn;
    private RecyclerView recyclerView;
    private VoucherAdapter  voucherAdapter;
    private MyVoucherAdapter  myVoucherAdapter;
    private List<MyVoucherItem> myVoucherItemList;
    String customDatabaseUrl = "https://busiorempit-default-rtdb.asia-southeast1.firebasedatabase.app";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(customDatabaseUrl);
    private Button myVoucherBtn;

    private String uid, type;

    public void setUid(String uid, String type) {
        this.type = type;
        this.uid = uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);

        //navigate to user voucher page
        myVoucherBtn = view.findViewById(R.id.myVoucherBtn);
        myVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment myVoucherFragment = new MyVoucherFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, myVoucherFragment).commit();
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
        List<VoucherItem> voucherItemList = new ArrayList<>();
        myVoucherItemList = new ArrayList<>();

        //create adapter to display the voucher list
        voucherAdapter = new VoucherAdapter(voucherItemList, this);
        myVoucherAdapter = new MyVoucherAdapter(myVoucherItemList, new MyVoucherAdapter.onUseClickListener(){
            @Override
            public void onUseClick(MyVoucherItem MyVoucherItem) {

            }
        });

        //set adapter to recycler view
        recyclerView.setAdapter(voucherAdapter);

//-------------------------------------------------------------------------------------
        //get all voucher from the firebase and display
        DatabaseReference voucherRef = firebaseDatabase.getReference("/Voucher/System/");
        final boolean[] voucherExist = {false};
        DatabaseReference userRef = firebaseDatabase.getReference("/Voucher/User/"+uid);


        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    voucherItemList.clear(); // Clear the voucher list

                    // Iterate through the /Voucher/User/uid and check whether user alrdy have the voucher
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        VoucherItem voucherItem = snapshot.getValue(VoucherItem.class);
                        String id = voucherItem.getId();
                        voucherExist[0] = false;

                        if (voucherItem.getAmount() != 0) {
                            //if user didnt have, add voucher into list
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Iterate through the user's vouchers
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            MyVoucherItem myVoucherItem = snapshot.getValue(MyVoucherItem.class);
                                            //if voucher exist, skip
                                            if (myVoucherItem.getId().equals(id)) {
                                                voucherExist[0] = true;
                                                break;
                                            }
                                        }
                                    }
                                    //add voucher to display
                                    if(voucherExist[0] == false){
                                        voucherItemList.add(voucherItem);
                                    }
                                    // Notify the adapter that the data has changed
                                    voucherAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //voucher claiming logic here
    @Override
    public void onClaimClick(VoucherItem voucherItem){
        final int[] userVoucherCount = {0};
        DatabaseReference addVoucherRef = firebaseDatabase.getReference("/Voucher/User/"+uid);
        DatabaseReference voucherRef = firebaseDatabase.getReference("/Voucher/System");
        DatabaseReference userRef = firebaseDatabase.getReference("/User/"+uid);

        //get voucher count from user data
        userRef.child("voucherCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userVoucherCount[0] =snapshot.getValue(Integer.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        //get user voucher and store inside a list
        addVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the data snapshot has a value
                if (dataSnapshot.exists()) {
                    myVoucherItemList.clear();

                    // Iterate through the dataSnapshot and add items to the list
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        // Map the data to a VoucherItem
                        MyVoucherItem myVoucherItem = snapshot.getValue(MyVoucherItem.class);

                        if (myVoucherItem.getAmount() != 0) {
                            // Add the voucher item to the list
                            myVoucherItemList.add(myVoucherItem);
                            myVoucherAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Decrement the amount by one in the voucher  xxx
        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        VoucherItem voucherItem1 = snapshot.getValue(VoucherItem.class);

                            //reduce amount of the voucher in firebase
                            int newAmount = voucherItem1.getAmount() - 1;
                            snapshot.getRef().child("amount").setValue(newAmount).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    //add the voucher into the /Voucher/User
                                    addVoucherRef.child(uid).child(voucherItem.getId()).setValue(voucherItem);
                                    addVoucherRef.child(uid).child(voucherItem.getId()).child("amount").setValue(1);
                                    userRef.child(uid).child("voucherCount").setValue(userVoucherCount[0]+1);

                                    Toast.makeText(getActivity(), "Voucher Claimed: " + voucherItem.getVoucherName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to update amount for voucher: " + voucherItem.getVoucherName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error if needed
            }
        });
    }
}