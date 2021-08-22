package com.example.santoriniapp.utils.urbanizationAddPhotoGallery;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.santoriniapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class JadblackPhotoViewer extends AppCompatActivity {

    private static final String IMAGE_PATH  = "IMAGE_PATH";

    public static Intent launchIntent(Context context, String imagePath)
    {
        Intent intent = new Intent(context, JadblackPhotoViewer.class);
        Bundle parms = new Bundle();
        parms.putString(IMAGE_PATH, imagePath);
        intent.putExtra("parms", parms);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jadblackcamerafield_layout);

        setupActionBar();

        // Get CustomerAddress register
        Bundle parms = getIntent().getBundleExtra("parms");
        final String imagePath     = parms.getString(IMAGE_PATH);
        Log.d("VALUES PHOTO VIEWER","ImagePath:" + imagePath);

        // Get Text and Image from the layout.
        final ProgressBar progressBar           = (ProgressBar) findViewById(R.id.progress_bar);
        final ImageView imageHolder             = (ImageView) findViewById(R.id.photo_holder);
        final TextView imageHolderText          = (TextView) findViewById(R.id.photo_holder_label);

        // Initially hide the No Photo label
        progressBar.setVisibility(View.VISIBLE);
        imageHolder.setVisibility(TextView.GONE);
        imageHolderText.setVisibility(TextView.GONE);

        Picasso.Builder picassoBuilder = new Picasso.Builder(this);
        picassoBuilder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });

        picassoBuilder.build()
                .load(imagePath)
                .error(R.drawable.ic_jadblack_error)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into(imageHolder, new Callback() {
                    @Override
                    public void onSuccess() {

                        progressBar.setVisibility(View.GONE);
                        imageHolderText.setVisibility(View.GONE);
                        imageHolder.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        imageHolder.setVisibility(View.GONE);
                        imageHolderText.setVisibility(View.VISIBLE);
                        String errorMessage = "Error al cargar imagen.";
                        if(imagePath.contains("http://"))
                            errorMessage += "\nAsegurarse de tener conexión a la red.";
                        imageHolderText.setText(errorMessage);
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void setupActionBar(){
        getSupportActionBar().setTitle("Imagen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
