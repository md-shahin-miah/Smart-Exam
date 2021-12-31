package com.crux.qxm.db.models.myQxm;

import android.os.Parcel;
import android.os.Parcelable;

import com.crux.qxm.db.models.evaluation.Participator;
import com.google.gson.annotations.SerializedName;

public class ParticipatorContainer implements Parcelable {

    @SerializedName("_id")
    private String participationId;
    @SerializedName("participator")
    private Participator participator;

    public String getParticipationId() {
        return participationId;
    }

    public void setParticipationId(String participationId) {
        this.participationId = participationId;
    }

    public Participator getParticipator() {
        return participator;
    }

    public void setParticipator(Participator participator) {
        this.participator = participator;
    }

    @Override
    public String toString() {
        return "ParticipatorContainer{" +
                "participationId='" + participationId + '\'' +
                ", participator=" + participator +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.participationId);
        dest.writeParcelable(this.participator, flags);
    }

    public ParticipatorContainer() {
    }

    protected ParticipatorContainer(Parcel in) {
        this.participationId = in.readString();
        this.participator = in.readParcelable(Participator.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParticipatorContainer> CREATOR = new Parcelable.Creator<ParticipatorContainer>() {
        @Override
        public ParticipatorContainer createFromParcel(Parcel source) {
            return new ParticipatorContainer(source);
        }

        @Override
        public ParticipatorContainer[] newArray(int size) {
            return new ParticipatorContainer[size];
        }
    };
}
