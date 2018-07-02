package com.blackspider.bloodforlife.models;

import java.io.Serializable;

/**
 * Created by Arhan Ashik on 2/20/2018.
 */

public class Location implements Serializable {
    private double lat;
    private double lng;
    private String city;
    private String fullAddress;

    public Location() {
    }

    public Location(double lat, double lng, String city, String fullAddress) {
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.fullAddress = fullAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
