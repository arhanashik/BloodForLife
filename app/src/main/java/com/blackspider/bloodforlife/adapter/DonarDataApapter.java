package com.blackspider.bloodforlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackspider.bloodforlife.R;
import com.blackspider.bloodforlife.activities.MainActivity;
import com.blackspider.bloodforlife.listener.OnDonorClickListener;
import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.views.DancingScriptRegularFontTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arhan Ashik on 2/18/2018.
 */

public class DonarDataApapter extends RecyclerView.Adapter<DonarDataApapter.ViewHolder> {
    private Context mContext;
    private List<Donor> mAllDonor;
    private OnDonorClickListener mDonorClickListener;

    public DonarDataApapter(Context mContext, List<Donor> mAllDonor, OnDonorClickListener OnDonorClickListener) {
        this.mContext = mContext;
        this.mAllDonor = mAllDonor;
        this.mDonorClickListener = OnDonorClickListener;
    }

    @Override
    public DonarDataApapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_donar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonarDataApapter.ViewHolder holder, int position) {
        Donor donor = mAllDonor.get(position);
        holder.tvName.setText(donor.getName());
        holder.tvStatus.setText(donor.getBloodGroup() + " | " + donor.getLastDonationDate());
        holder.icon_text.setText(donor.getName().substring(0, 1));
        if (MainActivity.donor.getEmail().equals(donor.getEmail())) {
            holder.tvName.setText("Me");
            holder.btnPhonCall.setVisibility(View.GONE);
        }
        else holder.btnPhonCall.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if (mAllDonor != null) return mAllDonor.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvStatus, icon_text;
        private ImageView iv_profile_img, btnPhonCall;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            icon_text = (DancingScriptRegularFontTextView) itemView.findViewById(R.id.icon_text);
            iv_profile_img = (ImageView) itemView.findViewById(R.id.iv_profile_img);
            btnPhonCall = (ImageView) itemView.findViewById(R.id.btnPhonCall);

            btnPhonCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDonorClickListener.onDonorCalled(mAllDonor.get(getLayoutPosition()).getPhone());
                }
            });
        }
    }

    public void setfilter(List<Donor> filterData){
        mAllDonor = new ArrayList<>();
        mAllDonor.addAll(filterData);
        notifyDataSetChanged();
    }
}
