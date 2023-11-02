package my.edu.devhacktesting3.BusStop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.edu.devhacktesting3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusStopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusStopFragment extends Fragment implements ScheduleAdapter.onGoClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText searchBar;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;

    public BusStopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusStopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusStopFragment newInstance(String param1, String param2) {
        BusStopFragment fragment = new BusStopFragment();
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
        View view = inflater.inflate(R.layout.fragment_bus_stop, container, false);

        searchBar=view.findViewById(R.id.searchBar);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        scheduleAdapter = new ScheduleAdapter(scheduleItemsList, this);
        recyclerView.setAdapter(scheduleAdapter);

        //add item into the item list
        for(int i=0; i<3; i++){
            ScheduleItem scheduleItem = new ScheduleItem
                    ("Cochrane", "Kajang","KL", "6" );
            scheduleItemsList.add(scheduleItem);
        }

        // Notify the adapter that the data has changed
        scheduleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGoClick(ScheduleItem scheduleItem) {

    }
}