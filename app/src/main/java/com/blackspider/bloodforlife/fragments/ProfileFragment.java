package com.blackspider.bloodforlife.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.listener.OnDonorClickListener;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.utils.CustomAnimation;
import com.blackspider.bloodforlife.utils.CustomImageConverter;
import com.blackspider.bloodforlife.views.CustomToast;
import com.blackspider.bloodforlife.views.DancingScriptRegularFontTextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements OnClickListener{
    private View view;
    private DancingScriptRegularFontTextView tvIcon;
    private TextView tvName, tvStatus, tvBloodGroup, tvLastDonationDate, tvCity, tvPhone, tvEmail,
            tvTotalDonation, tvBirthDate, tvWeight, tvOccupation, tvInstitute,
            tvAvailableHour, tvDiseases;
    private ImageView imgProfile, imgChangeImage, imgChangeName, imgChangeBloodGroup,
            imgChangeLastDonationDate, imgChangeCity, imgChangePhone, imgChangeTotalDonation, imgChangeBirthDate,
            imgChangeWeight, imgChangeOccupation, imgChangeInstitute, imgChangeAvailableHour,
            imgChangeDiseases, imageViewImageToUpload, btnConfirm, btnClose;
    private ImageButton imageButtonCamera,imageButtonGallery, imageButtonAvater;
    private EditText editText1, editText2, editText3;
    private CheckBox cbIsDonor, cbIsDrugAddicted;
    private DancingScriptRegularFontTextView tvCameraTitle, tvGalleryTitle, tvAvaterTitle, tvCancelUpload, tvUpload, tvCancelMethod;
    private LayoutInflater layoutInflater;
    private static AlertDialog.Builder alertDialogBuilder;
    private static AlertDialog alertDialog;
    private View promptsView, dialogView;
    private static Bitmap bitmap;

    private DBHelper dbHelper;
    private CustomAnimation mCustomAnimation;
    private CustomImageConverter mCustomImageConverter;

    int sdk = Build.VERSION.SDK_INT;

	public ProfileFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_profile, container, false);
		initViews();
		setData();
		return view;
	}

    private void initViews() {
	    tvIcon = view.findViewById(R.id.icon_text);
	    tvName = view.findViewById(R.id.tvName);
	    tvStatus = view.findViewById(R.id.tvStatus);
        tvBloodGroup = view.findViewById(R.id.tvBloodGroup);
        tvLastDonationDate = view.findViewById(R.id.tvLastDonationDate);
        tvCity = view.findViewById(R.id.tvCity);
        tvPhone = view.findViewById(R.id.tvNumber);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvTotalDonation = view.findViewById(R.id.tvTotalDonation);
        tvBirthDate = view.findViewById(R.id.tvBirthDate);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvOccupation = view.findViewById(R.id.tvOccupation);
        tvInstitute = view.findViewById(R.id.tvInstitute);
        tvAvailableHour = view.findViewById(R.id.tvAvailableHour);
        tvDiseases = view.findViewById(R.id.tvDiseases);

        imgProfile = view.findViewById(R.id.iv_profile_img);
        imgChangeImage = view.findViewById(R.id.iv_profile_img_btn);
        imgChangeName = view.findViewById(R.id.btnEditName);
        imgChangeBloodGroup = view.findViewById(R.id.btnEditBloodGroup);
        imgChangeLastDonationDate = view.findViewById(R.id.btnEditLastDonationDate);
        imgChangeCity = view.findViewById(R.id.btnEditCity);
        imgChangePhone = view.findViewById(R.id.btnEditNumber);
        imgChangeTotalDonation = view.findViewById(R.id.btnEditTotalDonation);
        imgChangeBirthDate = view.findViewById(R.id.btnEditBirthDate);
        imgChangeWeight = view.findViewById(R.id.btnEditWeight);
        imgChangeOccupation = view.findViewById(R.id.btnEditOccupation);
        imgChangeInstitute = view.findViewById(R.id.btnEditInstitute);
        imgChangeAvailableHour = view.findViewById(R.id.btnEditAvailableHour);
        imgChangeDiseases = view.findViewById(R.id.btnEditDiseases);

        cbIsDonor = view.findViewById(R.id.cbIsDonor);
        cbIsDrugAddicted = view.findViewById(R.id.cbIsDrugAddicted);

        dbHelper = new DBHelper(getContext());
        layoutInflater = LayoutInflater.from(getContext());
        mCustomAnimation = new CustomAnimation(getContext());
        mCustomImageConverter = new CustomImageConverter();
    }

    private void setData() {
	    tvIcon.setText(MainActivity.donor.getName().substring(0, 1));
	    tvName.setText(MainActivity.donor.getName());
	    String status = MainActivity.donor.getBloodGroup() + "|" + MainActivity.donor.getLastDonationDate();
	    if(status.contains("null")) status = "Blood Group | Last Donation";
	    tvStatus.setText(status);
	    String bloodGroup = MainActivity.donor.getBloodGroup();
	    if(bloodGroup==null) bloodGroup = "Not set";
	    tvBloodGroup.setText(bloodGroup);
	    String lastDonationDate = MainActivity.donor.getLastDonationDate();
	    if(lastDonationDate==null) lastDonationDate = "Not set";
	    tvLastDonationDate.setText(lastDonationDate);
	    String city = MainActivity.donor.getCity();
	    if(city==null) city = "Not set";
	    tvCity.setText(city);
	    tvPhone.setText(MainActivity.donor.getPhone());
	    tvEmail.setText(MainActivity.donor.getEmail());
	    tvTotalDonation.setText(MainActivity.donor.getTotalDonation() + " times");
	    String birthDate = (MainActivity.donor.getBirthDate()==null)? "Not set" : MainActivity.donor.getBirthDate();
	    tvBirthDate.setText(birthDate);
        tvWeight.setText(MainActivity.donor.getWeight() + " kg");
        String occupation = (MainActivity.donor.getOccupation()==null)? "Not set" : MainActivity.donor.getOccupation();
        tvOccupation.setText(occupation);
        String institute = (MainActivity.donor.getInstitute()==null)? "Not set" : MainActivity.donor.getInstitute();
        tvInstitute.setText(institute);
        cbIsDonor.setChecked(MainActivity.donor.isDonor());
        String availableHour = (MainActivity.donor.getAvailableHour()==null)? "Not set" : MainActivity.donor.getAvailableHour();
        tvAvailableHour.setText(availableHour);
        cbIsDrugAddicted.setChecked(MainActivity.donor.isDrugAddicted());
        String diseases = (MainActivity.donor.getDiseases()==null)? "Not set" : MainActivity.donor.getDiseases();
        tvDiseases.setText(diseases);

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
        imgChangeImage.setOnClickListener(this);
        imgChangeName.setOnClickListener(this);
        imgChangeBloodGroup.setOnClickListener(this);
        imgChangeLastDonationDate.setOnClickListener(this);
        imgChangeCity.setOnClickListener(this);
        imgChangePhone.setOnClickListener(this);
        imgChangeTotalDonation.setOnClickListener(this);
        imgChangeBirthDate.setOnClickListener(this);
        imgChangeWeight.setOnClickListener(this);
        imgChangeOccupation.setOnClickListener(this);
        imgChangeInstitute.setOnClickListener(this);
        imgChangeAvailableHour.setOnClickListener(this);
        imgChangeDiseases.setOnClickListener(this);

        cbIsDonor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.donor.setDonor(isChecked);
                dbHelper.updateColumnValue(AppConstants.TABLE_DONORS, MainActivity.donor.getId(), AppConstants.KEY_IS_DONOR, isChecked+"");
            }
        });

        cbIsDrugAddicted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.donor.setDrugAddicted(isChecked);
                dbHelper.updateColumnValue(AppConstants.TABLE_DONORS, MainActivity.donor.getId(), AppConstants.KEY_IS_DRUG_ADDICTED, isChecked+"");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==imgChangeImage){
            galleryPermissionDialog(AppConstants.GALLERY_ACCESS_PERMISSION_REQUEST_CODE);

        }else if(v==imgChangeName){
            changeValue(AppConstants.FLAG_CHANGE_NAME);

        }else if(v==imgChangeBloodGroup){
            changeValue(AppConstants.FLAG_CHANGE_BLOOD_GROUP);

        }else if(v==imgChangeLastDonationDate){
            changeValue(AppConstants.FLAG_CHANGE_LAST_DONATION_DATE);

        }else if(v==imgChangeCity){
            changeValue(AppConstants.FLAG_CHANGE_CITY);

        }else if(v==imgChangePhone){
            changeValue(AppConstants.FLAG_CHANGE_PHONE);

        }else if(v==imgChangeTotalDonation){
            changeValue(AppConstants.FLAG_CHANGE_TOTAL_DONATION);

        }else if(v==imgChangeBirthDate){
            changeValue(AppConstants.FLAG_CHANGE_BIRTH_DATE);

        }else if(v==imgChangeWeight){
            changeValue(AppConstants.FLAG_CHANGE_WEIGHT);

        }else if(v==imgChangeOccupation){
            changeValue(AppConstants.FLAG_CHANGE_OCCUPATION);

        }else if(v==imgChangeInstitute){
            changeValue(AppConstants.FLAG_CHANGE_INSTITUTE);

        }else if(v==imgChangeAvailableHour){
            changeValue(AppConstants.FLAG_CHANGE_AVAILABLE_HOUR);

        }else if(v==imgChangeDiseases){
            changeValue(AppConstants.FLAG_CHANGE_DISEASES);

        } else if(v==tvCameraTitle){
            alertDialog.dismiss();
            captureImage();

        }else if(v==tvGalleryTitle){
            alertDialog.dismiss();
            selectImageFromGallery();

        }else if(v==tvAvaterTitle){
            alertDialog.dismiss();
            bitmap = null;
            new CustomToast().Show_Toast(getActivity(), view,
                    "Coming soon...");

        }else if(v==tvCancelMethod){
            mCustomAnimation.revealShow(dialogView, false, alertDialog);

        }else if(v==tvUpload){
            mCustomAnimation.revealShow(dialogView, false, alertDialog);
            //uploadImage();
            if(bitmap!=null){
                tvIcon.setVisibility(View.INVISIBLE);
                imgProfile.setVisibility(View.VISIBLE);
                imgProfile.setImageBitmap(mCustomImageConverter.getCircledBitmap(bitmap));
            }

        }else if(v==tvCancelUpload){
            mCustomAnimation.revealShow(dialogView, false, alertDialog);

        }else if(v==btnClose){
            mCustomAnimation.revealShow(dialogView, false, alertDialog);

        }
    }

    void galleryPermissionDialog(int requestCode) {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return;

        } else imageSelectionMethodAlertDialog();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, String[] permissions, int[] grantResults) {
        Map<String, Integer> perms = new HashMap<String, Integer>();
        // Initial
        perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        // Fill with results
        for (int i = 0; i < permissions.length; i++)
            perms.put(permissions[i], grantResults[i]);
        // Check for READ_EXTERNAL_STORAGE

        boolean showRationale = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted
                imageSelectionMethodAlertDialog();

            } else {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
                if (showRationale) {
                    Toast.makeText(getContext(), "Please enable the Read Storage permission to change image", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Please Enable the Read Storage permission in permission", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, requestCode);

                    //proceed with logic by disabling the related features or quit the app.
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully captured the image
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = mCustomImageConverter.getResizedBitmap(bitmap, 350);
                imagePreviewAndUpload(bitmap);

                Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image selection
                Toast.makeText(getContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to select image
                new CustomToast().Show_Toast(getActivity(), view,
                        "Something's wrong! Try again.");
            }

            //else if the result is gallery Image
        } else if (requestCode == AppConstants.GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully selected the image
                Uri filePath = data.getData();
                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    bitmap = mCustomImageConverter.getResizedBitmap(bitmap, 350);
                    imagePreviewAndUpload(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image selection
//                Toast.makeText(getContext(),
//                        "User cancelled image selection", Toast.LENGTH_SHORT)
//                        .show();

            } else {
                // failed to select image
                new CustomToast().Show_Toast(getActivity(), view,
                        "Something's wrong! Try again.");
            }

        }
    }

    public void imageSelectionMethodAlertDialog(){
        // get layout_image_selection_method.xml view
        layoutInflater = LayoutInflater.from(getContext());
        promptsView = layoutInflater.inflate(R.layout.layout_image_selection_method, null);

        imageButtonCamera = (ImageButton) promptsView.findViewById(R.id.imageButtonCamera);
        imageButtonGallery = (ImageButton) promptsView.findViewById(R.id.imageButtonGallery);
        imageButtonAvater = (ImageButton) promptsView.findViewById(R.id.imageButtonAvater);
        tvCameraTitle = promptsView.findViewById(R.id.textViewCameraTitle);
        tvGalleryTitle = promptsView.findViewById(R.id.textViewGalleryTitle);
        tvAvaterTitle = promptsView.findViewById(R.id.textViewAvaterTitle);
        tvCancelMethod = promptsView.findViewById(R.id.tv_cancel_dialog);

        dialogView = promptsView.findViewById(R.id.dialog);

        imageButtonCamera.setOnClickListener(this);
        imageButtonGallery.setOnClickListener(this);
        imageButtonAvater.setOnClickListener(this);
        tvCameraTitle.setOnClickListener(this);
        tvGalleryTitle.setOnClickListener(this);
        tvAvaterTitle.setOnClickListener(this);
        tvCancelMethod.setOnClickListener(this);

        alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogStyle);

        // set promptsView to alertdialog builder
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

    public void captureImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture From"),
                AppConstants.GALLERY_IMAGE_REQUEST_CODE);
    }

    public void imagePreviewAndUpload(final Bitmap bmp){
        // get layout_developer.xml view
        layoutInflater = LayoutInflater.from(getContext());
        promptsView = layoutInflater.inflate(R.layout.layout_image_preview, null);

        imageViewImageToUpload = (ImageView) promptsView.findViewById(R.id.imageViewImageToUpload);
        tvUpload = promptsView.findViewById(R.id.tv_upload_image);
        tvCancelUpload = promptsView.findViewById(R.id.tv_cancel_upload);

        dialogView = promptsView.findViewById(R.id.dialog);

        bitmap = bmp;
        imageViewImageToUpload.setImageBitmap(mCustomImageConverter.getCircledBitmap(bmp));

        tvUpload.setOnClickListener(this);
        tvCancelUpload.setOnClickListener(this);
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

    String input1 = "", input2 = "", input3 = "";

    public void changeValue(final int flag){
        // get prompts_view_input_area.xml view
        layoutInflater = LayoutInflater.from(getContext());
        promptsView = layoutInflater.inflate(R.layout.prompts_view_input_area, null);

        editText1 = (EditText) promptsView.findViewById(R.id.input1);
        editText2 = (EditText) promptsView.findViewById(R.id.input2);
        editText3 = (EditText) promptsView.findViewById(R.id.input3);
        btnConfirm = (ImageView) promptsView.findViewById(R.id.img_confirm);
        btnClose = (ImageView) promptsView.findViewById(R.id.img_close);
        dialogView = promptsView.findViewById(R.id.dialog);

        if(flag== AppConstants.FLAG_CHANGE_PASSWORD){
            editText1.setHint("Enter old password");
            editText2.setHint("Enter new password");
            editText3.setHint("Confirm new password");
        }else {
            editText2.setVisibility(View.GONE);
            editText3.setVisibility(View.GONE);
            if(flag== AppConstants.FLAG_CHANGE_NAME) changeEditTextHintValue(editText1, "Enter new name", MainActivity.donor.getName());
            else if(flag== AppConstants.FLAG_CHANGE_BLOOD_GROUP) changeEditTextHintValue(editText1, "Enter new blood group", MainActivity.donor.getBloodGroup());
            else if(flag== AppConstants.FLAG_CHANGE_LAST_DONATION_DATE) changeEditTextHintValue(editText1, "Enter last donation date", MainActivity.donor.getLastDonationDate());
            else if(flag== AppConstants.FLAG_CHANGE_CITY) changeEditTextHintValue(editText1, "Enter new city", MainActivity.donor.getCity());
            else if(flag== AppConstants.FLAG_CHANGE_PHONE) changeEditTextHintValue(editText1, "Enter new phone number", MainActivity.donor.getPhone());
            else if(flag== AppConstants.FLAG_CHANGE_TOTAL_DONATION) {
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                changeEditTextHintValue(editText1, "Enter total donation", MainActivity.donor.getTotalDonation() + "");
            }
            else if(flag== AppConstants.FLAG_CHANGE_BIRTH_DATE) changeEditTextHintValue(editText1, "Enter new birth date", MainActivity.donor.getBirthDate());
            else if(flag== AppConstants.FLAG_CHANGE_WEIGHT) {
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                changeEditTextHintValue(editText1, "Enter new weight", MainActivity.donor.getWeight()+"");
            }
            else if(flag== AppConstants.FLAG_CHANGE_OCCUPATION) changeEditTextHintValue(editText1, "Enter new occupation", MainActivity.donor.getOccupation());
            else if(flag== AppConstants.FLAG_CHANGE_INSTITUTE) changeEditTextHintValue(editText1, "Enter new institute", MainActivity.donor.getInstitute());
            else if(flag== AppConstants.FLAG_CHANGE_DONOR_STATUS) changeEditTextHintValue(editText1, "Enter new donor status", MainActivity.donor.isDonor()+"");
            else if(flag== AppConstants.FLAG_CHANGE_AVAILABLE_HOUR) changeEditTextHintValue(editText1, "Enter available hour", MainActivity.donor.getAvailableHour());
            else if(flag== AppConstants.FLAG_CHANGE_IS_DRUG_ADDICTED) changeEditTextHintValue(editText1, "Enter drug addction", MainActivity.donor.isDrugAddicted()+"");
            else if(flag== AppConstants.FLAG_CHANGE_DISEASES) changeEditTextHintValue(editText1, "Enter diseases", MainActivity.donor.getDiseases());
        }

        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                input1 = editText1.getText().toString();
                String tableName = AppConstants.TABLE_DONORS;
                int donorId = MainActivity.donor.getId();
                if(!TextUtils.isEmpty(input1)) {
                    if(flag== AppConstants.FLAG_CHANGE_NAME){
                        MainActivity.donor.setName(input1);
                        tvIcon.setText(input1.substring(0,1));
                        tvName.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_NAME, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_BLOOD_GROUP){
                        if(input1!=null) input1 = input1.toUpperCase();
                        MainActivity.donor.setBloodGroup(input1);
                        tvBloodGroup.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_BLOOD_GROUP, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_LAST_DONATION_DATE){
                        MainActivity.donor.setLastDonationDate(input1);
                        tvLastDonationDate.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_LAST_DONATION_DATE, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_CITY){
                        MainActivity.donor.setCity(input1);
                        tvCity.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_CITY, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_PHONE){
                        MainActivity.donor.setPhone(input1);
                        tvPhone.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_PHONE, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_TOTAL_DONATION){
                        MainActivity.donor.setTotalDonation(Integer.parseInt(input1));
                        tvTotalDonation.setText(input1 + " times");
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_TOTAL_DONATION, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_BIRTH_DATE){
                        MainActivity.donor.setBirthDate(input1);
                        tvBirthDate.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_BIRTH_DATE, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_WEIGHT){
                        MainActivity.donor.setWeight(Integer.parseInt(input1));
                        tvWeight.setText(input1 + "kg");
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_WEIGHT, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_OCCUPATION){
                        MainActivity.donor.setOccupation(input1);
                        tvOccupation.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_OCCUPATION, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_INSTITUTE){
                        MainActivity.donor.setInstitute(input1);
                        tvInstitute.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_INSTITUTE, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_AVAILABLE_HOUR){
                        MainActivity.donor.setAvailableHour(input1);
                        tvAvailableHour.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_AVAILABLE_HOUR, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_DISEASES){
                        MainActivity.donor.setDiseases(input1);
                        tvDiseases.setText(input1);
                        dbHelper.updateColumnValue(tableName, donorId, AppConstants.KEY_DISEASES, input1);
                        mCustomAnimation.revealShow(dialogView, false, alertDialog);
                    }
                    else if(flag== AppConstants.FLAG_CHANGE_PASSWORD){
                        input2 = editText2.getText().toString();
                        input3 = editText3.getText().toString();
                        if(!(TextUtils.isEmpty(input2)||TextUtils.isEmpty(input3))){
                            if(input2.equals(input3)) {
                                mCustomAnimation.revealShow(dialogView, false, alertDialog);
                            }
                            else new CustomToast().Show_Toast(getContext(), view, "Password miss match");
                        }
                    }
                }
            }
        });
        btnClose.setOnClickListener(this);

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

    private void changeEditTextHintValue(EditText editText, String hint, String value){
        editText.setHint(hint);
        editText.setText(value);
    }
}
