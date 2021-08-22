package com.jadblack.Lienzo;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.andressantibanez.lienzo.LienzoView;

/**
 * Created by DESARROLLO on 12/8/2015.
 */
public class JadBlackLienzoView extends LienzoView {

    /**
     * Constructors
     */
    public JadBlackLienzoView(Context context) {
        super(context);
    }

    public JadBlackLienzoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JadBlackLienzoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Handle change
     */
    public void handleChange(Bundle data) {
        if(data == null)
            return;

        //Add Image
        if(data.containsKey("add_image")) {
            String imagePath = data.getString("add_image");
            Log.d("PHOTO CONTROL:","Se agrega:" + imagePath);
            addImage(imagePath);
        }

        //Load Images
        if(data.containsKey("load")) {
            loadImages();
        }

        //Clear Images
        if(data.containsKey("clear")) {
            clearImages();
        }

        //Set Visibility
        if(data.containsKey("visibility")) {
            int visibility = data.getInt("visibility", VISIBLE);
            if(visibility == VISIBLE)
                setVisibility(VISIBLE);
            else
                setVisibility(GONE);
        }
    }
}
