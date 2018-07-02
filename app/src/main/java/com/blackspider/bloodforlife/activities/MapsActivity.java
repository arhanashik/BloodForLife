package com.blackspider.bloodforlife.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.others.AppConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Donor> userList;
    private DBHelper mDbHelper;
    private Donor donor;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDbHelper = new DBHelper(this);
        userList = new ArrayList<>();
        donor = (Donor) getIntent().getSerializableExtra("DONOR");

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            Toast.makeText(this, "hey, its not for u, ok?", Toast.LENGTH_SHORT).show();
        } else {

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            prepareData();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //When Map Loads Successfully
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                for (Donor user : userList) {
                    if (user.getEmail().equals(MainActivity.donor.getEmail())) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(user.getLat()), Double.parseDouble(user.getLng())))
                                .title("Me")
                                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(R.drawable.no_img)))
                        );
                    } else {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(user.getLat()), Double.parseDouble(user.getLng())))
                                .title(user.getName())
                                .snippet(user.getBloodGroup() + ", Last donation:" + user.getLastDonationDate())
                                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(R.drawable.no_img)))
                        );
                    }
                    marker.showInfoWindow();
                }

                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(Double.parseDouble(donor.getLat()), Double.parseDouble(donor.getLng())));
                LatLngBounds bounds = builder.build();

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mMap.moveCamera(cu);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(7.0f), 2000, null);

                //mMap.setCompassEnabled(true);
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.setMinZoomPreference(2.0f);
                mMap.setMaxZoomPreference(20.0f);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String tt=marker.getTitle();
                        if(!tt.equalsIgnoreCase("me")) tt+= "\n" + marker.getSnippet();
                        Toast.makeText(MapsActivity.this, tt, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });
    }

    /**
     * load data to list
     */
    private void prepareData() {
        //set up list
        userList = mDbHelper.getDonors(AppConstants.TABLE_DONORS);

        //sort the list, give the Comparator the current location
        Collections.sort(userList, new SortPlaces(new LatLng(Double.parseDouble(donor.getLat()), Double.parseDouble(donor.getLng()))));
    }

    /**
     * sorting algorithm
     */
    public class SortPlaces implements Comparator<Donor> {
        LatLng currentLoc;

        public SortPlaces(LatLng current) {
            currentLoc = current;
        }

        @Override
        public int compare(final Donor place1, final Donor place2) {

            double lat1 = Double.parseDouble(place1.getLat());
            double lon1 = Double.parseDouble(place1.getLng());
            double lat2 = Double.parseDouble(place2.getLat());
            double lon2 = Double.parseDouble(place2.getLng());

            double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
            double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
            return (int) (distanceToPlace1 - distanceToPlace2);
        }

        public double distance(double fromLat, double fromLon, double toLat, double toLon) {
            double radius = 6378137;   // approximate Earth radius, *in meters*
            double deltaLat = toLat - fromLat;
            double deltaLon = toLon - fromLon;
            double angle = 2 * Math.asin(Math.sqrt(
                    Math.pow(Math.sin(deltaLat / 2), 2) +
                            Math.cos(fromLat) * Math.cos(toLat) *
                                    Math.pow(Math.sin(deltaLon / 2), 2)));

            double valueResult = radius * angle;

            //calculate distance
            double km = valueResult / 1000;
            DecimalFormat newFormat = new DecimalFormat("####");
            int kmInDec = Integer.valueOf(newFormat.format(km));
            double meter = valueResult;
            int meterInDec = Integer.valueOf(newFormat.format(meter));
            Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

            return valueResult;
        }
    }

    /**
     * Custom marker icon
     *
     * @param resource
     * @return
     */

    public Bitmap createCustomMarker(@DrawableRes int resource) {

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
}
