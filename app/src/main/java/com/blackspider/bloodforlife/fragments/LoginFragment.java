package com.blackspider.bloodforlife.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.activities.WelcomeActivity;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.models.Location;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.receiver.ConnectivityReceiver;
import com.blackspider.bloodforlife.utils.CustomAnimation;
import com.blackspider.bloodforlife.views.CatLoadingView;
import com.blackspider.bloodforlife.views.CustomToast;

public class LoginFragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText emailid, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
    private static CatLoadingView catLoadingView;

	private static FragmentManager fragmentManager;
    private static ProgressDialog progressDialog;

	private static TextWatcher textWatcher;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private static CustomAnimation mCustomAnimation;

	private boolean isConnected = false;

    int sdk = Build.VERSION.SDK_INT;

    private final int left=0, right=1;

    private DBHelper dbHelper;

	public LoginFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_login, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initiate Views
	private void initViews() {
		dbHelper = new DBHelper(getContext());
		fragmentManager = getActivity().getSupportFragmentManager();

		emailid = (EditText) view.findViewById(R.id.login_emailid);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view
				.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        catLoadingView = new CatLoadingView();

		// Load ShakeAnimation
		mCustomAnimation = new CustomAnimation(getContext());
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO, Context.MODE_PRIVATE);

        setCompDraw(emailid, R.drawable.user, left);
        setCompDraw(password, R.drawable.password, left);

        checkSessionValidity();
	}

	private void setCompDraw(EditText v, int img, int side){
        Drawable mDrawable = ContextCompat.getDrawable(getContext(), img);
        int size = (int) Math.round(v.getLineHeight() * 0.9);
        mDrawable.setBounds(0, 0, size, size);
        if(side==left) v.setCompoundDrawables(mDrawable, null, null, null);
        if(side==right) v.setCompoundDrawables(null, null, mDrawable, null);
    }

	private void setCompDrawErr(EditText v, int imgLeft, int imgRight){
		Drawable mDrawableLeft = ContextCompat.getDrawable(getContext(), imgLeft);
		Drawable mDrawableRight = ContextCompat.getDrawable(getContext(), imgRight);
		int size = (int) Math.round(v.getLineHeight() * 0.9);
		mDrawableLeft.setBounds(0, 0, size, size);
		mDrawableRight.setBounds(0, 0, size, size);
		DrawableCompat.setTint(mDrawableRight, ContextCompat.getColor(getContext(), R.color.colorAccent));
		v.setCompoundDrawables(mDrawableLeft, null, mDrawableRight, null);
	}

    private void checkSessionValidity(){
        if(sharedPreferences.getBoolean(AppConstants.IS_SESSION_EXIST, false)){
            String email = sharedPreferences.getString(AppConstants.KEY_EMAIL, null);
            String password = sharedPreferences.getString(AppConstants.KEY_PASSWORD, null);
            if(email != null && password != null){
                Donor donor = dbHelper.getValidDonor(AppConstants.TABLE_DONORS, email, password);
                if(donor != null){
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("DONOR", donor);
                    new CustomToast().Show_Toast(getContext(), view, "Welcome back " + donor.getName());
                    startActivity(intent);
                }
            }
        }
    }

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		// Set check listener over checkbox for showing and hiding password
		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						// If it is checkec then show password else hide
						// password
						if (isChecked) {

							show_hide_password.setText(R.string.hide_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());// show password
						} else {
							show_hide_password.setText(R.string.show_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password

						}

					}
				});

		textWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(isValidEmail(charSequence.toString()) || TextUtils.isEmpty(charSequence))
					setCompDraw(emailid, R.drawable.user, left);
				else {
                    if (sdk < Build.VERSION_CODES.LOLLIPOP) {
                        //
                    }
                    else setCompDrawErr(emailid, R.drawable.user, R.drawable.ic_warning_black_24dp);
                }
			}

			@Override
			public void afterTextChanged(Editable editable) {
				//tvSearch.setText("Show search result for "+editable);
			}
		};
		emailid.addTextChangedListener(textWatcher);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			isConnected = ConnectivityReceiver.isConnected();
			if(isConnected) checkValidation();
			else new CustomToast().Show_Toast(getActivity(), view,
					"Connection not available.");
			break;

		case R.id.forgot_password:

			// Replace forgot password fragment with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.left_enter, R.anim.zoom_out)
					.replace(R.id.frameContainer,
							new ForgotPassword_Fragment(),
							AppConstants.ForgotPasswordFragment).commit();
			break;
		case R.id.createAccount:

			// Replace signup frgament with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.bottom_enter, R.anim.zoom_out)
					.replace(R.id.frameContainer, new SignUpFragment(),
							AppConstants.SignUpFragment).commit();
			break;
		}

	}

	// Check Validation before login
	private void checkValidation() {
		// Get email id and password
		final String getEmailId = emailid.getText().toString();
		final String getPassword = password.getText().toString();

		// Check for both field is empty or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			mCustomAnimation.shakeAnim(loginLayout);
			new CustomToast().Show_Toast(getActivity(), view,
					"Enter both credentials.");

		}
		// Check if email id is valid or not
		else if (!isValidEmail(getEmailId))
        {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Username is Invalid.");
            //setCompDraw(emailid, R.drawable.error, right);
        }
		// Else do login and do your stuff
		else
        {
			checkOnServer(getEmailId, getPassword);
        }

	}

	public static boolean isValidEmail(String email){
		// Check patter for email id
		Pattern p = Pattern.compile(AppConstants.EmailRegEx);
		Matcher m = p.matcher(email);
		return m.find();
	}

    public void checkOnServer(final String username, final String password) {
		int flag = -1;
		if (sdk < Build.VERSION_CODES.LOLLIPOP) {
			progressDialog = new ProgressDialog(getContext());
			progressDialog.setMessage("Signing in...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			flag = 1;
		} else {
			catLoadingView.setText("SIGNING IN...");
			catLoadingView.setCancelable(false);
			catLoadingView.show(fragmentManager, "");
			flag = 2;
		}

		Donor donor = dbHelper.getValidDonor(AppConstants.TABLE_DONORS, username, password);
        if(flag == 1) progressDialog.dismiss();
        else catLoadingView.dismiss();
		if(donor != null){
            sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putBoolean(AppConstants.IS_SESSION_EXIST, true);
            sharedPreferencesEditor.putString(AppConstants.KEY_EMAIL, username);
            sharedPreferencesEditor.putString(AppConstants.KEY_PASSWORD, password);
            sharedPreferencesEditor.commit();
            new CustomToast().Show_Toast(getActivity(), view,
                    "Welcome " + donor.getName());
			Intent intent = new Intent(getContext(), MainActivity.class);
			intent.putExtra("DONOR", donor);
			startActivity(intent);
		}
		else new CustomToast().Show_Toast(getActivity(), view,
				"Invalid email or password.");
    }
}
