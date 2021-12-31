package com.crux.qxm.db.models.SignUp;

import io.realm.RealmObject;

public class SignUpStatus extends RealmObject {

    private boolean isSignUpTriggered;
    private boolean isUserProfilePicUploadSkipped;
    private boolean isUserInterestAndPreferredLanguageInputDone;

    public SignUpStatus() {
        isSignUpTriggered = false;
        isUserProfilePicUploadSkipped = false;
        isUserInterestAndPreferredLanguageInputDone = false;
    }

    public boolean isSignUpTriggered() {
        return isSignUpTriggered;
    }

    public void setSignUpTriggered(boolean signUpTriggered) {
        isSignUpTriggered = signUpTriggered;
    }

    public boolean isUserProfilePicUploadSkipped() {
        return isUserProfilePicUploadSkipped;
    }

    public void setUserProfilePicUploadSkipped(boolean userProfilePicUploadSkipped) {
        isUserProfilePicUploadSkipped = userProfilePicUploadSkipped;
    }

    public boolean isUserInterestAndPreferredLanguageInputDone() {
        return isUserInterestAndPreferredLanguageInputDone;
    }

    public void setUserInterestAndPreferredLanguageInputDone(boolean userInterestAndPreferredLanguageInputDone) {
        isUserInterestAndPreferredLanguageInputDone = userInterestAndPreferredLanguageInputDone;
    }

    @Override
    public String toString() {
        return "SignUpStatus{" +
                "isSignUpTriggered=" + isSignUpTriggered +
                ", isUserProfilePicUploadSkipped=" + isUserProfilePicUploadSkipped +
                ", isUserInterestAndPreferredLanguageInputDone=" + isUserInterestAndPreferredLanguageInputDone +
                '}';
    }
}
