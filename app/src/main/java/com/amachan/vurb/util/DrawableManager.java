package com.amachan.vurb.util;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anoosh on 3/10/15.
 */
public class DrawableManager {


    private final Map<String, Drawable> drawableMap;
    private String LOGTAG = getClass().getSimpleName();

    public DrawableManager() {
        drawableMap = new HashMap<String, Drawable>();
    }

    public Drawable fetchDrawable(String urlString) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }
        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = Drawable.createFromStream(is, "src");


            if (drawable != null) {
                drawableMap.put(urlString, drawable);
                Log.d(LOGTAG, "got a thumbnail drawable: " + drawable.getBounds() + ", "
                        + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                        + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
            } else {
                Log.d(LOGTAG, "could not get thumbnail");
            }

            return drawable;
        } catch (MalformedURLException e) {
            Log.d(LOGTAG, "fetchDrawable failed");
            return null;
        } catch (IOException e) {
            Log.d(LOGTAG, "fetchDrawable failed");
            return null;
        }
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }


}
