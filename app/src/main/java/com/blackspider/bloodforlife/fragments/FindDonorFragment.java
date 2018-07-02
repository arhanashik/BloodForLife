package com.blackspider.bloodforlife.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.adapter.DonarDataApapter;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.listener.OnDonorClickListener;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.others.AppConstants;
import com.blackspider.bloodforlife.views.CustomToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arhan Ashik on 2/18/2018.
 */

public class FindDonorFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnDonorClickListener {
    private View view;
    private Spinner filterBloodGroup, filterRadius;
    private RecyclerView mRecyclerView;
    private DonarDataApapter dataApapter;
    private List<Donor> donorList = new ArrayList<>();
    private  String filterBGroup, filterradius, bGroup;

    private DBHelper mDbHelper;
    public FindDonorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_find_donor, container, false);
        filterBloodGroup = view.findViewById(R.id.filter_blood_group);
        filterRadius = view.findViewById(R.id.filter_radius);
        mRecyclerView = view.findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mDbHelper = new DBHelper(getContext());
        donorList = mDbHelper.getDonors(AppConstants.TABLE_DONORS);
        dataApapter = new DonarDataApapter(getContext(), donorList, this);
        mRecyclerView.setAdapter(dataApapter);

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
        filterBloodGroup.setOnItemSelectedListener(this);
        filterRadius.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == filterBloodGroup) {
            filterBGroup = parent.getItemAtPosition(position).toString();
            if (position == 0 || position == 1) {
                dataApapter.setfilter(donorList);
            }else filterDonar(filterBGroup, filterradius);

        } else if (parent == filterRadius) {
            filterradius = parent.getItemAtPosition(position).toString().toLowerCase();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDonorClick(Donor donor) {
        Toast.makeText(getContext(), "Hi.."+donor.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonorCalled(String phoneNo) {
        if(TextUtils.isEmpty(phoneNo)){
            new CustomToast().Show_Toast(getContext(), view, "Phone number not found!");

        }else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        AppConstants.CALL_PHONE_REQUEST_CODE);
            }
            else startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNo, null)));
        }
    }

    private void filterDonar(String bloodGroup, String radius){
        List<Donor> filterList = new ArrayList<>();
        for (Donor donor : donorList){
            try {
                bGroup = donor.getBloodGroup().toString();
                if (bGroup.equals(bloodGroup)){
                    filterList.add(donor);
                }
            }catch (NullPointerException ex){

            }
        }
        dataApapter.setfilter(filterList);
    }
}
