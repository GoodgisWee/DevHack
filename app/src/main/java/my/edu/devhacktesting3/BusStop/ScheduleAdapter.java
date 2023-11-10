package my.edu.devhacktesting3.BusStop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.devhacktesting3.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ScheduleItem> scheduleItems;
    private onGoClickListener goClickListener;
    private onTrackBusClickListener trackBusClickListener;

    //constructor
    public ScheduleAdapter(List<ScheduleItem> scheduleItems, onGoClickListener goClickListener,
                           onTrackBusClickListener trackBusClickListener) {
        this.scheduleItems = scheduleItems;
        this.goClickListener = goClickListener;
        this.trackBusClickListener = trackBusClickListener;
    }

    //when recyclerview need create new viewholder for item in list
    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_stop_schedule_item, parent, false);
        return new ViewHolder(view);
    }

    //bind data to the view
    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder viewHolder, int position) {
        ScheduleItem scheduleItem = scheduleItems.get(position);

        viewHolder.busStop.setText(scheduleItem.getBusStop() + " Stop");
        viewHolder.busLine.setText(scheduleItem.getBusStart() + " --> " + scheduleItem.getBusEnd());
        viewHolder.distance.setText("Distance from you: "+ scheduleItem.getDistance()+ " KM");

        viewHolder.goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goClickListener != null){
                    goClickListener.onGoClick(scheduleItem);
                }
            }
        });

        viewHolder.trackBusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trackBusClickListener != null){
                    trackBusClickListener.onTrackBusClick(scheduleItem);
                }
            }
        });
    }

    //retrun totals number of item
    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    //represent a single item view within the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView busStop;
        TextView busLine;
        TextView distance;
        Button goBtn;
        Button trackBusBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busStop=itemView.findViewById(R.id.busStop);
            busLine=itemView.findViewById(R.id.busLine);
            distance=itemView.findViewById(R.id.distance);
            goBtn=itemView.findViewById(R.id.goBtn);
            trackBusBtn=itemView.findViewById(R.id.trackBusBtn);
        }
    }

    public interface onGoClickListener {
        void onGoClick(ScheduleItem scheduleItem);
    }

    public interface onTrackBusClickListener {
        void onTrackBusClick(ScheduleItem scheduleItem);
    }

}
