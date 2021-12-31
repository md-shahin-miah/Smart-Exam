package com.crux.qxm.db.models.singleMCQ;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.performance.UserPerformance;
import com.crux.qxm.db.models.questions.MultipleChoice;
import com.google.gson.annotations.SerializedName;

public class ParticipateSingleMCQModel implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("questionSet")
    private MultipleChoice multipleChoice;
    @SerializedName("SingleMCQSettings")
    private SingleMCQSettings singleMCQSettings;
    @SerializedName("userPerformance")
    private UserPerformance userPerformance;
    @SerializedName("marks")
    private String marks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MultipleChoice getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(MultipleChoice multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public SingleMCQSettings getSingleMCQSettings() {
        return singleMCQSettings;
    }

    public void setSingleMCQSettings(SingleMCQSettings singleMCQSettings) {
        this.singleMCQSettings = singleMCQSettings;
    }

    public UserPerformance getUserPerformance() {
        return userPerformance;
    }

    public void setUserPerformance(UserPerformance userPerformance) {
        this.userPerformance = userPerformance;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "SingleMCQModel{" +
                "id='" + id + '\'' +
                ", multipleChoice=" + multipleChoice +
                ", singleMCQSettings=" + singleMCQSettings +
                ", userPerformance=" + userPerformance +
                ", marks='" + marks + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.multipleChoice, flags);
        dest.writeParcelable(this.singleMCQSettings, flags);
        dest.writeParcelable(this.userPerformance, flags);
        dest.writeString(this.marks);
    }

    public ParticipateSingleMCQModel() {
    }

    protected ParticipateSingleMCQModel(Parcel in) {
        this.id = in.readString();
        this.multipleChoice = in.readParcelable(MultipleChoice.class.getClassLoader());
        this.singleMCQSettings = in.readParcelable(SingleMCQSettings.class.getClassLoader());
        this.userPerformance = in.readParcelable(UserPerformance.class.getClassLoader());
        this.marks = in.readString();
    }

    public static final Creator<ParticipateSingleMCQModel> CREATOR = new Creator<ParticipateSingleMCQModel>() {
        @Override
        public ParticipateSingleMCQModel createFromParcel(Parcel source) {
            return new ParticipateSingleMCQModel(source);
        }

        @Override
        public ParticipateSingleMCQModel[] newArray(int size) {
            return new ParticipateSingleMCQModel[size];
        }
    };
}
