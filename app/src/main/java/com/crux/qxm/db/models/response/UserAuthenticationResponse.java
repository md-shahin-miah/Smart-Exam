package com.crux.qxm.db.models.response;

import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.google.gson.annotations.SerializedName;

public class UserAuthenticationResponse {
    @SerializedName("user")
    private UserBasic user;
    @SerializedName("QxmToken")
    private QxmToken token;

    public UserAuthenticationResponse() {
    }

    public UserAuthenticationResponse(UserBasic user, QxmToken token) {
        this.user = user;
        this.token = token;
    }

    public UserBasic getUser() {
        return user;
    }

    public void setUser(UserBasic user) {
        this.user = user;
    }

    public QxmToken getToken() {
        return token;
    }

    public void setToken(QxmToken token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserAuthenticationResponse{" +
                "user=" + user.toString() +
                ", token=" + token.toString() +
                '}';
    }
}
