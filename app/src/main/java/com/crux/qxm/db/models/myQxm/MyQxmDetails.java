package com.crux.qxm.db.models.myQxm;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.questions.Question;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyQxmDetails implements Parcelable {

    @SerializedName("feedData")
    private FeedData myqxmFeedData;
    @SerializedName("question")
    private List<Question> questionList;

    @SerializedName("qxmStartScheduleDate")
    private String qxmStartScheduleDate;

    @SerializedName("qxmCurrentStatus")
    private String qxmCurrentStatus;

    @SerializedName("qxmSettings")
    private SingleQxmSettings singleQxmSettings;

    public FeedData getMyqxmFeedData() {
        return myqxmFeedData;
    }

    public void setMyqxmFeedData(FeedData myqxmFeedData) {
        this.myqxmFeedData = myqxmFeedData;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public String getQxmStartScheduleDate() {
        return qxmStartScheduleDate;
    }

    public void setQxmStartScheduleDate(String qxmStartScheduleDate) {
        this.qxmStartScheduleDate = qxmStartScheduleDate;
    }

    public String getQxmCurrentStatus() {
        return qxmCurrentStatus;
    }

    public void setQxmCurrentStatus(String qxmCurrentStatus) {
        this.qxmCurrentStatus = qxmCurrentStatus;
    }

    public SingleQxmSettings getSingleQxmSettings() {
        return singleQxmSettings;
    }

    public void setSingleQxmSettings(SingleQxmSettings singleQxmSettings) {
        this.singleQxmSettings = singleQxmSettings;
    }

    @Override
    public String toString() {
        return "MyQxmDetails{" +
                "myqxmFeedData=" + myqxmFeedData +
                ", questionList=" + questionList +
                ", qxmStartScheduleDate='" + qxmStartScheduleDate + '\'' +
                ", qxmCurrentStatus='" + qxmCurrentStatus + '\'' +
                ", singleQxmSettings=" + singleQxmSettings +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.myqxmFeedData, flags);
        dest.writeTypedList(this.questionList);
        dest.writeString(this.qxmStartScheduleDate);
        dest.writeString(this.qxmCurrentStatus);
        dest.writeParcelable(this.singleQxmSettings, flags);
    }

    public MyQxmDetails() {
    }

    protected MyQxmDetails(Parcel in) {
        this.myqxmFeedData = in.readParcelable(FeedData.class.getClassLoader());
        this.questionList = in.createTypedArrayList(Question.CREATOR);
        this.qxmStartScheduleDate = in.readString();
        this.qxmCurrentStatus = in.readString();
        this.singleQxmSettings = in.readParcelable(SingleQxmSettings.class.getClassLoader());
    }

    public static final Creator<MyQxmDetails> CREATOR = new Creator<MyQxmDetails>() {
        @Override
        public MyQxmDetails createFromParcel(Parcel source) {
            return new MyQxmDetails(source);
        }

        @Override
        public MyQxmDetails[] newArray(int size) {
            return new MyQxmDetails[size];
        }
    };
}
