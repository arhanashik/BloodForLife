package com.blackspider.bloodforlife.listener;

import com.blackspider.bloodforlife.models.Donor;

/**
 * Created by Arhan Ashik on 2/18/2018.
 */

public interface OnDonorClickListener {
    void onDonorClick(Donor donor);
    void onDonorCalled(String phoneNo);
}
