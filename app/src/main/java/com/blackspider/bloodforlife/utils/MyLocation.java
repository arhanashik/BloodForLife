package com.blackspider.bloodforlife.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.receiver.ConnectivityReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

/**
 * Created by Arhan Ashik on 2/19/2018.
 */

public class MyLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Activity mActivity;
    private static String city, currLocation;
    private static double lat, lng;
    private GoogleApiClient googleApiClient;
    private Geocoder geocoder;
    private List<Address> addresses;
    private boolean isConnected = false;

    private GPSTracker gps;

    public MyLocation(Activity activity) {
        this.mActivity = activity;
    }

    public void initLocationService(){
        isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            googleApiClient = new GoogleApiClient.Builder(mActivity, this,
                    this).addApi(LocationServices.API).build();
            if (googleApiClient != null) {
                googleApiClient.connect();
            }
        } else getOfflineLocation();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        MyLocation.city = city;
    }

    public String getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        MyLocation.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        MyLocation.lng = lng;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void disConnectGoogleApiClient() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");
        Toast.makeText(mActivity, "Connected to Google Play Services!", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            lat = lastLocation.getLatitude();
            lng = lastLocation.getLongitude();

            currLocation = lastLocation.getProvider();
            currLocation = getCompleteAddressString(lat, lng);
            Log.d("online lat", lat + "");
            Log.d("online lng", lng + "");
            Log.d("online loc", currLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
        Toast.makeText(mActivity, "Can't connect to Google Play Services!", Toast.LENGTH_SHORT).show();
    }

    private void getOfflineLocation() {
        // create class object
        gps = new GPSTracker(mActivity);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lng = gps.getLongitude();
            String addr = getCompleteAddressString(lat, lng);
            Log.d("offline lat: ", lat + "");
            Log.d("offline lon", lng + "");
            Log.d("offline", addr);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + LATITUDE + ","+ LONGITUDE + "&sensor=true";

        String strAdd = "";
        geocoder = new Geocoder(mActivity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                //strAdd = strReturnedAddress.toString();

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                //String city = addresses.get(0).getLocality();
                String dist = addresses.get(0).getSubAdminArea();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                city = dist.toUpperCase();
                if(city.contains("DISTRICT")) city = city.replace("DISTRICT", "").trim();

                strAdd += address + "\n" + dist + "\n" + state + "\n" + country + "\n" + postalCode + "\n" + knownName;

                Log.w("Current location: ", "" + strReturnedAddress.toString());
                //textView.setText("Current location: " + strAdd);
            } else {
                Log.w("Current location: ", "" + "No Address returned!");
                strAdd += "No Address returned!";
                //textView.setText("No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current location: ", "" + "Cannot get Address!");
            strAdd += "Canont get Address!";
            //textView.setText("Canont get Address!");
        }
        return strAdd;
    }

}
