package com.crux.qxm.db.models.feed;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedDataNotInterested implements Parcelable {
    private FeedDataCauseOfNotInterested causeOfNotInterested;

    public FeedDataNotInterested() {
    }

    public FeedDataNotInterested(FeedDataCauseOfNotInterested causeOfNotInterested) {
        this.causeOfNotInterested = causeOfNotInterested;
    }

    public FeedDataCauseOfNotInterested getCauseOfNotInterested() {
        return causeOfNotInterested;
    }

    public void setCauseOfNotInterested(FeedDataCauseOfNotInterested causeOfNotInterested) {
        this.causeOfNotInterested = causeOfNotInterested;
    }

    @Override
    public String toString() {
        return "FeedDataNotInterested{" +
                "causeOfNotInterested=" + causeOfNotInterested +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.causeOfNotInterested, flags);
    }

    protected FeedDataNotInterested(Parcel in) {
        this.causeOfNotInterested = in.readParcelable(FeedDataCauseOfNotInterested.class.getClassLoader());
    }

    public static final Creator<FeedDataNotInterested> CREATOR = new Creator<FeedDataNotInterested>() {
        @Override
        public FeedDataNotInterested createFromParcel(Parcel source) {
            return new FeedDataNotInterested(source);
        }

        @Override
        public FeedDataNotInterested[] newArray(int size) {
            return new FeedDataNotInterested[size];
        }
    };
}
