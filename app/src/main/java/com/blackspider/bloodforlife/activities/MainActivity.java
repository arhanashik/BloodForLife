package com.blackspider.bloodforlife.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.fragments.FindDonorFragment;
import com.blackspider.bloodforlife.fragments.HomeFragment;
import com.blackspider.bloodforlife.fragments.ProfileFragment;
import com.blackspider.bloodforlife.fragments.SettingsFragment;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.models.Location;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.utils.CustomAnimation;
import com.blackspider.bloodforlife.utils.MyLocation;
import com.blackspider.bloodforlife.views.CustomToast;
import com.blackspider.bloodforlife.views.DancingScriptRegularFontTextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private NavigationView navigationView;
    private Menu mMenu;

    public static Donor donor;

    public static FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;
    private static AlertDialog.Builder alertDialogBuilder;
    private static AlertDialog alertDialog;
    private View promptsView, dialogView;
    private DancingScriptRegularFontTextView tvLogoutName, tvLogoutTitle, tvConfirmLogout, tvCancelLogout;

    private DBHelper dbHelper;
    private CustomAnimation mCustomAnimation;
    public static MyLocation myLocation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    // # milliseconds, desired time passed between two back presses.
    private static final int TIME_INTERVAL = 2500;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        myLocation = new MyLocation(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        donor = (Donor) getIntent().getSerializableExtra("DONOR");
        fragmentManager = getSupportFragmentManager();
        mCustomAnimation = new CustomAnimation(this);
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO, Context.MODE_PRIVATE);

        addDummyData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        double lat = myLocation.getLat();
        double lng = myLocation.getLng();
        String city = myLocation.getCity();
        if(lat==0 || lng == 0) Toast.makeText(this, "Location not updated!", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "Location updated!", Toast.LENGTH_SHORT).show();
            dbHelper.updateLocation(AppConstants.TABLE_DONORS, donor.getId(), new Location(lat, lng, city, ""));
            donor.setLat(String.valueOf(lat));
            donor.setLng(String.valueOf(lng));
            donor.setCity(city);
        }
        replaceFragment(new HomeFragment(),
                AppConstants.HomeFragment);
    }

    @Override
    protected void onStop() {
        if(myLocation.getGoogleApiClient() != null) myLocation.disConnectGoogleApiClient();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                //super.onBackPressed();
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                return;
            }
            else {
                Snackbar.make(navigationView, "Click again to exit", Snackbar.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mMenu.getItem(0).setVisible(false);
            replaceFragment(new HomeFragment(),
                    AppConstants.HomeFragment);

        } else if (id == R.id.nav_profile) {
            mMenu.getItem(0).setVisible(false);
            replaceFragment(new ProfileFragment(),
                    AppConstants.ProfileFragment);

        } else if (id == R.id.nav_fnf) {
            mMenu.getItem(0).setVisible(false);

        } else if (id == R.id.nav_find_donor) {
            mMenu.getItem(0).setVisible(true);
            replaceFragment(new FindDonorFragment(),
                    AppConstants.FindDonorFragment);

        } else if (id == R.id.nav_be_donor) {
            mMenu.getItem(0).setVisible(false);

        } else if (id == R.id.nav_about_us) {
            mMenu.getItem(0).setVisible(false);

        } else if (id == R.id.nav_tnc) {
            mMenu.getItem(0).setVisible(false);

        } else if (id == R.id.nav_share) {
            mMenu.getItem(0).setVisible(false);

        } else if (id == R.id.nav_settings) {
            mMenu.getItem(0).setVisible(false);
            replaceFragment(new SettingsFragment(),
                    AppConstants.SettingsFragment);

        } else if (id == R.id.nav_logout) {
            mMenu.getItem(0).setVisible(false);
            logOutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Replace Login Fragment with animation
    public void replaceFragment(Fragment fragment, String tag) {
        fragmentManager
                .beginTransaction()
                //.setCustomAnimations(animIn, animOut)
                .replace(R.id.container, fragment,
                        tag).commit();
    }

    public void logOutDialog(){
        // get layout_developer.xml view
        layoutInflater = LayoutInflater.from(this);
        promptsView = layoutInflater.inflate(R.layout.custom_logout_dialog, null);

        tvLogoutName = promptsView.findViewById(R.id.tv_logout_name);
        tvLogoutTitle = promptsView.findViewById(R.id.tv_logout_title);
        tvConfirmLogout = promptsView.findViewById(R.id.tv_confirm_logout);
        tvCancelLogout = promptsView.findViewById(R.id.tv_cancel_logout);

        dialogView = promptsView.findViewById(R.id.dialog);
        tvLogoutName.setText(donor.getName());

        tvConfirmLogout.setOnClickListener(this);
        tvCancelLogout.setOnClickListener(this);
//
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle);

        // set layout_image_preview.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mCustomAnimation.revealShow(dialogView, true, alertDialog);
            }
        });
        // show it
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v==tvConfirmLogout){
            mCustomAnimation.revealShow(dialogView, false, alertDialog);
            sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.remove(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO);
            sharedPreferencesEditor.remove(AppConstants.IS_SESSION_EXIST);
            sharedPreferencesEditor.remove(AppConstants.KEY_EMAIL);
            sharedPreferencesEditor.remove(AppConstants.KEY_PASSWORD);
            sharedPreferencesEditor.commit();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();

        }else if(v==tvCancelLogout){
            mCustomAnimation.revealShow(dialogView, false, alertDialog);
            new CustomToast().Show_Toast(this, navigationView,
                    "Good decision :)");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        mMenu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_map:
                Intent intent  = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("DONOR", donor);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDummyData(){
        Donor donor = new Donor("Arhan", "arhan@gmail.com", "123456", "A+", "Jessore", "23.259756", "89.191923", "10 Jan, 2018");
        donor.setPhone("01764515461");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Ashik", "ashik@gmail.com", "123456", "A+", "Khulna", "22.839562", "89.533254", "12 Mar, 2017");
        donor.setPhone("01764515461");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Momin", "momin@gmail.com", "123456", "B+", "Mouchak, Dhaka", "23.748209", "90.420257", "15 Dec, 2016");
        donor.setPhone("01779488622");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("AL-Amin", "alamin@gmail.com", "123456", "AB+", "Santibag, Dhaka", "23.745973", "90.418915", "12 Jan, 2018");
        donor.setPhone("01782213727");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Rujel", "rujel@gmail.com", "123456", "O+", "Kolkata", "22.591939", "88.367948", "20 Feb, 2018");
        donor.setPhone("01758951289");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Shawki", "shawki@gmail.com", "123456", "O-", "Khulna", "22.839923", "89.533234", "25 Jan, 2018");
        donor.setPhone("01839308129");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Shadath", "shadath@gmail.com", "123456", "O-", "Firmgate, Dhaka", "23.756002", "90.386969", "28 Dec, 2017");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Ador", "ador@gmail.com", "123456", "B-", "Mohammadpur, Dhaka", "23.765796", "90.358483", "11 Jan, 2018");
        donor.setPhone("01798266860");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Riad", "riad@gmail.com", "123456", "AB-", "Chittagong", "22.378765", "91.817655", "5 Feb, 2018");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);
        donor = new Donor("Sanzid", "sanzid@gmail.com", "123456", "A+", "Gazipur, Dhaka", "24.000157", "90.420499", "1 Jan, 2018");
        dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor);

    }
}
