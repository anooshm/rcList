package com.amachan.vurb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amachan.vurb.util.AsyncJsonPostTask;
import com.amachan.vurb.util.Constants;
import com.amachan.vurb.util.TaskCallback;

import org.json.JSONObject;

import java.util.List;

/**
 * Splash screen to show till the location and json is loaded.
 * Sometimes it is loaded very fast, so a screen is shown for 1 second
 */
public class SplashScreenActivity extends ActionBarActivity {
    // Logtag
    private static String LOGTAG = SplashScreenActivity.class.getName();
    //sharedPrefs
    private SharedPreferences mPrefs;
    // location related
    private LocationManager locationManager;
    private String provider;
    //Thread related
    private static int SPLASH_TIME_OUT = 1000;
    private Activity mActivity;
    // boolean to check whether the loading is done
    private boolean isLocationLoaded;
    private boolean isJsonLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mPrefs = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        isJsonLoaded = false;
        isLocationLoaded = false;
        mActivity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     * Fetch the location and json everytime the app starts
     */
    @Override
    public void onResume(){
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer.
             */
            @Override
            public void run() {
                String[] params = new String[1];
                params[0] = Constants.JSON_URL;
                new AsyncJsonPostTask(mActivity, new JsonCallback()).execute(params);
                getUserLocation();
            }
        }, SPLASH_TIME_OUT);

    }

    /**
     * Callback from asyncTask
     */
    private class JsonCallback implements TaskCallback {
        @Override
        public void process(JSONObject json){
            Log.d(LOGTAG, "json : " + json.toString());
            mPrefs.edit().putString("cards", json.toString()).commit();
            isJsonLoaded = true;
            if(isLocationLoaded){
                switchActivity();
            }
        }
    }

    private void getUserLocation() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            List<String> providers = locationManager.getProviders(true);

            for (int i = providers.size() - 1; i >= 0; i--) {
                location = locationManager.getLastKnownLocation(providers.get(i));
                if (location != null)
                    break;
            }
            if (location == null) {
                Toast.makeText(this, "Please turn ON the location services",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if(location != null){
            String latitude = Double.toString(location.getLatitude());
            String longitude = Double.toString(location.getLongitude());
            mPrefs.edit().putString("latitude", latitude).commit();
            mPrefs.edit().putString("longitude", longitude).commit();
            isLocationLoaded = true;
            if(isJsonLoaded){
                switchActivity();
            }
        }

    }

    /**
     * Open the MainActivity
     */
    private void switchActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
