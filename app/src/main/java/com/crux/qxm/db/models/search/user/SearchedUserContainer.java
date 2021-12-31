package com.crux.qxm.db.models.search.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchedUserContainer {

    @SerializedName("followingUser")
    private List<SearchedUser> meFollowingUserList;
    @SerializedName("otherUser")
    private List<SearchedUser> generalUserList;

    public List<SearchedUser> getMeFollowingUserList() {
        return meFollowingUserList;
    }

    public void setMeFollowingUserList(List<SearchedUser> meFollowingUserList) {
        this.meFollowingUserList = meFollowingUserList;
    }

    public List<SearchedUser> getGeneralUserList() {
        return generalUserList;
    }

    public void setGeneralUserList(List<SearchedUser> generalUserList) {
        this.generalUserList = generalUserList;
    }


}
