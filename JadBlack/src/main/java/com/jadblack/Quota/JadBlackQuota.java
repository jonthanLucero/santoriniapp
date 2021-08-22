package com.jadblack.Quota;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.andressantibanez.android.quota.QuotaLayout;

public class JadBlackQuota extends QuotaLayout {

    public JadBlackQuota(Context context) {
        super(context);
    }

    public JadBlackQuota(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JadBlackQuota(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void handleChange(Bundle data) {
        if(data == null)
            return;

        //Set Complied Amount
        if(data.containsKey("complied_amount")) {
            double compliedAmount = data.getDouble("complied_amount");
            setCompliedAmount(compliedAmount);
            Log.d(TAG, "Complied Amount set: " + compliedAmount);
        }

        //Set Total Amount
        if(data.containsKey("total_amount")) {
            double totalAmount = data.getDouble("total_amount");
            setTotalAmount(totalAmount);
            Log.d(TAG, "Total Amount set: " + totalAmount);
        }

        //Set Visibility
        if(data.containsKey("visibility")) {
            int visibility = data.getInt("visibility", VISIBLE);
            if(visibility == VISIBLE)
                setVisibility(VISIBLE);
            else
                setVisibility(GONE);
        }
    }

}
