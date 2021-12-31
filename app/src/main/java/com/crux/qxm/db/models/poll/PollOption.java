package com.crux.qxm.db.models.poll;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PollOption implements Parcelable {

    @SerializedName("voteNum")
    private int voteNum;
    @SerializedName("title")
    private String title;
    @SerializedName("voter")
    private ArrayList<String> voter;
    @SerializedName("isMeParticatedOnPoll")
    private boolean isMeParticipatedOnPoll;

    public PollOption() {
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getVoter() {
        return voter;
    }

    public void setVoter(ArrayList<String> voter) {
        this.voter = voter;
    }

    public boolean isMeParticipatedOnPoll() {
        return isMeParticipatedOnPoll;
    }

    public void setMeParticipatedOnPoll(boolean meParticipatedOnPoll) {
        isMeParticipatedOnPoll = meParticipatedOnPoll;
    }

    @Override
    public String toString() {
        return "PollOption{" +
                "voteNum='" + voteNum + '\'' +
                ", title='" + title + '\'' +
                ", voter=" + voter +
                ", isMeParticipatedOnPoll=" + isMeParticipatedOnPoll +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.voteNum);
        dest.writeString(this.title);
        dest.writeStringList(this.voter);
        dest.writeByte(this.isMeParticipatedOnPoll ? (byte) 1 : (byte) 0);
    }

    protected PollOption(Parcel in) {
        this.voteNum = in.readInt();
        this.title = in.readString();
        this.voter = in.createStringArrayList();
        this.isMeParticipatedOnPoll = in.readByte() != 0;
    }

    public static final Creator<PollOption> CREATOR = new Creator<PollOption>() {
        @Override
        public PollOption createFromParcel(Parcel source) {
            return new PollOption(source);
        }

        @Override
        public PollOption[] newArray(int size) {
            return new PollOption[size];
        }
    };
}
