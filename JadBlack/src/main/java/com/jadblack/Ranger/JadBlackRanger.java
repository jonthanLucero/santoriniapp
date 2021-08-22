package com.jadblack.Ranger;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

public class JadBlackRanger extends RangerLocal {

    /**
     * Required constructors
     */
    public JadBlackRanger(Context context) {
        super(context);
    }

    public JadBlackRanger(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JadBlackRanger(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Custom implementation
     */
    public void handleChange(Bundle data) {
        if(data == null)
            return;

    }

    //Get bundle
    public Bundle getBundle() {
        Bundle data = new Bundle();
        data.putInt("selected_day",  getSelectedDay());
        data.putInt("selected_month",  getSelectedMonth());
        data.putInt("selected_year",  getSelectedYear());

        return data;
    }

}
