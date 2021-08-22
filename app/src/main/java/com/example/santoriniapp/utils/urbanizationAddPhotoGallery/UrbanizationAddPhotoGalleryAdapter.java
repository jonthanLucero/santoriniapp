package com.example.santoriniapp.utils.urbanizationAddPhotoGallery;

import android.animation.Animator;
import android.content.Context;
import androidx.annotation.NonNull;
import com.example.santoriniapp.R;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UrbanizationAddPhotoGalleryAdapter extends RecyclerView.Adapter<com.example.santoriniapp.utils.urbanizationAddPhotoGallery.UrbanizationAddPhotoGalleryAdapter.GalleryViewHolder>  {

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<UrbanizationAddPhotoGalleryItem> mImagenList;
    private OnPhotoClickListener onPhotoClickListener;


    // -------------------------------------------------------------------------------
    //  Listener.
    // -------------------------------------------------------------------------------
    public interface OnPhotoClickListener {
        void deleteImage(int mPosition);
    }

    public void setListener(OnPhotoClickListener listener) {
        this.onPhotoClickListener = listener;
    }

    // -------------------------------------------------------------------------------
    //  Constructor
    // -------------------------------------------------------------------------------
    public UrbanizationAddPhotoGalleryAdapter(Context context) {
        layoutInflater  = LayoutInflater.from(context);
        mContext        = context;
    }

    // -------------------------------------------------------------------------------
    //  OnCreateViewHolder.
    // -------------------------------------------------------------------------------
    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item_urbanization_add_photo_gallery, parent, false);
        GalleryViewHolder viewHolder = new GalleryViewHolder(itemView);
        return viewHolder;
    }

    // -------------------------------------------------------------------------------
    //  OnBindViewHolder.
    // -------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        if (mImagenList != null) {
            UrbanizationAddPhotoGalleryItem item = mImagenList.get(position);
            holder.setData(item,position);
            holder.setListeners();
        }
    }

    @Override
    public int getItemCount() {
        if (mImagenList != null) return mImagenList.size();
        return 0;
    }

    // -------------------------------------------------------------------------------
    //  Setter/Getter items
    // -------------------------------------------------------------------------------
    public void setImageList(List<UrbanizationAddPhotoGalleryItem> mImagenList) {
        this.mImagenList=mImagenList;
        notifyDataSetChanged();
    }

    public List<UrbanizationAddPhotoGalleryItem> getImageList() {
        return mImagenList;
    }

    // -------------------------------------------------------------------------------
    //  GalleryViewHolder
    // -------------------------------------------------------------------------------
    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        private int mPosition;

        private boolean mIsDisplayMode;

        private FloatingActionButton deletePhotoButton;
        private ImageView imageItem;
        private TextView titleTextView;


        // Constructor
        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get Views.
            deletePhotoButton   = itemView.findViewById(R.id.delete_action);
            imageItem 	        = itemView.findViewById(R.id.inalambrik_photo_image_view);
            titleTextView 	    = itemView.findViewById(R.id.inalambrik_photo_title);
        }


        public void setData(UrbanizationAddPhotoGalleryItem imagen, int position) {

            String finalFilePath = "";
            String filePath = imagen.photoPath();
            if(filePath.contains("pedidos.inalambrik.com.ec"))
                finalFilePath = "https://".concat(filePath);
            else if(filePath.contains(UrbanizationConstants.APP_LOCAL_URL))
                finalFilePath = "http://".concat(filePath);
            else
                finalFilePath = "file://".concat(filePath);

            // Load Photo.
            Picasso.with(mContext)
                    .load(finalFilePath)
                    .resize(200, 200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .centerCrop() // is a cropping technique that scales the image so that it fills the requested bounds of the ImageView and then crops the extra.
                    .into(imageItem);

            this.titleTextView.setText(!imagen.photoTitle().isEmpty() ? imagen.photoTitle() : "Sin Título");
            this.mPosition      = position;
            this.mIsDisplayMode = imagen.isDisplayMode();



            // If it is Display Mode, then disable the delete button (hide it).
            if(mIsDisplayMode) {
                deletePhotoButton.hide();
                deletePhotoButton.setEnabled(false);
            }else {

                // Enable Add Photo button.
                deletePhotoButton.setEnabled(true);

                // Put some animation.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    deletePhotoButton.setScaleX(0);
                    deletePhotoButton.setScaleY(0);

                    final Interpolator interpolador = AnimationUtils.loadInterpolator(mContext,
                            android.R.interpolator.fast_out_slow_in);
                    deletePhotoButton.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .setInterpolator(interpolador)
                            .setDuration(200)
                            .setStartDelay(100)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    deletePhotoButton.animate()
                                            .scaleY(1)
                                            .scaleX(1)
                                            .setInterpolator(interpolador)
                                            .setDuration(100)
                                            .start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                }else {
                    // Animation cant be placed, just show the button.
                    deletePhotoButton.setScaleX(1);
                    deletePhotoButton.setScaleY(1);
                }
            }



        }


        public void setListeners() {
            deletePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhotoClickListener.deleteImage(mPosition);
                }
            });
        }


    }

}
