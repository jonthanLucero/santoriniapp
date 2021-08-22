package com.example.santoriniapp.utils.urbanizationAddPhotoGallery;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.santoriniapp.R;
import com.example.santoriniapp.utils.JadBlackCameraField;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class UrbanizationAddPhotoGalleryAddPhotoDialogFragment extends DialogFragment {


    public JadBlackCameraField photo_ctl;
    public Button addPhotoActionButton;
    public Button cancelPhotoActionButton;
    private TextInputEditText editTextDescripcion;
    private TextInputEditText editTexttitulo;
    private View view;
    private AddPhotoListener addPhotoListener;

    public UrbanizationAddPhotoGalleryAddPhotoDialogFragment() {
        // Required empty public constructor
    }


    // ------------------------------------------------------------------------------
    //  Instance Helper
    // ------------------------------------------------------------------------------
    public static UrbanizationAddPhotoGalleryAddPhotoDialogFragment newInstance() {
        UrbanizationAddPhotoGalleryAddPhotoDialogFragment fragment = new UrbanizationAddPhotoGalleryAddPhotoDialogFragment();
        return fragment;
    }


    // ----------------------------------------------------------------------
    //  Listener
    // ----------------------------------------------------------------------
    public interface AddPhotoListener{
        void addImage(UrbanizationAddPhotoGalleryItem image);
    }

    public void setAddPhotoListener(AddPhotoListener inteInsertar) {
        this.addPhotoListener = inteInsertar;
    }

    // ------------------------------------------------------------------------------
    //  OnCreateView
    // ------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inalambrik_add_photo_gallery_dialog, container, false);

        // Get Views.
        addPhotoActionButton        = view.findViewById(R.id.add_photo_action);
        cancelPhotoActionButton     = view.findViewById(R.id.cancel_photo_action);
        editTexttitulo              = view.findViewById(R.id.photo_title_edit_text);
        editTextDescripcion         = view.findViewById(R.id.photo_description_edit_text);

        // --------------------------------
        // Set OnClickListeners
        // --------------------------------
        addPhotoActionButton.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View view){

                // If photo is EMPTY, then show error message.
                if(photo_ctl.getFilePathIfWasChanged().isEmpty()) {
                    Toast.makeText(getContext(), "Debe seleccionar una foto", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create Image and add to the caller.
                UrbanizationAddPhotoGalleryItem image = new UrbanizationAddPhotoGalleryItem(editTexttitulo.getText().toString(),
                                                                                            editTextDescripcion.getText().toString(),
                                                                                            photo_ctl.getFilePathIfWasChanged(),
                                                                                            false);
                if(addPhotoListener != null){
                    addPhotoListener.addImage(image);
                    dismiss();
                }else
                    Toast.makeText(getContext(),"No se pudo agregar foto. Intente nuevamente",Toast.LENGTH_SHORT).show();
            }
        });
        cancelPhotoActionButton.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View view){
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get Jadblack Camera Field Control.
        photo_ctl = view.findViewById(R.id.photo_ctl);
        photo_ctl.setRequestingAppName("sfaplus");
        photo_ctl.setControlRequestsCodes(6188, 5181);
        photo_ctl.initialize(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if(dialog.getWindow()!=null) dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.InalambrikAnimation;
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getDialog() != null) {
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setLayout(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getDialog().getWindow()!=null)
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(photo_ctl.handleRequest(requestCode, resultCode, data))
            return;
    }
}
