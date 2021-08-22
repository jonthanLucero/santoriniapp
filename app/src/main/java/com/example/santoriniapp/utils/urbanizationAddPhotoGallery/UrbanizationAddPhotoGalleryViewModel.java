package com.example.santoriniapp.utils.urbanizationAddPhotoGallery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class UrbanizationAddPhotoGalleryViewModel extends ViewModel {


    private MutableLiveData<List<UrbanizationAddPhotoGalleryItem>> mImageList;


    public UrbanizationAddPhotoGalleryViewModel() {

    }

    public MutableLiveData<List<UrbanizationAddPhotoGalleryItem>> getImageList() {
        if (mImageList == null) {
            mImageList = new MutableLiveData<>();
            mImageList.setValue(new ArrayList<UrbanizationAddPhotoGalleryItem>());
        }
        return mImageList;
    }

    public void setInitialImageList(List<UrbanizationAddPhotoGalleryItem> initialItemList){
        if (mImageList == null)
            mImageList = new MutableLiveData<>();
        mImageList.setValue(initialItemList != null ? initialItemList : new ArrayList<UrbanizationAddPhotoGalleryItem>());
    }


    public void addImageToList(UrbanizationAddPhotoGalleryItem imagen){
        if(mImageList.getValue()!=null){
            mImageList.getValue().add(imagen);
            mImageList.setValue(mImageList.getValue());
        }
    }

    public void deleteImageFromList(int position){
        if(mImageList.getValue()!=null){
            mImageList.getValue().remove(position);
            mImageList.setValue(mImageList.getValue());
        }
    }

}
