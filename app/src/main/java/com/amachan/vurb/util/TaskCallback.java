package com.amachan.vurb.util;

import org.json.JSONObject;

/**
 * Created by anoosh on 3/10/15.
 */

/**
 * Implement interface to get callback from the asynctask
 */
public interface TaskCallback {
    public void process(JSONObject json);
}
