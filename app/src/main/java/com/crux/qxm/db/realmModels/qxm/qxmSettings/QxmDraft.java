package com.crux.qxm.db.realmModels.qxm.qxmSettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class QxmDraft extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @SerializedName("_id")
    private String qxmId;
    @SerializedName("questionSetName")
    private String questionSetTitle;
    @SerializedName("questionSetDescription")
    private String questionSetDescription;
    @SerializedName("questionSetThumbnail")
    private String questionSetThumbnail;
    @SerializedName("youtubeLink")
    private String youtubeLink;
    @SerializedName("questionStatus")
    private String questionStatus;
    @Ignore
    @SerializedName("questionCategory")
    private ArrayList<String> questionCategory;
    private String questionCategories;
    @SerializedName("questionSetJson")
    private String questionSetJson;
    @SerializedName("lastEditedAt")
    private String lastEditedAt;
    @SerializedName("createdAt")
    private String createdAt;


    public QxmDraft() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQxmId() {
        return qxmId;
    }

    public void setQxmId(String qxmId) {
        this.qxmId = qxmId;
    }

    public String getQuestionSetTitle() {
        return questionSetTitle;
    }

    public void setQuestionSetTitle(String questionSetTitle) {
        this.questionSetTitle = questionSetTitle;
    }

    public String getQuestionSetDescription() {
        return questionSetDescription;
    }

    public void setQuestionSetDescription(String questionSetDescription) {
        this.questionSetDescription = questionSetDescription;
    }

    public String getQuestionSetThumbnail() {
        if (questionSetThumbnail != null) {
            // as image might be from facebook or google as through social login
            if (questionSetThumbnail.contains("facebook") || questionSetThumbnail.contains("googleusercontent")
                    || questionSetThumbnail.contains("i.ytimg.com"))
                return questionSetThumbnail;

            return questionSetThumbnail;
        }
        return "";
    }

    public void setQuestionSetThumbnail(String questionSetThumbnail) {
        this.questionSetThumbnail = questionSetThumbnail;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public ArrayList<String> getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(ArrayList<String> questionCategory) {
        this.questionCategory = questionCategory;
    }

    public String getQuestionCategories() {
        return questionCategories;
    }

    public void setQuestionCategories(String questionCategories) {
        this.questionCategories = questionCategories;
    }

    public String getQuestionSetJson() {
        return questionSetJson;
    }

    public void setQuestionSetJson(String questionSetJson) {
        this.questionSetJson = questionSetJson;
    }

    public String getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(String lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "QxmDraft{" +
                "id='" + id + '\'' +
                ", qxmId='" + qxmId + '\'' +
                ", questionSetTitle='" + questionSetTitle + '\'' +
                ", questionSetDescription='" + questionSetDescription + '\'' +
                ", questionSetThumbnail='" + questionSetThumbnail + '\'' +
                ", youtubeLink='" + youtubeLink + '\'' +
                ", questionStatus='" + questionStatus + '\'' +
                ", questionCategory=" + questionCategory +
                ", questionCategories='" + questionCategories + '\'' +
                ", questionSetJson='" + questionSetJson + '\'' +
                ", lastEditedAt='" + lastEditedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.qxmId);
        dest.writeString(this.questionSetTitle);
        dest.writeString(this.questionSetDescription);
        dest.writeString(this.questionSetThumbnail);
        dest.writeString(this.youtubeLink);
        dest.writeString(this.questionStatus);
        dest.writeStringList(this.questionCategory);
        dest.writeString(this.questionCategories);
        dest.writeString(this.questionSetJson);
        dest.writeString(this.lastEditedAt);
        dest.writeString(this.createdAt);
    }

    protected QxmDraft(Parcel in) {
        this.id = in.readString();
        this.qxmId = in.readString();
        this.questionSetTitle = in.readString();
        this.questionSetDescription = in.readString();
        this.questionSetThumbnail = in.readString();
        this.youtubeLink = in.readString();
        this.questionStatus = in.readString();
        this.questionCategory = in.createStringArrayList();
        this.questionCategories = in.readString();
        this.questionSetJson = in.readString();
        this.lastEditedAt = in.readString();
        this.createdAt = in.readString();
    }

    public static final Creator<QxmDraft> CREATOR = new Creator<QxmDraft>() {
        @Override
        public QxmDraft createFromParcel(Parcel source) {
            return new QxmDraft(source);
        }

        @Override
        public QxmDraft[] newArray(int size) {
            return new QxmDraft[size];
        }
    };
}
