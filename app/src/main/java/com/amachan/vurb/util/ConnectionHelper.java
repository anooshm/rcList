package com.amachan.vurb.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by anoosh on 3/10/15.
 */

/**
 * Communicate with the external server
 * All the put, post, get method have to be here
 */
public class ConnectionHelper {
    /**
     * Post to the 'https' server and get the response.
     * Also converts the response to JSON
     * Throws exception if json is not well formed or json is null
     * @param url
     * @return
     * @throws Exception
     */
    public static JSONObject postHttpsRequest(String url) throws Exception{
        URL urlObj = new URL(url);
        HttpsURLConnection https = (HttpsURLConnection) urlObj.openConnection();
        https.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(https.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        // convert to json
        JSONObject json = null;
        try {
            json = new JSONObject(sb.toString());
        } catch (JSONException je) {
            throw new RuntimeException(je);
        }
        return json;
    }
}
