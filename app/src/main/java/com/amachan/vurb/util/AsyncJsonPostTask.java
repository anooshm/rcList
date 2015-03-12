package com.amachan.vurb.util;

/**
 * Created by anoosh on 3/10/15.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

/**
 * Async Task to communicate with the server in the background
 */
public class AsyncJsonPostTask extends AsyncTask<String, Void, JSONObject> {
    private TaskCallback mCallback;
    private Activity mActivity;
    private String LOGTAG = this.getClass().getName();
    public AsyncJsonPostTask(Activity activity, TaskCallback callback){
        mActivity = activity;
        mCallback = callback;
    }
    // add any loading animation here
    @Override
    protected void onPreExecute(){

    }
    // do the heavy communication in the background
    @Override
    protected JSONObject doInBackground(String... params){
        String url = params[0];
        JSONObject json = null;
        try{
            json = ConnectionHelper.postHttpsRequest(url);
        }catch(Exception ex){
            Log.d(LOGTAG, ex.toString());
        }

        return json;
    }
    // callback to the UI
    @Override
    protected void onPostExecute(JSONObject json){
        mCallback.process(json);
        return;
    }
}
