package edu.csi.niu.z1818828.weatherviewer;

/**
 * This class holds the coordinates retrieved from the geocodio API
 */
public class Coordinate {
    public String lat;
    public String lng;

    /**
     * Constructor for the Coordinate Object
     */
    public Coordinate() {
        this.lat = null;
        this.lng = null;
    }

    /**
     * @return the latitude of the coordinate object
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat the latitude to set for the object
     */
    public void setLat(double lat) {
        this.lat = String.valueOf(lat);
    }

    /**
     * @return the longitude of the coordinate object
     */
    public String getLng() {
        return lng;
    }

    /**
     * @param lng the longitude to set for the object
     */
    public void setLng(double lng) {
        this.lng = String.valueOf(lng);
    }
}
