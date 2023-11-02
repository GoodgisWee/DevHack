package my.edu.devhacktesting3.Voucher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import my.edu.devhacktesting3.Voucher.VoucherAdapter;
import my.edu.devhacktesting3.Voucher.VoucherItem;

import my.edu.devhacktesting3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherFragment extends Fragment implements VoucherAdapter.onClaimClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private VoucherAdapter  voucherAdapter;

    public VoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoucherFragment newInstance(String param1, String param2) {
        VoucherFragment fragment = new VoucherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);

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

        //create adapter to display the voucher list
        voucherAdapter = new VoucherAdapter(voucherItemList, this);

        //set adapter to recycler view
        recyclerView.setAdapter(voucherAdapter);

        //add all voucher item into the voucher list
        for(int i=0; i<2; i++){
            VoucherItem voucherItem = new VoucherItem
                    ("14/11/2024", "Grab Unlimited RM 10 Voucher","foodpanda" );
            voucherItemList.add(voucherItem);
        }

        VoucherItem voucherItem = new VoucherItem
                ("14/11/2023", "Mytsery Gift Worth up to RM 100","gift" );
        voucherItemList.add(voucherItem);

        //notify adapter that data has changed
        voucherAdapter.notifyDataSetChanged();
    }

    public void onClaimClick(VoucherItem VoucherItem){

    }
}