package com.blackspider.bloodforlife.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.activities.WelcomeActivity;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.receiver.ConnectivityReceiver;
import com.blackspider.bloodforlife.views.CatLoadingView;
import com.blackspider.bloodforlife.views.CustomToast;

public class SignUpFragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText fullName, emailId, mobileNumber,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private static CatLoadingView catLoadingView;

	private static TextWatcher textWatcher;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedPreferencesEditor;

	private static FragmentManager fragmentManager;
	private static ProgressDialog progressDialog;

	private boolean isConnected = false;

	int sdk = Build.VERSION.SDK_INT;

	private static String strName;
	private static String strPhone;
	private static String strEmail;
	private static String strPassword;
	private static String strConfirmPassword;

	private DBHelper dbHelper;
	private Donor donor;

	public SignUpFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_signup, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {
		dbHelper = new DBHelper(getContext());
		donor = new Donor();
		fragmentManager = getActivity().getSupportFragmentManager();

		fullName = (EditText) view.findViewById(R.id.fullName);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
		catLoadingView = new CatLoadingView();
		sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO, Context.MODE_PRIVATE);

		setCompDraw(fullName, R.drawable.user);
		setCompDraw(mobileNumber, R.drawable.phone);
		setCompDraw(emailId, R.drawable.email);
		setCompDraw(password, R.drawable.password);
		setCompDraw(confirmPassword, R.drawable.confirm_password);

	}

	private void setCompDraw(EditText v, int img){
		Drawable mDrawable = ContextCompat.getDrawable(getContext(), img);
		int size = (int) Math.round(v.getLineHeight() * 0.9);
		mDrawable.setBounds(0, 0, size, size);
		v.setCompoundDrawables(mDrawable, null, null, null);
	}

	private void setCompDrawErr(EditText v, int imgLeft, int imgRight){
		Drawable mDrawableLeft = ContextCompat.getDrawable(getContext(), imgLeft);
		Drawable mDrawableRight = ContextCompat.getDrawable(getContext(), imgRight);
		int size = (int) Math.round(v.getLineHeight() * 0.9);
		mDrawableLeft.setBounds(0, 0, size, size);
		mDrawableRight.setBounds(0, 0, size, size);
		DrawableCompat.setTint(mDrawableRight, ContextCompat.getColor(getContext(), R.color.red));
		v.setCompoundDrawables(mDrawableLeft, null, mDrawableRight, null);
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);

		textWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(LoginFragment.isValidEmail(charSequence.toString()) || TextUtils.isEmpty(charSequence))
                    setCompDraw(emailId, R.drawable.email);
				else {
					if (sdk < Build.VERSION_CODES.LOLLIPOP) {
						//
					}
					else setCompDrawErr(emailId, R.drawable.user, R.drawable.ic_warning_black_24dp);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {
				//tvSearch.setText("Show search result for "+editable);
			}
		};
		emailId.addTextChangedListener(textWatcher);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.signUpBtn:
                isConnected = ConnectivityReceiver.isConnected();
                if(isConnected) checkValidation();
                else new CustomToast().Show_Toast(getActivity(), view,
                        "Connection not available.");
                // Call checkValidation method
                //checkValidation();
                break;

            case R.id.already_user:
                // Replace login fragment
                new WelcomeActivity().replaceLoginFragment();
                break;
		}

	}

	// Check Validation Method
	private void checkValidation() {
		// Get all edittext texts
		strName = fullName.getText().toString();
		strPhone = mobileNumber.getText().toString();
		strEmail = emailId.getText().toString();
		strPassword = password.getText().toString();
		strConfirmPassword = confirmPassword.getText().toString();

		// Check if all strings are null or not
		if (strName.equals("") || strName.length() == 0
				|| strPhone.equals("") || strPhone.length() == 0
				|| strEmail.equals("") || strEmail.length() == 0
				|| strPassword.equals("") || strPassword.length() == 0
				|| strConfirmPassword.equals("") || strConfirmPassword.length() == 0)
			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

		// Check if email id valid or not
		else if (!LoginFragment.isValidEmail(strEmail))
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Check if email id already used or not
		else if (dbHelper.isInserted(AppConstants.TABLE_DONORS, AppConstants.KEY_EMAIL, strEmail))
			new CustomToast().Show_Toast(getActivity(), view,
					"ID already exists with this email.");

		// Check password length
		else if (strPassword.length() < 6)
			new CustomToast().Show_Toast(getActivity(), view,
					"Password too short.");

		// Check if both password should be equal
		else if (!strConfirmPassword.equals(strPassword))
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");

		// Else do signup or do your stuff
		else{
			donor.setName(strName);
			donor.setEmail(strEmail);
			donor.setPassword(strPassword);
			donor.setPhone(strPhone);
			if(dbHelper.saveDonor(AppConstants.TABLE_DONORS, donor) > 0) {
				new CustomToast().Show_Toast(getActivity(), view,
						"Signed up successfully!");
                donor = dbHelper.getValidDonor(AppConstants.TABLE_DONORS, strEmail, strPassword);
                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putBoolean(AppConstants.IS_SESSION_EXIST, true);
                sharedPreferencesEditor.putString(AppConstants.KEY_EMAIL, donor.getEmail());
                sharedPreferencesEditor.putString(AppConstants.KEY_PASSWORD, donor.getPassword());
                sharedPreferencesEditor.commit();
				Intent intent = new Intent(getContext(), MainActivity.class);
				intent.putExtra("DONOR", donor);
				startActivity(intent);
			}
			else
				new CustomToast().Show_Toast(getActivity(), view,
						"Signing up failed! Please try again.");
		}

	}
}
