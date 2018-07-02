package com.blackspider.bloodforlife.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.adapter.DonarDataApapter;
import com.blackspider.bloodforlife.database.DBHelper;
import com.blackspider.bloodforlife.listener.OnDonorClickListener;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.others.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arhan Ashik on 2/18/2018.
 */

public class HomeFragment extends Fragment{
    private View view;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);

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
    }
}
