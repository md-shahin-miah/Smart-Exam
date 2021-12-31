package com.crux.qxm.db.models.notification.qxmNotification;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.questions.QuestionSet;
import com.google.gson.annotations.SerializedName;

public class QxmNotification implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("qxmCreatorId")
    private QxmCreator qxmCreator;
    @SerializedName("questionSet")
    private QuestionSet questionSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public QxmCreator getQxmCreator() {
        return qxmCreator;
    }

    public void setQxmCreator(QxmCreator qxmCreator) {
        this.qxmCreator = qxmCreator;
    }

    public QuestionSet getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(QuestionSet questionSet) {
        this.questionSet = questionSet;
    }

    @Override
    public String toString() {
        return "QxmNotification{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", qxmCreator=" + qxmCreator +
                ", questionSet=" + questionSet +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.qxmCreator, flags);
        dest.writeParcelable(this.questionSet, flags);
    }

    public QxmNotification() {
    }

    protected QxmNotification(Parcel in) {
        this.id = in.readString();
        this.createdAt = in.readString();
        this.qxmCreator = in.readParcelable(QxmCreator.class.getClassLoader());
        this.questionSet = in.readParcelable(QuestionSet.class.getClassLoader());
    }

    public static final Parcelable.Creator<QxmNotification> CREATOR = new Parcelable.Creator<QxmNotification>() {
        @Override
        public QxmNotification createFromParcel(Parcel source) {
            return new QxmNotification(source);
        }

        @Override
        public QxmNotification[] newArray(int size) {
            return new QxmNotification[size];
        }
    };
}
