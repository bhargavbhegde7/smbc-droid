package com.smbc.droid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comic {
    @Expose
    @SerializedName("index")
    public final Integer mIndex;

    @Expose
    @SerializedName("imageurl")
    public final String mImageUrl;

    @Expose
    @SerializedName("title")
    public final String mTitle;

    public Comic(Integer index, String imageUrl, String title) {
        mIndex = index;
        mImageUrl = imageUrl;
        mTitle = title;
    }
}
