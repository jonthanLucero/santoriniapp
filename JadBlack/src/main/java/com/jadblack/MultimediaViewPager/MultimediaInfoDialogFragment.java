package com.jadblack.MultimediaViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MultimediaInfoDialogFragment extends DialogFragment {
	 
	static MultimediaInfoDialogFragment newInstance(String title, String message){
		MultimediaInfoDialogFragment fragment = new MultimediaInfoDialogFragment();
		Bundle args = new Bundle();
	    args.putString("title", title);
	    args.putString("message", message);
	    fragment.setArguments(args);
	    return fragment;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        
        return new AlertDialog.Builder(getActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            })            
            .create();
    }
}
