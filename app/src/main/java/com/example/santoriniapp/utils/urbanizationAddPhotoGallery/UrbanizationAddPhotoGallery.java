package com.example.santoriniapp.utils.urbanizationAddPhotoGallery;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.annotation.Nullable;

import com.example.santoriniapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class UrbanizationAddPhotoGallery extends LinearLayout{

    // Tag
    public final String TAG = "UrbanizationGallery";

    // DialogFragment Tag.
    public final String GalleryDialogFragmentTag = "GalleryDescripFragment";

    // Views
    private RecyclerView UrbanizationGalleryRecyclerView;
    private FloatingActionButton addNewImageButton;
    private TextView noPhotosFoundTextView;

    // Adapter
    private UrbanizationAddPhotoGalleryAdapter mPhotoAdapter;

    // Control Max Photos
    private int maxPhotosNum = 5; // By Default 5 Photos as Maximum.
    private UrbanizationAddPhotoGalleryAddPhotoDialogFragment addPhotoDialogFragment;


    private UrbanizationAddPhotoGalleryViewModel UrbanizationGalleryViewModel;

    private boolean isDisplayMode;

    private List<UrbanizationAddPhotoGalleryItem> finalImageList = new ArrayList<>();

    // -------------------------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------------------------
    public UrbanizationAddPhotoGallery(Context context) {
        super(context);
    }

    public UrbanizationAddPhotoGallery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // -------------------------------------------------------------------------
    //  Initialize
    // -------------------------------------------------------------------------
    public void initialize(final AppCompatActivity mActivity){

        // Inflate view.
        View inflate = inflate(mActivity, R.layout.view_urbanization_add_photo_gallery, this);

        // Get Views
        UrbanizationGalleryRecyclerView = inflate.findViewById(R.id.inalambrik_gallery_recyclerview);
        addNewImageButton               = inflate.findViewById(R.id.image_add);
        noPhotosFoundTextView           = inflate.findViewById(R.id.no_photos_found);

        // --------------------------------
        // Add Image Listener.
        // --------------------------------
        addNewImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDisplayMode) return;

                if(mPhotoAdapter.getItemCount() >= maxPhotosNum) {
                    Toast.makeText(getContext(), "¡Límite máximo de fotos excedido!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Calling "Add Photo" Dialog Fragment.
                addPhotoDialogFragment = UrbanizationAddPhotoGalleryAddPhotoDialogFragment.newInstance();
                addPhotoDialogFragment.show(mActivity.getSupportFragmentManager(), GalleryDialogFragmentTag);
                addPhotoDialogFragment.setAddPhotoListener(new UrbanizationAddPhotoGalleryAddPhotoDialogFragment.AddPhotoListener() {
                    @Override
                    public void addImage(UrbanizationAddPhotoGalleryItem imagen) {
                        UrbanizationGalleryViewModel.addImageToList(imagen);
                        mPhotoAdapter.notifyItemInserted(mPhotoAdapter.getItemCount()+1);
                        UrbanizationGalleryRecyclerView.smoothScrollToPosition(mPhotoAdapter.getItemCount());
                        mPhotoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        // --------------------------------
        //  Photo Recycler view.
        // --------------------------------
        mPhotoAdapter = new UrbanizationAddPhotoGalleryAdapter(mActivity);
        UrbanizationGalleryRecyclerView.setAdapter(mPhotoAdapter);
        UrbanizationGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL, false));

        // -----------------------------------------------------------
        // Recover an "Add Photo" Dialog fragment if there is anyone
        // -----------------------------------------------------------
        addPhotoDialogFragment = (UrbanizationAddPhotoGalleryAddPhotoDialogFragment) mActivity.getSupportFragmentManager().findFragmentByTag(GalleryDialogFragmentTag);
        if(addPhotoDialogFragment !=null){
            addPhotoDialogFragment.setAddPhotoListener(new UrbanizationAddPhotoGalleryAddPhotoDialogFragment.AddPhotoListener() {
                @Override
                public void addImage(UrbanizationAddPhotoGalleryItem imagen) {
                    if(isDisplayMode) return;
                    UrbanizationGalleryViewModel.addImageToList(imagen);
                    mPhotoAdapter.notifyItemInserted(mPhotoAdapter.getItemCount()+1);
                    UrbanizationGalleryRecyclerView.smoothScrollToPosition(mPhotoAdapter.getItemCount());
                    mPhotoAdapter.notifyDataSetChanged();
                }
            });
        }


        // --------------------------------
        //  ViewModel
        // --------------------------------
        UrbanizationGalleryViewModel = ViewModelProviders.of(mActivity).get(UrbanizationAddPhotoGalleryViewModel.class);
        mPhotoAdapter.setListener(new UrbanizationAddPhotoGalleryAdapter.OnPhotoClickListener() {
            @Override
            public void deleteImage(int mPosition) {
                if(isDisplayMode) return;
                UrbanizationGalleryViewModel.deleteImageFromList(mPosition);
                mPhotoAdapter.notifyItemRemoved(mPosition);
            }
        });


        // Create the observer which updates the UI.
        UrbanizationGalleryViewModel.getImageList().observe(mActivity, new Observer<List<UrbanizationAddPhotoGalleryItem>>() {
            @Override
            public void onChanged(@Nullable final List<UrbanizationAddPhotoGalleryItem> imageList) {

                // Get ImageList.
                finalImageList = imageList;

                // Set List in Adapter.
                mPhotoAdapter.setImageList(finalImageList);

                // Show/Hide RecyclerView or Not-Found message.
                if(imageList != null && imageList.size() > 0){
                    noPhotosFoundTextView.setVisibility(View.GONE);
                    UrbanizationGalleryRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    UrbanizationGalleryRecyclerView.setVisibility(View.GONE);
                    noPhotosFoundTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setInitialImageList(List<UrbanizationAddPhotoGalleryItem> imageList, boolean isDisplayMode){
        setDisplayMode(isDisplayMode);

        // Set Initial Image List.
        UrbanizationGalleryViewModel.setInitialImageList(imageList);
    }

    public void setDisplayMode(boolean isDisplayMode){
        // Set DisplayMode.
        this.isDisplayMode = isDisplayMode;
        refreshControls();
    }

    public void setMaxPhotosNum(int maxPhotosNum) {
        this.maxPhotosNum = maxPhotosNum;
    }

    public UrbanizationAddPhotoGalleryAddPhotoDialogFragment getAddPhotoDialogFragment() {
        return addPhotoDialogFragment;
    }

    private void refreshControls(){
        if(isDisplayMode)
            addNewImageButton.hide();
        else
            addNewImageButton.show();
    }

    // ---------------------------------------------------------------------------------
    //  Method to get all the attached/taken photos with their title and description.
    // ---------------------------------------------------------------------------------
    public List<UrbanizationAddPhotoGalleryItem> getFinalImageList(){
        return finalImageList!=null ? finalImageList : new ArrayList<UrbanizationAddPhotoGalleryItem>();
    }
}
