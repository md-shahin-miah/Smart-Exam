package com.crux.qxm.utils;

import android.text.TextUtils;

public class QxmUrlHelper {

    public QxmUrlHelper() {
    }

    // region getUserProfileUrl
    public String getUserProfileUrl(String userId) {
        if (!TextUtils.isEmpty(userId))
            return String.format("https://www.myqxm.com/user/%s", userId);
        else
            return "";
    }
    // endregion

    // region getQxmUrl
    public String getQxmUrl(String qxmId) {
        if (!TextUtils.isEmpty(qxmId))
            return String.format("https://www.myqxm.com/qxm/%s", qxmId);
        else
            return "";
    }
    // endregion

    // region GetGroupUrl
    public String getGroupUrl(String groupId) {
        if (!TextUtils.isEmpty(groupId))
            return String.format("https://www.myqxm.com/group/%s", groupId);
        else
            return "";
    }
    // endregion

    // region getSingleMCQUrl
    public String getSingleMCQUrl(String singleMCQId) {
        if (!TextUtils.isEmpty(singleMCQId))
            return String.format("https://www.myqxm.com/singleMCQ/%s", singleMCQId);
        else
            return "";
    }

    public String getPollUrl(String pollId) {
        if (!TextUtils.isEmpty(pollId))
            return String.format("https://www.myqxm.com/poll/%s", pollId);
        else
            return "";

    }
    // endregion

}
