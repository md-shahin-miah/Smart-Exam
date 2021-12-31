package com.crux.qxm.db.models.menu.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyGroupDataInMenuContainer implements Parcelable {

    @SerializedName("_id")
    private String id;
    @SerializedName("myGroup")
    private List<MyGroupDataInMenu> myGroupDataInMenuList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MyGroupDataInMenu> getMyGroupDataInMenuList() {
        return myGroupDataInMenuList;
    }

    public void setMyGroupDataInMenuList(List<MyGroupDataInMenu> myGroupDataInMenuList) {
        this.myGroupDataInMenuList = myGroupDataInMenuList;

    }

    @Override
    public String toString() {
        return "MyGroupDataInMenuObject{" +
                "id='" + id + '\'' +
                ", myGroupDataInMenuList=" + myGroupDataInMenuList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(this.myGroupDataInMenuList);
    }

    public MyGroupDataInMenuContainer() {
    }

    protected MyGroupDataInMenuContainer(Parcel in) {
        this.id = in.readString();
        this.myGroupDataInMenuList = in.createTypedArrayList(MyGroupDataInMenu.CREATOR);
    }

    public static final Parcelable.Creator<MyGroupDataInMenuContainer> CREATOR = new Parcelable.Creator<MyGroupDataInMenuContainer>() {
        @Override
        public MyGroupDataInMenuContainer createFromParcel(Parcel source) {
            return new MyGroupDataInMenuContainer(source);
        }

        @Override
        public MyGroupDataInMenuContainer[] newArray(int size) {
            return new MyGroupDataInMenuContainer[size];
        }
    };
}
