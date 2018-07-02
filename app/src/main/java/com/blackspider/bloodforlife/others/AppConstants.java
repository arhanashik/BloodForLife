package com.blackspider.bloodforlife.others;

import com.blackspider.bloodforlife.activities.MainActivity;

/**
 * Created by Arhan Ashik on 2/14/2018.
 */

public class AppConstants {
    public static final String GOOGLE_MAP_API_ID = "AIzaSyCRT7AVhoMZ-fBReHVAcq0cIkFFEVrD4A8";

    // LogCat tag
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE = "blood_for_life";

    //Fragments Tags
    public static final String LoginFragment = "LoginFragment";
    public static final String SignUpFragment = "SignUpFragment";
    public static final String ForgotPasswordFragment = "ForgotPasswordFragment";
    public static final String HomeFragment = "HomeFragment";
    public static final String ProfileFragment = "ProfileFragment";
    public static final String FindDonorFragment = "FindDonorFragment";
    public static final String SettingsFragment = "SettingsFragment";

    //permission code
    public static final int GALLERY_ACCESS_PERMISSION_REQUEST_CODE = 11;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 12;
    public static final int GALLERY_IMAGE_REQUEST_CODE = 13;
    public static final int PERMISSION_ACCESS_LOCATION = 14;
    public static final int CALL_PHONE_REQUEST_CODE = 15;

    //Email Validation pattern
    public static final String EmailRegEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    public static final String SHARED_PREFERENCES_SIGN_IN_INFO = BASE + "_sign_in_info";
    public static final String SHARED_PREFERENCES_IMAGE_UPDATE_SIGNATURE = "_image_update_signature";

    public static final String IS_SESSION_EXIST = BASE + "_is_session_exist";

    public static final int FLAG_EMAIL_CHECK = 1;
    public static final int FLAG_PHONE_CHECK = 2;
    public static final int FLAG_SIGNUP = 3;
    public static final int FLAG_UPDATE_IMAGE = 4;
    public static final int FLAG_CHANGE_PASSWORD = 5;
    public static final int FLAG_CHANGE_NAME = 6;
    public static final int FLAG_CHANGE_BLOOD_GROUP = 7;
    public static final int FLAG_CHANGE_LAST_DONATION_DATE = 8;
    public static final int FLAG_CHANGE_CITY = 9;
    public static final int FLAG_CHANGE_PHONE = 10;
    public static final int FLAG_CHANGE_TOTAL_DONATION = 11;
    public static final int FLAG_CHANGE_BIRTH_DATE = 12;
    public static final int FLAG_CHANGE_WEIGHT = 13;
    public static final int FLAG_CHANGE_OCCUPATION = 14;
    public static final int FLAG_CHANGE_INSTITUTE = 15;
    public static final int FLAG_CHANGE_DONOR_STATUS = 16;
    public static final int FLAG_CHANGE_AVAILABLE_HOUR = 17;
    public static final int FLAG_CHANGE_IS_DRUG_ADDICTED = 18;
    public static final int FLAG_CHANGE_DISEASES = 19;

    //variables
    public static final String IMAGE_SIGNATURE = "_image_signature";
    public static final String KEY_IMAGE_NAME = "image_name";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_FAILURE = "failure";
    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;

    //urls
    public static final String IMAGE_UPDATE_URL = "";

    //Database information
    public static final String TABLE_DONORS = "donors";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_BIRTH_DATE = "birth_date";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_BLOOD_GROUP = "blood_group";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CITY = "city";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_INSTITUTE = "institute";
    public static final String KEY_IS_DONOR = "is_donor";
    public static final String KEY_AVAILABLE_HOUR = "available_hour";
    public static final String KEY_LAST_DONATION_DATE = "last_donation_date";
    public static final String KEY_TOTAL_DONATION = "total_donation";
    public static final String KEY_IS_DRUG_ADDICTED = "is_drug_addicted";
    public static final String KEY_DISEASES = "diseases";

    public static final String CREATE_TABLE_DONORS = "CREATE TABLE IF NOT EXISTS " + TABLE_DONORS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT, "
            + KEY_PHONE + " TEXT, "
            + KEY_BIRTH_DATE + " TEXT, "
            + KEY_WEIGHT + " TEXT, "
            + KEY_BLOOD_GROUP + " TEXT, "
            + KEY_IMAGE + " TEXT, "
            + KEY_CITY + " TEXT, "
            + KEY_LAT + " TEXT, "
            + KEY_LNG + " TEXT, "
            + KEY_OCCUPATION + " TEXT, "
            + KEY_INSTITUTE + " TEXT, "
            + KEY_IS_DONOR + " TEXT, "
            + KEY_AVAILABLE_HOUR + " TEXT, "
            + KEY_LAST_DONATION_DATE + " TEXT, "
            + KEY_TOTAL_DONATION + " INTEGER, "
            + KEY_IS_DRUG_ADDICTED + " TEXT, "
            + KEY_DISEASES + " TEXT" + ")";
}
