package my.edu.devhacktesting3.BusStop;

import android.widget.Button;
import android.widget.TextView;

public class ScheduleItem {

    private String busStop, busStart, busEnd, distance;

    public ScheduleItem(String busStop, String busStart, String busEnd, String distance) {
        this.busStop = busStop;
        this.busStart = busStart;
        this.busEnd = busEnd;
        this.distance = distance;
    }

    public String getBusStop() {
        return busStop;
    }

    public void setBusStop(String busStop) {
        this.busStop = busStop;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBusStart() {
        return busStart;
    }

    public void setBusStart(String busStart) {
        this.busStart = busStart;
    }

    public String getBusEnd() {
        return busEnd;
    }

    public void setBusEnd(String busEnd) {
        this.busEnd = busEnd;
    }
}
