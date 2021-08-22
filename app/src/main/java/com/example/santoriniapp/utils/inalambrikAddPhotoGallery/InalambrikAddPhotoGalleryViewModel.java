package  com.example.santoriniapp.utils.inalambrikAddPhotoGallery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class InalambrikAddPhotoGalleryViewModel extends ViewModel {


    private MutableLiveData<List<InalambrikAddPhotoGalleryItem>> mImageList;


    public InalambrikAddPhotoGalleryViewModel() {

    }

    public MutableLiveData<List<InalambrikAddPhotoGalleryItem>> getImageList() {
        if (mImageList == null) {
            mImageList = new MutableLiveData<>();
            mImageList.setValue(new ArrayList<InalambrikAddPhotoGalleryItem>());
        }
        return mImageList;
    }

    public void setInitialImageList(List<InalambrikAddPhotoGalleryItem> initialItemList){
        if (mImageList == null)
            mImageList = new MutableLiveData<>();
        mImageList.setValue(initialItemList != null ? initialItemList : new ArrayList<InalambrikAddPhotoGalleryItem>());
    }


    public void addImageToList(InalambrikAddPhotoGalleryItem imagen){
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
