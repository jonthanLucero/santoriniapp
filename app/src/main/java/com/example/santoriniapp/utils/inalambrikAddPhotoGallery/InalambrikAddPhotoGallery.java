package com.example.santoriniapp.utils.inalambrikAddPhotoGallery;

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


public class InalambrikAddPhotoGallery extends LinearLayout{

    // Tag
    public final String TAG = "InalambrikGallery";

    // DialogFragment Tag.
    public final String GalleryDialogFragmentTag = "GalleryDescripFragment";

    // Views
    private RecyclerView inalambrikGalleryRecyclerView;
    private FloatingActionButton addNewImageButton;
    private TextView noPhotosFoundTextView;

    // Adapter
    private InalambrikAddPhotoGalleryAdapter mPhotoAdapter;

    // Control Max Photos
    private int maxPhotosNum = 5; // By Default 5 Photos as Maximum.
    private InalambrikAddPhotoGalleryAddPhotoDialogFragment addPhotoDialogFragment;


    private InalambrikAddPhotoGalleryViewModel inalambrikGalleryViewModel;

    private boolean isDisplayMode;

    private List<InalambrikAddPhotoGalleryItem> finalImageList = new ArrayList<>();

    // -------------------------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------------------------
    public InalambrikAddPhotoGallery(Context context) {
        super(context);
    }

    public InalambrikAddPhotoGallery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // -------------------------------------------------------------------------
    //  Initialize
    // -------------------------------------------------------------------------
    public void initialize(final AppCompatActivity mActivity){

        // Inflate view.
        View inflate = inflate(mActivity, R.layout.view_inalambrik_add_photo_gallery, this);

        // Get Views
        inalambrikGalleryRecyclerView 	= inflate.findViewById(R.id.inalambrik_gallery_recyclerview);
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
                addPhotoDialogFragment = InalambrikAddPhotoGalleryAddPhotoDialogFragment.newInstance();
                addPhotoDialogFragment.show(mActivity.getSupportFragmentManager(), GalleryDialogFragmentTag);
                addPhotoDialogFragment.setAddPhotoListener(new InalambrikAddPhotoGalleryAddPhotoDialogFragment.AddPhotoListener() {
                    @Override
                    public void addImage(InalambrikAddPhotoGalleryItem imagen) {
                        inalambrikGalleryViewModel.addImageToList(imagen);
                        mPhotoAdapter.notifyItemInserted(mPhotoAdapter.getItemCount()+1);
                        inalambrikGalleryRecyclerView.smoothScrollToPosition(mPhotoAdapter.getItemCount());
                        mPhotoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        // --------------------------------
        //  Photo Recycler view.
        // --------------------------------
        mPhotoAdapter = new InalambrikAddPhotoGalleryAdapter(mActivity);
        inalambrikGalleryRecyclerView.setAdapter(mPhotoAdapter);
        inalambrikGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL, false));

        // -----------------------------------------------------------
        // Recover an "Add Photo" Dialog fragment if there is anyone
        // -----------------------------------------------------------
        addPhotoDialogFragment = (InalambrikAddPhotoGalleryAddPhotoDialogFragment) mActivity.getSupportFragmentManager().findFragmentByTag(GalleryDialogFragmentTag);
        if(addPhotoDialogFragment !=null){
            addPhotoDialogFragment.setAddPhotoListener(new InalambrikAddPhotoGalleryAddPhotoDialogFragment.AddPhotoListener() {
                @Override
                public void addImage(InalambrikAddPhotoGalleryItem imagen) {
                    if(isDisplayMode) return;
                    inalambrikGalleryViewModel.addImageToList(imagen);
                    mPhotoAdapter.notifyItemInserted(mPhotoAdapter.getItemCount()+1);
                    inalambrikGalleryRecyclerView.smoothScrollToPosition(mPhotoAdapter.getItemCount());
                    mPhotoAdapter.notifyDataSetChanged();
                }
            });
        }


        // --------------------------------
        //  ViewModel
        // --------------------------------
        inalambrikGalleryViewModel = ViewModelProviders.of(mActivity).get(InalambrikAddPhotoGalleryViewModel.class);
        mPhotoAdapter.setListener(new InalambrikAddPhotoGalleryAdapter.OnPhotoClickListener() {
            @Override
            public void deleteImage(int mPosition) {
                if(isDisplayMode) return;
                inalambrikGalleryViewModel.deleteImageFromList(mPosition);
                mPhotoAdapter.notifyItemRemoved(mPosition);
            }
        });


        // Create the observer which updates the UI.
        inalambrikGalleryViewModel.getImageList().observe(mActivity, new Observer<List<InalambrikAddPhotoGalleryItem>>() {
            @Override
            public void onChanged(@Nullable final List<InalambrikAddPhotoGalleryItem> imageList) {

                // Get ImageList.
                finalImageList = imageList;

                // Set List in Adapter.
                mPhotoAdapter.setImageList(finalImageList);

                // Show/Hide RecyclerView or Not-Found message.
                if(imageList != null && imageList.size() > 0){
                    noPhotosFoundTextView.setVisibility(View.GONE);
                    inalambrikGalleryRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    inalambrikGalleryRecyclerView.setVisibility(View.GONE);
                    noPhotosFoundTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setInitialImageList(List<InalambrikAddPhotoGalleryItem> imageList, boolean isDisplayMode){
        setDisplayMode(isDisplayMode);

        // Set Initial Image List.
        inalambrikGalleryViewModel.setInitialImageList(imageList);
    }

    public void setDisplayMode(boolean isDisplayMode){
        // Set DisplayMode.
        this.isDisplayMode = isDisplayMode;
        refreshControls();
    }

    public void setMaxPhotosNum(int maxPhotosNum) {
        this.maxPhotosNum = maxPhotosNum;
    }

    public InalambrikAddPhotoGalleryAddPhotoDialogFragment getAddPhotoDialogFragment() {
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
    public List<InalambrikAddPhotoGalleryItem> getFinalImageList(){
        return finalImageList!=null ? finalImageList : new ArrayList<InalambrikAddPhotoGalleryItem>();
    }
}
