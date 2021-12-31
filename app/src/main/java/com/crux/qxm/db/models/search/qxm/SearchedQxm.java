package com.crux.qxm.db.models.search.qxm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SearchedQxm implements Parcelable {

    @SerializedName("feedQxmId")
    private String qxmId;
    @SerializedName("feedTitle")
    private String qxmTitle;
    @SerializedName("feedCreatorName")
    private String qxmCreatorName;

    public String getQxmId() {
        return qxmId;
    }

    public void setQxmId(String qxmId) {
        this.qxmId = qxmId;
    }

    public String getQxmTitle() {
        return qxmTitle;
    }

    public void setQxmTitle(String qxmTitle) {
        this.qxmTitle = qxmTitle;
    }

    public String getQxmCreatorName() {
        return qxmCreatorName;
    }

    public void setQxmCreatorName(String qxmCreatorName) {
        this.qxmCreatorName = qxmCreatorName;
    }

    @Override
    public String toString() {
        return "SearchedQxm{" +
                "qxmId='" + qxmId + '\'' +
                ", qxmTitle='" + qxmTitle + '\'' +
                ", qxmCreatorName='" + qxmCreatorName + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.qxmId);
        dest.writeString(this.qxmTitle);
        dest.writeString(this.qxmCreatorName);
    }

    public SearchedQxm() {
    }

    protected SearchedQxm(Parcel in) {
        this.qxmId = in.readString();
        this.qxmTitle = in.readString();
        this.qxmCreatorName = in.readString();
    }

    public static final Parcelable.Creator<SearchedQxm> CREATOR = new Parcelable.Creator<SearchedQxm>() {
        @Override
        public SearchedQxm createFromParcel(Parcel source) {
            return new SearchedQxm(source);
        }

        @Override
        public SearchedQxm[] newArray(int size) {
            return new SearchedQxm[size];
        }
    };
}
