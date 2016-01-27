package com.example.god.myapplication;

import android.util.MutableFloat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by god on 2016/1/25.
 */
public class Photo {
    private static final String JSON_FILENAME="filename";
    private String mFileName;
    public Photo(String fileName)
    {
        this.mFileName=fileName;
    }
    public Photo(JSONObject json) throws JSONException
    {
        mFileName=json.getString(JSON_FILENAME);
    }
    public JSONObject toJSON() throws JSONException
    {
        JSONObject json=new JSONObject();
        json.put(JSON_FILENAME,mFileName);
        return json;
    }
    public String getFileName()
    {
        return mFileName;
    }
}
