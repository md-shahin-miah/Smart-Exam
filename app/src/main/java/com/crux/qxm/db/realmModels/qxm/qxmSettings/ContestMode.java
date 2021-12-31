package com.crux.qxm.db.realmModels.qxm.qxmSettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ContestMode extends RealmObject implements Parcelable {

    private String id;
    @SerializedName("isContestModeEnabled")
    private boolean isContestModeEnabled;
    @SerializedName("contestStartTimeType")
    private String contestStartTimeType;
    @SerializedName("contestStartTime")
    private long contestStartTime;
    @SerializedName("contestEndTimeType")
    private String contestEndTimeType;
    @SerializedName("contestEndTime")
    private long contestEndTime;
    @SerializedName("numberOfWinners")
    private int numberOfWinners;
    @SerializedName("isRaffleDrawEnabled")
    private boolean isRaffleDrawEnabled;
    @Ignore
    @SerializedName("winnerSelectionPriorityRules")
    private ArrayList<String> winnerSelectionPriorityRules = null;
    private RealmList<String> winnerSelectionPriorityRulesLocal = null;
    @SerializedName("isParticipateAfterContestEnd")
    private boolean isParticipateAfterContestEnd;

    public ContestMode() {
    }

    public ContestMode(boolean isContestModeEnabled, String contestStartTimeType, long contestStartTime, String contestEndTimeType, long contestEndTime, int numberOfWinners, boolean isRaffleDrawEnabled, ArrayList<String> winnerSelectionPriorityRules, RealmList<String> winnerSelectionPriorityRulesLocal, boolean isParticipateAfterContestEnd) {
        this.isContestModeEnabled = isContestModeEnabled;
        this.contestStartTimeType = contestStartTimeType;
        this.contestStartTime = contestStartTime;
        this.contestEndTimeType = contestEndTimeType;
        this.contestEndTime = contestEndTime;
        this.numberOfWinners = numberOfWinners;
        this.isRaffleDrawEnabled = isRaffleDrawEnabled;
        this.winnerSelectionPriorityRules = winnerSelectionPriorityRules;
        this.winnerSelectionPriorityRulesLocal = winnerSelectionPriorityRulesLocal;
        this.isParticipateAfterContestEnd = isParticipateAfterContestEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isContestModeEnabled() {
        return isContestModeEnabled;
    }

    public void setContestModeEnabled(boolean contestModeEnabled) {
        isContestModeEnabled = contestModeEnabled;
    }

    public String getContestStartTimeType() {
        return contestStartTimeType;
    }

    public void setContestStartTimeType(String contestStartTimeType) {
        this.contestStartTimeType = contestStartTimeType;
    }

    public long getContestStartTime() {
        return contestStartTime;
    }

    public void setContestStartTime(long contestStartTime) {
        this.contestStartTime = contestStartTime;
    }

    public String getContestEndTimeType() {
        return contestEndTimeType;
    }

    public void setContestEndTimeType(String contestEndTimeType) {
        this.contestEndTimeType = contestEndTimeType;
    }

    public long getContestEndTime() {
        return contestEndTime;
    }

    public void setContestEndTime(long contestEndTime) {
        this.contestEndTime = contestEndTime;
    }

    public int getNumberOfWinners() {
        return numberOfWinners;
    }

    public void setNumberOfWinners(int numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }

    public boolean isRaffleDrawEnabled() {
        return isRaffleDrawEnabled;
    }

    public void setRaffleDrawEnabled(boolean raffleDrawEnabled) {
        isRaffleDrawEnabled = raffleDrawEnabled;
    }

    public ArrayList<String> getWinnerSelectionPriorityRules() {
        return winnerSelectionPriorityRules;
    }

    public void setWinnerSelectionPriorityRules(ArrayList<String> winnerSelectionPriorityRules) {
        this.winnerSelectionPriorityRules = winnerSelectionPriorityRules;
    }

    public RealmList<String> getWinnerSelectionPriorityRulesLocal() {
        return winnerSelectionPriorityRulesLocal;
    }

    public void setWinnerSelectionPriorityRulesLocal(RealmList<String> winnerSelectionPriorityRulesLocal) {
        this.winnerSelectionPriorityRulesLocal = winnerSelectionPriorityRulesLocal;
    }

    public boolean isParticipateAfterContestEnd() {
        return isParticipateAfterContestEnd;
    }

    public void setParticipateAfterContestEnd(boolean participateAfterContestEnd) {
        isParticipateAfterContestEnd = participateAfterContestEnd;
    }

    @Override
    public String toString() {
        return "ContestMode{" +
                "id='" + id + '\'' +
                ", isContestModeEnabled=" + isContestModeEnabled +
                ", contestStartTimeType='" + contestStartTimeType + '\'' +
                ", contestStartTime=" + contestStartTime +
                ", contestEndTimeType='" + contestEndTimeType + '\'' +
                ", contestEndTime=" + contestEndTime +
                ", numberOfWinners=" + numberOfWinners +
                ", isRaffleDrawEnabled=" + isRaffleDrawEnabled +
                ", winnerSelectionPriorityRules=" + winnerSelectionPriorityRules +
                ", winnerSelectionPriorityRulesLocal=" + winnerSelectionPriorityRulesLocal +
                ", isParticipateAfterContestEnd=" + isParticipateAfterContestEnd +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.isContestModeEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.contestStartTimeType);
        dest.writeLong(this.contestStartTime);
        dest.writeString(this.contestEndTimeType);
        dest.writeLong(this.contestEndTime);
        dest.writeInt(this.numberOfWinners);
        dest.writeByte(this.isRaffleDrawEnabled ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.winnerSelectionPriorityRules);
        dest.writeStringList(this.winnerSelectionPriorityRulesLocal);
        dest.writeByte(this.isParticipateAfterContestEnd ? (byte) 1 : (byte) 0);
    }

    protected ContestMode(Parcel in) {
        this.id = in.readString();
        this.isContestModeEnabled = in.readByte() != 0;
        this.contestStartTimeType = in.readString();
        this.contestStartTime = in.readLong();
        this.contestEndTimeType = in.readString();
        this.contestEndTime = in.readLong();
        this.numberOfWinners = in.readInt();
        this.isRaffleDrawEnabled = in.readByte() != 0;
        this.winnerSelectionPriorityRules = in.createStringArrayList();
        this.winnerSelectionPriorityRulesLocal = new RealmList<>();
        this.winnerSelectionPriorityRulesLocal.addAll(in.createStringArrayList());
        this.isParticipateAfterContestEnd = in.readByte() != 0;
    }

    public static final Creator<ContestMode> CREATOR = new Creator<ContestMode>() {
        @Override
        public ContestMode createFromParcel(Parcel source) {
            return new ContestMode(source);
        }

        @Override
        public ContestMode[] newArray(int size) {
            return new ContestMode[size];
        }
    };
}
