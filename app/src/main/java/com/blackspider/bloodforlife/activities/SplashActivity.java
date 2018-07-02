package com.blackspider.bloodforlife.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.utils.MyLocation;
import com.blackspider.bloodforlife.views.CustomToast;
import com.blackspider.bloodforlife.views.DancingScriptRegularFontTextView;
import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Handler mChangeActivityHandler = new Handler();
    private final Runnable mShowLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            loadingGif.setVisibility(View.VISIBLE);
            Glide.with(SplashActivity.this).load(R.drawable.loading_blue).asGif().into(loadingGif);
            checkSessionValidity();
        }
    };
    private final Runnable mGoToWelcomeActivityRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            Pair<View, String> pair1 = Pair.create((View)appIcon, ViewCompat.getTransitionName(appIcon));
            Pair<View, String> pair2 = Pair.create((View) appTitle, ViewCompat.getTransitionName(appTitle));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(SplashActivity.this,
                            pair1,
                            pair2);
            startActivity(intent, options.toBundle());
        }
    };
    private final Runnable mGoToMainActivityRunnable = new Runnable() {
        @Override
        public void run() {
            String email = sharedPreferences.getString(AppConstants.KEY_EMAIL, null);
            String password = sharedPreferences.getString(AppConstants.KEY_PASSWORD, null);
            Intent intent = new Intent();
            if(email != null && password != null){
                Donor donor = dbHelper.getValidDonor(AppConstants.TABLE_DONORS, email, password);
                if(donor != null){
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("DONOR", donor);
                    new CustomToast().Show_Toast(SplashActivity.this, mContentView, "Welcome back " + donor.getName());
                }
                else {
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                }
            }
            else {
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            }
            startActivity(intent);
            finish();
        }
    };

    private ImageView appIcon, loadingGif;
    private DancingScriptRegularFontTextView appTitle, statusTxt;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private DBHelper dbHelper;
    private MyLocation myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dbHelper = new DBHelper(this);
        myLocation = new MyLocation(this);
        checkLocationPermission();

        mContentView = findViewById(R.id.fullscreen_content);
        statusTxt = findViewById(R.id.status);

        appIcon = findViewById(R.id.app_icon);
        appTitle = findViewById(R.id.app_title);
        loadingGif = findViewById(R.id.loading_gif);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=21)
        {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.shared_element_transation));
        }
        //hide the action bar and status bar - to make the activity fullscreen
        hide();

        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO, Context.MODE_PRIVATE);
        //Glide.with(this).load(R.drawable.ic_blood).thumbnail(1.0f).into(appIcon);
        loadingGif.setVisibility(View.GONE);

        if(getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }else {
            mChangeActivityHandler.removeCallbacks(mGoToWelcomeActivityRunnable);
            mChangeActivityHandler.removeCallbacks(mGoToMainActivityRunnable);
            mChangeActivityHandler.postDelayed(mShowLoadingRunnable, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChangeActivityHandler.removeCallbacks(mShowLoadingRunnable);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE},
                    AppConstants.PERMISSION_ACCESS_LOCATION);
        }
        else {
            myLocation.initLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PERMISSION_ACCESS_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    Toast.makeText(this, "Searching location!", Toast.LENGTH_SHORT).show();
                    myLocation.initLocationService();
                } else {
                    Toast.makeText(this, "Need location access permission!", Toast.LENGTH_SHORT).show();
                    checkLocationPermission();
                }

                break;
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
        mHideHandler.postDelayed(mHidePart2Runnable, 0);
    }

    private void checkSessionValidity(){
        mChangeActivityHandler.removeCallbacks(mShowLoadingRunnable);
        if(sharedPreferences.getBoolean(AppConstants.IS_SESSION_EXIST, false)){
            mChangeActivityHandler.postDelayed(mGoToMainActivityRunnable, 2000);
        }else {
            mChangeActivityHandler.postDelayed(mGoToWelcomeActivityRunnable, 2000);
        }
    }
}
