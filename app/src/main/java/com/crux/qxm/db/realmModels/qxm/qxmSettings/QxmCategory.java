package com.crux.qxm.db.realmModels.qxm.qxmSettings;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class QxmCategory extends RealmObject implements Parcelable {
    private String category;

    public QxmCategory() {
    }

    public QxmCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "QxmCategory{" +
                "category='" + category + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
    }

    protected QxmCategory(Parcel in) {
        this.category = in.readString();
    }

    public static final Parcelable.Creator<QxmCategory> CREATOR = new Parcelable.Creator<QxmCategory>() {
        @Override
        public QxmCategory createFromParcel(Parcel source) {
            return new QxmCategory(source);
        }

        @Override
        public QxmCategory[] newArray(int size) {
            return new QxmCategory[size];
        }
    };
}
