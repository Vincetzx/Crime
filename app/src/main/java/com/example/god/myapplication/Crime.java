package com.example.god.myapplication;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by god on 2016/1/19.
 */
public class Crime {
    private static final String JSON_ID="id";
    private static final String JSON_TITLE="title";
    private static final String JSON_SOLVED="solved";
    private static final String JSON_DATE="date";
    private static final String JSON_PHOTO="photo";

    public Crime(JSONObject json) throws JSONException
    {
        mID=UUID.fromString(json.getString(JSON_ID));
        mTitle=json.getString(JSON_TITLE);
        mDate=new Date(json.getLong(JSON_DATE));
        mSolved=json.getBoolean(JSON_SOLVED);
        mPhoto=new Photo(json.getJSONObject(JSON_PHOTO));
    }

    public JSONObject toJson() throws JSONException
    {
        JSONObject json=new JSONObject();
        json.put(JSON_ID,mID);
        json.put(JSON_DATE,mDate);
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SOLVED,mSolved);
        json.put(JSON_PHOTO,mPhoto.toJSON());
        return json;
    }

    public Photo getPhoto()
    {
        return mPhoto;
    }

    public void setPhoto(Photo photo)
    {
        this.mPhoto=photo;
    }

    private Photo mPhoto;
    private UUID mID;
    private Date mDate;
    private boolean mSolved;
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public UUID getID() {
        return mID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Crime()
    {
        mDate=new Date();
        mID=UUID.randomUUID();

    }

}
