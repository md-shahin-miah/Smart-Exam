
package com.crux.qxm.db.models.group.groupFeed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyGroupFeed implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("myGroup")
    private List<MyGroup> myGroup;

    public MyGroupFeed() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MyGroup> getMyGroup() {
        return myGroup;
    }

    public void setMyGroup(List<MyGroup> myGroup) {
        this.myGroup = myGroup;
    }

    @Override
    public String toString() {
        return "MyGroupFeed{" +
                "id='" + id + '\'' +
                ", myGroup=" + myGroup +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeList(this.myGroup);
    }

    protected MyGroupFeed(Parcel in) {
        this.id = in.readString();
        this.myGroup = new ArrayList<MyGroup>();
        in.readList(this.myGroup, MyGroup.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyGroupFeed> CREATOR = new Parcelable.Creator<MyGroupFeed>() {
        @Override
        public MyGroupFeed createFromParcel(Parcel source) {
            return new MyGroupFeed(source);
        }

        @Override
        public MyGroupFeed[] newArray(int size) {
            return new MyGroupFeed[size];
        }
    };
}
