package my.edu.devhacktesting3.BusStop;

import android.widget.Button;
import android.widget.TextView;

public class ScheduleItem {

    private String busStop, busStart, busEnd, carplate;
    private double distance;

    public ScheduleItem(String busStop, String busStart, String busEnd, double distance, String carplate) {
        this.busStop = busStop;
        this.busStart = busStart;
        this.busEnd = busEnd;
        this.distance = distance;
        this.carplate = carplate;
    }

    public String getBusStop() {
        return busStop;
    }

    public void setBusStop(String busStop) {
        this.busStop = busStop;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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

    public String getCarplate() {
        return carplate;
    }

    public void setCarplate(String carplate) {
        this.carplate = carplate;
    }
}
