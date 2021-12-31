package com.crux.qxm.utils;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crux.qxm.R;
import com.crux.qxm.db.models.poll.pollEdit.PollData;
import com.crux.qxm.db.models.questions.QxmModel;
import com.crux.qxm.db.models.singleMCQ.SingleMCQModel;
import com.crux.qxm.db.realmModels.user.UserBasic;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.views.activities.LoginActivity;
import com.crux.qxm.views.fragments.EvaluateQxmFragment;
import com.crux.qxm.views.fragments.HomeFragment;
import com.crux.qxm.views.fragments.ParticipateQxmFragment;
import com.crux.qxm.views.fragments.YoutubePlaybackFragment;
import com.crux.qxm.views.fragments.acitivityLog.ActivityLogFragment;
import com.crux.qxm.views.fragments.enroll.ParticipationParentFragment;
import com.crux.qxm.views.fragments.evaluationListQxm.MyQxmEvaluationListFragment;
import com.crux.qxm.views.fragments.following.FollowingListFragment;
import com.crux.qxm.views.fragments.group.AddMembersToGroupFragment;
import com.crux.qxm.views.fragments.group.CreateGroupFragment;
import com.crux.qxm.views.fragments.group.GroupMemberListFragment;
import com.crux.qxm.views.fragments.group.GroupPendingRequestListFragment;
import com.crux.qxm.views.fragments.group.GroupSentInvitationListFragment;
import com.crux.qxm.views.fragments.group.GroupSettingsFragment;
import com.crux.qxm.views.fragments.group.InviteMembersToGroupFragment;
import com.crux.qxm.views.fragments.group.ViewQxmGroupFragment;
import com.crux.qxm.views.fragments.leaderboard.LeaderboardFragment;
import com.crux.qxm.views.fragments.loginActivityFragments.ProfilePicUploadFragment;
import com.crux.qxm.views.fragments.loginActivityFragments.SignUpFragment;
import com.crux.qxm.views.fragments.loginActivityFragments.UserInterestAndPreferableLanguageInputFragment;
import com.crux.qxm.views.fragments.myQxm.MyQxmMyPollFragment;
import com.crux.qxm.views.fragments.myQxm.MyQxmParentFragment;
import com.crux.qxm.views.fragments.myQxm.list.SingleListQxmFragment;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmDetailsFragment;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmEnrolledListFragment;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmParticipatorListFragment;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmPendingEvaluationListFragment;
import com.crux.qxm.views.fragments.notification.NotificationParentFragment;
import com.crux.qxm.views.fragments.poll.CreatePollFragment;
import com.crux.qxm.views.fragments.poll.EditPollFragment;
import com.crux.qxm.views.fragments.poll.PollOverviewFragment;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverViewFragment;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverviewEnrolledListFragment;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverviewParticipantListFragment;
import com.crux.qxm.views.fragments.qxmResult.FullResultFragment;
import com.crux.qxm.views.fragments.search.SearchableFragment;
import com.crux.qxm.views.fragments.sendFeedback.SendFeedbackFragment;
import com.crux.qxm.views.fragments.settings.QxmAppSettingsFragment;
import com.crux.qxm.views.fragments.singleMCQ.CreateSingleMultipleChoiceFragment;
import com.crux.qxm.views.fragments.singleMCQ.EditSingleMultipleChoiceFragment;
import com.crux.qxm.views.fragments.singleMCQ.FullResultSingleMCQFragment;
import com.crux.qxm.views.fragments.singleMCQ.LeaderboardSingleMCQFragment;
import com.crux.qxm.views.fragments.singleMCQ.ParticipateSingleMCQFragment;
import com.crux.qxm.views.fragments.singleMCQ.SingleMCQDetailsFragment;
import com.crux.qxm.views.fragments.singleMCQ.SingleMCQOverviewFragment;
import com.crux.qxm.views.fragments.singleMCQ.SingleMultipleChoiceSettingsFragment;
import com.crux.qxm.views.fragments.termsAndPrivacy.QxmTermsAndPrivacyFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.EditUserProfileFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.FollowerListFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.LoggedInDevicesFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.MyFollowingListFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.ProfileSettingsFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.PublicGroupFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.UserProfileFragment;
import com.onesignal.OneSignal;

import io.realm.Realm;

public class QxmFragmentTransactionHelper {

    private static final String TAG = "QxmFragmentTransactionH";
    private FragmentActivity fragmentActivity;

    // region Class-Constructor
    public QxmFragmentTransactionHelper(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }
    // endregion

    // region LoginActivity ====> FragmentTransactions

    // region LoadSignUpFragment

    public void loadSignUpFragment() {
        // transaction to sign up fragment for registration
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragment_container, signUpFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    // endregion

    // region LoadProfilePicUploadFragment

    public void loadProfilePicUploadFragment() {
        ProfilePicUploadFragment profilePicUploadFragment = new ProfilePicUploadFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
            fragmentTransaction.replace(R.id.fragment_container, profilePicUploadFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    // endregion

    // region LoadUserInterestAndPreferableLanguageInputFragment

    public void loadUserInterestAndPreferableLanguageInputFragment(){
        UserInterestAndPreferableLanguageInputFragment interestAndPreferableLanguageInputFragment= new UserInterestAndPreferableLanguageInputFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragment_container, interestAndPreferableLanguageInputFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    // endregion

    // endregion

    // region LoadQuestionOverviewFragment

    public void loadQuestionOverviewFragment(Bundle args) {
        QuestionOverViewFragment questionOverViewFragment = new QuestionOverViewFragment();
        questionOverViewFragment.setArguments(args);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, questionOverViewFragment, QuestionOverViewFragment.class.getName());
        fragmentTransaction.addToBackStack(QuestionOverViewFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadParticipateQxmFragment

    public void loadParticipateQxmFragment(QxmModel qxmModel, String viewFor) {
        // destination Fragment
        ParticipateQxmFragment participateQxmFragment = ParticipateQxmFragment.newInstance(qxmModel, viewFor);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, participateQxmFragment, ParticipateQxmFragment.class.getName());
        fragmentTransaction.addToBackStack(ParticipateQxmFragment.class.getName());
        fragmentTransaction.commit();

    }

    // endregion

    // region LoadUserProfileFragment

    public void loadUserProfileFragment(String userId, String userFullName) {
        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(userId, userFullName);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction
                .add(R.id.frame_container, userProfileFragment, UserProfileFragment.class.getName())
                .addToBackStack(UserProfileFragment.class.getName())
                .commit();
    }

    // endregion

    //region LoadMyQxmDetailsFragment

    public void loadMyQxmDetailsFragment(Bundle args) {
        MyQxmSingleQxmDetailsFragment myQxmSingleQxmDetailsFragment = new MyQxmSingleQxmDetailsFragment();
        myQxmSingleQxmDetailsFragment.setArguments(args);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmSingleQxmDetailsFragment, MyQxmSingleQxmDetailsFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmSingleQxmDetailsFragment.class.getName());
        fragmentTransaction.commit();
    }

    //endregion

    //region LoadSingleMCQDetailsFragment

    public void loadSingleMCQDetailsFragment(Bundle args) {
        SingleMCQDetailsFragment singleMCQDetailsFragment = new SingleMCQDetailsFragment();
        singleMCQDetailsFragment.setArguments(args);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, singleMCQDetailsFragment, SingleMCQDetailsFragment.class.getName());
        fragmentTransaction.addToBackStack(SingleMCQDetailsFragment.class.getName());
        fragmentTransaction.commit();
    }

    //endregion

    // region LoadMyQxmSingleQxmEnrolledListFragment

    public void loadMyQxmSingleQxmEnrolledListFragment(Bundle bundle) {
        MyQxmSingleQxmEnrolledListFragment myQxmSingleQxmEnrolledListFragment = new MyQxmSingleQxmEnrolledListFragment();
        myQxmSingleQxmEnrolledListFragment.setArguments(bundle);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmSingleQxmEnrolledListFragment, MyQxmSingleQxmEnrolledListFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmSingleQxmEnrolledListFragment.class.getName());
        fragmentTransaction.commit();

    }

    // endregion

    // region LoadMyQxmSingleQxmParticipatorListFragment

    public void loadMyQxmSingleQxmParticipatorListFragment(Bundle bundle) {
        // Creating destination fragment
        MyQxmSingleQxmParticipatorListFragment myQxmSingleQxmParticipatorListFragment = new MyQxmSingleQxmParticipatorListFragment();
        myQxmSingleQxmParticipatorListFragment.setArguments(bundle);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmSingleQxmParticipatorListFragment, MyQxmSingleQxmParticipatorListFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmSingleQxmParticipatorListFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadMyQxmSingleQxmPendingEvaluationListFragment

    public void loadMyQxmSingleQxmPendingEvaluationListFragment(Bundle bundle) {
        // Creating destination fragment
        MyQxmSingleQxmPendingEvaluationListFragment myQxmSingleQxmPendingEvaluationListFragment = new MyQxmSingleQxmPendingEvaluationListFragment();
        myQxmSingleQxmPendingEvaluationListFragment.setArguments(bundle);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmSingleQxmPendingEvaluationListFragment, MyQxmSingleQxmPendingEvaluationListFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmSingleQxmPendingEvaluationListFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region loadEvaluateQxmFragment

    public void loadEvaluateQxmFragment(String participationId, String participatorName){
        // Creating destination fragment
        EvaluateQxmFragment evaluateQxmFragment =  EvaluateQxmFragment.newInstance(participationId, participatorName);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, evaluateQxmFragment, EvaluateQxmFragment.class.getName());
        fragmentTransaction.addToBackStack(EvaluateQxmFragment.class.getName());
        fragmentTransaction.commit();
    }


    // endregion

    // region LoadFullResultFragment

    public void loadFullResultFragment(String qxmId) {
        FullResultFragment fullResultFragment = FullResultFragment.newInstance(qxmId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, fullResultFragment, FullResultFragment.class.getName());
        fragmentTransaction.addToBackStack(FullResultFragment.class.getName());
        fragmentTransaction.commit();

    }

    public void loadFullResultFragment(String qxmId, UserBasic userBasic) {
        FullResultFragment fullResultFragment = FullResultFragment.newInstance(qxmId, userBasic);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, fullResultFragment, FullResultFragment.class.getName());
        fragmentTransaction.addToBackStack(FullResultFragment.class.getName());
        fragmentTransaction.commit();

    }



    // endregion

    // region loadSearchFragment

    public void loadSearchFragmentFragment() {
        SearchableFragment searchableFragment = SearchableFragment.newInstance();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, searchableFragment, SearchableFragment.class.getName());
        fragmentTransaction.addToBackStack(SearchableFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region Logout

    public void logout(RealmService realmService) {
        realmService.deleteQxmToken();
        OneSignal.setSubscription(false);
        OneSignal.deleteTag("name");
        fragmentActivity.finish();
        fragmentActivity.startActivity(new Intent(fragmentActivity, LoginActivity.class));
    }

    public void logout() {
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        realmService.deleteQxmToken();
        OneSignal.setSubscription(false);
        fragmentActivity.finish();
        fragmentActivity.startActivity(new Intent(fragmentActivity, LoginActivity.class));
    }


    // endregion

    // region LoadNotificationFragment

    public void loadNotificationFragment() {
        NotificationParentFragment notificationParentFragment = new NotificationParentFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, notificationParentFragment, NotificationParentFragment.class.getName());
        fragmentTransaction.addToBackStack(NotificationParentFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    public void loadNotificationFragment(int viewPagerPosition) {
        NotificationParentFragment notificationParentFragment = new NotificationParentFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, notificationParentFragment, NotificationParentFragment.class.getName());
        fragmentTransaction.addToBackStack(NotificationParentFragment.class.getName());
        fragmentTransaction.commit();

    }

    // region LoadFollowingListFragment

    public void loadFollowingListFragment() {

        FollowingListFragment followingListFragment = new FollowingListFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, followingListFragment, FollowingListFragment.class.getName());
        fragmentTransaction.addToBackStack(FollowingListFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion


    // region CreateGroupFragment

    public void loadCreateGroupFragment(){
        CreateGroupFragment createGroupFragment = new CreateGroupFragment ();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, createGroupFragment, CreateGroupFragment.class.getName());
        fragmentTransaction.addToBackStack(CreateGroupFragment.class.getName());
        fragmentTransaction.commit();

    }

    // endregion

    // region loadViewQxmGroupFragment

    public void loadViewQxmGroupFragment(String groupId, String groupName) {
        ViewQxmGroupFragment viewQxmGroupFragment = ViewQxmGroupFragment.newInstance(groupId, groupName);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, viewQxmGroupFragment, ViewQxmGroupFragment.class.getName());
        fragmentTransaction.addToBackStack(ViewQxmGroupFragment.class.getName());
        fragmentTransaction.commit();

    }

    //endregion

    // region LoadAddMemberToGroupFragment

    public void loadGroupJoinRequestSentFragment(String groupId) {

        InviteMembersToGroupFragment inviteMembersToGroupFragment = InviteMembersToGroupFragment.newInstance(groupId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, inviteMembersToGroupFragment, InviteMembersToGroupFragment.class.getName());
        fragmentTransaction.addToBackStack(InviteMembersToGroupFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    //region LoadSingleListQxmFragment
    public void loadSingleListQxmFragment(String listId, String listTitle) {

        SingleListQxmFragment singleListQxmFragment = SingleListQxmFragment.newInstance(listId, listTitle);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, singleListQxmFragment, SingleListQxmFragment.class.getName());
        fragmentTransaction.addToBackStack(SingleListQxmFragment.class.getName());
        fragmentTransaction.commit();
    }
    //endregion

    //region LoadSingleListQxmFragment
    public void loadFollowerListFragment(String userId) {

        FollowerListFragment followerListFragment = FollowerListFragment.newInstance(userId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, followerListFragment, FollowerListFragment.class.getName());
        fragmentTransaction.addToBackStack(FollowerListFragment.class.getName());
        fragmentTransaction.commit();
    }
    //endregion

    //region LoadSingleListQxmFragment
    public void loadMyFollowingListFragment(String userId) {

        MyFollowingListFragment myFollowingListFragment = MyFollowingListFragment.newInstance(userId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myFollowingListFragment, MyFollowingListFragment.class.getName());
        fragmentTransaction.addToBackStack(MyFollowingListFragment.class.getName());
        fragmentTransaction.commit();
    }
    //endregion

    //region LoadSingleListQxmFragment
    public void loadPublicGroupFragment() {

        PublicGroupFragment publicGroupFragment = PublicGroupFragment.newInstance();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, publicGroupFragment, PublicGroupFragment.class.getName());
        fragmentTransaction.addToBackStack(PublicGroupFragment.class.getName());
        fragmentTransaction.commit();
    }
    //endregion

    //region LoadEditProfileFragment

    public void loadEditProfileFragment() {

        EditUserProfileFragment editUserProfileFragment = EditUserProfileFragment.newInstance();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, editUserProfileFragment, EditUserProfileFragment.class.getName());
        fragmentTransaction.addToBackStack(EditUserProfileFragment.class.getName());
        fragmentTransaction.commit();
    }

    //endregion

    // region LoadActivityLogFragment
    public void loadActivityLogFragment() {

        ActivityLogFragment activityLogFragment = ActivityLogFragment.newInstance();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, activityLogFragment, ActivityLogFragment.class.getName());
        fragmentTransaction.addToBackStack(ActivityLogFragment.class.getName());
        fragmentTransaction.commit();
    }
    // endregion

    // region LoadMyQxmParentFragment
    public void loadEnrollParentFragmentsSpecificFragment(int viewPagerPosition) {
        ParticipationParentFragment participationParentFragment = ParticipationParentFragment.newInstance(viewPagerPosition);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, participationParentFragment, ParticipationParentFragment.class.getName());
        fragmentTransaction.addToBackStack(ParticipationParentFragment.class.getName());
        fragmentTransaction.commit();

    }
    // endregion

    // region LoadMyQxmParentFragment
    public void loadMyQxmParentFragmentsSpecificFragment(int viewPagerPosition) {
        MyQxmParentFragment myQxmParentFragment = MyQxmParentFragment.newInstance(viewPagerPosition);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmParentFragment, MyQxmParentFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmParentFragment.class.getName());
        fragmentTransaction.commit();

    }
    // endregion

    //region loadCreatePollFragment
    public void loadCreatePollFragment() {

        CreatePollFragment createPollFragment = CreatePollFragment.newInstance();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, createPollFragment, CreatePollFragment.class.getName());
        fragmentTransaction.addToBackStack(CreatePollFragment.class.getName());
        fragmentTransaction.commit();
    }
    //endregion

    //region loadCreatePollFragment
    public void loadHomeFragment() {

        HomeFragment homeFragment =  HomeFragment.newInstance();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, homeFragment, HomeFragment.class.getName());
        fragmentTransaction.addToBackStack(HomeFragment.class.getName());
        fragmentTransaction.commit();
    }
    //endregion


    // region LoadGroupSettingsFragment

    public void loadGroupSettingsFragment(String groupId, String myStatus) {
        GroupSettingsFragment groupSettingsFragment = GroupSettingsFragment.newInstance(groupId, myStatus);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, groupSettingsFragment, GroupSettingsFragment.class.getName());
        fragmentTransaction.addToBackStack(GroupSettingsFragment.class.getName());
        fragmentTransaction.commit();
    }
    // endregion

    // region LoadLeaderboardFragment

    public void loadLeaderboardFragment(Bundle bundle) {
        LeaderboardFragment leaderboardFragment= LeaderboardFragment.newInstance(bundle);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, leaderboardFragment, LeaderboardFragment.class.getName());
        fragmentTransaction.addToBackStack(LeaderboardFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadQuestionOverviewParticipantListFragment

    public void loadQuestionOverviewParticipantListFragment(String qxmId, String participantsListFor) {
        QuestionOverviewParticipantListFragment questionOverviewParticipantListFragment = QuestionOverviewParticipantListFragment.newInstance(qxmId, participantsListFor);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, questionOverviewParticipantListFragment, QuestionOverviewParticipantListFragment.class.getName());
        fragmentTransaction.addToBackStack(QuestionOverviewParticipantListFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadQuestionOverviewParticipantListFragment

    public void loadQuestionOverviewEnrolledListFragment(String qxmId) {
        QuestionOverviewEnrolledListFragment questionOverviewEnrolledListFragment = QuestionOverviewEnrolledListFragment.newInstance(qxmId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, questionOverviewEnrolledListFragment, QuestionOverviewEnrolledListFragment.class.getName());
        fragmentTransaction.addToBackStack(QuestionOverviewEnrolledListFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadQuestionOverviewParticipantListFragment

    public void loadYoutubePlaybackFragment(String youtubeLink) {
        YoutubePlaybackFragment youtubePlaybackFragment = YoutubePlaybackFragment.newInstance(youtubeLink);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, youtubePlaybackFragment, YoutubePlaybackFragment.class.getName());
        fragmentTransaction.addToBackStack(YoutubePlaybackFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadQxmTermsAndPrivacyFragment

    public void loadQxmTermsAndPrivacyFragment() {
        QxmTermsAndPrivacyFragment qxmTermsAndPrivacyFragment = new QxmTermsAndPrivacyFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, qxmTermsAndPrivacyFragment, QxmTermsAndPrivacyFragment.class.getName());
        fragmentTransaction.addToBackStack(QxmTermsAndPrivacyFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadQxmAppSettingsFragment

    public void loadQxmAppSettingsFragment() {
        QxmAppSettingsFragment qxmAppSettingsFragment = new QxmAppSettingsFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, qxmAppSettingsFragment, QxmAppSettingsFragment.class.getName());
        fragmentTransaction.addToBackStack(QxmAppSettingsFragment.class.getName());
        fragmentTransaction.commit();
    }


    // endregion

    // region LoadMyAllQxmEvaluationListFragment

    public void loadMyAllQxmEvaluationListFragment() {
        MyQxmEvaluationListFragment myQxmEvaluationListFragment = new MyQxmEvaluationListFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmEvaluationListFragment, MyQxmEvaluationListFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmEvaluationListFragment.class.getName());
        fragmentTransaction.commit();
    }

    // endregion

    // region LoadSendFeedbackFragment

    public void loadSendFeedbackFragment() {
        SendFeedbackFragment sendFeedbackFragment = new SendFeedbackFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, sendFeedbackFragment, SendFeedbackFragment.class.getName());
        fragmentTransaction.addToBackStack(SendFeedbackFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadGroupMemberListFragment(String groupId, String myStatus) {
        GroupMemberListFragment groupMemberListFragment = GroupMemberListFragment.newInstance(groupId, myStatus);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, groupMemberListFragment, GroupMemberListFragment.class.getName());
        fragmentTransaction.addToBackStack(GroupMemberListFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadGroupSentInvitationListFragment(String groupId) {
        GroupSentInvitationListFragment groupSentInvitationListFragment = GroupSentInvitationListFragment.newInstance(groupId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, groupSentInvitationListFragment, GroupSentInvitationListFragment.class.getName());
        fragmentTransaction.addToBackStack(GroupSentInvitationListFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadProfileSettingsFragment() {
        ProfileSettingsFragment profileSettingsFragment = new ProfileSettingsFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, profileSettingsFragment, ProfileSettingsFragment.class.getName());
        fragmentTransaction.addToBackStack(ProfileSettingsFragment.class.getName());
        fragmentTransaction.commit();

    }

    public void loadCreateSingleMultipleChoiceFragment() {
        CreateSingleMultipleChoiceFragment createSingleMultipleChoiceFragment = new CreateSingleMultipleChoiceFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, createSingleMultipleChoiceFragment, CreateSingleMultipleChoiceFragment.class.getName());
        fragmentTransaction.addToBackStack(CreateSingleMultipleChoiceFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadSingleMultipleChoiceSettingsFragment(SingleMCQModel singleMCQModel) {
        SingleMultipleChoiceSettingsFragment singleMultipleChoiceSettingsFragment = SingleMultipleChoiceSettingsFragment.newInstance(singleMCQModel);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, singleMultipleChoiceSettingsFragment, SingleMultipleChoiceSettingsFragment.class.getName());
        fragmentTransaction.addToBackStack(SingleMultipleChoiceSettingsFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadParticipateSingleMCQFragment(String singleMCQId, String viewFor) {
        ParticipateSingleMCQFragment participateSingleMCQFragment = ParticipateSingleMCQFragment.newInstance(singleMCQId, viewFor);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, participateSingleMCQFragment, ParticipateSingleMCQFragment.class.getName());
        fragmentTransaction.addToBackStack(ParticipateSingleMCQFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadFullResultSingleMCQFragment(String singleMCQId, UserBasic userBasic) {
        FullResultSingleMCQFragment fullResultSingleMCQFragment = FullResultSingleMCQFragment.newInstance(singleMCQId, userBasic);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, fullResultSingleMCQFragment, FullResultSingleMCQFragment.class.getName());
        fragmentTransaction.addToBackStack(FullResultSingleMCQFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadFullResultSingleMCQFragment(String singleMCQId) {
        FullResultSingleMCQFragment fullResultSingleMCQFragment = FullResultSingleMCQFragment.newInstance(singleMCQId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, fullResultSingleMCQFragment, FullResultSingleMCQFragment.class.getName());
        fragmentTransaction.addToBackStack(FullResultSingleMCQFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadLeaderboardSingleMCQFragment(Bundle bundle) {
        LeaderboardSingleMCQFragment leaderboardSingleMCQFragment = LeaderboardSingleMCQFragment.newInstance(bundle);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, leaderboardSingleMCQFragment, LeaderboardSingleMCQFragment.class.getName());
        fragmentTransaction.addToBackStack(LeaderboardSingleMCQFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadEditSingleMultipleChoiceFragment(SingleMCQModel singleMCQModel) {
        EditSingleMultipleChoiceFragment editSingleMultipleChoiceFragment = EditSingleMultipleChoiceFragment.newInstance(singleMCQModel);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, editSingleMultipleChoiceFragment, EditSingleMultipleChoiceFragment.class.getName());
        fragmentTransaction.addToBackStack(EditSingleMultipleChoiceFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadEditPollFragment(PollData pollData) {
        EditPollFragment editPollFragment = EditPollFragment.newInstance(pollData);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, editPollFragment, EditPollFragment.class.getName());
        fragmentTransaction.addToBackStack(EditPollFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadAddMembersToGroupFragment(String groupId) {
        AddMembersToGroupFragment addMembersToGroupFragment = AddMembersToGroupFragment.newInstance(groupId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, addMembersToGroupFragment, AddMembersToGroupFragment.class.getName());
        fragmentTransaction.addToBackStack(AddMembersToGroupFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadGroupPendingRequestListFragment(String groupId) {

        GroupPendingRequestListFragment groupPendingRequestListFragment = GroupPendingRequestListFragment.newInstance(groupId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, groupPendingRequestListFragment, GroupPendingRequestListFragment.class.getName());
        fragmentTransaction.addToBackStack(GroupPendingRequestListFragment.class.getName());
        fragmentTransaction.commit();

    }

    public void loadMyPollFragment() {

        MyQxmMyPollFragment myQxmMyPollFragment = new MyQxmMyPollFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, myQxmMyPollFragment, MyQxmMyPollFragment.class.getName());
        fragmentTransaction.addToBackStack(MyQxmMyPollFragment.class.getName());
        fragmentTransaction.commit();

    }

    public void loadSingleMCQOverviewFragment(String singleMCQId) {
        SingleMCQOverviewFragment singleMCQOverviewFragment =  SingleMCQOverviewFragment.newInstance(singleMCQId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, singleMCQOverviewFragment, SingleMCQOverviewFragment.class.getName());
        fragmentTransaction.addToBackStack(SingleMCQOverviewFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadPollOverviewFragment(String pollId){
        PollOverviewFragment pollOverviewFragment =  PollOverviewFragment.newInstance(pollId);

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, pollOverviewFragment, PollOverviewFragment.class.getName());
        fragmentTransaction.addToBackStack(PollOverviewFragment.class.getName());
        fragmentTransaction.commit();
    }

    public void loadLoggedInDevicesFragment() {

        LoggedInDevicesFragment loggedInDevicesFragment =  new LoggedInDevicesFragment();

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_container, loggedInDevicesFragment, LoggedInDevicesFragment.class.getName());
        fragmentTransaction.addToBackStack(LoggedInDevicesFragment.class.getName());
        fragmentTransaction.commit();

    }
    // endregion

    /*@Override
    public void onOSPermissionChanged(OSPermissionStateChanges stateChanges) {

        if (stateChanges.getFrom().getEnabled() &&
                !stateChanges.getTo().getEnabled()) {
            new AlertDialog.Builder(fragmentActivity)
                    .setMessage("Notifications Disabled!")
                    .show();
        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
           // Toast.makeText(fragmentActivity, "You've successfully subscribed to push notifications!", Toast.LENGTH_SHORT).show();
            // get player ID
            stateChanges.getTo().getUserId();

            Log.d(TAG, "onOSSubscriptionChanged: player id: " + stateChanges.getTo().getUserId());

        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);

    }*/
}
