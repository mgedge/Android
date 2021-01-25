package edu.csi.niu.z1818828.weatherviewer;

public class Coordinate {
    public String lat;
    public String lng;

    public Coordinate() {
        this.lat = null;
        this.lng = null;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = String.valueOf(lat);
    }

    public void setLng(double lng) {
        this.lng = String.valueOf(lng);
    }
}
