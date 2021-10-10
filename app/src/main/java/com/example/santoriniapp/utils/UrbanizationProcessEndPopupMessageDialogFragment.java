package com.example.santoriniapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.santoriniapp.R;
import com.example.santoriniapp.databinding.FragmentUrbanizationSendResultInformationBinding;

public class UrbanizationProcessEndPopupMessageDialogFragment extends DialogFragment
{
    FragmentUrbanizationSendResultInformationBinding mBinding;

    // Listener
    UrbanizationProcessEndPopupListener mListener;

    // Bundle variable names.
    public final static String IMAGE_RESOURCE_ID        = "IMAGE_RESOURCE_ID";
    public final static String SUB_IMAGE_RESOURCE_ID    = "SUB_IMAGE_RESOURCE_ID";
    public final static String TITLE                    = "TITLE";
    public final static String SUBTITLE                 = "SUBTITLE";
    public final static String INFO_MESSAGE             = "INFO_MESSAGE";
    public final static String EXTRA_ACTION_CODE        = "EXTRA_ACTION_CODE";
    public final static String EXTRA_ACTION_TITLE       = "EXTRA_ACTION_TITLE";

    // Bundle variable.
    public int mImageResourceId     = R.drawable.action_help;
    public int mSubImageResourceId  = R.drawable.ic_material_circle_white;
    public String mTitle            = "TITLE";
    public String mSubtitle         = "SUBTITLE";
    public String mInfoMessage      = "";
    public int mExtraActionCode     = 0;
    public String mExtraActionTitle = "";

    // -------------------------------------------------------
    //  NewInstance Helper.
    // -------------------------------------------------------
    public static UrbanizationProcessEndPopupMessageDialogFragment newInstance(int imageResourceId, String title, String subtitle) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RESOURCE_ID, imageResourceId);
        args.putInt(SUB_IMAGE_RESOURCE_ID,0);
        args.putString(TITLE, title);
        args.putString(SUBTITLE,subtitle);
        args.putString(INFO_MESSAGE,"");
        args.putInt(EXTRA_ACTION_CODE,0);
        args.putString(EXTRA_ACTION_TITLE,"");

        UrbanizationProcessEndPopupMessageDialogFragment fragment = new UrbanizationProcessEndPopupMessageDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static UrbanizationProcessEndPopupMessageDialogFragment newInstanceWithSubImage(int imageResourceId, int subImageResourceId, String title, String subtitle) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RESOURCE_ID, imageResourceId);
        args.putInt(SUB_IMAGE_RESOURCE_ID,subImageResourceId);
        args.putString(TITLE, title);
        args.putString(SUBTITLE,subtitle);
        args.putString(INFO_MESSAGE,"");
        args.putInt(EXTRA_ACTION_CODE,0);
        args.putString(EXTRA_ACTION_TITLE,"");

        UrbanizationProcessEndPopupMessageDialogFragment fragment = new UrbanizationProcessEndPopupMessageDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static UrbanizationProcessEndPopupMessageDialogFragment newInstanceWithAction(int imageResourceId, String title, String subtitle,int actionCode, String actionTitle) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RESOURCE_ID, imageResourceId);
        args.putInt(SUB_IMAGE_RESOURCE_ID,0);
        args.putString(TITLE, title);
        args.putString(SUBTITLE,subtitle);
        args.putString(INFO_MESSAGE,"");
        args.putInt(EXTRA_ACTION_CODE,actionCode);
        args.putString(EXTRA_ACTION_TITLE,actionTitle);

        UrbanizationProcessEndPopupMessageDialogFragment fragment = new UrbanizationProcessEndPopupMessageDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static UrbanizationProcessEndPopupMessageDialogFragment newInstanceWithInfoMessage(int imageResourceId, String title, String subtitle, String infoMessage) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RESOURCE_ID, imageResourceId);
        args.putInt(SUB_IMAGE_RESOURCE_ID,0);
        args.putString(TITLE, title);
        args.putString(SUBTITLE,subtitle);
        args.putString(INFO_MESSAGE,infoMessage);
        args.putInt(EXTRA_ACTION_CODE,0);
        args.putString(EXTRA_ACTION_TITLE,"");

        UrbanizationProcessEndPopupMessageDialogFragment fragment = new UrbanizationProcessEndPopupMessageDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // -------------------------------------------------------
    //  Interface to send actions to calling Activity.
    // -------------------------------------------------------
    public interface UrbanizationProcessEndPopupListener {
        void onExtraActionClick(int extraActionCode);
        void onAcceptOrCancelClick();
    }


    // -------------------------------------------------------
    //  OnCreate
    // -------------------------------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting Bundle.
        Bundle bundle = getArguments();

        // Get Intent variables
        if(bundle !=null) {
            mImageResourceId    = bundle.getInt(IMAGE_RESOURCE_ID,R.drawable.action_help);
            mSubImageResourceId = bundle.getInt(SUB_IMAGE_RESOURCE_ID,R.drawable.ic_material_circle_white);
            mTitle              = bundle.getString(TITLE, "");
            mSubtitle           = bundle.getString(SUBTITLE,"");
            mInfoMessage        = bundle.getString(INFO_MESSAGE,"");
            mExtraActionCode    = bundle.getInt(EXTRA_ACTION_CODE,0);
            mExtraActionTitle   = bundle.getString(EXTRA_ACTION_TITLE,"");
        }
    }

    // -------------------------------------------------------
    //  OnCreateView
    // -------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set the Binding.
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_urbanization_send_result_information, container, false);

        // Avoid to be Closeable if pressed outside.
        setCancelable(false);

        // Setting the Values.
        mBinding.iconImageView.setImageResource(mImageResourceId);
        mBinding.titleTextView.setText(mTitle);
        mBinding.subtitleTextView.setText(mSubtitle);

        // If EXTRA_ACTION_IS_NEEDED.
        if(mExtraActionCode > 0){
            mBinding.acceptButton.setVisibility(View.GONE);
            mBinding.extraActionContainer.setVisibility(View.VISIBLE);
            mBinding.extraActionButton.setText(mExtraActionTitle);
            mBinding.extraActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(mListener!=null) mListener.onExtraActionClick(mExtraActionCode);
                    UrbanizationProcessEndPopupMessageDialogFragment.this.dismiss();
                }
            });
            mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(mListener!=null) mListener.onAcceptOrCancelClick();
                    UrbanizationProcessEndPopupMessageDialogFragment.this.dismiss();
                }
            });
        }else { // IF NO EXTRA ACTION IS NEEDED...
            mBinding.extraActionContainer.setVisibility(View.GONE);
            mBinding.acceptButton.setVisibility(View.VISIBLE);

            // Closing the Dialog Fragment.
            mBinding.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(mListener!=null) mListener.onAcceptOrCancelClick();
                    UrbanizationProcessEndPopupMessageDialogFragment.this.dismiss();
                }
            });
        }

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        mListener = (UrbanizationProcessEndPopupListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}
