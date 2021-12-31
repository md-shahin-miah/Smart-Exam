package com.crux.qxm.db.models.notification;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.evaluation.ParticipatedQxm;
import com.crux.qxm.db.models.evaluation.Participator;
import com.google.gson.annotations.SerializedName;

public class EvaluationNotification implements Parcelable {
    @SerializedName("_id")
    private String id;
    @SerializedName("participatedAt")
    private String participatedAt;
    @SerializedName("participatedQxm")
    private ParticipatedQxm participatedQxm;
    @SerializedName("participator")
    private Participator participator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticipatedAt() {
        return participatedAt;
    }

    public void setParticipatedAt(String participatedAt) {
        this.participatedAt = participatedAt;
    }

    public ParticipatedQxm getParticipatedQxm() {
        return participatedQxm;
    }

    public void setParticipatedQxm(ParticipatedQxm participatedQxm) {
        this.participatedQxm = participatedQxm;
    }

    public Participator getParticipator() {
        return participator;
    }

    public void setParticipator(Participator participator) {
        this.participator = participator;
    }

    @Override
    public String toString() {
        return "EvaluationNotification{" +
                "id='" + id + '\'' +
                ", participatedAt='" + participatedAt + '\'' +
                ", participatedQxm=" + participatedQxm +
                ", participator=" + participator +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.participatedAt);
        dest.writeParcelable(this.participatedQxm, flags);
        dest.writeParcelable(this.participator, flags);
    }

    public EvaluationNotification() {
    }

    protected EvaluationNotification(Parcel in) {
        this.id = in.readString();
        this.participatedAt = in.readString();
        this.participatedQxm = in.readParcelable(ParticipatedQxm.class.getClassLoader());
        this.participator = in.readParcelable(Participator.class.getClassLoader());
    }

    public static final Parcelable.Creator<EvaluationNotification> CREATOR = new Parcelable.Creator<EvaluationNotification>() {
        @Override
        public EvaluationNotification createFromParcel(Parcel source) {
            return new EvaluationNotification(source);
        }

        @Override
        public EvaluationNotification[] newArray(int size) {
            return new EvaluationNotification[size];
        }
    };
}
