package com.crux.qxm.db.realmModels.apiToken;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QxmToken extends RealmObject {

    @PrimaryKey
    private String _id = UUID.randomUUID().toString();
    @SerializedName("userId")
    private String userId;
    @SerializedName("token")
    private String token;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("email")
    private String email;
    @SerializedName("socialId")
    private String socialId;


    public QxmToken() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return "qxm " + token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    @Override
    public String toString() {
        return "QxmToken{" +
                "_id='" + _id + '\'' +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", email='" + email + '\'' +
                ", socialId='" + socialId + '\'' +
                '}';
    }
}
