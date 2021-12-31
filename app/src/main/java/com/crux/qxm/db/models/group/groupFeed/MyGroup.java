
package com.crux.qxm.db.models.group.groupFeed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MyGroup implements Parcelable {

    @SerializedName("status")
    private String status;
    @SerializedName("group")
    private Group group;

    public MyGroup() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "MyGroup{" +
                "status='" + status + '\'' +
                ", group=" + group +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeParcelable(this.group, flags);
    }

    protected MyGroup(Parcel in) {
        this.status = in.readString();
        this.group = in.readParcelable(Group.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyGroup> CREATOR = new Parcelable.Creator<MyGroup>() {
        @Override
        public MyGroup createFromParcel(Parcel source) {
            return new MyGroup(source);
        }

        @Override
        public MyGroup[] newArray(int size) {
            return new MyGroup[size];
        }
    };
}
