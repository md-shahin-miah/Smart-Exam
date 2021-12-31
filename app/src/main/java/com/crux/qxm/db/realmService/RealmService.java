package com.crux.qxm.db.realmService;

import android.util.Log;

import com.crux.qxm.db.models.SignUp.SignUpStatus;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.db.realmModels.apiToken.QxmTokenFields;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraft;
import com.crux.qxm.db.realmModels.qxm.qxmSettings.QxmDraftFields;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmModels.user.UserBasicFields;
import com.crux.qxm.db.realmModels.userSessionTracker.UserActivitySessionTracker;
import com.crux.qxm.utils.QxmDateHelper;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmService {

    private static final String TAG = "RealmService";

    private Realm realm;

    //region RealmService-Constructor
    public RealmService(Realm realm) {
        this.realm = realm;
    }
    //endregion

    // region User

    public void addUser(UserBasic user) {

        realm.executeTransaction(realm1 -> {
            UserBasic userBasic = realm1.where(UserBasic.class).findFirst();
            if (userBasic != null) {
                userBasic.setFirstName(user.getFirstName());
                userBasic.setLastName(user.getLastName());
                userBasic.setFullName(user.getFullName());
                userBasic.setUserName(user.getUserName());
                userBasic.setEmail(user.getEmail());
                userBasic.setProfilePic(user.getProfilePic());
                userBasic.setVerified(user.isVerified());
                userBasic.setAccountProvider(user.getAccountProvider());
                Log.d(TAG, "addUser userBasic: "+userBasic);

            } else {
                realm1.insertOrUpdate(user);
                Log.d(TAG, "addUser userBasic: "+user);
            }
        });

    }

    public void addUser(UserBasic user,String imageUrl) {

        realm.executeTransaction(realm1 -> {
            UserBasic userBasic = realm1.where(UserBasic.class).findFirst();
            if (userBasic != null) {
                userBasic.setFirstName(user.getFirstName());
                userBasic.setLastName(user.getLastName());
                userBasic.setFullName(user.getFullName());
                userBasic.setUserName(user.getUserName());
                userBasic.setEmail(user.getEmail());
                userBasic.setProfilePic(imageUrl);
                userBasic.setVerified(user.isVerified());
                userBasic.setAccountProvider(user.getAccountProvider());
                Log.d(TAG, "addUser userBasic: "+userBasic);

            } else {
                realm1.insertOrUpdate(user);
                Log.d(TAG, "addUser userBasic: "+user);
            }
        });

    }

    public void setUserProfileImage(UserBasic user) {

        realm.executeTransactionAsync(realm1 -> {

            UserBasic userBasic = realm1.where(UserBasic.class).findFirst();
            if (userBasic != null) {
                userBasic.setProfilePic(user.getProfilePic());
            } else {
                realm1.insertOrUpdate(user);
            }
        });
    }

    public void setUserProfileImage(UserBasic user, String profileImageURL) {

        realm.executeTransactionAsync(realm1 -> {

            UserBasic userBasic = realm1.where(UserBasic.class).findFirst();
            if (userBasic != null) {
                userBasic.setProfilePic(profileImageURL);
            } else {
                realm1.insertOrUpdate(user);
            }
        });
    }

    public void deleteAll() {

        realm.executeTransaction(realm1 -> {
            RealmResults user = realm1.where(UserBasic.class).findAll();
            user.deleteAllFromRealm();

            RealmResults token = realm1.where(QxmToken.class).findAll();
            token.deleteAllFromRealm();

            RealmResults drafts = realm1.where(QxmDraft.class).findAll();
            drafts.deleteLastFromRealm();

            RealmResults session = realm1.where(UserActivitySessionTracker.class).findAll();
            session.deleteLastFromRealm();

            // for more assure
            realm1.deleteAll();

        });
    }

    public UserBasic getSavedUser() {
        return realm.where(UserBasic.class).findFirst();
    }

    public boolean isUserIdMatched(String userId) {
        return realm.where(UserBasic.class).equalTo(UserBasicFields.USER_ID, userId).findFirst() != null;
    }

    public void deleteQxmToken() {
        realm.executeTransaction(realm1 -> {
            RealmResults qxmToken = realm1.where(QxmToken.class).findAll();
            qxmToken.deleteAllFromRealm();

            RealmResults signUpStatus = realm1.where(SignUpStatus.class).findAll();
            signUpStatus.deleteAllFromRealm();
        });
    }

    public void updateUserEmail(String userId, String updatedUserEmail) {

        UserBasic userBasic = realm.where(UserBasic.class).equalTo(UserBasicFields.USER_ID, userId).findFirst();
        if (userBasic != null) {
            userBasic.setEmail(updatedUserEmail);
        }
    }

    // endregion

    // region ApiToken

    public void saveToken(QxmToken qxmToken) {
        QxmToken token = realm.where(QxmToken.class).findFirst();

        if (token == null) {
            realm.executeTransaction(realm1 -> realm1.insert(qxmToken));
        } else {
            realm.executeTransaction(realm1 -> {
                qxmToken.set_id(token.get_id());
                realm1.insertOrUpdate(qxmToken);
            });
        }
    }

    public void deleteApiToken() {
        RealmResults<QxmToken> qxmTokens = realm.where(QxmToken.class).findAll();
        realm.executeTransaction(realm1 -> qxmTokens.deleteAllFromRealm());
    }

    public void updateApiToken(String id, String newApiToken) {

        QxmToken qxmToken = realm.where(QxmToken.class).equalTo(QxmTokenFields._ID, id).findFirst();
        if (qxmToken != null) {
            qxmToken.setToken(newApiToken);
        }
    }

    public void updateRefreshToken(String id, String newRefreshToken) {

        QxmToken qxmToken = realm.where(QxmToken.class).equalTo(QxmTokenFields._ID, id).findFirst();
        if (qxmToken != null) {
            qxmToken.setRefreshToken(newRefreshToken);
        }

    }

    public QxmToken getApiToken() {
        return realm.where(QxmToken.class).findFirst();
    }


    // endregion

    //region SaveQxmAsDraft
    public void saveQxmAsDraft(QxmDraft qxmDraft) {
        realm.executeTransactionAsync(realm1 -> realm1.insertOrUpdate(qxmDraft));
    }
    //endregion

    //region GetQxmDrafts
    public List<QxmDraft> getQxmDrafts() {
//        List<QxmDraft> qxmDraftList = realm.where(QxmDraft.class).findAll();
//        return realm.copyFromRealm(qxmDraftList);
        return realm.where(QxmDraft.class)
                .sort(QxmDraftFields.LAST_EDITED_AT, Sort.DESCENDING)
                .findAll();
    }
    //endregion

    //region DeleteDraftedQxm
    public void deleteDraftedQxm(QxmDraft qxmDraft) {

        realm.executeTransaction(realm1 -> {
            if (qxmDraft != null) {
                qxmDraft.deleteFromRealm();
            }
        });
    }

    public void deleteDraftedQxmById(String id){
        QxmDraft draft = realm.where(QxmDraft.class).equalTo(QxmDraftFields.ID, id).findFirst();

        realm.executeTransaction(realm1 -> {
            if (draft != null) {
                draft.deleteFromRealm();
            }
        });
    }
    //endregion

    // region SaveSignUpStatus
    public void saveSignUpStatus(SignUpStatus signUpStatus) {

        realm.executeTransaction(realm1 ->
                realm1.insertOrUpdate(signUpStatus));

    }
    // endregion

    // region GetSignUpStatus

    public SignUpStatus getSignUpStatus() {

        SignUpStatus result = realm.where(SignUpStatus.class).findFirst();

        if (result == null)
            return new SignUpStatus();
        else
            return result;
    }

    public void setUserProfilePicUploadDone(boolean done) {
        SignUpStatus signUpStatus = realm.where(SignUpStatus.class).findFirst();

        realm.executeTransaction(realm1 -> {
            if (signUpStatus != null) {
                signUpStatus.setUserProfilePicUploadSkipped(done);
            }
        });
    }

    public void saveUserSignUpProcessDone(boolean signUpTriggered, boolean interestAndPreferredLanguageInputDone) {

        SignUpStatus signUpStatus = realm.where(SignUpStatus.class).findFirst();

        realm.executeTransaction(realm1 -> {
            if (signUpStatus != null) {
                signUpStatus.setSignUpTriggered(signUpTriggered);
                signUpStatus.setUserInterestAndPreferredLanguageInputDone(interestAndPreferredLanguageInputDone);
            }
        });

    }

    // endregion


    // region -----------user-active-session------------

    public void saveUserActivitySessionTracker(UserActivitySessionTracker activitySessionTracker) {
        realm.executeTransaction(realm1 -> {
            UserActivitySessionTracker sessionTracker = realm1.where(UserActivitySessionTracker.class).findFirst();

            if (sessionTracker != null) {
                sessionTracker.setLoginActivitySession(activitySessionTracker.getLoginActivitySession());
                sessionTracker.setHomeActivitySession(activitySessionTracker.getHomeActivitySession());
                sessionTracker.setCreateQxmActivitySession(activitySessionTracker.getCreateQxmActivitySession());
                sessionTracker.setYouTubePlaybackActivitySession(activitySessionTracker.getYouTubePlaybackActivitySession());
                sessionTracker.setDate(activitySessionTracker.getDate());
                sessionTracker.setDataUploadedToServer(activitySessionTracker.isDataUploadedToServer());
            } else {
                realm1.insertOrUpdate(activitySessionTracker);
            }
        });
    }

    public UserActivitySessionTracker getUserActivitySessionTrackerData() {
        return realm.where(UserActivitySessionTracker.class).findFirst();
    }

    // when sessionTracker data upload finished.. then deletePrevious sessionTrackerData
    public void resetUserActivitySessionTrackerData() {

        realm.executeTransaction(realm1 -> {

            UserActivitySessionTracker activitySessionTracker = realm1.where(UserActivitySessionTracker.class).findFirst();

            if (activitySessionTracker == null) {
                activitySessionTracker = new UserActivitySessionTracker();
            }

            activitySessionTracker.setDate(String.valueOf(
                    QxmDateHelper.getDateOnly(Calendar.getInstance().getTime()))
            );
            activitySessionTracker.setLoginActivitySession(0);
            activitySessionTracker.setHomeActivitySession(0);
            activitySessionTracker.setCreateQxmActivitySession(0);
            activitySessionTracker.setYouTubePlaybackActivitySession(0);

            realm1.insertOrUpdate(activitySessionTracker);

        });
    }

    public void saveLoginActivitySession(long loginActivitySession) {

        realm.executeTransaction(realm1 -> {
            UserActivitySessionTracker activitySessionTracker = realm1.where(UserActivitySessionTracker.class).findFirst();

            if (activitySessionTracker != null)
                activitySessionTracker.setLoginActivitySession(activitySessionTracker.getLoginActivitySession() + loginActivitySession);
            else {
                activitySessionTracker = new UserActivitySessionTracker();
                activitySessionTracker.setDate(String.valueOf(
                        QxmDateHelper.getDateOnly(Calendar.getInstance().getTime())
                        )
                );
                activitySessionTracker.setLoginActivitySession(loginActivitySession);

            }
            realm1.insertOrUpdate(activitySessionTracker);
        });
    }

    public void saveHomeActivitySession(long homeActivitySession) {
        realm.executeTransaction(realm1 -> {
            UserActivitySessionTracker activitySessionTracker = realm1.where(UserActivitySessionTracker.class).findFirst();
            if (activitySessionTracker != null)
                activitySessionTracker.setHomeActivitySession(activitySessionTracker.getHomeActivitySession() + homeActivitySession);
            else {
                activitySessionTracker = new UserActivitySessionTracker();
                activitySessionTracker.setHomeActivitySession(homeActivitySession);
                realm1.insert(activitySessionTracker);
            }
        });
    }

    public void saveYouTubePlayBackActivitySession(long youTubePlaybackActivitySession) {
        realm.executeTransaction(realm1 -> {
            UserActivitySessionTracker activitySessionTracker = realm1.where(UserActivitySessionTracker.class).findFirst();
            if (activitySessionTracker != null)
                activitySessionTracker.setYouTubePlaybackActivitySession(
                        activitySessionTracker.getYouTubePlaybackActivitySession() + youTubePlaybackActivitySession);
            else {
                activitySessionTracker = new UserActivitySessionTracker();
                activitySessionTracker.setYouTubePlaybackActivitySession(youTubePlaybackActivitySession);
            }
        });
    }

    public void saveCreateQxmActivitySession(long createQxmActivitySession) {
        realm.executeTransaction(realm1 -> {
            UserActivitySessionTracker activitySessionTracker = realm1.where(UserActivitySessionTracker.class).findFirst();
            if (activitySessionTracker != null) {

                activitySessionTracker.setCreateQxmActivitySession(activitySessionTracker.getCreateQxmActivitySession() + createQxmActivitySession);
            } else {
                activitySessionTracker = new UserActivitySessionTracker();
                activitySessionTracker.setCreateQxmActivitySession(createQxmActivitySession);
            }
        });
    }


    public void setSignUpTriggered(boolean isSignUp) {

        realm.executeTransaction(realm1 -> {
            SignUpStatus result = realm1.where(SignUpStatus.class).findFirst();

            Log.d(TAG, "setSignUpTriggered all SignUp: Trigger"+ realm1.where(SignUpStatus.class).findAll());

            if (result == null) {
                result = new SignUpStatus();
                result.setSignUpTriggered(isSignUp);

            } else {
                result.setSignUpTriggered(isSignUp);
            }
            realm1.insertOrUpdate(result);

        });
    }

    public void removeSavedSignUpStatus() {

        realm.executeTransaction(realm -> realm.delete(SignUpStatus.class));
    }


    // endregion

    public void close() {
        realm.close();
    }
}
