package com.crux.qxm.db.models.users;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * pojo class for Institution
 */

public class Institution implements Comparable, Parcelable {


    @SerializedName("_id")
    private String id;
    @SerializedName("institute")
    private String instituteName;
    @SerializedName("fieldOfStudy")
    private String fieldOfStudy;
    @SerializedName("degree")
    private String degree;
    @SerializedName("grade")
    private String grade;
    @SerializedName("fromTo")
    private String fromTo;

    public Institution() {
    }

    public Institution(String id, String instituteName, String fieldOfStudy, String degree, String grade, String fromTo) {
        this.id = id;
        this.instituteName = instituteName;
        this.fieldOfStudy = fieldOfStudy;
        this.degree = degree;
        this.grade = grade;
        this.fromTo = fromTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFromTo() {
        return fromTo;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + id + '\'' +
                ", instituteName='" + instituteName + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", degree='" + degree + '\'' +
                ", grade='" + grade + '\'' +
                ", fromTo='" + fromTo + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object obj) {

        Institution institution1 = (Institution) obj;

        String studyYear1 = institution1.getFromTo();
        String studyYear2 = this.getFromTo();

        String sy1Arr1[] = studyYear1.split("-");
        String sy1Arr2[] = studyYear2.split("-");

        int sy1Int1 = Integer.parseInt(sy1Arr1[1]);
        int sy1Int2 = Integer.parseInt(sy1Arr2[1]);

        return Integer.compare(sy1Int2, sy1Int1);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.instituteName);
        dest.writeString(this.fieldOfStudy);
        dest.writeString(this.degree);
        dest.writeString(this.grade);
        dest.writeString(this.fromTo);
    }

    protected Institution(Parcel in) {
        this.id = in.readString();
        this.instituteName = in.readString();
        this.fieldOfStudy = in.readString();
        this.degree = in.readString();
        this.grade = in.readString();
        this.fromTo = in.readString();
    }

    public static final Creator<Institution> CREATOR = new Creator<Institution>() {
        @Override
        public Institution createFromParcel(Parcel source) {
            return new Institution(source);
        }

        @Override
        public Institution[] newArray(int size) {
            return new Institution[size];
        }
    };
}
