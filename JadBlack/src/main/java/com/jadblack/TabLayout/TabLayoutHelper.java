package com.jadblack.TabLayout;

import android.os.Bundle;

public class TabLayoutHelper {

    private int mSelectedTabPosition;

    public TabLayoutHelper() {
        mSelectedTabPosition = 1;
    }

    public void setFromBundle(Bundle data) {
        mSelectedTabPosition = data.getInt("selected_tab_position");
    }

    public int getSelectedTabPosition() {
        return mSelectedTabPosition;
    }
}
