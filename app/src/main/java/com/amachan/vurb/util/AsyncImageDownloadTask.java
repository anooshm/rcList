package com.amachan.vurb.util;


import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.amachan.vurb.R;

import java.lang.ref.WeakReference;

/**
 * Created by anoosh on 3/10/15.
 */

/**
 * Used to download image asynchronously to a specific imageView
 */
public class AsyncImageDownloadTask extends AsyncTask<String, Void, Drawable> {

    private final WeakReference<ImageView> imageViewReference;

    public AsyncImageDownloadTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    // Actual download method, run in the task thread
    protected Drawable doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        DrawableManager drawableManager = new DrawableManager();
        Drawable drawable = null;
        if(params[0] != null && !params[0].equals("null")) {
            drawable = drawableManager.fetchDrawable(params[0]);
        }
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if (isCancelled()) {
            drawable = null;
        }
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if(imageView !=null) {
                if(drawable != null){
                    imageView.setImageDrawable(drawable);
                }else {
                    // handle cases where you have to set the default image
                }
            }

        }
    }

}

