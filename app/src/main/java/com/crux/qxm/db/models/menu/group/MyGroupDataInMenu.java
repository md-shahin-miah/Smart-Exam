package com.crux.qxm.db.models.menu.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MyGroupDataInMenu implements Parcelable {

    @SerializedName("status")
    private String myStatusInGroup;
    @SerializedName("group")
    private Group group;

    public String getMyStatusInGroup() {
        return myStatusInGroup;
    }

    public void setMyStatusInGroup(String myStatusInGroup) {
        this.myStatusInGroup = myStatusInGroup;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "MyGroupData{" +
                "myStatusInGroup='" + myStatusInGroup + '\'' +
                ", group=" + group +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.myStatusInGroup);
        dest.writeParcelable(this.group, flags);
    }

    public MyGroupDataInMenu() {
    }

    protected MyGroupDataInMenu(Parcel in) {
        this.myStatusInGroup = in.readString();
        this.group = in.readParcelable(Group.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyGroupDataInMenu> CREATOR = new Parcelable.Creator<MyGroupDataInMenu>() {
        @Override
        public MyGroupDataInMenu createFromParcel(Parcel source) {
            return new MyGroupDataInMenu(source);
        }

        @Override
        public MyGroupDataInMenu[] newArray(int size) {
            return new MyGroupDataInMenu[size];
        }
    };
}
