package my.edu.devhacktesting3;

public class UserLocation {
    private double longitude;
    private double latitude;

    public UserLocation(){

    }

    public UserLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
