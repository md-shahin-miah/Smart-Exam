package com.crux.qxm.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crux.qxm.R;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.StaticValues;
import com.crux.qxm.utils.deepLinkingHelper.IdExtractorFromUrl;
import com.crux.qxm.utils.deepLinkingHelper.IdValidator;
import com.crux.qxm.utils.userSessionTrackerHelper.UserActivitySessionTrackerHelper;
import com.crux.qxm.views.fragments.EvaluateQxmFragment;
import com.crux.qxm.views.fragments.HomeFragment;
import com.crux.qxm.views.fragments.ParticipateQxmFragment;
import com.crux.qxm.views.fragments.create.CreateFragment;
import com.crux.qxm.views.fragments.enroll.ParticipationParentFragment;
import com.crux.qxm.views.fragments.following.FollowingFragment;
import com.crux.qxm.views.fragments.group.ViewQxmGroupFragment;
import com.crux.qxm.views.fragments.myQxm.MyQxmParentFragment;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmDetailsFragment;
import com.crux.qxm.views.fragments.myQxm.myQxmInsideInitialFragment.MyQxmSingleQxmPendingEvaluationListFragment;
import com.crux.qxm.views.fragments.poll.CreatePollFragment;
import com.crux.qxm.views.fragments.questionOverview.QuestionOverViewFragment;
import com.crux.qxm.views.fragments.singleMCQ.CreateSingleMultipleChoiceFragment;
import com.crux.qxm.views.fragments.singleMCQ.ParticipateSingleMCQFragment;
import com.crux.qxm.views.fragments.userProfileActivityFragments.UserProfileFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Calendar;
import java.util.Stack;

import static com.crux.qxm.utils.StaticValues.CHANNEL_ENROLL_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_EVALUATION_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_FOLLOWER_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_GROUP_INVITE_REQUEST_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_GROUP_JOIN_REQUEST_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_GROUP_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_KEY;
import static com.crux.qxm.utils.StaticValues.CHANNEL_PRIVATE_QXM_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_QXM_NAME;
import static com.crux.qxm.utils.StaticValues.CHANNEL_RESULT_NAME;
import static com.crux.qxm.utils.StaticValues.CREATE_QXM_REQUEST;
import static com.crux.qxm.utils.StaticValues.EDIT_QXM_REQUEST;
import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_BUNDLE;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_FOLLOWER_FULL_NAME;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_FOLLOWER_ID;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_GROUP_ID_KEY;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_GROUP_NAME_KEY;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_PARTICIPATION_ID;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_PARTICIPATOR_NAME;
import static com.crux.qxm.utils.StaticValues.NOTIFICATION_QXM_ID;
import static com.crux.qxm.utils.StaticValues.QUESTION_STATUS_DRAFTED;
import static com.crux.qxm.utils.StaticValues.QXM_ID_KEY;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_KEY;
import static com.crux.qxm.utils.StaticValues.URI_GROUP;
import static com.crux.qxm.utils.StaticValues.URI_POLL;
import static com.crux.qxm.utils.StaticValues.URI_QXM;
import static com.crux.qxm.utils.StaticValues.URI_SINGLE_MCQ;
import static com.crux.qxm.utils.StaticValues.URI_USER;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_KEY;
import static com.crux.qxm.utils.StaticValues.VIEW_FOR_SEE_QUESTION;


public class HomeActivity extends AppCompatActivity {

    // region HomeActivity-Properties

    private static final String TAG = "MyHomeActivity";
    BottomNavigationViewEx bottomNavigationViewEx;
    BottomNavigationViewEx.OnNavigationItemSelectedListener navigationItemSelectedListener;
    private FragmentManager fragmentManager;
    private Stack<String> fragmentStack;
    // private QxmCreateNewQxmOrPollDialog createNewQxmOrPoll;
    private Fragment homeFragment;
    private ParticipationParentFragment participationParentFragment;
    private FollowingFragment followingFragment;
    private MyQxmParentFragment myQxmParentFragment;
    private CreateFragment createFragment;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private long startTime = 0;
    private UserActivitySessionTrackerHelper activitySessionTrackerHelper;
    // endregion

    // region Override-onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setUpBottomNavigationItemSelectedListener();
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Intent intent = getIntent();
        String action = intent.getAction();

        Log.d(TAG, "onCreate: intent action type: " + action);

        //region DeepLinking
        if (action != null && action.equals("android.intent.action.VIEW")) {
            Uri data = intent.getData();
            Log.d(TAG, "onCreate: " + (data != null ? data.toString() : "intent data is null"));

            String url = data != null ? data.toString() : null;

            if (!TextUtils.isEmpty(url)) {
                if (url.contains(URI_USER)) {
                    // Goto Profile

                    String userId = IdExtractorFromUrl.getId(url);
                    if (!userId.isEmpty() && IdValidator.isValidMongoDBObjectId(userId))
                        qxmFragmentTransactionHelper.loadUserProfileFragment(userId, "Loading User Profile...");
                    else{
                        Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                        qxmFragmentTransactionHelper.loadHomeFragment();
                    }

                } else if (url.contains(URI_GROUP)) {
                    // Goto ViewGroupFragment

                    String groupId = IdExtractorFromUrl.getId(url);
                    Log.d(TAG, "onCreate: DeepLinking-Group groupId: " + groupId);

                    if (!groupId.isEmpty() && IdValidator.isValidMongoDBObjectId(groupId))
                        qxmFragmentTransactionHelper.loadViewQxmGroupFragment(groupId, "Loading Group Info...");
                    else{
                        Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                        qxmFragmentTransactionHelper.loadHomeFragment();
                    }

                } else if (url.contains(URI_POLL)) {
                    // Goto PollParticipate

                    String pollId = IdExtractorFromUrl.getId(url);
                    Log.d(TAG, "onCreate: DeepLinking-Poll pollId: " + pollId);

                    if (!pollId.isEmpty() && IdValidator.isValidMongoDBObjectId(pollId)) {
                        // TODO:: poll view fragment
                        qxmFragmentTransactionHelper.loadHomeFragment();
                    } else{
                        Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                        qxmFragmentTransactionHelper.loadHomeFragment();
                    }

                } else if (url.contains(URI_SINGLE_MCQ)) {
                    // Goto ViewGroupFragment

                    String singleMCQId = IdExtractorFromUrl.getId(url);
                    Log.d(TAG, "onCreate: DeepLinking-SingleMCQ singleMCQId: " + singleMCQId);
                    if (!singleMCQId.isEmpty() && IdValidator.isValidMongoDBObjectId(singleMCQId)) {

                        qxmFragmentTransactionHelper.loadParticipateSingleMCQFragment(singleMCQId, VIEW_FOR_SEE_QUESTION);
                    } else{
                        Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                        loadFragment(homeFragment);
                    }

                } else if (url.contains(URI_QXM)){
                    // QuestionOverViewFragment
                    String qxmId = IdExtractorFromUrl.getId(url);
                    Log.d(TAG, "onCreate: DeepLinking-Qxm qxmId: " + qxmId);
                    Log.d(TAG, "onCreate: IdValidation Checking: " + IdValidator.isValidMongoDBObjectId(qxmId));

                    if(!TextUtils.isEmpty(qxmId) && IdValidator.isValidMongoDBObjectId(qxmId)){
                        Log.d(TAG, "onCreate: DeepLinking-Qxm qxmId: is valid" );
                        Bundle args = new Bundle();
                        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
                        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                    }else{
                        Log.d(TAG, "onCreate: DeepLinking: invalid url: " + url);
                        Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                        loadFragment(homeFragment);
                    }

                } else{
                    Log.d(TAG, "onCreate: DeepLinking: invalid url: " + url);
                    Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show();
                    loadFragment(homeFragment);
                }

            }


        }
        //endregion

        else {
            // region Notification
            //Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(NOTIFICATION_BUNDLE);

            if (bundle != null) {
                String channelName = bundle.getString(CHANNEL_KEY);
                Log.d(TAG, "App is launching by taping notification.");

                if (!TextUtils.isEmpty(channelName)) {
                    Log.d(TAG, "onCreate: CHANNEL_KEY: " + channelName);
                    Log.d(TAG, "Now checking notification channels...");

                    switch (channelName) {
                        case CHANNEL_FOLLOWER_NAME:
                            Log.d(TAG, "Notification: CHANNEL_FOLLOWER_NAME - opening userProfile");
                            String followerId = bundle.getString(NOTIFICATION_FOLLOWER_ID);
                            String followingFullName = bundle.getString(NOTIFICATION_FOLLOWER_FULL_NAME);
                            if (!TextUtils.isEmpty(followerId) && !TextUtils.isEmpty(followingFullName))
                                qxmFragmentTransactionHelper.loadUserProfileFragment(followerId, followingFullName);
                            else
                                loadFragment(homeFragment);

                            break;
                        case CHANNEL_QXM_NAME:
                            Log.d(TAG, "Notification: CHANNEL_QXM_NAME - opening QuestionOverview ");
                            String qxmId = bundle.getString(NOTIFICATION_QXM_ID);
                            if (!TextUtils.isEmpty(qxmId)) {

                                Log.d(TAG, "CHANNEL_QXM-> qxmId = " + qxmId);
                                Bundle args = new Bundle();
                                args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);
                                qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                            } else {
                                Log.d(TAG, "CHANNEL_QXM-> qxmId is null");
                                loadFragment(homeFragment);
                            }

                            break;
                        case CHANNEL_PRIVATE_QXM_NAME:

                            Log.d(TAG, "Notification: CHANNEL_PRIVATE_QXM_NAME - open PrivateQxm");
                            //TODO:: create private - qxm Fragment and load there


                            break;
                        case CHANNEL_ENROLL_NAME:

                            Log.d(TAG, "Notification: CHANNEL_ENROLL_NAME");
                            qxmFragmentTransactionHelper.loadEnrollParentFragmentsSpecificFragment(4);

                            break;
                        case CHANNEL_EVALUATION_NAME:
                            Log.d(TAG, "Notification: CHANNEL_EVALUATION_NAME");
                            String participationId = bundle.getString(NOTIFICATION_PARTICIPATION_ID);
                            String participatorName = bundle.getString(NOTIFICATION_PARTICIPATOR_NAME);
                            if (!TextUtils.isEmpty(participationId)) {

                                qxmFragmentTransactionHelper.loadEvaluateQxmFragment(participationId, participatorName);
                            } else
                                loadFragment(homeFragment);

                            break;
                        case CHANNEL_RESULT_NAME:

                            Log.d(TAG, "Notification: CHANNEL_RESULT_NAME");
                            String resultQxmId = bundle.getString(QXM_ID_KEY);

                            if (!TextUtils.isEmpty(resultQxmId))
                                qxmFragmentTransactionHelper.loadFullResultFragment(resultQxmId);
                            else
                                loadFragment(homeFragment);

                            break;
                        case CHANNEL_GROUP_JOIN_REQUEST_NAME:

                            Log.d(TAG, "Notification: CHANNEL_GROUP_JOIN_REQUEST_NAME");
                            if(!TextUtils.isEmpty(bundle.getString(NOTIFICATION_GROUP_ID_KEY))){
                                Log.d(TAG, "onCreate: groupId: " + bundle.getString(NOTIFICATION_GROUP_ID_KEY));
                                qxmFragmentTransactionHelper.loadGroupPendingRequestListFragment(bundle.getString(NOTIFICATION_GROUP_ID_KEY));

                            }else{
                                Log.d(TAG, "onCreate: groupId is null");
                                loadFragment(homeFragment);
                            }

                            break;

                        case CHANNEL_GROUP_INVITE_REQUEST_NAME:

                            Log.d(TAG, "Notification: CHANNEL_GROUP_INVITE_REQUEST_NAME");
                            qxmFragmentTransactionHelper.loadNotificationFragment(5);

                            break;

                        case CHANNEL_GROUP_NAME:
                            Log.d(TAG, "Notification: CHANNEL_GROUP_NAME");

                            if(!TextUtils.isEmpty(bundle.getString(NOTIFICATION_GROUP_ID_KEY))){
                                Log.d(TAG, "onCreate: groupId: " + bundle.getString(NOTIFICATION_GROUP_ID_KEY));

                                qxmFragmentTransactionHelper.loadViewQxmGroupFragment(bundle.getString(NOTIFICATION_GROUP_ID_KEY),
                                        bundle.getString(NOTIFICATION_GROUP_NAME_KEY));

                            }else{
                                Log.d(TAG, "onCreate: groupId is null");
                                loadFragment(homeFragment);
                            }


                            break;
                        default:
                            Log.d(TAG, "Not matched any notification channel");
                            Log.d(TAG, "App is loading Home fragment");
                            loadFragment(homeFragment);
                    }

                } else {
                    Log.d(TAG, "Bundle is empty");
                    Log.d(TAG, "App is loading Home fragment");
                    loadFragment(homeFragment);
                }
            } else {
                Log.d(TAG, "App is not launching from notification");
                Log.d(TAG, "App is loading Home fragment normally");
                loadFragment(homeFragment);
            }

            // endregion
        }


    }

    // endregion

    // region Init

    private void init() {
        homeFragment = new HomeFragment();
        participationParentFragment = new ParticipationParentFragment();
        followingFragment = new FollowingFragment();
        myQxmParentFragment = new MyQxmParentFragment();
        createFragment = new CreateFragment();
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(HomeActivity.this);
        activitySessionTrackerHelper = new UserActivitySessionTrackerHelper();

        fragmentManager = getSupportFragmentManager();
        fragmentStack = new Stack<>();
        // createNewQxmOrPoll = new QxmCreateNewQxmOrPollDialog(HomeActivity.this, this);

        bottomNavigationViewEx = findViewById(R.id.bottom_navigation_view);

        //TODO hiding bottom navigation bar during scroll
        //RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bottomNavigationViewEx.getLayoutParams();
        //layoutParams.setBehavior(new BottomNavigationViewBehavior());

        // disabling all animation in bottom navigation bar
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableAnimation(false);
    }

    // endregion

    // region setUpBottomNavigationItemSelectedListener

    private void setUpBottomNavigationItemSelectedListener() {
        navigationItemSelectedListener = item -> {
            Fragment fragment = fragmentManager.findFragmentById(R.id.frame_container);

            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.d(TAG, "onNavigationItemSelected: Home");

                    if (fragment instanceof HomeFragment) {
                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    } else {
                        loadFragment(homeFragment);
                    }

                    return true;
                case R.id.nav_enroll:
                    Log.d(TAG, "onNavigationItemSelected: Enroll");
                    loadFragment(participationParentFragment);

                    return true;
                case R.id.nav_following:
                    Log.d(TAG, "onNavigationItemSelected: Following");
                    loadFragment(followingFragment);
                    return true;
                case R.id.nav_my_qxm:
                    Log.d(TAG, "onNavigationItemSelected: My Qxm");
                    loadFragment(myQxmParentFragment);
                    return true;
                case R.id.nav_create_qxm:
                    Log.d(TAG, "onNavigationItemSelected: Create Qxm");

                    loadFragment(createFragment);

                    // qxmFragmentTransactionHelper.loadCreateFragment();
                    // createNewQxmOrPoll.showDialog();
                    // startActivityForResult(new Intent(HomeActivity.this, CreateQxmActivity.class), CREATE_QXM_REQUEST);
                    return true;
            }

            return false;
        };
    }

    // endregion

    // region LoadFragment

    private void loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        Log.d(TAG, "loadFragment: backStackName: " + backStateName);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment visibleFragment : fragmentManager.getFragments()) {
            fragmentTransaction.detach(visibleFragment);
        }

        // Retrieve fragment instance, if it was already created
        Fragment backStackedFragment = fragmentManager.findFragmentByTag(backStateName);
        if (backStackedFragment == null) { // If not, crate new instance and add it
            Log.d(TAG, "loadFragment: create new fragment - " + backStateName);
            fragmentTransaction.add(R.id.frame_container, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentStack.push(backStateName);
        } else { // otherwise just show it
            Log.d(TAG, "loadFragment: load from backStack - " + backStateName);
            fragmentTransaction.attach(backStackedFragment);

            if (!backStateName.equals(HomeFragment.class.getName())) {
                fragmentStack.removeElement(backStateName);
                fragmentStack.push(backStateName);
            }
        }
        fragmentTransaction.commit();

        Log.d(TAG, "loadFragment: BackStackCount: " + fragmentManager.getBackStackEntryCount() +
                "\n backStack = " + fragmentStack);
    }

    // endregion

    // region Override-OnActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode: " + requestCode);
        Log.d(TAG, "onActivityResult: resultCode: " + requestCode);

        if ((requestCode == CREATE_QXM_REQUEST || requestCode == EDIT_QXM_REQUEST) && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: new qxm created.");
            String qxm_publish_status = getIntent().getStringExtra(QXM_PRIVACY_KEY);

            if (!TextUtils.isEmpty(qxm_publish_status) && qxm_publish_status.equals(QUESTION_STATUS_DRAFTED)) {
                // GOTO MyQxmDraft
                int myQxmDraftFragmentPositionOnViewPager = 4;
                qxmFragmentTransactionHelper.loadMyQxmParentFragmentsSpecificFragment(myQxmDraftFragmentPositionOnViewPager);
            } else {
                // GOTO homeFragment & reload feed
                StaticValues.isHomeFeedRefreshingNeeded = true;
                loadFragment(homeFragment);
            }

        } else {
            Log.d(TAG, "onActivityResult: cancelled creating new qxm.");
        }

    }

    // endregion


    @Override
    protected void onStart() {
        super.onStart();
        startTime = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "onStart: startTime: " + startTime);
    }

    @Override
    protected void onStop() {
        super.onStop();

        long endTime = Calendar.getInstance().getTimeInMillis();
        activitySessionTrackerHelper.saveHomeActivitySession(startTime, endTime);
        Log.d(TAG, "onStop: activitySession: " + activitySessionTrackerHelper.getActivitySessionTracker().toString());

    }

    // region Override-onBackPressed()
    @Override
    public void onBackPressed() {


        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_container);

        if (fragment instanceof HomeFragment) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                finish();
            }
        } else if (fragment instanceof ParticipateQxmFragment) {

            // region ParticipateQxmFragment->TwoInOneView (See Question & Participation Qxm)

            String viewFor = fragment.getArguments() != null ? fragment.getArguments().getString(VIEW_FOR_KEY) : null;
            if (TextUtils.equals(viewFor, VIEW_FOR_SEE_QUESTION)) {
                fragmentManager.popBackStack();
                findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
                Log.d(TAG, "onBackPressed override in ParticipateQxmFragment for SeeQuestion: popBackStack");
            } else {

                AppCompatDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_dialog_alert);
                builder.setTitle("Warning !");
                builder.setMessage("Are you sure to exit the exam? \n" +
                        "Your all progress will be gone and you will not be able to participate again in this question!");

                builder.setPositiveButton("OK", (dialogInterface, i) -> fragmentManager.popBackStack());
                builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

                });

                dialog = builder.create();
                dialog.show();


            }
            // endregion

        } else if (fragment instanceof UserProfileFragment) {
            Log.d(TAG, "onBackPressed override in QuestionOverViewFragment: popBackStack");
            fragmentManager.popBackStack();
            if (fragmentManager.getBackStackEntryCount() == 1) {
                loadFragment(homeFragment);
                Log.d(TAG, "loading homeFragment: BackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
            } else {
                Log.d(TAG, "already has fragment: BackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
            }

        } else if (fragment instanceof QuestionOverViewFragment) {
            Log.d(TAG, "onBackPressed override in QuestionOverViewFragment: popBackStack");
            fragmentManager.popBackStack();
            loadHomeFragmentIfBackStackIsEmpty();
//            loadFragmentFromBackStack();
            findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

        } else if (fragment instanceof ParticipationParentFragment) {
            Log.d(TAG, "onBackPressed override in ParticipationParentFragment");
            loadFragmentFromBackStack();

            bottomNavigationViewEx.getMenu().getItem(1).setChecked(true);

        } else if (fragment instanceof FollowingFragment) {
            Log.d(TAG, "onBackPressed override in FollowingFragment");
            loadFragmentFromBackStack();

        } else if (fragment instanceof MyQxmParentFragment) {
            Log.d(TAG, "onBackPressed override in MyQxmFragment");
            loadFragmentFromBackStack();

        } else if (fragment instanceof MyQxmSingleQxmDetailsFragment) {
            Log.d(TAG, "onBackPressed override in MyQxmSingleQxmDetailsFragment");
            fragmentManager.popBackStack();

        } else if (fragment instanceof MyQxmSingleQxmPendingEvaluationListFragment) {
            Log.d(TAG, "onBackPressed override in  MyQxmSingleQxmPendingEvaluationFragment");
            fragmentManager.popBackStack();

        } else if (fragment instanceof EvaluateQxmFragment) {
            Log.d(TAG, "onBackPressed: EvaluateQxmFragment");
            fragmentManager.popBackStack();
            loadHomeFragmentIfBackStackIsEmpty();

        } else if (fragment instanceof ViewQxmGroupFragment) {
            Log.d(TAG, "onBackPressed override in ViewQxmGroupFragment: popBackStack");
            fragmentManager.popBackStack();
            loadHomeFragmentIfBackStackIsEmpty();
            findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

        } else if (fragment instanceof ParticipateSingleMCQFragment) {
            Log.d(TAG, "onBackPressed override in ParticipateSingleMCQFragment: popBackStack");
            fragmentManager.popBackStack();
            loadHomeFragmentIfBackStackIsEmpty();
            findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);

        } else if (fragment instanceof CreateSingleMultipleChoiceFragment) {


            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage(R.string.alert_dialog_message_create_single_multiple_choice_fragment_on_back_press_warning);
            builder.setTitle("Warning !");

            builder.setPositiveButton("OK", (dialogInterface, i) -> fragmentManager.popBackStack());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });

            dialog = builder.create();
            dialog.show();

        }else if (fragment instanceof CreatePollFragment) {


            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage(R.string.alert_dialog_message_create_poll_fragment_on_back_press_warning);
            builder.setTitle("Warning !");

            builder.setPositiveButton("OK", (dialogInterface, i) -> {

                fragmentManager.popBackStack();


            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });

            dialog = builder.create();
            dialog.show();

        }
        else {
            Log.d(TAG, "onBackPressed: ");
            super.onBackPressed();
        }

    }

    private void loadHomeFragmentIfBackStackIsEmpty() {
        if (fragmentManager.getBackStackEntryCount() == 1) {
            loadFragment(homeFragment);
            Log.d(TAG, "loading homeFragment: BackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
        } else {
            Log.d(TAG, "already has fragment: BackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
        }
    }

    private void loadFragmentFromBackStack() {
        Log.d(TAG, "loadFragmentFromBackStack: called");
        if (fragmentStack != null && !fragmentStack.isEmpty()) {

            Log.d(TAG, "loadFragmentFromBackStack: stack is not empty");
            if (!fragmentStack.peek().equals(HomeFragment.class.getName()))
                Log.d(TAG, "fragmentStack.pop() = " + fragmentStack.pop());

            String tag = fragmentStack.peek();
            Log.d(TAG, "Fragment tag: " + tag);

            if (tag.equals(HomeFragment.class.getName())) loadFragment(new HomeFragment());

            else if (tag.equals(ParticipationParentFragment.class.getName())) loadFragment(new ParticipationParentFragment());

            else if (tag.equals(MyQxmParentFragment.class.getName())) loadFragment(new MyQxmParentFragment());

            else if (tag.equals(FollowingFragment.class.getName())) loadFragment(new FollowingFragment());

            else {
                Log.d(TAG, "fragment tag not matched...");
                loadFragment(new HomeFragment());
            }

        } else{
            Log.d(TAG, "loadFragmentFromBackStack: Stack is empty!");
            loadFragment(new HomeFragment());
        }
    }

    // endregion


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}