package com.blackspider.bloodforlife.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.fragments.LoginFragment;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.receiver.ConnectivityReceiver;
import com.blackspider.bloodforlife.utils.CustomAnimation;
import com.blackspider.bloodforlife.utils.MyLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class WelcomeActivity extends AppCompatActivity  {
    private TextView textView1, textView2;
    private ImageView appIcon;
    private static FragmentManager fragmentManager;

    private CustomAnimation customAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.shared_element_transation));
        }

        customAnimation = new CustomAnimation(this);

        appIcon = findViewById(R.id.img_app_logo);
        textView1 = findViewById(R.id.textViewWelcomeNote1);
        textView2 = findViewById(R.id.textViewWelcomeNote2);
        customAnimation.crossFade(textView1, textView2);

        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(),
                            AppConstants.LoginFragment).commit();
        }

        // On close icon click finish activity
        appIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        customAnimation.crossFade(textView1, textView2);
                    }
                });

        if (getIntent().getBooleanExtra("EXIT", false)) {
            Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // Replace Login Fragment with animation
    public void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.zoom_in, R.anim.bottom_out)
                .replace(R.id.frameContainer, new LoginFragment(),
                        AppConstants.LoginFragment).commit();
    }

    // Replace Login Fragment with animation
    public void replaceForgotPasswordFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.zoom_in, R.anim.left_out)
                .replace(R.id.frameContainer, new LoginFragment(),
                        AppConstants.LoginFragment).commit();
    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUpFragment = fragmentManager
                .findFragmentByTag(AppConstants.SignUpFragment);
        Fragment ForgotPasswordFragment = fragmentManager
                .findFragmentByTag(AppConstants.ForgotPasswordFragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUpFragment != null)
            replaceLoginFragment();
        else if (ForgotPasswordFragment != null)
            replaceForgotPasswordFragment();
        else {
            Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            //super.onBackPressed();
        }
    }
}
