package com.jadblack.Ranger;

import android.os.Bundle;

public class JadBlackRangerHelper {

    private int mSelectedDay;
    private int mSelectedMonth;
    private int mSelectedYear;

    public JadBlackRangerHelper() {
        mSelectedDay = 0;
        mSelectedMonth = 0;
        mSelectedYear = 0;
    }

    public void setFromBundle(Bundle data) {
        mSelectedDay = data.getInt("selected_day");
        mSelectedMonth = data.getInt("selected_month");
        mSelectedYear = data.getInt("selected_year");
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    public int getSelectedYear() {
        return mSelectedYear;
    }
}
