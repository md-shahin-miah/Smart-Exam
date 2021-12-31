package com.crux.qxm.db.models.createQxm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class YoutubeModel implements Parcelable {

    @SerializedName("title")
    private String youtubeVideoTitle;
    @SerializedName("thumbnail_url")
    private String thumbnailURL;

    public String getYoutubeVideoTitle() {
        return youtubeVideoTitle;
    }

    public void setYoutubeVideoTitle(String youtubeVideoTitle) {
        this.youtubeVideoTitle = youtubeVideoTitle;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return "YoutubeModel{" +
                "youtubeVideoTitle='" + youtubeVideoTitle + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.youtubeVideoTitle);
        dest.writeString(this.thumbnailURL);
    }

    public YoutubeModel() {
    }

    protected YoutubeModel(Parcel in) {
        this.youtubeVideoTitle = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Creator<YoutubeModel> CREATOR = new Creator<YoutubeModel>() {
        @Override
        public YoutubeModel createFromParcel(Parcel source) {
            return new YoutubeModel(source);
        }

        @Override
        public YoutubeModel[] newArray(int size) {
            return new YoutubeModel[size];
        }
    };
}
