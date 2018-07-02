package com.blackspider.bloodforlife.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.activities.MapsActivity;
import com.blackspider.bloodforlife.activities.WelcomeActivity;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.utils.CustomAnimation;
import com.blackspider.bloodforlife.views.CustomToast;
import com.blackspider.bloodforlife.views.DancingScriptRegularFontTextView;

/**
 * Created by Arhan Ashik on 2/18/2018.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private DBHelper dbHelper;

    private Button deleteAccount;
    private ImageView changePassword, btnConfirm, btnClose;
    ;
    private EditText editText1, editText2, editText3;

    private String input1, input2, input3, tableName;

    private LayoutInflater layoutInflater;
    private static AlertDialog.Builder alertDialogBuilder;
    private static AlertDialog alertDialog;
    private View promptsView, dialogView;
    private DancingScriptRegularFontTextView tvDeleteAccountName, tvDeleteAccountTitle, tvConfirmDelete, tvCancelDelete;
    private CustomAnimation mCustomAnimation;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_settings, container, false);

        dbHelper = new DBHelper(getContext());
        mCustomAnimation = new CustomAnimation(getContext());

        sharedPreferences = getContext().getSharedPreferences(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO, Context.MODE_PRIVATE);

        deleteAccount = view.findViewById(R.id.btn_delete_account);
        changePassword = view.findViewById(R.id.btn_change_password);

        return view;
    }

    @Override
    public Animation onCreateAnimation(final int transit, boolean enter, int nextAnim) {
        int anim;
        if(enter) anim = R.anim.right_enter;
        else anim = R.anim.left_out;
        Animation animation = AnimationUtils.loadAnimation(getContext(), anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                setListeners();
                //setValues();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        return animation;
    }

    private void setListeners() {
        deleteAccount.setOnClickListener(this);
        changePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == deleteAccount) {
            deleteAccountDialog();

        } else if (v == changePassword) {
            changePassword();

        } else if (v == tvCancelDelete) {
            mCustomAnimation.revealShow(dialogView, false, alertDialog);
            alertDialog.dismiss();

        } else if (v == tvConfirmDelete) {
            mCustomAnimation.revealShow(dialogView, false, alertDialog);
            if (dbHelper.deleteDonor(AppConstants.TABLE_DONORS, MainActivity.donor.getId())) {
                new CustomToast().Show_Toast(getContext(), view, "Account deleted.");

                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.remove(AppConstants.SHARED_PREFERENCES_SIGN_IN_INFO);
                sharedPreferencesEditor.remove(AppConstants.IS_SESSION_EXIST);
                sharedPreferencesEditor.remove(AppConstants.KEY_EMAIL);
                sharedPreferencesEditor.remove(AppConstants.KEY_PASSWORD);
                sharedPreferencesEditor.commit();
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", false);
                startActivity(intent);
                getActivity().finish();
            } else {
                new CustomToast().Show_Toast(getContext(), view, "Account not deleted.");
            }

        }
    }

    public void deleteAccountDialog() {
        // get layout_developer.xml view
        layoutInflater = LayoutInflater.from(getContext());
        promptsView = layoutInflater.inflate(R.layout.custom_logout_dialog, null);

        tvDeleteAccountName = promptsView.findViewById(R.id.tv_logout_name);
        tvDeleteAccountTitle = promptsView.findViewById(R.id.tv_logout_title);
        tvConfirmDelete = promptsView.findViewById(R.id.tv_confirm_logout);
        tvCancelDelete = promptsView.findViewById(R.id.tv_cancel_logout);

        dialogView = promptsView.findViewById(R.id.dialog);
        tvDeleteAccountName.setText(MainActivity.donor.getName());
        tvDeleteAccountTitle.setText("Are you sure to delete your account?\n\nOnce deleted there is no undo option.");
        tvConfirmDelete.setText("Yes, I'm sure");
        tvCancelDelete.setText("Cancel");

        tvConfirmDelete.setOnClickListener(this);
        tvCancelDelete.setOnClickListener(this);
//
        alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogStyle);

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

    public void changePassword() {
        // get prompts_view_input_area.xml view
        layoutInflater = LayoutInflater.from(getContext());
        promptsView = layoutInflater.inflate(R.layout.prompts_view_input_area, null);

        editText1 = promptsView.findViewById(R.id.input1);
        editText2 = promptsView.findViewById(R.id.input2);
        editText3 = promptsView.findViewById(R.id.input3);
        btnConfirm = promptsView.findViewById(R.id.img_confirm);
        btnClose = promptsView.findViewById(R.id.img_close);
        dialogView = promptsView.findViewById(R.id.dialog);

        editText1.setHint("Enter old password");
        editText2.setHint("Enter new password");
        editText3.setHint("Confirm new password");

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input1 = editText1.getText().toString();
                tableName = AppConstants.TABLE_DONORS;
                int donorId = MainActivity.donor.getId();
                if (!TextUtils.isEmpty(input1)) {
                    input2 = editText2.getText().toString();
                    input3 = editText3.getText().toString();
                    if (!(TextUtils.isEmpty(input2) || TextUtils.isEmpty(input3))) {
                        if(input2.length() > 5){
                            if (input2.equals(input3)) {
                                mCustomAnimation.revealShow(dialogView, false, alertDialog);
                                dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_PASSWORD, input3);
                                new CustomToast().Show_Toast(getContext(), view, "Password change successfully");
                            } else
                                new CustomToast().Show_Toast(getContext(), view, "Password miss match");
                        } else {
                            new CustomToast().Show_Toast(getContext(), view, "Password too short");
                        }
                    }
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomAnimation.revealShow(dialogView, false, alertDialog);
            }
        });

        alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogStyle);

        // set layout_image_preview.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
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
}
