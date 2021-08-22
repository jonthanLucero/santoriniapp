package com.example.santoriniapp.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class DataBindingUtils
{
    @BindingAdapter("bind:enable")
    public static void enable(View view, boolean enable) {
        view.setEnabled(enable);
    }

    @BindingAdapter("bind:visible")
    public static void visible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("bind:visibleOrInvisibleOnFalse")
    public static void visibleOrInvisibleOnFalse(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
