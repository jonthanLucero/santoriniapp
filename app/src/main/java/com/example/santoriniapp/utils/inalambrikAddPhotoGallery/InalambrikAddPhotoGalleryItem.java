package  com.example.santoriniapp.utils.inalambrikAddPhotoGallery;

public class InalambrikAddPhotoGalleryItem {

    private String photoTitle;
    private String photoDescription;
    private String photoPath;
    private String photoBase64;

    private boolean isDisplayMode;

    public InalambrikAddPhotoGalleryItem(String photoTitle, String photoDescription, String photoPath, boolean isDisplayMode) {
        this.photoTitle         = photoTitle;
        this.photoDescription   = photoDescription;
        this.photoPath          = photoPath;
        this.isDisplayMode      = isDisplayMode;
    }

    // ---------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------
    public String photoTitle() {
        return photoTitle.trim();
    }

    public String photoDescription() {
        return photoDescription.trim();
    }

    public String photoPath() {
        return photoPath;
    }

    public String getPhotoBase64()
    {
        return photoBase64;
    }

    public void setPhotoBase64(String base64)
    {
        photoBase64 = base64;
    }

    public void setIsDisplayMode(boolean displayMode)
    {
        isDisplayMode = displayMode;
    }

    public boolean isDisplayMode(){ return isDisplayMode;}

    @Override
    public String toString() {
        return "Imagenes{" +
                "titulo='" + photoTitle + '\'' +
                ", descripcion='" + photoDescription + '\'' +
                ", url='" + photoPath + '\'' +
                '}';
    }
}
