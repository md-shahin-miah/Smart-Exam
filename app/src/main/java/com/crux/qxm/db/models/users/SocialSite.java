package com.crux.qxm.db.models.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by frshafi on 2/14/18.
 */

public class SocialSite implements Parcelable {

    @SerializedName("facebook")
    private List<String> facebookList;
    @SerializedName("instragram")
    private List<String> instragramList;
    @SerializedName("linkedin")
    private List<String> linkedinList;
    @SerializedName("youtube")
    private List<String> youtubeList;
    @SerializedName("twitter")
    private List<String> twitterList;


    // constructor with arguments
    public SocialSite(List<String> facebookList, List<String> instragramList, List<String> linkedinList, List<String> youtubeList,List<String> twitterList) {
        this.facebookList = facebookList;
        this.instragramList = instragramList;
        this.linkedinList = linkedinList;
        this.youtubeList = youtubeList;
        this.twitterList = twitterList;
    }

    // empty constructor
    public SocialSite() {
    }


    public List<String> getFacebookList() {
        return facebookList;
    }

    public void setFacebookList(List<String> facebookList) {
        this.facebookList = facebookList;
    }

    public List<String> getInstragramList() {
        return instragramList;
    }

    public void setInstragramList(List<String> instragramList) {
        this.instragramList = instragramList;
    }

    public List<String> getLinkedinList() {
        return linkedinList;
    }

    public void setLinkedinList(List<String> linkedinList) {
        this.linkedinList = linkedinList;
    }

    public List<String> getYoutubeList() {
        return youtubeList;
    }

    public void setYoutubeList(List<String> youtubeList) {
        this.youtubeList = youtubeList;
    }

    public List<String> getTwitterList() {
        return twitterList;
    }
    public void setTwitterList(List<String> twitterList) {
        this.twitterList = twitterList;
    }

    @Override
    public String toString() {
        return "SocialSite{" +
                "facebookList=" + facebookList +
                ", instragramList=" + instragramList +
                ", linkedinList=" + linkedinList +
                ", youtubeList=" + youtubeList +
                ", twitterList=" + twitterList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.facebookList);
        dest.writeStringList(this.instragramList);
        dest.writeStringList(this.linkedinList);
        dest.writeStringList(this.youtubeList);
        dest.writeStringList(this.twitterList);
    }

    protected SocialSite(Parcel in) {
        this.facebookList = in.createStringArrayList();
        this.instragramList = in.createStringArrayList();
        this.linkedinList = in.createStringArrayList();
        this.youtubeList = in.createStringArrayList();
        this.twitterList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SocialSite> CREATOR = new Parcelable.Creator<SocialSite>() {
        @Override
        public SocialSite createFromParcel(Parcel source) {
            return new SocialSite(source);
        }

        @Override
        public SocialSite[] newArray(int size) {
            return new SocialSite[size];
        }
    };
}
